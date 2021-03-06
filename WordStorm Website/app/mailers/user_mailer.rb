class UserMailer < ActionMailer::Base
  default from: "scott@wordstorm.com"
  require 'net/smtp'

  def welcome_email(user)
    @user = user
    @url  = "http://localhost:3000/login"
    mail(:to => user.email, :subject => "Welcome to My Awesome Site")
  end

  def share_storm(targetemail, stormurl)
	mgstr = "Check out this storm here #{stormurl}"
	Net::SMTP::start('localhost', 25) do |smtp|
		smtp.send_message mgstr, 'notifications@wordstorms.com', targetemail
   	end
  end 
end
