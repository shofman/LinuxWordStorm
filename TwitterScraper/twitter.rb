#Code adapted from http://ruby.bastardsbook.com/chapters/intro_tweet_fetch/#download-tweets
#			  from http://simonstarr.com/2009/06/29/using-ruby-and-libxml-to-parse-xml-from-friendfeed/

require 'rubygems'
require 'open-uri'
require 'nokogiri'


#Function parses twitter username (if of the form [http[s]://]twitter). Assumes well formed and valid user (still needs check)
def checkURL(twitter_user)
	checker = twitter_user.to_s
	if checker.start_with?("http://") or checker.start_with?("https://") or checker.start_with?("twitter.")
		return checker[checker.rindex('/')+1..checker.length]
	else 
		return checker
	end
end

#Function
def parseCreateYear(created, text, hash)
	datechecker = created.to_s
	textchecker = text.to_s
	@year = ""
	@month = ""
	@parseText = ""
	
	#parse the text information
	leftindex = textchecker.index("<text>")
	rightindex = textchecker.index("</text>")
	if  leftindex != nil and rightindex != nil
		@parseText = textchecker[leftindex+6..rightindex-1]
	else
		@parseText = ""
	end
	
	#Make sure that it's well formed
	if datechecker.index("<created_at>") != nil and datechecker.index("</created_at>") != nil
		@splitCreate = datechecker[0..datechecker.index("</created_at>")-1].split
		@year = @splitCreate.last
		@month = @splitCreate[1]
	else
		@year = "Unknown"
		@month = "Unknown"
	end

	if hash.has_key?(@year.to_s + " " + @month.to_s)
		hash[@year.to_s + " " + @month.to_s] << @parseText
	else
		hash[@year.to_s + " " + @month.to_s] = [@parseText]
	end
	
	return hash
	
end


remote_base_url = "http://api.twitter.com/1/statuses/user_timeline.xml?include_rts=1&count=200&screen_name="
puts "Please enter a twitter user name: "

twitter_user = gets.chomp
result = checkURL(twitter_user)
remote_full_url = remote_base_url + result

xmlResult = open(remote_full_url).read
@doc = Nokogiri::XML(xmlResult)
#Needs error checking here
#@doc = Nokogiri::XML(File.open("user_timeline.xml"))

statuses = @doc.xpath("//status")
#puts statuses
myHash = Hash.new("No data")

for x in statuses
	myHash = parseCreateYear(x.xpath("created_at"), x.xpath("text"), myHash)
end

puts myHash.keys
for x in myHash.keys
	my_local_file = open(x + ".txt", "w")
	myHash[x].each {|text| my_local_file.write(text+" ")}
	#my_local_file.write()    
	my_local_file.close
end

#uri = URI.parse('http://api.twitter.com/1.1/statuses/user_timeline.json')
#params = {:screen_name => "fractallight", :count => "2"}

#puts Net::HTTP.get(uri)

#uri.query = URI.encode_www_form(params)
#puts uri.open.read


#puts "Please enter a user page to scan: "
#url = gets.chomp
#puts url


#?screen_name=twitterapi&count=2

=begin


	
	
(first_page..last_page).each do |page_num|
    
   puts "Downloading page: " + page_num.to_s
   puts remote_base_url + result + "&page=" + page_num.to_s
   #xmlResult = open(remote_base_url + result).read
   #@doc = Nokogiri::XML(xmlResult)
   #Needs error checking here
   @doc = Nokogiri::XML(File.open("user_timeline.xml"))
   
   
   tweets = @doc.xpath("//text")
   dates = 	@doc.xpath("//created_at")
   
   if tweets.length != dates.length
		break
   end
   
   content = source.parse
   
   entries = content.root.find('./status')
   entries.each do |entry|
		text = entry.find_first('text').content
		puts text
	end

   my_local_filename = result + "-tweets-page-" + page_num.to_s + ".xml"
   my_local_file = open(my_local_filename, "w")
   my_local_file.write(tweets)    
   my_local_file.close
   
   sleep 5
   end
=end