class CreateWordStorms < ActiveRecord::Migration
  def change
    create_table :word_storms do |t|
      t.string :name
      t.integer :user_id
      t.integer :cloud_id

      t.timestamps
    end
  end
end
