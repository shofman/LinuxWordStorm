# Wordstorm App
## About the app

This application is designed to create Wordstorms (a series of interrelated word clouds), based on Joaquim Castella's word storm code, within a web browser. 
Using a jquery fileupload as a base, the user uploads text files to the server. The server then processes these files with Castella's code, to create wordstorms. 
Following this, the user can manipulate the word storms, and create a number of different users to share their storms. Dissertation project at the University of Edinburgh.
This app uses Rails 3.2.6.

## Sections

The rails code for creating the website is housed within the folder WordStorm Website.
Castella's code, with certain changes made to accommodate the website's functionality, is housed within the folder WordStorm Code. Further information can be found in the readme there.
The files used to create my dissertation paper in LaTeX is housed within the folder WordStorm Dissertation. 


## Use

The app uses the following projects: 

* [jquery-fileupload-rails](https://github.com/tors/jquery-fileupload-rails)
* [Paperclip](https://github.com/thoughtbot/paperclip)
* [twitter-bootstrap-rails](https://github.com/seyhunak/twitter-bootstrap-rails) 

### Running the website

    rake db:create
    rake db:migrate
    rails server
Open http:://localhost:3000
	
### Running the wordstorm code
Run StormLauncher.main with no arguments for the default storm (need to specify path to folder with text for wordstorm within code).
Else pass in the parameters for customizing storm.