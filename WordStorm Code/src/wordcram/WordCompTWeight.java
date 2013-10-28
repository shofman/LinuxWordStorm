package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;

public class WordCompTWeight  implements Comparator<Word>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3444966423820690325L;
	Map<String, CoordProp> index;
	
	public WordCompTWeight(Map<String, CoordProp> index){
		this.index = index;
	}
    public int compare(Word worda, Word wordb) {
    	Float a = index.get(worda.word).getTotWeight();
    	Float b = index.get(wordb.word).getTotWeight();    	
    	return b.compareTo(a);
    }
}

