class GalleryController < ApplicationController
  respond_to :html, :xml, :json
  def view
	if (params.has_key?(:wordstorm))
		@storm = WordStorm.find(Integer(params[:stormid]))
		@curr_storm_id = params[:stormid]
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
		if !params[:stormid].nil?
			@stormid = Integer(params[:stormid])-1
			if @stormid < WordStorm.first.id
				@stormid = WordStorm.last.id
			end
			@storm = WordStorm.find(@stormid)
		else
			@storm = WordStorm.last
			flash[:notice] = "There's an error in the database"
		end
		@curr_storm_id = @storm.id
	elsif (params.has_key?(:front))
		if !params[:stormid].nil?
			@stormid = Integer(params[:stormid])+1
			if @stormid > WordStorm.last.id
				@stormid = WordStorm.first.id
			end
			@storm = WordStorm.find(@stormid)
		else
			@storm = WordStorm.first
			flash[:notice] = "There's an error in the database"
		end
		@curr_storm_id = @storm.id
	else
		#Viewing as normal
		#TODO make this generic so users don't have to login
		@user = User.find(session[:user_id])
		@storm = @user.word_storms.last
		@curr_storm_id = @storm.id
	end
	@files = Dir.glob(@storm.file_location + '/*.png')
	
	#render :json => @storm
  end

  def edit
  end
end
