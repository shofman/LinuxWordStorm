class AddFileLocationToWordStorms < ActiveRecord::Migration
  def change
    add_column :word_storms, :file_location, :string
  end
end
