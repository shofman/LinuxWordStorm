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

/**
 * A RandomWordNudger, where each attempt's MyPVector has X & Y coords
 * distributed randomly around the desired point, multiplied by a standard deviation,
 * and multiplied by the attempt number (so it gets farther, as it gets more 
 * desperate).
 * 
 * @author Dan Bernier
 */
public class RandomWordNudger implements WordNudger, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9104533381678023938L;
	private Random r = new Random();
	private float stdDev;
	
	/**
	 * Create a RandomWordNudger with a standard deviation of 0.6.
	 */
	public RandomWordNudger() {
		this(0.6f);
	}

	/**
	 * Create a RandomWordNudger with your own standard deviation.
	 */
	public RandomWordNudger(float stdDev) {
		this.stdDev = stdDev;
	}

	public MyPVector nudgeFor(Word w, int attempt) {
		return new MyPVector(next(attempt), next(attempt));
	}
	
	private float next(int attempt) {
		return (float)r.nextGaussian() * attempt * stdDev;
	}

}
