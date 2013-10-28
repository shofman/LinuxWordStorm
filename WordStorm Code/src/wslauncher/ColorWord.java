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

public class ColorWord {

	/**
	 * @param args
	 * 0: Output path
	 * 1: Cloud id to change
	 * 2: X pos of user click 
	 * 3: Y pos of user click
	 * 4: Red color 
	 * 5: Blue color
	 * 6: Green color
	 */
	 
	public static void main(String args[]) {
			try {
				//Reset apConf
				AppletConf apConf = new AppletConf(640,480,255); //cloud width, height and bk color
				
				
				DataPath.setOutputPath("./newstorm");
				DataPath.setInputPath("");
				int id = 0;
				int moveClickX = 120; // 
				int moveClickY = 210; //
				int red = 0;
				int blue = 0;
				int green = 0;
				
				if(args.length > 0) {
					if (args.length == 7) {
						DataPath.setOutputPath(args[0]);
						id = Integer.parseInt(args[1]);
						moveClickX= Integer.parseInt(args[2]);
						moveClickY= Integer.parseInt(args[3]);
						red= Integer.parseInt(args[4]);
						blue= Integer.parseInt(args[5]);
						green = Integer.parseInt(args[6]);
						
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
					
				lines = readFile(DataPath.getOutputPath()+"/colorlist.txt");
				
				for (int i=0; i<lines.length; i++) {
					String[] parts = lines[i].split(" ");
					index.get(parts[0]).setColor(Integer.parseInt(parts[1]));
				}

				/*
				 * Reset storm, create original bounding box that scaled image up, find the word based on reversing that scaling, and set its color
				 */
				StormConf conf = storm.stormConfig;
				Loader load = new FileLoader(storm);
				Combined f = new Combined(load, apConf,conf);
				f.init();
				storm.reloadStorm(index, (MyPApplet) f);
				Rectangle bBox = storm.setupReverseCoordinates();
				
				try {
					if (storm.scalingFactor == 0) storm.scalingFactor = 1.33333333333f;
					float adjustedClickX = (moveClickX / storm.scalingFactor) + bBox.x;
					float adjustedClickY = (moveClickY / storm.scalingFactor) + bBox.y;
					String wordChoice = storm.clouds[id].getRenderedWordAt(adjustedClickX, adjustedClickY).word;
					Integer color = getColor(conf, f, index.get(wordChoice), red, green, blue);
					index.get(wordChoice).setColor(color);
					f.reloadStorm(storm);
					f.saveWordStorm();
					storm.saveWordData();
					storm.saveColorData();
					f.fraw();
				} catch (NullPointerException e) {
					System.out.println("Invalid click");
					System.exit(3);
				}
				ois.close();
				fin.close();
						
			} catch (FileNotFoundException e) {
				System.out.println("Error reading file - not found" + e);
				System.exit(2);
			} catch (IOException e) {
				System.out.println("Error reading file" + e);
				System.exit(2);
			} catch (ClassNotFoundException e) {
				System.out.println("No class found" + e);
				System.exit(1);
			}

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
			System.exit(2);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Cannot read file");
			System.exit(2);
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
		
		color = f.colorer(hue, sat, bri, alpha);
		return color;
	}
	


}
