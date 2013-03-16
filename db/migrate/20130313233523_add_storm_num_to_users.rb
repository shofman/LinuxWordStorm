class AddStormNumToUsers < ActiveRecord::Migration
  def change
    add_column :users, :storm_num, :integer
  end
end
