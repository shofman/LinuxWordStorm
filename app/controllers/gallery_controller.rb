class GalleryController < ApplicationController
  def view
	if (params.has_key?(:wordstorm))
		@storm = WordStorm.find(Integer(params[:stormid]))
		@params = params
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
	else 
		#Viewing as normal
		#TODO make this generic so users don't have to login
		@user = User.find(session[:user_id])
		@storm = @user.word_storms.first
	end
	@files = Dir.glob(@storm.file_location + '/*.png')
  end

  def edit
  end
end
