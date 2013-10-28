package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.Font;
import java.io.InputStream;
import java.io.Serializable;

public class MyPApplet extends PApplet implements Serializable {
	/** The current colorMode */
	public static int colorMode; // = RGB;

	/** Max value for red (or hue) set by colorMode */
	public static float colorModeX; // = 255;

	/** Max value for green (or saturation) set by colorMode */
	public static float colorModeY; // = 255;

	/** Max value for blue (or value) set by colorMode */
	public static float colorModeZ; // = 255;

	/** Max value for alpha set by colorMode */
	public static float colorModeA; // = 255;

	/** True if colors are not in the range 0..1 */
	static boolean colorModeScale; // = true;

	/** True if colorMode(RGB, 255) */
	boolean colorModeDefault; // = true;

	protected static float calcR;

	protected static float calcG;

	protected static float calcB;

	protected static float calcA;
	protected static int calcRi;

	protected static int calcGi;

	protected static int calcBi;

	protected static int calcAi;
	protected static int calcColor;
	protected static boolean calcAlpha;


	public int myColor(float v1, float v2, float v3) {
		return colorCalc(v1, v2, v3);
	}

	protected static int colorCalc(float x, float y, float z) {
		colorCalc(x, y, z, colorModeA);
		return calcColor;
	}

	public int colorer(float v1, float v2, float v3, float alpha) {
		if (this.g == null) {
			if (alpha > 255) alpha = 255; else if (alpha < 0) alpha = 0;
			if (v1 > 255) v1 = 255; else if (v1 < 0) v1 = 0;
			if (v2 > 255) v2 = 255; else if (v2 < 0) v2 = 0;
			if (v3 > 255) v3 = 255; else if (v3 < 0) v3 = 0;

			return ((int)alpha << 24) | ((int)v1 << 16) | ((int)v2 << 8) | (int)v3;
		}
		return super.g.color(v1, v2, v3, alpha);
	}


	public int myColor(float v1, float v2, float v3, float v4) {
		return super.color(v1, v2, v3, v4);
	}

	public MyPFont createFont(String name, float size) {
		return createFont(name, size, true, null);
	}


	public MyPFont createFont(String name, float size, boolean smooth) {
		return createFont(name, size, smooth, null);
	}


	/**
	 * ( begin auto-generated from createFont.xml )
	 *
	 * Dynamically converts a font to the format used by Processing from either
	 * a font name that's installed on the computer, or from a .ttf or .otf
	 * file inside the sketches "data" folder. This function is an advanced
	 * feature for precise control. On most occasions you should create fonts
	 * through selecting "Create Font..." from the Tools menu.
	 * <br /><br />
	 * Use the <b>PFont.list()</b> method to first determine the names for the
	 * fonts recognized by the computer and are compatible with this function.
	 * Because of limitations in Java, not all fonts can be used and some might
	 * work with one operating system and not others. When sharing a sketch
	 * with other people or posting it on the web, you may need to include a
	 * .ttf or .otf version of your font in the data directory of the sketch
	 * because other people might not have the font installed on their
	 * computer. Only fonts that can legally be distributed should be included
	 * with a sketch.
	 * <br /><br />
	 * The <b>size</b> parameter states the font size you want to generate. The
	 * <b>smooth</b> parameter specifies if the font should be antialiased or
	 * not, and the <b>charset</b> parameter is an array of chars that
	 * specifies the characters to generate.
	 * <br /><br />
	 * This function creates a bitmapped version of a font in the same manner
	 * as the Create Font tool. It loads a font by name, and converts it to a
	 * series of images based on the size of the font. When possible, the
	 * <b>text()</b> function will use a native font rather than the bitmapped
	 * version created behind the scenes with <b>createFont()</b>. For
	 * instance, when using P2D, the actual native version of the font will be
	 * employed by the sketch, improving drawing quality and performance. With
	 * the P3D renderer, the bitmapped version will be used. While this can
	 * drastically improve speed and appearance, results are poor when
	 * exporting if the sketch does not include the .otf or .ttf file, and the
	 * requested font is not available on the machine running the sketch.
	 *
	 * ( end auto-generated )
	 * @webref typography:loading_displaying
	 * @param name name of the font to load
	 * @param size point size of the font
	 * @param smooth true for an antialiased font, false for aliased
	 * @param charset array containing characters to be generated
	 * @see PFont#PFont
	 * @see PGraphics#textFont(PFont, float)
	 * @see PGraphics#text(String, float, float, float, float, float)
	 * @see PApplet#loadFont(String)
	 */
	public MyPFont createFont(String name, float size,
			boolean smooth, char charset[]) {
		String lowerName = name.toLowerCase();
		Font baseFont = null;

		try {
			InputStream stream = null;
			if (lowerName.endsWith(".otf") || lowerName.endsWith(".ttf")) {
				stream = createInput(name);
				if (stream == null) {
					System.err.println("The font \"" + name + "\" " +
							"is missing or inaccessible, make sure " +
							"the URL is valid or that the file has been " +
					"added to your sketch and is readable.");
					return null;
				}
				baseFont = Font.createFont(Font.TRUETYPE_FONT, createInput(name));

			} else {
				baseFont = MyPFont.findFont(name);
			}
			return new MyPFont(baseFont.deriveFont(size), smooth, charset,
					stream != null);

		} catch (Exception e) {
			System.err.println("Problem createFont(" + name + ")");
			e.printStackTrace();
			return null;
		}
	}

	protected static void colorCalc(float x, float y, float z, float a) {
		if (x > colorModeX) x = colorModeX;
		if (y > colorModeY) y = colorModeY;
		if (z > colorModeZ) z = colorModeZ;
		if (a > colorModeA) a = colorModeA;

		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (z < 0) z = 0;
		if (a < 0) a = 0;

		x /= colorModeX; // h
		y /= colorModeY; // s
		z /= colorModeZ; // b

		calcA = colorModeScale ? (a/colorModeA) : a;

		if (y == 0) {  // saturation == 0
			calcR = calcG = calcB = z;

		} else {
			float which = (x - (int)x) * 6.0f;
			float f = which - (int)which;
			float p = z * (1.0f - y);
			float q = z * (1.0f - y * f);
			float t = z * (1.0f - (y * (1.0f - f)));

			switch ((int)which) {
			case 0: calcR = z; calcG = t; calcB = p; break;
			case 1: calcR = q; calcG = z; calcB = p; break;
			case 2: calcR = p; calcG = z; calcB = t; break;
			case 3: calcR = p; calcG = q; calcB = z; break;
			case 4: calcR = t; calcG = p; calcB = z; break;
			case 5: calcR = z; calcG = p; calcB = q; break;
			}
		}
		calcRi = (int)(255*calcR); calcGi = (int)(255*calcG);
		calcBi = (int)(255*calcB); calcAi = (int)(255*calcA);
		calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
		calcAlpha = (calcAi != 255);
	}

	public static int myRound(float n) {
		return Math.round(n);
	}

	static public float myMap(float value,
			float start1, float stop1,
			float start2, float stop2) {
		return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
	}

	static public String join(String[] list, char separator) {
		return join(list, String.valueOf(separator));
	}


	static public String join(String[] list, String separator) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if (i != 0) buffer.append(separator);
			buffer.append(list[i]);
		}
		return buffer.toString();
	}

	public PGraphics createGraphics(int w, int h) {
		return createGraphics(w, h, JAVA2D);
	}


	/**
	 * ( begin auto-generated from createGraphics.xml )
	 *
	 * Creates and returns a new <b>PGraphics</b> object of the types P2D or
	 * P3D. Use this class if you need to draw into an off-screen graphics
	 * buffer. The PDF renderer requires the filename parameter. The DXF
	 * renderer should not be used with <b>createGraphics()</b>, it's only
	 * built for use with <b>beginRaw()</b> and <b>endRaw()</b>.<br />
	 * <br />
	 * It's important to call any drawing functions between <b>beginDraw()</b>
	 * and <b>endDraw()</b> statements. This is also true for any functions
	 * that affect drawing, such as <b>smooth()</b> or <b>colorMode()</b>.<br/>
	 * <br/> the main drawing surface which is completely opaque, surfaces
	 * created with <b>createGraphics()</b> can have transparency. This makes
	 * it possible to draw into a graphics and maintain the alpha channel. By
	 * using <b>save()</b> to write a PNG or TGA file, the transparency of the
	 * graphics object will be honored. Note that transparency levels are
	 * binary: pixels are either complete opaque or transparent. For the time
	 * being, this means that text characters will be opaque blocks. This will
	 * be fixed in a future release (<a
	 * href="http://code.google.com/p/processing/issues/detail?id=80">Issue 80</a>).
	 *
	 * ( end auto-generated )
	 * <h3>Advanced</h3>
	 * Create an offscreen PGraphics object for drawing. This can be used
	 * for bitmap or vector images drawing or rendering.
	 * <UL>
	 * <LI>Do not use "new PGraphicsXxxx()", use this method. This method
	 * ensures that internal variables are set up properly that tie the
	 * new graphics context back to its parent PApplet.
	 * <LI>The basic way to create bitmap images is to use the <A
	 * HREF="http://processing.org/reference/saveFrame_.html">saveFrame()</A>
	 * function.
	 * <LI>If you want to create a really large scene and write that,
	 * first make sure that you've allocated a lot of memory in the Preferences.
	 * <LI>If you want to create images that are larger than the screen,
	 * you should create your own PGraphics object, draw to that, and use
	 * <A HREF="http://processing.org/reference/save_.html">save()</A>.
	 * <PRE>
	 *
	 * PGraphics big;
	 *
	 * void setup() {
	 *   big = createGraphics(3000, 3000);
	 *
	 *   big.beginDraw();
	 *   big.background(128);
	 *   big.line(20, 1800, 1800, 900);
	 *   // etc..
	 *   big.endDraw();
	 *
	 *   // make sure the file is written to the sketch folder
	 *   big.save("big.tif");
	 * }
	 *
	 * </PRE>
	 * <LI>It's important to always wrap drawing to createGraphics() with
	 * beginDraw() and endDraw() (beginFrame() and endFrame() prior to
	 * revision 0115). The reason is that the renderer needs to know when
	 * drawing has stopped, so that it can update itself internally.
	 * This also handles calling the defaults() method, for people familiar
	 * with that.
	 * <LI>With Processing 0115 and later, it's possible to write images in
	 * formats other than the default .tga and .tiff. The exact formats and
	 * background information can be found in the developer's reference for
	 * <A HREF="http://dev.processing.org/reference/core/javadoc/processing/core/PImage.html#save(java.lang.String)">PImage.save()</A>.
	 * </UL>
	 *
	 * @webref rendering
	 * @param w width in pixels
	 * @param h height in pixels
	 * @param renderer Either P2D, P3D, or PDF
	 *
	 * @see PGraphics#PGraphics
	 *
	 */
	public PGraphics createGraphics(int w, int h, String renderer) {
		PGraphics pg = makeGraphics(w, h, renderer, null, false);
		//pg.parent = this;  // make save() work
		return pg;
	}

	private static final long serialVersionUID = 4235377436017337897L;

}
