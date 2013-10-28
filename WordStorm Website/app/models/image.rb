class Image < ActiveRecord::Base
  attr_accessible :fileLocation, :name, :local_num, :pos_x, :pos_y, :word_storm_id 
  belongs_to :word_storm
  
  validates :fileLocation, :presence => true
  validates :local_num, :presence => true
end
