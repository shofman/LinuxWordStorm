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

import java.util.Random;
import java.io.Serializable;

public class Fonters implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5329039010809912885L;

	public static WordFonter alwaysUse(final MyPFont MyPFont) {
		return new WordFonter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8055104612898323159L;

			public MyPFont fontFor(Word word) {
				return MyPFont;
			}
		};
	}
	
	public static WordFonter pickFrom(final MyPFont... fonts) {
		final Random r = new Random();
		return new WordFonter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1448556570494233030L;

			public MyPFont fontFor(Word w) {
				return fonts[r.nextInt(fonts.length)];
			}
		};
	}
}
