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
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.util.Random;
import java.io.Serializable;

public class UpperLeftWordPlacer implements WordPlacer, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3495648170752769496L;
	private Random r = new Random();
	
	public MyPVector place(Word word, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
		int x = getOneUnder(fieldWidth - wordImageWidth);
		int y = getOneUnder(fieldHeight - wordImageHeight);
		return new MyPVector(x, y);
	}
	
	private int getOneUnder(int limit) {
		return MyPApplet.round(MyPApplet.map(random(random(random(random(random(1.0f))))), 0, 1, 0, limit));
	}
	
	private float random(float limit) {
		return r.nextFloat() * limit;
	}

}
