class Image < ActiveRecord::Base
  attr_accessible :fileLocation, :imgdata, :name
  belongs_to :word_storm
end
