class WordStorm < ActiveRecord::Base
  attr_accessible :cloud_id, :name, :user_id, :file_location
  
  has_many :images, :dependent => :destroy
  belongs_to :user
  
  #validates :cloud_id, :presence => true
  validates :user_id, :presence => true
  validates :file_location, :uniqueness => true
  
end
