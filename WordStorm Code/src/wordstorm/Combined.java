package wordstorm;

import io.*;
import java.io.*;
import java.awt.Rectangle;
import wordcram.*;

/*
 * Quim Castella
 * 
 * Combined Layout WordStorm
 * 
 * Initialisation by the Iterative Algorithm,
 * continued by the force layout
 */
/*
 * Scott Hofman 2013
 * Made serializable. Added reloading of storm. Added saving of storm. Split the algorithm into logical sections
 */
public class Combined extends MyPApplet implements Algorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8032157674874584757L;
	StormConf conf;
	int process = 50; //Frequency updates at the console
	boolean verbose = true;
	boolean printIndex = false;
	boolean movie = false; //To print a movie of the process
	boolean pLog = false;
	int frames = 1; //One movie frame for each "frames" iterations
	Timer time;
	String log;
	
	Loader load;
	AppletConf apConf;
	AppletConf desApConf;

	private WordStorm storm;
	public MDS mds;

	public Combined(Loader l, AppletConf apConf, StormConf conf) {
		this.load = l;
		this.apConf = new AppletConf(apConf.width/2,apConf.height/2,apConf.bgCol);
		this.desApConf = apConf;
		this.conf = conf;
		this.randomSeed(conf.rSeed);
		logHeader();
	}

	public void setup() {
		size(apConf.width, apConf.height);
		smooth();
	}

	public WordStorm getWordStorm() {
		return storm;
	}

	public void initProcess() {
		time = new Timer();
		initStorm();
		initCoordination();
		storm.coordinatedPlacer();
		storm.separateUnique();
		
		int cont = mainAlgorithm();
		
		storm.placeUnique(conf.rSeed);
		Rectangle bBox = storm.setupReverseCoordinates();
		float scale = (float) Math.min(desApConf.height*1.0/bBox.height,desApConf.width*1.0/bBox.width);
		storm.scalingFactor = scale;

		saveWordStorm();
		storm.saveWordData();
		
		System.out.println("Iterations Stopped");
		if(movie) fraw((cont/frames)+1);
		fraw();
		storm.saveColorData();
		time.stop();
		//System.out.println("Finished");

		String t = "Time: " + time;
		System.out.println(t);
		if(pLog) printLog(t);
	}
	
	private int mainAlgorithm() {
		int cont = 0;
		if(verbose) System.out.println(++cont+" Iteration");
		storm.placeWords(printIndex, verbose);
		if(movie) fraw(1);
		int nConverged = storm.nConverged();
		if(verbose) printProgress(nConverged, mds.getScoreMDS());	
		
		int maxIter = conf.maxIterations == -1? 10000 : conf.maxIterations;
		placeWords(nConverged, cont, maxIter);

		changeForce();	
		System.out.println("-Force Algorithm-");
		System.out.println(++cont + " Iteration");
		storm.moveWords(verbose);		
		if(movie) fraw(1);
		printProgress(mds.getScoreMDS(), mds.getScoreOverlap(), 
				mds.getGradient(), mds.getScoreCompact(),-1);
		
		gradientAlgorithm(cont);
		return cont;
	}
	
	public void placeWords(int nConverged, int cont, int maxIter) {
		while(conf.tol!= -1 && nConverged!=0 && cont < maxIter){
			if(verbose) System.out.println(++cont+" Iteration");
			storm.placeWords(printIndex, verbose);
			if(movie && (cont%frames)==0) fraw(cont/frames);	
			nConverged = storm.nConverged();
			if(verbose) printProgress(nConverged, mds.getScoreMDS());
		}
	}
	
	public void gradientAlgorithm(int cont) {
//		Set of optimization problems
//		// 					0   1   2  3	  4		5	6	 7
		float[] overEps = {0, 0.1f, 0, 1,    1,     1,     10};// Overlap lambda
		float[] sameEps = {1,   1,  1, 1,    0.1f,  0,     0};// Stress and correspondence
		float[] compEps = {0, 0.5f, 0, 0.5f, 0.5f,  0.9f,  0};// Compactness
		
		int end = 0;
		int maxIter = 1000000;
		for(int ii=0; ii<overEps.length; ++ii) {
			mds.overEps = overEps[ii];
			mds.sameEps = sameEps[ii];
			mds.compEps = compEps[ii];
			mds.setGradient(100);		
			if(ii == overEps.length-1) end = 1;
			if(ii == overEps.length-2) end = 2;
			while(!mds.converged(end) && cont < maxIter) {
				++cont;
				if((cont%process)==0) System.out.println(cont + " Iteration");
			 	mds.nextIteration();
				storm.moveWords(verbose);
				if(movie && (cont%frames)==0) fraw(cont/frames);			
			/*	if((cont%process)==0) {
					printProgress(mds.getScoreMDS(), mds.getScoreOverlap(),
							mds.getGradient(),mds.getScoreCompact(),ii);
					System.out.println("Single overlap " + mds.getOverlap());
				}*/
			}
		}
	}

	private void initStorm() {
		storm = new WordStorm(this);
		storm.loadText(conf, load);
		storm.cloudSettings(conf, load);
		mds = new MDS(storm, apConf, conf);
	}
	
	public void reloadStorm(WordStorm s) {
		this.storm = s;
		mds = new MDS(storm, apConf, conf);
		storm.startCoordination(mds, conf.rSeed, conf.tfIdf);
	}

	private void initCoordination() {
		storm.createIndex();
		storm.startCoordination(mds, conf.rSeed, conf.tfIdf);
		if (conf.tfIdf == 2)
			storm.cloudIdf();
		
		if (conf.globalOrder == 1)
			storm.reorderWords("weight");
		
		if (conf.coorPlacer == 1)
			storm.coordinatedPlacer();
		else if (conf.coorPlacer == 3)
			storm.mdsGradient();
		
		if (conf.coorColorer == 1)
			storm.coordinatedColorer();
		
		if (conf.coorAngler == 1)
			storm.coordinatedAngler();
	}
	
	private void changeForce(){
		storm.mdsGradient();
		storm.setZeroOverlap();
		storm.setAlgorithm(3);
	}
	
	public void moveWordAlgorithm() {
		storm.placeWords(false, true);
		int nConverged = storm.nConverged();
		if(verbose) printProgress(nConverged, mds.getScoreMDS());
		int cont = 0;
		int maxIter = conf.maxIterations == -1? 10000 : conf.maxIterations;
		placeWords(nConverged, cont, maxIter);
		changeForce();
		printProgress(mds.getScoreMDS(), mds.getScoreOverlap(), mds.getGradient(), mds.getScoreCompact(), -1);
		gradientAlgorithm(cont);
		storm.placeUnique(conf.rSeed);
		Rectangle bBox = storm.setupReverseCoordinates();
		float scale = (float) Math.min(desApConf.height*1.0/bBox.height,desApConf.width*1.0/bBox.width);
		storm.scalingFactor = scale;
		saveWordStorm();
		storm.saveWordData();
		storm.saveColorData();
		fraw();
	}

	void setFrame() {
		Rectangle frame = storm.minBBox();
		float scale = (float) Math.min(desApConf.height*1.0/frame.height,desApConf.width*1.0/frame.width);
		storm.scaleLayout(scale);
		apConf.height = desApConf.height;
		apConf.width = desApConf.width;
		setup();
	}
	
	public void fraw() {		
		setFrame();
		String output;
		for (int ii = 0; ii < storm.numClouds; ++ii) {
			if (verbose && storm.numClouds > 50 && ii % 50 == 0)
				System.out.print(" " + ii);
			background(apConf.bgCol);
			output = load.getOutput(conf, ii);
			storm.draw(ii, output);
		}
		if (verbose && storm.numClouds > 50)
			System.out.println();
		finishUp();
	}
	
	public void frawone(int cloud) {
		setFrame();
		background(apConf.bgCol);
		String output = load.getOutput(conf, cloud);
		storm.draw(cloud, output);
		finishUp();
	}
	
	public void fraw(int frameNum) {
		String output;
		for (int ii = 0; ii < storm.numClouds; ++ii) {
			if (verbose && storm.numClouds > 50 && ii % 50 == 0)
				System.out.print(" " + ii);
			background(apConf.bgCol);
			output = load.getOutputMovie(conf, ii, frameNum);
			storm.draw(ii, output);
		}
		if (verbose && storm.numClouds > 50)
			System.out.println();
		finishUp();
	}
	
	public void finishUp() {
		noLoop();
		stop();
	}
	
	private void printProgress(int nConverged, double mdsScore){
		log += "non-converged, "+nConverged+", ";
		log += "mds, "+mdsScore+"\n";
		System.out.println("non-converged "+nConverged);
		System.out.println("mds "+mdsScore);
	}

	private void printProgress(double scoreMDS, float scoreOverlap, 
			float gradient, float scoreCompact, int l) {
		scoreMDS *= 1000;
		log += scoreMDS+", " +scoreOverlap+ ", "+scoreCompact +", "
		+(scoreOverlap+scoreMDS+scoreCompact) +"\n";
		System.out.println(l+" MDS " + scoreMDS);
		System.out.println(l+" Overlap " + scoreOverlap);
		System.out.println(l+" Compact " +scoreCompact);
		System.out.println(l+" Score " + (scoreOverlap+scoreMDS+scoreCompact));
		System.out.println(l+" Gradient " + gradient);
	}

	private void logHeader() {
		log = "word clouds, "+ load.numClouds + "\n";
		log += conf.toString() + "\n";
		log += apConf + "\n\n";
	}

	private void printLog(String t) {
		try {
			FileOutputStream fstreamOut = new FileOutputStream(load.getStormLog(conf));
			DataOutputStream out = new DataOutputStream(fstreamOut);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			bw.write(t+"\n");
			bw.write(log);
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveWordStorm() {
		try{
			File directory = new File(DataPath.getOutputPath());
			directory.mkdirs();
			FileOutputStream fout = new FileOutputStream(DataPath.getOutputPath()+"/wordstorm.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(storm);			
			oos.close();
			fout.close();	 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}   
}