class SessionsController < ApplicationController
	before_filter :authenticate_user, :except => [:index, :login, :login_attempt, :logout]
	before_filter :save_login_state, :only => [:index, :login, :login_attempt]

	def home
		#Home Page
	end

	def profile
		@user = User.find(session[:user_id])
	end

	def setting
		#Setting Page
	end
	
	def save 
	#TODO: Needs to have a check here that the values cannot be greater than a certain value
		@user = User.find(session[:user_id])
		@user.settings.lcase = params[:scoped_settings][:lcase]
		@user.settings.tfidf = params[:scoped_settings][:tfidf]
		@user.settings.algo = params[:scoped_settings][:algo]
		@user.settings.tolerance = params[:scoped_settings][:tolerance]
		@user.settings.iterations = params[:scoped_settings][:iterations]
		@user.settings.maxwords= params[:scoped_settings][:maxwords]
		@user.settings.color = params[:scoped_settings][:color]
		@user.settings.angle = params[:scoped_settings][:angle]
		@user.settings.scale = params[:scoped_settings][:scale]	
		@user.settings.font = params[:scoped_settings][:font]
		@user.increase_count
		@user.save
	end 

	def login
		#Login Form
	end

	def login_attempt
		authorized_user = User.authenticate(params[:username_or_email],params[:login_password])
		if authorized_user
			session[:user_id] = authorized_user.id
			flash[:notice] = "Welcome #{authorized_user.username}"
			redirect_to(:root)
		else
			flash[:notice] = "Invalid Username or Password"
        	flash[:color]= "invalid"
			render "login"	
		end
	end

	def logout
		session[:user_id] = nil
		redirect_to :back
	end

end