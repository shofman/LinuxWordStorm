package wordcram;

/*
Copyright 2010 Daniel Bernier

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */

import processing.core.PConstants;
import java.io.Serializable;

abstract class HsbWordColorer implements WordColorer, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3156983999382773560L;
	private transient MyPApplet host;
	private int range;
	
	HsbWordColorer(MyPApplet host) {
		this(host, 255);
	}
	HsbWordColorer(MyPApplet host, int range) {
		this.host = host;
		this.range = range;
	}
	
	public int colorFor(Word word) {
		host.pushStyle();
		host.colorMode(PConstants.HSB, range);
		int color = getColorFor(word);
		host.popStyle();
		return color;
	}
	
	abstract int getColorFor(Word word);
}
