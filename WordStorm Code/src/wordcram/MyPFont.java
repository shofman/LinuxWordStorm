package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import processing.core.PFont;

import java.awt.Font;
import java.io.Serializable;

public class MyPFont extends PFont implements Serializable {
	
	
	MyPFont(Font f) {
		super(f, true);
	}
	public MyPFont(Font deriveFont, boolean smooth, char[] charset, boolean b) {
		// TODO Auto-generated constructor stub
		super(deriveFont, smooth, charset, b);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6844333389089489104L;

}
