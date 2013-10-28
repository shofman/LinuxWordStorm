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
import java.io.Serializable;

public class SpiralWordNudger implements WordNudger, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4092070554469878334L;
	// Who knows? this seems to be good, but it seems to depend on the font --
	// bigger fonts need a bigger thetaIncrement.
	private float thetaIncrement = (float) (Math.PI * 0.03);

	public MyPVector nudgeFor(Word w, int attempt) {
		float rad = powerMap(0.6f, attempt, 0, 600, 1, 100);

		thetaIncrement = powerMap(1, attempt, 0, 600, 0.5f, 0.3f);
		float theta = thetaIncrement * attempt;
		float x = MyPApplet.cos(theta) * rad;
		float y = MyPApplet.sin(theta) * rad;
		return new MyPVector(x, y);
	}

	private float powerMap(float power, float v, float min1, float max1,
			float min2, float max2) {

		float val = MyPApplet.norm(v, min1, max1);
		val = MyPApplet.pow(val, power);
		return MyPApplet.lerp(min2, max2, val);
	}

}
