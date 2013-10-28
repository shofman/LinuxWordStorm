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

public class Placers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5294385555955177850L;

	public static WordPlacer horizLine(long rSeed) {
		final Random r = new Random(rSeed);

		return new WordPlacer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1985056889762385409L;

			public MyPVector place(Word word, int wordIndex, int wordsCount,
					int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
				int centerHorizLine = (int) ((fieldHeight - wordImageHeight) * 0.5);
				int centerVertLine = (int) ((fieldWidth - wordImageWidth) * 0.5);

				float xOff = (float) r.nextGaussian() * ((fieldWidth - wordImageWidth) * 0.2f);
				float yOff = (float) r.nextGaussian() * 50;

				return new MyPVector(centerVertLine + xOff, centerHorizLine + yOff);
			}
		};
	}

	public static WordPlacer centerClump(long rSeed) {
		final Random r = new Random(rSeed);
		final float stdev = 0.4f;

		return new WordPlacer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6958670139433173430L;

			public MyPVector place(Word word, int wordIndex, int wordsCount,
					int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
				return new MyPVector(getOneUnder(fieldWidth - wordImageWidth),
						getOneUnder(fieldHeight - wordImageHeight));
			}

			private int getOneUnder(float upperLimit) {
				return MyPApplet.round(MyPApplet.map((float) r.nextGaussian()
						* stdev, -2, 2, 0, upperLimit));
			}
		};
	}

	public static WordPlacer horizBandAnchoredLeft(long rSeed) {
		final Random r = new Random(rSeed);
		return new WordPlacer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3574964073674870025L;

			public MyPVector place(Word word, int wordIndex, int wordsCount,
					int wordImageWidth, int wordImageHeight, int fieldWidth,
					int fieldHeight) {
				float x = (1 - word.weight) * fieldWidth * r.nextFloat(); // big=left, small=right
				float y = ((float) fieldHeight) * 0.5f;
				return new MyPVector(x, y);
			}
		};
	}

	public static WordPlacer swirl() {
		return new SwirlWordPlacer();
	}

	public static WordPlacer upperLeft() {
		return new UpperLeftWordPlacer();
	}

	public static WordPlacer wave() {
		return new WaveWordPlacer();
	}
}
