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

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import java.io.Serializable;

class EngineWord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5545513758259780175L;
	Word word;
	int rank;
	int cloudNumber;
	
	private Shape shape;
	private BBTreeBuilder bbTreeBuilder;
	private BBTree bbTree;
	
	private MyPVector desiredLocation;
	private MyPVector currentLocation;

	EngineWord(Word word, int rank, int wordCount, BBTreeBuilder bbTreeBuilder) {
		this.word = word;
		this.rank = rank;
		this.bbTreeBuilder = bbTreeBuilder;
	}
	EngineWord(Word word, int rank, int wordCount, BBTreeBuilder bbTreeBuilder, MyPVector desLoc, MyPVector curLoc) {
		this.word = word;
		this.rank = rank;
		this.bbTreeBuilder = bbTreeBuilder;
		this.desiredLocation = desLoc;
	}

	void setShape(Shape shape, int swelling) {
		this.shape = shape;
		this.bbTree = bbTreeBuilder.makeTree(shape, swelling);
	}

	Shape getShape() {
		return shape;
	}

	boolean overlaps(EngineWord other) {
		return bbTree.overlaps(other.bbTree);
	}

	MyPVector getDesiredLocation(){
		return desiredLocation;
	}
	
	void setDesiredLocation(WordPlacer placer, int count, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
		desiredLocation = word.getTargetPlace(placer, rank, count, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
		currentLocation = desiredLocation.get();
	}
	
	public void updateCurrentLocation() {
		currentLocation = word.getTargetPlace();
	}

	void nudge(MyPVector nudge) {
		MyPVector temp = new MyPVector();
		currentLocation = temp.add(desiredLocation, nudge);
		bbTree.setLocation((int)currentLocation.x, (int)currentLocation.y);
	}

	void finalizeLocation() {
		AffineTransform transform = AffineTransform.getTranslateInstance(
				currentLocation.x, currentLocation.y);
		shape = transform.createTransformedShape(shape);
		bbTree.setLocation((int)currentLocation.x, (int)currentLocation.y);
		word.setRenderedPlace(currentLocation);
	}
	
	MyPVector getCurrentLocation() {
		return currentLocation;
	}
	
	boolean wasPlaced() {
		return word.wasPlaced();
	}
	
	boolean wasSkipped() {
		return word.wasSkipped();
	}

	void setCloudNumber(int cloudNumber){
		this.cloudNumber = cloudNumber;
	}

	int getCloudNumber(){
		return cloudNumber;
	}

	void resetProgress(){
		shape = null;
		bbTreeBuilder = null;
		bbTree = null;
		word.resetProgress();
	}
	
	void setLocations(MyPVector curLoc, MyPVector desLoc){
		this.currentLocation = curLoc;
		this.desiredLocation = desLoc;
	}
}

