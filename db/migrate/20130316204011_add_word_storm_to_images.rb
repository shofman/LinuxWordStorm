class AddWordStormToImages < ActiveRecord::Migration
  def change
    add_column :images, :word_storm_id, :int
  end
end
