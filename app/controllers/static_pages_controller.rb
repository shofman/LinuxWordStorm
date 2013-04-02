require 'controller_module'
class StaticPagesController < ApplicationController
  	include ControllerModule
	before_filter :authenticate_user, :except => :home
	before_filter :find_user, :except => :home
	
  def home
  end

  def help
  end
  
  def noscript
  end

require 'open-uri'



#Function parses twitter username (if of the form [http[s]://]twitter). Assumes well formed and valid user (still needs check)
def checkURL(twitter_user)
	checker = twitter_user.to_s
	if checker.start_with?("http://") or checker.start_with?("https://") or checker.start_with?("twitter.")
		return checker[checker.rindex('/')+1..checker.length]
	else 
		return checker
	end
end

#Function
def parseCreateYear(created, text, hash)
	datechecker = created.to_s
	textchecker = text.to_s
	@year = ""
	@month = ""
	@parseText = ""
	
	#parse the text information
	leftindex = textchecker.index("<text>")
	rightindex = textchecker.index("</text>")
	if  leftindex != nil and rightindex != nil
		@parseText = textchecker[leftindex+6..rightindex-1]
	else
		@parseText = ""
	end
	
	#Make sure that it's well formed
	if datechecker.index("<created_at>") != nil and datechecker.index("</created_at>") != nil
		@splitCreate = datechecker[0..datechecker.index("</created_at>")-1].split
		@year = @splitCreate.last
		@month = @splitCreate[1]
	else
		@year = "Unknown"
		@month = "Unknown"
	end

	if hash.has_key?(@year.to_s + " " + @month.to_s)
		hash[@year.to_s + " " + @month.to_s] << @parseText
	else
		hash[@year.to_s + " " + @month.to_s] = [@parseText]
	end
	
	return hash
	
end

  def twitter
	if params.has_key?(:twitter)
		username = Sanitize.clean(params[:twitter][:username])
		@input_folder = Rails.root.join('public', 'assets', 'twitter', "#{@user.id}").to_s
		Dir.mkdir(@input_folder) unless File.exists?(@input_folder)
		remote_base_url = "http://api.twitter.com/1/statuses/user_timeline.xml?include_rts=1&count=200&screen_name="

		result = checkURL(username.chomp)
		remote_full_url = remote_base_url + result

		xmlResult = open(remote_full_url).read
		@doc = Nokogiri::XML(xmlResult)
		#Needs error checking here

		statuses = @doc.xpath("//status")
		myHash = Hash.new("No data")

		for x in statuses
			myHash = parseCreateYear(x.xpath("created_at"), x.xpath("text"), myHash)
		end

		for x in myHash.keys
			count = 0
			filetag = 2
			my_local_file = open(@input_folder + "/" + x + ".txt", "w")
			myHash[x].each do |text| 
				my_local_file.write(text+" ")
				count += 1
				if count > 30
					my_local_file = open(@input_folder + "/" + x + filetag.to_s + ".txt", "w")
					filetag += 1
					count = 0
				end
			end    
			my_local_file.close
		end
		if Dir.glob(@input_folder).length > 0
			@allsettings = @user.settings.all
			@allsettings.each_key do |key|
				if @allsettings[key].blank? 
					resetdefault(@user, key)
				end
			end
		
			#@image_root = File.expand_path("/home/app/storms")
			@image_root = Rails.root.join('app', 'assets', 'images')
			Dir.mkdir(@image_root) unless File.exists?(@image_root)
			#@image_user = File.expand_path("/home/app/storms/#{@user.id}")
			@image_user = Rails.root.join('app', 'assets', 'images', "#{@user.id}")
			Dir.mkdir(@image_user) unless File.exists?(@image_user)
			#@output_folder = File.expand_path("/home/app/storms/#{@user.id}/#{@user.storm_num}")
			@output_folder = Rails.root.join('app', 'assets', 'images', "#{@user.id}", "#{@user.storm_num}")
			Dir.mkdir(@output_folder) unless File.exists?(@output_folder)
		
			#This creates the path for the Wordstorm arguments to look
			args = "#{@input_folder}/ #{@output_folder}/ 100 #{@user.settings.maxwords} #{convertTfIdf(@user.settings.tfidf)} #{@user.settings.color} \"#{convertAlgo(@user.settings.algo)}\" #{@user.settings.tolerance} #{@user.settings.iterations} \"#{@user.settings.font}\" \"#{convertCase(@user.settings.lcase)}\" #{@user.settings.scale} #{@user.settings.angle}"

			#Use the jar located at the path lib/assets/test2.jar with the constraits from args
			results = `java -jar #{Rails.root.join('lib', 'assets', Rails.configuration.version.to_s, 'wordstorm.jar').to_s} #{args}`
			#results = `#{Rails.root.join('lib', 'assets', 'display.sh').to_s} #{Rails.root.join('lib', 'assets', 'wordstorm.jar').to_s} "#{args.to_s}" `
			#results = "test"
			if (results.length > 200)
				@files = Dir.glob(@output_folder.to_s + '/*.png')		
				
				AWS::S3::DEFAULT_HOST.replace "s3-eu-west-1.amazonaws.com"
				AWS::S3::Base.establish_connection!(
					:access_key_id	=>	Rails.configuration.access_key_id,
					:secret_access_key => 	Rails.configuration.secret_access_key
				)
				#Create Wordstorm in database
				@wordstorm = @user.word_storms.find_or_create_by_file_location(:name => "Untitled", :cloud_id => @user.storm_num, :file_location => @output_folder.to_s, :size => 160, :version => Rails.configuration.version, :algorithm => Integer(convertAlgo(@user.settings.algo)))
				@wordstorm.save
	
				#Place the files for the database
				y = 0
				x = 0
				baseURL = "http://s3-eu-west-1.amazonaws.com/wordstorm.bucket"
				@files.sort!
				@files.each_with_index do |file, i|
					AWS::S3::S3Object.store("/#{@user.id}/#{@user.storm_num}/#{file.split("/").last.to_s}", open(file), 'wordstorm.bucket', :access => :public_read_write)
					if !AWS::S3::Service.response.success?
						@wordstorm.delete
						@wordstorm.save
						break;
					end
					#Use measurements by default
					xpos = (x * 185) + 112 
					if xpos > 1500
						x = 0
						y += 1
						xpos = 112 
					end
					ypos = (y * 145) + 146
					name = file.split("/").last.to_s

					#Add to db
					dbFile = @wordstorm.images.find_or_create_by_fileLocation(:name => name, :fileLocation => baseURL + '/' + @user.id.to_s + '/' + @user.storm_num.to_s + '/' + name, :local_num => i, :pos_x => xpos, :pos_y => ypos)
					dbFile.save
					x += 1

					#Remove file
					File.delete(file)
				end

				#Find amazon files
				@files = []
				@wordstorm.images.each do |image|
					@files.push (image.fileLocation)
				end
				@files.sort!
				redirect_to "/finalize?stormedit=#{@user.storm_num}"
			else
				flash[:notice] = "Error generating storm data"
				redirect_to "/uploads"	
			end
		else
			flash[:notice] = "Error reading twitter data"
			redirect_to "/uploads"	
		end
	else 
		flash[:notice] = "Error with input data"
		redirect_to "/uploads"
	end
  end
  
  def create
	if @user.uploads.count > 0
		@allsettings = @user.settings.all
		@allsettings.each_key do |key|
			if @allsettings[key].blank? 
				resetdefault(@user, key)
			end
		end
	  
		
		#@image_root = File.expand_path("/home/app/storms")
		@image_root = Rails.root.join('app', 'assets', 'images')
		Dir.mkdir(@image_root) unless File.exists?(@image_root)
		#@image_user = File.expand_path("/home/app/storms/#{@user.id}")
		@image_user = Rails.root.join('app', 'assets', 'images', "#{@user.id}")
		Dir.mkdir(@image_user) unless File.exists?(@image_user)
		#@output_folder = File.expand_path("/home/app/storms/#{@user.id}/#{@user.storm_num}")
		@output_folder = Rails.root.join('app', 'assets', 'images', "#{@user.id}", "#{@user.storm_num}")
		@input_folder = Rails.root.join('public', 'assets', 'images', "#{@user.id}").to_s
		Dir.mkdir(@output_folder) unless File.exists?(@output_folder)
		
		#This creates the path for the Wordstorm arguments to look
		args = "#{@input_folder}/ #{@output_folder}/ 100 #{@user.settings.maxwords} #{convertTfIdf(@user.settings.tfidf)} #{@user.settings.color} \"#{convertAlgo(@user.settings.algo)}\" #{@user.settings.tolerance} #{@user.settings.iterations} \"#{@user.settings.font}\" \"#{convertCase(@user.settings.lcase)}\" #{@user.settings.scale} #{@user.settings.angle}"

		#Use the jar located at the path lib/assets/test2.jar with the constraits from args
		results = `java -jar #{Rails.root.join('lib', 'assets', Rails.configuration.version.to_s, 'wordstorm.jar').to_s} #{args}`
		#results = `#{Rails.root.join('lib', 'assets', 'display.sh').to_s} #{Rails.root.join('lib', 'assets', 'wordstorm.jar').to_s} "#{args.to_s}" `
		#results = "test"
		if (results.length > 200)
			@files = Dir.glob(@output_folder.to_s + '/*.png')		
			
			AWS::S3::DEFAULT_HOST.replace "s3-eu-west-1.amazonaws.com"
			AWS::S3::Base.establish_connection!(
				:access_key_id	=>	Rails.configuration.access_key_id,
				:secret_access_key => 	Rails.configuration.secret_access_key
			)
			#Create Wordstorm in database
			@wordstorm = @user.word_storms.find_or_create_by_file_location(:name => "Untitled", :cloud_id => @user.storm_num, :file_location => @output_folder.to_s, :size => 160, :version => Rails.configuration.version, :algorithm => Integer(convertAlgo(@user.settings.algo)))
			@wordstorm.save
	
			#Place the files for the database
			y = 0
			x = 0
			baseURL = "http://s3-eu-west-1.amazonaws.com/wordstorm.bucket"
			@files.sort!
			@files.each_with_index do |file, i|
				AWS::S3::S3Object.store("/#{@user.id}/#{@user.storm_num}/#{file.split("/").last.to_s}", open(file), 'wordstorm.bucket', :access => :public_read_write)
				if !AWS::S3::Service.response.success?
					@wordstorm.delete
					@wordstorm.save
					break;
				end
				#Use measurements by default
				xpos = (x * 185) + 112 
				if xpos > 1500
					x = 0
					y += 1
					xpos = 112 
				end
				ypos = (y * 145) + 146
				name = file.split("/").last.to_s

				#Add to db
				dbFile = @wordstorm.images.find_or_create_by_fileLocation(:name => name, :fileLocation => baseURL + '/' + @user.id.to_s + '/' + @user.storm_num.to_s + '/' + name, :local_num => i, :pos_x => xpos, :pos_y => ypos)
				dbFile.save
				x += 1

				#Remove file
				File.delete(file)
			end

			#Find amazon files
			@files = []
			@wordstorm.images.each do |image|
				@files.push (image.fileLocation)
			end
			@files.sort!
			redirect_to "/finalize?stormedit=#{@user.storm_num}"
		else
			flash[:notice] = "Results failed"
			@user.decrease_count
			#redirect_to :controller => 'gallery', :action => 'debug', :results => results, :image_user => @image_user, :output_folder => @output_folder, :input_folder => @input_folder, :args => args
			redirect_to "/gallery/view"
		end
		
	else
		redirect_to "/uploads"
		flash[:notice] = "No uploads detected. Please upload at least one file"
	end
	
  end
  def convertTfIdf(tfidf)
	if tfidf == 'tfidf'
		return 1
	elsif tfidf == 'idf'
		return 2
	else 
		return 0
	end
  end
  
  def convertCase(lcase)
	if lcase == 'Regular Case'
		return -1
	elsif lcase == 'Lower Case'
		return 0
	else 
		return 1
	end
  end
  
  def convertAlgo(algo)
	if algo == 'Combined'
		return 4
	elsif algo == 'Iterative'
		return 2
	elsif algo == 'Gradient'
		return 3
	else
		return 1
	end
  end
  
   def find_user
	@user = User.find(session[:user_id])
  end
  
end
