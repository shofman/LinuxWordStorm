class Upload < ActiveRecord::Base
  belongs_to :user
  
  attr_accessible :upload
  has_attached_file :upload, {:url => "/assets/images/:user_id/:filename"}

  include Rails.application.routes.url_helpers

  def to_jq_upload
	#:url => "/system/#{read_attribute(:user_id)}/:filename"
    {
      "name" => read_attribute(:upload_file_name),
      "size" => read_attribute(:upload_file_size),
	  "userid" => read_attribute(:user_id),
      "url" => upload.url(:original),
      "delete_url" => upload_path(self),
      "delete_type" => "DELETE" 
    }
  end
  
  Paperclip.interpolates :user_id do |attachment, style|
	attachment.instance.user_id
  end

end
