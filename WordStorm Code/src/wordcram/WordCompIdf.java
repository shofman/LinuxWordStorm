package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;

public class WordCompIdf  implements Comparator<Word>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5834998675294178612L;
	Map<String, CoordProp> index;
	
	public WordCompIdf(Map<String, CoordProp> index){
		this.index = index;
	}
    public int compare(Word worda, Word wordb) {
    	Float a = index.get(worda.word).getIdf();
    	Float b = index.get(wordb.word).getIdf();
    	return b.compareTo(a);
    }
}
