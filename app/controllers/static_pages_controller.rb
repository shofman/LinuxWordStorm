require 'controller_module'
class StaticPagesController < ApplicationController
  	include ControllerModule
	before_filter :authenticate_user
	before_filter :find_user
	
  def home
  end

  def help
  end
  
  def create  
	@user = User.find(session[:user_id])
	@allsettings = @user.settings.all
	@allsettings.each_key do |key|
		if @allsettings[key].blank? 
			resetdefault(@user, key)
		end
	end
  
	
	puts "Settings #{@allsettings}"
	@image_root = Rails.root.join('app', 'assets', 'images')
	Dir.mkdir(@image_test) unless File.exists?(@image_root)
	@output_folder = Rails.root.join('app', 'assets', 'images', "#{@user.id}", "#{@user.storm_num}")
	@input_folder = Rails.root.join('public', 'assets', 'images', "#{@user.id}").to_s
	Dir.mkdir(@output_folder) unless File.exists?(@output_folder)
	
	#This creates the path for the Wordstorm arguments to look
	args = "#{@input_folder}/ #{@output_folder}/ 100 #{@user.settings.maxwords} #{convertTfIdf(@user.settings.tfidf)} #{@user.settings.color} \"#{convertAlgo(@user.settings.algo)}\" #{@user.settings.tolerance} #{@user.settings.iterations} \"#{@user.settings.font}\" \"#{convertCase(@user.settings.lcase)}\" #{@user.settings.scale} #{@user.settings.angle}"
	#Use the jar located at the path lib/assets/test2.jar with the constraits from args
	#results = `java -jar #{Rails.root.join('lib', 'assets', 'wordstorms.jar').to_s} #{args}`
	results = "test"*300
	if (results.length > 300)
		@files = Dir.glob(@output_folder.to_s + '/*.png')
		#Create Wordstorm in database
		@wordstorm = @user.word_storms.find_or_create_by_file_location(:name => "Untitled", :cloud_id => @user.storm_num, :file_location => @output_folder.to_s)
		@wordstorm.save
	
		#Place the files for the database
		@files.each_with_index do |file, i|
			dbFile = @wordstorm.images.find_or_create_by_fileLocation(:name => file.split("/").last.to_s, :fileLocation => file.to_s, :local_num => i, :pos_x => i * 20, :pos_y => 10)
			dbFile.save
		end
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
