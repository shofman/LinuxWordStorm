class AddSizeToWordStorm < ActiveRecord::Migration
  def change
    add_column :word_storms, :size, :int
  end
end
