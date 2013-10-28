package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.util.Comparator;
import java.io.Serializable;

public class WordCompString  implements Comparator<Word>, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5457936213599614752L;

	// Comparator interface requires defining compare method.
    public int compare(Word worda, Word wordb) {
    	String a = worda.word;
    	String b = wordb.word;
    	return b.compareTo(a);
    }
}
