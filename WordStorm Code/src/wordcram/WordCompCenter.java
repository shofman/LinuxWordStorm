package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;

public class WordCompCenter implements Comparator<Word>, Serializable  {

		/**
	 * 
	 */
	private static final long serialVersionUID = 4499197422529553164L;
		Map<String, CoordProp> index;
		MyPVector center;
		
		public WordCompCenter(Map<String, CoordProp> index, MyPVector center){
			this.index = index;
			this.center = center; 
		}
		   
	    public int compare(Word worda, Word wordb) {
	    	//MyPVector aLoc = index.get(worda.word).getCurrentLocation(worda.cloudIndex);
	    	//MyPVector bLoc = index.get(wordb.word).getCurrentLocation(wordb.cloudIndex);
	    	
	    	MyPVector aLoc = index.get(worda.word).getAverage();
	    	MyPVector bLoc = index.get(wordb.word).getAverage();
	    	
	    	Float a = aLoc.dist(center);
	    	Float b = bLoc.dist(center);
 	
	    	return a.compareTo(b);
	    }
	}
