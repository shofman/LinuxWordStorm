class AddInfoToImages < ActiveRecord::Migration
  def change
    add_column :images, :local_num, :int
    add_column :images, :pos_x, :int
    add_column :images, :pos_y, :int
  end
end
