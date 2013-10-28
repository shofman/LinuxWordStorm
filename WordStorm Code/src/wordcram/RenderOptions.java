package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.io.Serializable;

class RenderOptions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3227720599284885202L;
	int maxAttemptsToPlaceWord = -1; // default: based on Word weight
	int maxNumberOfWordsToDraw = -1; // default: unlimited
	int minShapeSize = 1;// 7;
	int wordPadding = 1;
}
