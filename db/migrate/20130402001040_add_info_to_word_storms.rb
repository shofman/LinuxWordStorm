class AddInfoToWordStorms < ActiveRecord::Migration
  def change
    add_column :word_storms, :version, :int
    add_column :word_storms, :algorithm, :int
  end
end
