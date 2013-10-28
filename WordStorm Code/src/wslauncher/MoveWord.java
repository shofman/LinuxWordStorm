package wslauncher;
/*
 * Scott Hofman 2013
 * Loads a saved storm and moves it
 */
import io.DataPath;
import io.FileLoader;
import io.Loader;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import wordcram.CoordProp;
import wordcram.MyPApplet;
import wordcram.MyPVector;
import wordcram.WordStorm;
import wordstorm.AppletConf;
import wordstorm.Combined;
import wordstorm.StormConf;

public class MoveWord {

	/**
	 * @param args size of 6
	 * 
	 */
	public static void main(String args[]) {
			try {
				//Reset apConf
				AppletConf apConf = new AppletConf(640,480,255); //cloud width, height and bk color
				
				//Set dataPath
				DataPath.setOutputPath("C://Users/Scott/Desktop/");
				DataPath.setInputPath("");
				int id = 0;
				int wordxClick = 516;
				int wordyClick = 219;
				int movexClick = 200;
				int moveyClick = 300;
				
				if(args.length > 0) {
					if (args.length == 6) {
						DataPath.setOutputPath(args[0]);
						id = Integer.parseInt(args[1]);
						wordxClick= Integer.parseInt(args[2]);
						wordyClick= Integer.parseInt(args[3]);
						movexClick= Integer.parseInt(args[4]);
						moveyClick= Integer.parseInt(args[5]);				
					} else {
						System.exit(0);
					}
				
				}

				//Read in the wordstorm object
				FileInputStream fin = new FileInputStream(DataPath.getOutputPath()+"/wordstorm.ser");
				ObjectInputStream ois = new ObjectInputStream(fin);
				WordStorm storm = (WordStorm) ois.readObject();
				
				//Fetch previous index (empty because of failed serialization)
				Map<String, CoordProp>index = storm.getIndex();
				
				//Read data to update index and storm positions
				String[] lines = readFile(DataPath.getOutputPath()+"/wordlist.txt");
					
				for(int i=0; i<lines.length; i++) {
					String[] parts = lines[i].split("\\[");					//Split on the position vectors 
					String[] words = parts[0].split(" ");					//Split the first into cloud and word
					String[] position = parts[1].split(" ");				//Split the second into x and y positions for Rendering
					String[] target = parts[2].split(" ");					//Split the third into x and y positions for target place
					
					//Update index and renderedPosition from the data within the file of where it displayed to
					MyPVector renderedPlace = new MyPVector(Float.parseFloat(position[1].replace(',', ' ')), Float.parseFloat(position[2].replace(',', ' ')));
					storm.clouds[Integer.parseInt(words[0])].getWord(Integer.parseInt(position[5])).setRenderedPlace(renderedPlace);
					index.get(words[1]).setCurrentLocation(Integer.parseInt(words[0]), renderedPlace);

					//Update where it should be (necessary for calculations)
					MyPVector targetPlace = new MyPVector(Float.parseFloat(target[1].replace(',', ' ')), Float.parseFloat(target[2].replace(',', ' ')));
					storm.clouds[Integer.parseInt(words[0])].getWord(Integer.parseInt(position[5])).setTargetPlace(targetPlace);	
				}
					
				//Reset colours
				lines = readFile(DataPath.getOutputPath()+"/colorlist.txt");
				for (int i=0; i<lines.length; i++) {
					String[] parts = lines[i].split(" ");
					index.get(parts[0]).setColor(Integer.parseInt(parts[1]));
				}

				//Reset Storm
				StormConf conf = storm.stormConfig;
				Loader load = new FileLoader(storm);
				Combined f = new Combined(load, apConf,conf);
				f.init();
				storm.reloadStorm(index, (MyPApplet) f);
				Rectangle bBox = storm.setupReverseCoordinates();
				try {
					if (storm.scalingFactor == 0) storm.scalingFactor = 1.33333333333f;
					float adjustedClickX = (wordxClick / storm.scalingFactor) + bBox.x;
					float adjustedClickY = (wordyClick / storm.scalingFactor) + bBox.y;
					String move = storm.clouds[id].getRenderedWordAt(adjustedClickX, adjustedClickY).word;
					float adjustedMoveX = (movexClick / storm.scalingFactor) + bBox.x;
					float adjustedMoveY = (moveyClick / storm.scalingFactor) + bBox.y;
					storm.moveWord(move, new MyPVector(adjustedMoveX, adjustedMoveY));
					f.reloadStorm(storm);
					f.moveWordAlgorithm();
					fin.close();
					ois.close();
				} catch(NullPointerException e) {
					System.out.println("Word not found");
					System.exit(3);
				}
				
				//storm.moveWord("make", new MyPVector(100, 100));
				
				/**/
				

								
			} catch (FileNotFoundException e) {
				System.out.println("File not found error");
				e.printStackTrace();
				System.exit(2);
			} catch (IOException e) {
				System.out.println("Error reading file");
				e.printStackTrace();
				System.exit(2);
			} catch (ClassNotFoundException e) {
				System.out.println("No class found");
				e.printStackTrace();
				System.exit(1);
			}

		}
	
	public static double distance(float x, float y, float x1, float y1) {
		return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
	}
	
	public static String[] readFile(String fileLoc) {
		try {
			File file = new File(fileLoc);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();
			return stringBuffer.toString().split("\n");
		} catch (IOException e) {
			System.out.println("Cannot read file");
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Cannot read file");
		}
		return null;
	}
	
	//hue, sat, and bri are named as such to maintain coding standard with previous version
	//These are, in fact, rgb values. Treat them as such. 
	public static int getColor(StormConf conf, MyPApplet f, CoordProp p, float hue, float sat, float bri) {
		int color;
		float alpha = 255;
		if(conf.tfIdf == 2) {
			float A = 40;
			float K = 100;
			float S = 20;
			float x0 = 0.2f;
			float idf = p.getIdf();
			alpha = (255/K)*(A + (float) ((K-A)/(1+Math.exp(-S*(idf-x0)))));
		}
		//alpha = 50;
		
		color = f.colorer(hue, sat, bri, alpha);
		return color;
	}
	


}
