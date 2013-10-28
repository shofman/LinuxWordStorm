package io;

import java.io.Serializable;
/*
 * Data path to the folder that contains 
 * the input and where the output will be stored
 */
public class DataPath implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -618500873497505144L;
	static String root =  "/home/quim/Documents/workspace/WordStorm/examples/data";
	private static String input = root + "/input/"; 
	private static String output = root + "/output/";
	private static boolean hasSetInput = false;
	private static boolean hasSetOutput = false;
	
	public static boolean checkSet() {
		return hasSetInput && hasSetOutput; 
	}
	
	public static String dataPath() {
		return root;		
	}
	
	public static void setDathPath(String dataPath) {
		root = dataPath;
		input = root + "/input/";
	}
	
	public static String getInputPath() {
		return input;
	}
	
	public static void setInputPath(String dataPath) {
		hasSetInput = true;
		input = dataPath;
	}
	
	public static String getOutputPath() {
		return output;
	}
	
	public static void setOutputPath(String dataPath) {
		hasSetOutput = true;
		output = dataPath;
	}
}
