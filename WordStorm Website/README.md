## About the app

This application is designed to create Wordstorms (a series of interrelated word clouds), based on Joaquim Castella's word storm code, within a web browser. 
Using a jquery fileupload as a base, the user uploads text files to the server. The server then processes these files with Castella's code, to create wordstorms. 
Following this, the user can manipulate the word storms, and create a number of different users to share their storms. Dissertation project at the University of Edinburgh.
This app uses Rails 3.2.6.

* [jquery-fileupload-rails](https://github.com/tors/jquery-fileupload-rails)
* [Paperclip](https://github.com/thoughtbot/paperclip)
* [twitter-bootstrap-rails](https://github.com/seyhunak/twitter-bootstrap-rails) 

## Running the app

    rake db:create
    rake db:migrate
    rails server

Open http:://localhost:3000