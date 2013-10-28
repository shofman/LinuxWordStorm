package io;

import java.io.*;
import java.util.Arrays;

import wordcram.MyPApplet;

import wordcram.Word;
import wordcram.WordCounter;
import wordcram.WordCram;
import wordcram.WordSorterAndScaler;
import wordcram.WordCram.TextCase;
import wordcram.WordStorm;
import wordstorm.*;

/*
 * Quim Castella
 * 
 * Loads .txt files in the given folder
 * (Omits files starting by "_meta")
 */

public class FileLoader extends Loader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7465247844060687055L;
	private static String folder;
	public static String[] fileNames;
	private boolean fromStorm = false;

	public FileLoader(int maxFiles, String folder){
		FileLoader.folder = folder;
		this.maxFiles = maxFiles;
		//this.inPath = DataPath.dataPath()+"/input/"+folder+"/";
		this.inPath = DataPath.getInputPath();
		this.numClouds = loadFiles();
	}
	
	//Added by Scott - Used for when wordstorms are reloaded back into the code
	public FileLoader(WordStorm ws) {
		folder = "";
		maxFiles = ws.stormConfig.maxFiles;
		numClouds = ws.numClouds;
		fromStorm = true;
		fileNames = ws.fileNames;
		
	}
	
	private static String imgName(int cloudIndex){
		String name = fileNames[cloudIndex];
		return name.substring(0, name.lastIndexOf('.'))+".png";
	}
	
	@Override
	public String getOutput( StormConf conf, int cloudIndex ){
		if(DataPath.checkSet()) {
			return DataPath.getOutputPath()+"/"+imgName(cloudIndex);
		} else {
			//Default
			return DataPath.dataPath()+"/output/"+folder+"/"+conf+"/"+imgName(cloudIndex);
		}
	}
	
	@Override
	public String getOutputMovie( StormConf conf, int cloudIndex, int frameNum ){
		String movieName = imgName(cloudIndex);
		movieName = movieName.substring(0, movieName.lastIndexOf('.'))+
				movieNumber(frameNum)+".png";
		return DataPath.dataPath()+"/output/"+folder+"/"+
				conf+"/Movie/"+movieName;
	}
	@Override
	public String getLocalOutput( StormConf conf, int cloudIndex ){
		return conf+"/"+imgName(cloudIndex);
	}
	@Override
	public String getHTMLFolder(){
		if(DataPath.checkSet()) {
			return DataPath.getOutputPath()+"/";
		} else {
			//Default
			return DataPath.dataPath()+"/output/"+folder+"/";
		}
	}
	@Override
	public String getFolder(){
		return folder;
	}
	@Override
	public String getHTMLOutput(int maxWords){
		String printMF = maxFiles == -1 || numClouds < maxFiles ? "all" :
			""+numClouds;
		return DataPath.dataPath()+"/output/"+folder+"/comp "+printMF+"d.html";
	}
	@Override
	public String getStormHTML(StormConf conf){
		if(DataPath.checkSet()) {
			return DataPath.getOutputPath()+"/"+conf+".html";
		} else {
			//Default
			return DataPath.dataPath()+"/output/"+folder+"/"+ conf+".html";
		}
	}
	@Override
	public String getStormLog(StormConf conf){
		if(DataPath.checkSet()) {
			return DataPath.getOutputPath()+"/"+conf+".txt";
		} else {
			//Default
			return DataPath.dataPath()+"/output/"+folder+"/"+conf+".txt";
		}
	}
	
	private int loadFiles(){
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".txt") && !name.startsWith("_meta");
		    }
		};
		File directory = new File(inPath);  
		System.out.println(inPath);
		File[] files = directory.listFiles(filter);
		if(files == null){ 
			System.err.println("No files");
			System.exit(0);
		}
		Arrays.sort(files);
		int numClouds = maxFiles == -1 ? files.length : 
			Math.min(files.length, maxFiles);
		fileNames = new String[numClouds];
		
		System.out.println(numClouds+" files");
		for (int ii = 0; ii < numClouds; ++ii) {
			fileNames[ii] = files[ii].getName();
			//System.out.println(fileNames[ii]);
		}
		return numClouds;
	}
	
	public String getName(int index){
		return fileNames[index];
	}
	
	@Override
	public void loadText(MyPApplet parent, WordCram w, int index){
		if(!fromStorm) {
			String source = join(parent.
					loadStrings(inPath+"/"+fileNames[index]), ' ');		
			source = w.textCase == TextCase.Lower ? source.toLowerCase()
					: w.textCase == TextCase.Upper ? source.toUpperCase()
				    : source;
					
	//		w = w.withStopWords("london twitter added directory http://wefollow.com rt uk");
	//		w = w.withStopWords("quote");
			String extraStopWords = w.getExtraStopWords();
			boolean excludeNumbers = w.getExcludeNumbers();
			Word[] words = new WordCounter().withExtraStopWords(extraStopWords).
					shouldExcludeNumbers(excludeNumbers).count(source);
			words = new WordSorterAndScaler().sortAndScale(words,false);
			w.setWords(words);
		}
		
	}
	
	 public String join(String[] list, char separator) {
		    return join(list, String.valueOf(separator));
		  }


	 public String join(String[] list, String separator) {
		    StringBuffer buffer = new StringBuffer();
		    for (int i = 0; i < list.length; i++) {
		      if (i != 0) buffer.append(separator);
		      buffer.append(list[i]);
		    }
		    return buffer.toString();
		  }
}
