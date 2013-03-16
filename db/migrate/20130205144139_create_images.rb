class CreateImages < ActiveRecord::Migration
  def change
    create_table :images do |t|
      t.string :name
      t.string :fileLocation

      t.timestamps
    end
  end
end
