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
import java.util.HashMap;
import java.awt.geom.Rectangle2D;

import java.io.Serializable;

/**
 * A weighted word, for rendering in the word cloud image.
 * <p>
 * Each Word object has a {@link #word} String, and its associated {@link #weight}, and it's constructed
 * with these two things.
 * 
 * 
 * <h3>Hand-crafting Your Words</h3>
 * 
 * If you're creating your own <code>Word[]</code> to pass
 * to the WordCram (rather than using something like {@link WordCram#fromWebPage(String)}),
 * you can specify how a Word should be rendered: set a Word's font, size, etc:
 * 
 * <pre>
 * Word w = new Word("texty", 20);
 * w.setFont(createFont("myFontName", 1));
 * w.setAngle(radians(45));
 * </pre>
 * 
 * Any values set on a Word will override the corresponding component ({@link WordColorer}, 
 * {@link WordAngler}, etc) - it won't even be called for that word.
 * 
 * <h3>Word Properties</h3>
 * A word can also have properties. If you're creating custom components,
 * you might want to send other information along with the word, for the components to use:
 * 
 * <pre>
 * Word strawberry = new Word("strawberry", 10);
 * strawberry.setProperty("isFruit", true);
 * 
 * Word pea = new Word("pea", 10);
 * pea.setProperty("isFruit", false);
 * 
 * new WordCram(this)
 *   .fromWords(new Word[] { strawberry, pea })
 *   .withColorer(new WordColorer() {
 *      public int colorFor(Word w) {
 *        if (w.getProperty("isFruit") == true) {
 *          return color(255, 0, 0);
 *        }
 *        else {
 *          return color(0, 200, 0);
 *        }
 *      }
 *    });
 * </pre>
 * 
 * @author Dan Bernier
 */
public class Word implements Comparable<Word>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3487199241065015031L;
	public String word;	
	public float weight;
	public float origWeight;
	public int rank;
	public int cloudIndex;

	private Float presetSize;
	private Float presetAngle;
	private MyPFont presetFont;
	private Integer presetColor;
	private transient MyPVector presetTargetPlace;
	private Rectangle2D bounds;
	
	// These are null until they're rendered, and can be wiped out for a re-render.
	private Float renderedSize;
	private Float renderedAngle;
	private MyPFont renderedFont;
	private Integer renderedColor;
	private transient MyPVector targetPlace;
	private transient MyPVector renderedPlace;
	private Integer skippedBecause;
	
	private HashMap<String,Object> properties = new HashMap<String,Object>();
	
	public Word(String word, float weight) {
		this.word = word;
		this.weight = weight;
	}
	
	public Word(String word, float weight, int cloudIndex) {
		this.word = word;
		this.weight = weight;
		this.cloudIndex = cloudIndex;
	}
	
	public void setCloudIndex(int cloudIndex) {
		this.cloudIndex = cloudIndex;
	}
	
	/**
	 * Set the size this Word should be rendered at - WordCram won't even call the WordSizer.
	 * @return the Word, for more configuration
	 */
	public Word setSize(float size) {
		this.presetSize = size;
		return this;
	}
	
	/**
	 * Set the angle this Word should be rendered at - WordCram won't even call the WordAngler.
	 * @return the Word, for more configuration
	 */
	public Word setAngle(float angle) {
		this.presetAngle = angle;
		return this;
	}
	
	/**
	 * Set the font this Word should be rendered in - WordCram won't call the WordFonter.
	 * @return the Word, for more configuration
	 */
	public Word setFont(MyPFont font) {  // TODO provide a string overload? Will need the PApplet...
		this.presetFont = font;
		return this;
	}
	
	/**
	 * Set the color this Word should be rendered in - WordCram won't call the WordColorer.
	 * @return the Word, for more configuration
	 */
	public Word setColor(int color) {
		this.presetColor = color;
		return this;
	}
	
	/**
	 * Set the place this Word should be rendered at - WordCram won't call the WordPlacer.
	 * @return the Word, for more configuration
	 */
	public Word setPlace(MyPVector place) {
		this.presetTargetPlace = (MyPVector) place.get();
		return this;
	}
	
	/**
	 * Set the place this Word should be rendered at - WordCram won't call the WordPlacer.
	 * @return the Word, for more configuration
	 */
	public Word setPlace(float x, float y) {
		this.presetTargetPlace = new MyPVector(x, y);
		return this;
	}
	
	public void setBounds(Rectangle2D bounds,int swell){
		this.bounds = new Rectangle2D.Double(bounds.getX()-swell, 
				bounds.getY()-swell, bounds.getWidth()+swell,  bounds.getHeight()+swell);
	}
	
	public Rectangle2D getBounds(){
		return this.bounds;
	}

	/*
	 * These methods are called by EngineWord: they return (for instance)
	 * either the color the user set via setColor(), or the value returned
	 * by the WordColorer. They're package-local, so they can't be called by the sketch.
	 */
	
	Float getSize(WordSizer sizer, int rank, int wordCount) {
		if(presetSize == null) presetSize = sizer.sizeFor(this, rank, wordCount);
		renderedSize = presetSize != null ? presetSize : sizer.sizeFor(this, rank, wordCount);
		return renderedSize;
	}
	
	Float getAngle(WordAngler angler) {
		if(presetAngle == null) presetAngle = angler.angleFor(this); // QUIM Change
		renderedAngle = presetAngle != null ? presetAngle : angler.angleFor(this);
		return renderedAngle;
	}
	
	MyPFont getFont(WordFonter fonter) {
		renderedFont = presetFont != null ? presetFont : fonter.fontFor(this);
		return renderedFont;
	}
	
	Integer getColor(WordColorer colorer) {
		renderedColor = presetColor != null ? presetColor : colorer.colorFor(this);
		return renderedColor;
	}
	
	MyPVector getTargetPlace(WordPlacer placer, int rank, int count, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
		//targetPlace = presetTargetPlace != null ? presetTargetPlace : placer.place(this, rank, count, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
		targetPlace =  placer.place(this, rank, count, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
		return targetPlace;
	}
	
	public void setRenderedPlace(MyPVector place) {
		renderedPlace = (MyPVector) place.get();
	}
	
	public void setTargetPlace(MyPVector place) {
		targetPlace = place;
	}
	
	public void setRenderedColor(Integer Color) {
		renderedColor = Color;
	}
	
	

	/**
	 * Get the size the Word was rendered at: either the value passed to setSize(), or the value returned from the WordSizer. 
	 * @return the rendered size
	 */
	public float getRenderedSize() {
		return renderedSize;
	}

	/**
	 * Get the angle the Word was rendered at: either the value passed to setAngle(), or the value returned from the WordAngler. 
	 * @return the rendered angle
	 */
	public float getRenderedAngle() {
		return renderedAngle;
	}

	/**
	 * Get the font the Word was rendered in: either the value passed to setFont(), or the value returned from the WordFonter. 
	 * @return the rendered font
	 */
	public MyPFont getRenderedFont() {
		return renderedFont;
	}

	/**
	 * Get the color the Word was rendered in: either the value passed to setColor(), or the value returned from the WordColorer. 
	 * @return the rendered color
	 */
	public int getRenderedColor() {
		return renderedColor;
	}

	/**
	 * Get the place the Word was supposed to be rendered at: either the value passed to setPlace(), 
	 * or the value returned from the WordPlacer.
	 */
	public MyPVector getTargetPlace() {
		return targetPlace;
	}

	/**
	 * Get the final place the Word was rendered at, or null if it couldn't be placed.
	 * It returns the original target location (which is either the value passed to setPlace(), 
	 * or the value returned from the WordPlacer), plus the nudge vector returned by the WordNudger.
	 * @return If word was placed, it's the (x,y) coordinates of the word's final location; else it's null.
	 */
	public MyPVector getRenderedPlace() {
		return renderedPlace;
	}
	
	/**
	 * Indicates whether the Word was placed successfully. It's the same as calling word.getRenderedPlace() != null.
	 * If this returns false, it's either because a) WordCram didn't get to this Word yet,
	 * or b) it was skipped for some reason (see {@link #wasSkipped()} and {@link #wasSkippedBecause()}).
	 * @return true only if the word was placed.
	 */
	public boolean wasPlaced() {
		return renderedPlace != null;
	}
	
	/**
	 * Indicates whether the Word was skipped.
	 * @see Word#wasSkippedBecause()
	 * @return true if the word was skipped
	 */
	public boolean wasSkipped() {
		return wasSkippedBecause() != null;
	}
	
	/**
	 * Tells you why this Word was skipped.
	 * 
	 * If the word was skipped, 
	 * then this will return an Integer, which will be one of 
	 * {@link WordCram#WAS_OVER_MAX_NUMBER_OF_WORDS}, {@link WordCram#SHAPE_WAS_TOO_SMALL}, 
	 * or {@link WordCram#NO_SPACE}.
	 * 
	 * If the word was successfully placed, or WordCram hasn't
	 * gotten to this word yet, this will return null.
	 * 
	 * @return the code for the reason the word was skipped, or null if it wasn't skipped.  
	 */
	public Integer wasSkippedBecause() {
		return skippedBecause;
	}
	
	void wasSkippedBecause(int reason) {
		skippedBecause = reason;
	}

	/**
	 * Get a property value from this Word, for a WordColorer, a WordPlacer, etc.
	 * @param propertyName
	 * @return the value of the property, or <code>null</code>, if it's not there.
	 */
	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}
	
	/**
	 * Set a property on this Word, to be used by a WordColorer, a WordPlacer, etc, down the line.
	 * @param propertyName
	 * @param propertyValue
	 * @return the Word, for more configuration
	 */
	public Word setProperty(String propertyName, Object propertyValue) {
		properties.put(propertyName, propertyValue);
		return this;
	}
	
	/**
	 * Displays the word, and its weight (in parentheses).
	 * <code>new Word("hello", 1.3).toString()</code> will return "hello (0.3)".
	 */
	@Override
	public String toString() {
		String status = "";
		if (wasPlaced()) {
			status = renderedPlace.x + "," + renderedPlace.y;
		}
		else if (wasSkipped()) {
			switch (wasSkippedBecause()) {
			case WordCram.WAS_OVER_MAX_NUMBER_OF_WORDS:
				status = "was over the maximum number of words";
				break;
			case WordCram.SHAPE_WAS_TOO_SMALL:
				status = "shape was too small";
				break;
			case WordCram.NO_SPACE:
				status = "couldn't find a spot";
				break;
			}
		}
		if (status.length() != 0) {
			status = " [" + status + "]";
		}
		return word + " (" + weight + ")" + status;
	}
	
	/**
	 * Compares Words based on weight only. Words with equal weight are arbitrarily sorted.
	 */
	public int compareTo(Word w) {
		if (w.weight < weight) {
			return -1;
		}
		else if (w.weight > weight) {
			return 1;
		}
		else return 0;
	}

	void refactorSize(float factor){
		presetSize = (presetSize != null)? presetSize*factor : null;
		renderedSize = null;
	}
	
	void resetSize(){
		presetSize = null;
	}
	
	
	void resetSize(WordSizer sizer, int wordCount){
		presetSize = null; 
		renderedSize = getSize(sizer, rank, wordCount);
	}
	
	void notSkipped(){
		skippedBecause = null;
	}
	
	void resetProgress(){
		renderedSize = null;
		renderedAngle = null;
		renderedFont = null;
		renderedColor = null;
		targetPlace = null;
		renderedPlace = null;
		skippedBecause = null;
	}
}
