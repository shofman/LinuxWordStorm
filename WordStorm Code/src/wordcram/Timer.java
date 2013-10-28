package wordcram;

import java.util.concurrent.TimeUnit;
import java.io.Serializable;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
public class Timer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2173545341135715916L;
	long millis;
	
	public Timer(){
		millis = System.currentTimeMillis();
	}
	
	public long stop(){
		millis = System.currentTimeMillis() - millis;
		return millis;
	}
	
	public String toString(){
		return String.format("%d ms (%d min %d sec)", millis,
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
			    );
	}

}
