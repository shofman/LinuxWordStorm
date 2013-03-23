class GalleryController < ApplicationController
  respond_to :html, :xml, :json
    before_filter :authenticate_user, :except => [:all]
  def view
	@user = User.find(session[:user_id])
    if @user.word_storms.size > 0
		if (params.has_key?(:wordstorm))
			@storm = WordStorm.find(Integer(params[:stormid]))
			@user = User.find(@storm.user_id)
			#Update storm and image positions based on create
			@storm.name = params[:wordstorm][:name]
			@storm.images.each_with_index do |image, i|
				if params.has_key?(("Left"+i.to_s).to_sym) && params.has_key?(("Left"+i.to_s).to_sym)
					image.pos_x = params[("Left"+i.to_s).to_sym]
					image.pos_y = params[("Top"+i.to_s).to_sym]
					image.save
				end
			end
			@storm.save
		elsif (params.has_key?(:back))
			@user = User.find(session[:user_id])
			if !params[:stormid].nil?
				@stormid = Integer(params[:stormid])-1
				if @stormid < @user.word_storms.first.cloud_id
					@stormid = @user.word_storms.last.cloud_id
				end
				@storm = @user.word_storms.find_by_cloud_id(@stormid)
			else
				@storm = @user.word_storms.first
				flash[:notice] = "There's an error in the database"
			end
		elsif (params.has_key?(:front))
			@user = User.find(session[:user_id])
			if !params[:stormid].nil?
				@stormid = Integer(params[:stormid])+1
				if @stormid > @user.word_storms.last.cloud_id
					@stormid = @user.word_storms.first.cloud_id
				end
				@storm = @user.word_storms.find_by_cloud_id(@stormid)
			else
				@storm = @user.word_storms.last
				flash[:notice] = "There's an error in the database"
			end
		else
			#Viewing as normal
			@user = User.find(session[:user_id])
			@storm = @user.word_storms.last
		end
		if @storm.nil?
			flash[:notice] = "No storm found"
			redirect_to :action => :view
		else 
			@curr_storm_id = @storm.cloud_id
			@files = Dir.glob(@storm.file_location + '/*.png')
		end
		
	else 
		flash[:notice] = "No storms exist yet. Try Creating One"
		redirect_to "/upload"

	end
	#render :json => @storm
  end

  def edit
	puts params
	if (params.has_key?(:stormedit))
		@user = User.find(session[:user_id])
  		@wordstorm = @user.word_storms.find_by_cloud_id(Integer(params[:stormedit]))
		if @wordstorm.nil?
			flash[:notice] = "No storm exists" 
			redirect_to :action => :view
		else 
			@files = Dir.glob(@wordstorm.file_location.to_s + '/*.png')
		end
	elsif (params.has_key?(:plus))
		@user = User.find(session[:user_id])
		@wordstorm = @user.word_storms.find_by_cloud_id(Integer(params[:plus]))
		@wordstorm.size += 40
		if @wordstorm.size > 640
			flash[:notice] = "Cannot go larger"
			@wordstorm.size = 640
		end
		@wordstorm.save
		@files = Dir.glob(@wordstorm.file_location.to_s + '/*.png')
	elsif (params.has_key?(:minus))
		@user = User.find(session[:user_id])
		@wordstorm = @user.word_storms.find_by_cloud_id(Integer(params[:minus]))
		@wordstorm.size -= 40
		if @wordstorm.size < 80
			flash[:notice] = "Cannot go smaller"
			@wordstorm.size = 80
		end
		@wordstorm.save
		@files = Dir.glob(@wordstorm.file_location.to_s + '/*.png')
	else
		redirect_to :action => :view
	end
  end
  
  
  def all
	if WordStorm.count > 0
		if cookies[:gallery].blank?
			cookies[:gallery] = 0
		end
		if (params.has_key?(:back))
			if !params[:stormid].nil?
				@stormid = Integer(params[:stormid])-1
				if @stormid < WordStorm.first.id
					@stormid = WordStorm.last.id
				end
				@storm = WordStorm.find(@stormid)
			else
				@storm = WordStorm.first
				flash[:notice] = "There's an error in the database"
			end
		elsif (params.has_key?(:front))
			if !params[:stormid].nil?
				@stormid = Integer(params[:stormid])+1
				if @stormid > WordStorm.last.id
					@stormid = WordStorm.first.id
				end
				@storm = WordStorm.find(@stormid)
			else
				@storm = WordStorm.last
				flash[:notice] = "There's an error in the database"
			end
		else
			begin 
				@storm = WordStorm.find(Integer(cookies[:gallery]))
			rescue
				flash[:notice] = "cookie error"
				@storm = WordStorm.first
			end
		end
		@name = User.find(@storm.user_id).username
		@curr_storm_id = @storm.id
		cookies[:gallery] = @storm.id
		@files = Dir.glob(@storm.file_location + '/*.png')

	else 
		flash[:notice] = WordStorm.count
		redirect_to "/home"
	end
   end
   
   def move
		if params.has_key?(:fClickX) and params.has_key?(:fClickY) and params.has_key?(:sClickX) and params.has_key?(:sClickY) and params.has_key?(:storm) and params.has_key?(:cloud)
			storm = WordStorm.find(params[:storm])
			output_folder = storm.file_location
			args = "#{output_folder} #{Integer(params[:cloud])} #{Integer(params[:fClickX])} #{Integer(params[:fClickY])} #{Integer(params[:sClickX])} #{Integer(params[:sClickY])}"
			results = `java -jar #{Rails.root.join('lib', 'assets', 'move.jar').to_s} #{args}`
			redirect_to "/gallery/edit?stormedit=" + storm.cloud_id.to_s
		else 
			redirect_to :all
		end
   end
   
   def color
		if params.has_key?(:color) and params.has_key?(:storm) and params.has_key?(:cloud) and params.has_key?(:one) and params.has_key?(:two)
			storm = WordStorm.find(params[:storm])
			output_folder = storm.file_location
			args = "#{output_folder} #{Integer(params[:cloud])} #{Integer(params[:one])} #{Integer(params[:two])} #{Integer(params[:color][:red])} #{Integer(params[:color][:blue])} #{Integer(params[:color][:green])}"
			results = `java -jar #{Rails.root.join('lib', 'assets', 'color.jar').to_s} #{args}`
			redirect_to "/gallery/edit?stormedit=" + storm.cloud_id.to_s
		else
			redirect_to :all
		end
   end
end
