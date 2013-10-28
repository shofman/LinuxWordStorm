package wslauncher;

import io.*;
import wordstorm.*;

/*
 * Quim Castella
 * 
 * Storm generator
 */
/*
 * Scott Hofman 2013
 * Added passing of parameters to customize the storm
 */
public class StormLauncher {
	
	public static void main(String[] args) {			
		String folder = ""; //set the path to the data with io.DataPath and folder
		AppletConf apConf = new AppletConf(640,480,255); //cloud width, height and bk color 
		
		StormConf conf = new StormConf(); //Storm Parameters
		conf.maxFiles = -1;
		conf.maxWords = 25;
//		conf.rSeed = 12345;
		conf.tfIdf = 2;
		conf.coorColorer = 1;
		conf.coorAngler = 1;
		conf.coorPlacer = 4; //4 combined alg	
		conf.tol = 50;
		conf.maxIterations = 5;
		conf.font = "ChunkFive";

		//Change these to input
		DataPath.setInputPath("./data");
		DataPath.setOutputPath("./newstorm");
		
		if(args.length > 0) {
			if (args.length == 13) {
				DataPath.setInputPath(args[0]);
				DataPath.setOutputPath(args[1]);
				conf.maxFiles = Integer.parseInt(args[2]);
				conf.maxWords = Integer.parseInt(args[3]);
				conf.tfIdf = Integer.parseInt(args[4]);
				conf.coorColorer = Integer.parseInt(args[5]);
				conf.coorPlacer = Integer.parseInt(args[6]);
				conf.tol = Integer.parseInt(args[7]);
				conf.maxIterations = Integer.parseInt(args[8]);
				conf.font = args[9];
				conf.textCase = Integer.parseInt(args[10]);
				conf.scale = Integer.parseInt(args[11]);
				conf.coorAngler = Integer.parseInt(args[12]);
				
			} else {
				System.exit(0);
			}
		}
		Loader load = new FileLoader(conf.maxFiles, folder);
//		load = new FileLoaderWeight(conf.maxFiles, folder);

		
		
		Algorithm f;		
		if(conf.coorPlacer <= 1)
			f = new Iterative(load, apConf, conf);
		else if(conf.coorPlacer == 3) 
			f = new Force(load, apConf, conf);
		else 
			f = new Combined(load, apConf,conf);	
		f.init(); 
		f.initProcess();	
		//HTMLSaver.singleStorm(load, conf, apConf);
	}
}