require 'controller_module'
class UsersController < ApplicationController
  	include ControllerModule
  before_filter :save_login_state, :only => [:new, :create]
  def new
	@user = User.new
  end
  def create
	@user = User.new(params[:user])
	setdefaults(@user)
	@user.storm_num = 0
	if @user.save
		
		flash[:notice] = "You just signed up"
		flash[:color] = "valid"
		redirect_to(:root)
	else
		flash[:notice] = "Form is invalid"
		flash[:color] = "invalid"
		render "new"
	end
	
  end

end
