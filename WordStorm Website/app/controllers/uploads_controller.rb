class UploadsController < ApplicationController

  before_filter :authenticate_user
  before_filter :find_user
  # GET /uploads
  # GET /uploads.json
  def index
	
    @uploads = @user.uploads.all
	@lcase = @user.settings.lcase
	@tfidf = @user.settings.tfidf
	@algo = @user.settings.algo
	@tolerance = @user.settings.tolerance
	@iterations = @user.settings.iterations
	@maxwords = @user.settings.maxwords
	@color = @user.settings.color
	@angle  = @user.settings.angle
	@scale = @user.settings.scale
	@font = @user.settings.font
	@settings = @user.settings
	@fontchoices = ['Aharoni Bold', 'Andalus', 'Angsana New', 'Angsana New Bold', 'Angsana New Bold Italic', 'Angsana New Italic', 'AngsanaUPC', 'AngsanaUPC Bold', 'AngsanaUPC Bold Italic', 'AngsanaUPC Italic', 'Arabic Typesetting', 'Arial', 'Arial Black', 'Arial Bold', 'Arial Bold Italic', 'Arial Italic', 'Arial Narrow', 'Arial Narrow Bold', 'Arial Narrow Bold Italic', 'Arial Narrow Italic', 'Arial Unicode MS', 'Batang', 'BatangChe', 'Book Antiqua', 'Book Antiqua Bold', 'Book Antiqua Bold Italic', 'Book Antiqua Italic', 'Bookman Old Style', 'Bookman Old Style Bold', 'Bookman Old Style Bold Italic', 'Bookman Old Style Italic', 'Bookshelf Symbol 7', 'Bradley Hand ITC', 'Browallia New', 'Browallia New Bold', 'Browallia New Bold Italic', 'Browallia New Italic', 'BrowalliaUPC', 'BrowalliaUPC Bold', 'BrowalliaUPC Bold Italic', 'BrowalliaUPC Italic', 'Calibri', 'Calibri Bold', 'Calibri Bold Italic', 'Calibri Italic', 'Cambria', 'Cambria Bold', 'Cambria Bold Italic', 'Cambria Italic', 'Cambria Math', 'Candara', 'Candara Bold', 'Candara Bold Italic', 'Candara Italic', 'Century', 'Century Gothic', 'Century Gothic Bold', 'Century Gothic Bold Italic', 'Century Gothic Italic', 'ChunkFive', 'Comic Sans MS', 'Comic Sans MS Bold', 'Consolas', 'Consolas Bold', 'Consolas Bold Italic', 'Consolas Italic', 'Constantia', 'Constantia Bold', 'Constantia Bold Italic', 'Constantia Italic', 'Corbel', 'Corbel Bold', 'Corbel Bold Italic', 'Corbel Italic', 'Cordia New', 'Cordia New Bold', 'Cordia New Bold Italic', 'Cordia New Italic', 'CordiaUPC', 'CordiaUPC Bold', 'CordiaUPC Bold Italic', 'CordiaUPC Italic', 'Courier New', 'Courier New Bold', 'Courier New Bold Italic', 'Courier New Italic', 'DFKai-SB', 'DaunPenh', 'David', 'David Bold', 'DejaVu Sans', 'DejaVu Sans Bold', 'DejaVu Sans Bold Oblique', 'DejaVu Sans Condensed', 'DejaVu Sans Condensed Bold', 'DejaVu Sans Condensed Bold Oblique', 'DejaVu Sans Condensed Oblique', 'DejaVu Sans ExtraLight', 'DejaVu Sans Mono', 'DejaVu Sans Mono Bold', 'DejaVu Sans Mono Bold Oblique', 'DejaVu Sans Mono Oblique', 'DejaVu Sans Oblique', 'DejaVu Serif', 'DejaVu Serif Bold', 'DejaVu Serif Bold Italic', 'DejaVu Serif Condensed', 'DejaVu Serif Condensed Bold', 'DejaVu Serif Condensed Bold Italic', 'DejaVu Serif Italic', 'DejaVu Serif Italic Condensed', 'Dialog.bold', 'Dialog.bolditalic', 'Dialog.italic', 'Dialog.plain', 'DialogInput.bold', 'DialogInput.bolditalic', 'DialogInput.italic', 'DialogInput.plain', 'DilleniaUPC', 'DilleniaUPC Bold', 'DilleniaUPC Bold Italic', 'DilleniaUPC Italic', 'DokChampa', 'Dotum', 'DotumChe', 'Estrangelo Edessa', 'EucrosiaUPC', 'EucrosiaUPC Bold', 'EucrosiaUPC Bold Italic', 'EucrosiaUPC Italic', 'Euphemia', 'FangSong', 'FrankRuehl', 'Franklin Gothic Medium', 'Franklin Gothic Medium Italic', 'FreesiaUPC', 'FreesiaUPC Bold', 'FreesiaUPC Bold Italic', 'FreesiaUPC Italic', 'Freestyle Script', 'French Script MT', 'Garamond', 'Garamond Bold', 'Garamond Italic', 'Gautami', 'Gentium Basic', 'Gentium Basic Bold', 'Gentium Basic Bold Italic', 'Gentium Basic Italic', 'Gentium Book Basic', 'Gentium Book Basic Bold', 'Gentium Book Basic Bold Italic', 'Gentium Book Basic Italic', 'Georgia', 'Georgia Bold', 'Georgia Bold Italic', 'Georgia Italic', 'Gisha', 'Gisha Bold', 'Gulim', 'GulimChe', 'Gungsuh', 'GungsuhChe', 'Impact', 'IrisUPC', 'IrisUPC Bold', 'IrisUPC Bold Italic', 'IrisUPC Italic', 'Iskoola Pota', 'JasmineUPC', 'JasmineUPC Bold', 'JasmineUPC Bold Italic', 'JasmineUPC Italic', 'Juice ITC', 'KaiTi', 'Kalinga', 'Kartika', 'KodchiangUPC', 'KodchiangUPC Bold', 'KodchiangUPC Bold Italic', 'KodchiangUPC Italic', 'Kristen ITC', 'Latha', 'Leelawadee', 'Leelawadee Bold', 'Levenim MT', 'Levenim MT Bold', 'LilyUPC', 'LilyUPC Bold', 'LilyUPC Bold Italic', 'LilyUPC Italic', 'Lucida Console', 'Lucida Handwriting Italic', 'Lucida Sans Regular', 'Lucida Sans Unicode', 'MS Gothic', 'MS LineDraw', 'MS Mincho', 'MS PGothic', 'MS PMincho', 'MS Reference Sans Serif', 'MS Reference Specialty', 'MS UI Gothic', 'MT Extra', 'MV Boli', 'Malgun Gothic', 'Malgun Gothic Bold', 'Mangal', 'Marlett', 'Meiryo', 'Meiryo Bold', 'Meiryo Bold Italic', 'Meiryo Italic', 'Meiryo UI', 'Meiryo UI Bold', 'Meiryo UI Bold Italic', 'Meiryo UI Italic', 'Microsoft Himalaya', 'Microsoft JhengHei', 'Microsoft JhengHei Bold', 'Microsoft Sans Serif', 'Microsoft Uighur', 'Microsoft YaHei', 'Microsoft YaHei Bold', 'Microsoft Yi Baiti', 'MingLiU', 'MingLiU-ExtB', 'MingLiU_HKSCS', 'MingLiU_HKSCS-ExtB', 'Miriam', 'Miriam Fixed', 'Mistral', 'Mongolian Baiti', 'Monospaced.bold', 'Monospaced.bolditalic', 'Monospaced.italic', 'Monospaced.plain', 'Monotype Corsiva', 'MoolBoran', 'NSimSun', 'Narkisim', 'Nyala', 'OpenSymbol', 'PMingLiU', 'PMingLiU-ExtB', 'Palatino Linotype', 'Palatino Linotype Bold', 'Palatino Linotype Bold Italic', 'Palatino Linotype Italic', 'Papyrus', 'Plantagenet Cherokee', 'Pristina', 'Raavi', 'Rod', 'SansSerif.bold', 'SansSerif.bolditalic', 'SansSerif.italic', 'SansSerif.plain', 'Segoe Print', 'Segoe Print Bold', 'Segoe Script', 'Segoe Script Bold', 'Segoe UI', 'Segoe UI Bold', 'Segoe UI Bold Italic', 'Segoe UI Italic', 'Serif.bold', 'Serif.bolditalic', 'Serif.italic', 'Serif.plain', 'Shruti', 'SimHei', 'SimSun', 'SimSun-ExtB', 'Simplified Arabic', 'Simplified Arabic Bold', 'Simplified Arabic Fixed', 'Sylfaen', 'Symbol', 'Tahoma', 'Tahoma Bold', 'Tempus Sans ITC', 'Times New Roman', 'Times New Roman Bold', 'Times New Roman Bold Italic', 'Times New Roman Italic', 'Traditional Arabic', 'Traditional Arabic Bold', 'Trebuchet MS', 'Trebuchet MS Bold', 'Trebuchet MS Bold Italic', 'Trebuchet MS Italic', 'Tunga', 'Verdana', 'Verdana Bold', 'Verdana Bold Italic', 'Verdana Italic', 'Vrinda', 'Webdings', 'Wingdings', 'Wingdings 2', 'Wingdings 3']
	
	

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @uploads.map{|upload| upload.to_jq_upload } }
    end
  end

  # GET /uploads/1
  # GET /uploads/1.json
  def show
	@upload = @user.uploads.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @upload }
    end
  end

  # GET /uploads/new
  # GET /uploads/new.json
  def new
    @upload = @user.uploads.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @upload }
    end
  end

  # GET /uploads/1/edit
  def edit
    @upload = @user.uploads.find(params[:id])
  end

  # POST /uploads
  # POST /uploads.json
  def create
    @upload = @user.uploads.new(params[:upload])

    respond_to do |format|
      if @upload.save
        format.html {
          render :json => [@upload.to_jq_upload].to_json,
          :content_type => 'text/html',
          :layout => false
        }
        format.json { render json: [@upload.to_jq_upload].to_json, status: :created, location: @upload }
      else
        format.html { render action: "new" }
        format.json { render json: @upload.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /uploads/1
  # PUT /uploads/1.json
  def update
    @upload = @user.uploads.find(params[:id])

    respond_to do |format|
      if @upload.update_attributes(params[:upload])
        format.html { redirect_to @upload, notice: 'Upload was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @upload.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /uploads/1
  # DELETE /uploads/1.json
  def destroy
    @upload = @user.uploads.find(params[:id])
    @upload.destroy

    respond_to do |format|
      format.html { redirect_to uploads_url }
      format.json { head :no_content }
    end
  end
  
  def find_user
	@user = User.find(session[:user_id])
  end
  
end
