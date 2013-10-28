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
import java.awt.Dimension;
import java.io.Serializable;
import java.lang.reflect.Constructor;

import processing.core.PGraphics;

public class Colorers implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4334976541023569154L;

	public static WordColorer Random(final MyPApplet host) {

		final float[] hues = new float[] { random(256), random(256) };

		return new HsbWordColorer(host) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2093168020740656805L;

			public int getColorFor(Word w) {

				float hue = (int)random(256);
				float sat = 200;
				float val = 50 + random(150);

				return MyPApplet.colorCalc(hue, sat, val);
			}
		};
	}

	public final static float random(float high) {
		Random internalRandom = null;
		// avoid an infinite loop when 0 or NaN are passed in
		if (high == 0 || high != high) {
			return 0;
		}

		if (internalRandom == null) {
			internalRandom = new Random();
		}

		// for some reason (rounding error?) Math.random() * 3
		// can sometimes return '3' (once in ~30 million tries)
		// so a check was added to avoid the inclusion of 'howbig'
		float value = 0;
		do {
			value = internalRandom.nextFloat() * high;
		} while (value == high);
		return value;
	}




	public static WordColorer twoHuesRandomSats(final MyPApplet host) {

		final float[] hues = new float[] { random(256), random(256) };

		return new HsbWordColorer(host) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4106245239783983003L;

			public int getColorFor(Word w) {

				float hue = hues[(int)random(hues.length)];
				float sat = random(256);
				float val = random(156);

				return MyPApplet.colorCalc(hue, sat, val);
			}
		};
	}

	public static WordColorer twoHuesRandomSatsOnWhite(final MyPApplet host) {

		final float[] hues = new float[] { random(256), random(256) };

		return new HsbWordColorer(host) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3484924115218111382L;

			public int getColorFor(Word w) {

				float hue = hues[(int)random(hues.length)];
				float sat = random(256);
				float val = random(156);

				return MyPApplet.colorCalc(hue, sat, val);
			}
		};
	}

	public static WordColorer pickFrom(final int... colors) {
		final Random r = new Random();
		return new WordColorer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1893681003362713410L;

			public int colorFor(Word w) {
				return colors[r.nextInt(colors.length)];
			}
		};
	}

	// TODO add an overload that takes 1 int (greyscale), 2 ints (greyscale/alpha), etc
	public static WordColorer alwaysUse(final int color) {
		return new WordColorer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3522113039655382982L;

			public int colorFor(Word w) {
				return color;
			}
		};
	}
}
