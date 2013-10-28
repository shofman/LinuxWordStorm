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
import processing.core.*;
import java.io.Serializable;

public class SwirlWordPlacer implements WordPlacer, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5233054425664443308L;

	public MyPVector place(Word word, int wordIndex, int wordsCount,
			int wordImageWidth, int wordImageHeight, int fieldWidth,
			int fieldHeight) {

		float normalizedIndex = (float) wordIndex / wordsCount;

		float theta = normalizedIndex * 6 * PConstants.TWO_PI;
		float radius = normalizedIndex * fieldWidth / 2f;

		float centerX = fieldWidth * 0.5f;
		float centerY = fieldHeight * 0.5f;

		float x = MyPApplet.cos(theta) * radius;
		float y = MyPApplet.sin(theta) * radius;

		return new MyPVector(centerX + x, centerY + y);
	}
}
