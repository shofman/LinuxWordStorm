package wordcram;

import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.io.Serializable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/*
 * Quim Castella
 * 
 * Coordination of the word clouds
 * 
 * averageCenter- placement of the words using the iterative algorithm
 * mdsGradient- placement of the words using the optimisation approach
 * 
 * sameColor
 * sameAngle
 */

/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */

public class Coordinated implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1027491576672882940L;
	long rSeed;
	Random r;
	int tfIdf;
	transient PGraphics pg;

	Map<String, CoordProp> index;
	ArrayList<HashMap<String, ArrayList<String>>> overlapped;
	MDS mds;	

	public Coordinated( Map<String, CoordProp> index, MDS mds, 
			ArrayList<HashMap<String, ArrayList<String>>> overlapped, long rSeed, int tfIdf, PGraphics g){
		this.index = index;
		this.overlapped = overlapped;
		this.mds = mds;
		this.rSeed = rSeed;	
		this.r = new Random(rSeed);
		this.tfIdf = tfIdf;
		this.pg = g;
	}

	public void setOverlapped(ArrayList<HashMap<String, ArrayList<String>>> overlapped){
		this.overlapped = overlapped;
	}

	public WordPlacer averageCenter(){
		final float stdev = 0.2f;
		return new WordPlacer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -812761737972568084L;
			public MyPVector place(Word word, int wordIndex, int wordsCount,
					int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {

				CoordProp prop = index.get(word.word); 
				//				MyPVector curLoc = prop.getCurrentLocation(word.cloudIndex); // BAD INIT
				//				if (curLoc.z == -1.0f) //
				//					return new MyPVector(getOneUnder(fieldWidth - wordImageWidth), // 
				//							getOneUnder(fieldHeight - wordImageHeight));// 
				if( prop.getAverage().z == (float)-1 ){
					return new MyPVector(getOneUnder(fieldWidth - wordImageWidth),
							getOneUnder(fieldHeight - wordImageHeight));
				}else{
					//					if(prop.hasConverged(word.cloudIndex)){ //GOTO THE CENTER
					//						MyPVector curLoc = prop.getCurrentLocation(word.cloudIndex);
					//						MyPVector center = new MyPVector(fieldWidth/2, fieldHeight/2);
					//						MyPVector dirCenter = MyPVector.sub(center, curLoc);
					//						float eps = 0.01f;
					//						return MyPVector.add(curLoc, MyPVector.mult(dirCenter,eps));
					//					}

					return prop.getAverage();
				}
			}
			private int getOneUnder(float upperLimit) {
				return MyPApplet.myRound(MyPApplet.myMap((float) r.nextGaussian()
						* stdev, -2, 2, 0, upperLimit));
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

	public WordPlacer mdsGradient(){

		return new WordPlacer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2772276436433914928L;
			public MyPVector place(Word word, int wordIndex, int wordsCount,
					int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {				
				//				final float stdevX = 1; //0.6f;
				//				final float stdevY = 1; //0.4f;
				CoordProp prop = index.get(word.word);
				MyPVector curLoc = prop.getCurrentLocation(word.cloudIndex);
				MyPVector aver = prop.getAverage();

				if( aver.z == -1.0f ){ //First time in all word clouds
					return new MyPVector(r.nextInt(fieldWidth),r.nextInt(fieldHeight));
					//					return new MyPVector(getOneUnder(fieldWidth - wordImageWidth,stdevX), 
					//						getOneUnder(fieldHeight - wordImageHeight,stdevY));
				}
				if (curLoc.z == -1.0f){ //First time in this word cloud
					//					return new MyPVector(r.nextInt(fieldWidth),r.nextInt(fieldHeight));
					return aver;
				}

				MyPVector sameWordGrad = new MyPVector();
				
				if(mds.sameEps > 0.001){
					float sameWordEps = 0.000001f; // 1f; more weight?
					for(int ii: prop.getCloudIndices()){
						if(ii == word.cloudIndex) continue;
						MyPVector otherLoc = prop.getCurrentLocation(ii);
						sameWordGrad.x -= sameWordEps*mds.S[ii][word.cloudIndex]*(curLoc.x - otherLoc.x);	
						sameWordGrad.y -= sameWordEps*mds.S[ii][word.cloudIndex]*(curLoc.y - otherLoc.y);	
					}
					sameWordGrad.mult(mds.sameEps);
				}

				// OVERLAP
				boolean overlap = false;
				MyPVector overGrad = new MyPVector();
				if(mds.overEps > 0.001){
					Rectangle2D wRect = mds.rectangleLoc(word.word,word.cloudIndex);
					ArrayList<String> over = overlapped.get(word.cloudIndex).get(word.word);
					for(String other: over){
						MyPVector dir = genOverlapDirection(word, wRect, other);		
						overGrad.x -= dir.x;
						overGrad.y -= dir.y;
					}
					overGrad.mult(0.1f*mds.overEps);
				}
				//				if(overGrad.dist(new MyPVector(0f,0f))> 0.1){
				//					overlap = true;
				//				}

				// COMPACT
				MyPVector compGrad = new MyPVector();
				if(!overlap && mds.compEps > 0.001){
					MyPVector center = new MyPVector((float)(1.0*fieldWidth)/2,(float) (1.0*fieldHeight)/2);
					compGrad.x = curLoc.x - center.x;
					compGrad.y = curLoc.y - center.y;
					if(compGrad.dist(new MyPVector()) > 1){
						compGrad.normalize();
						//						compGrad.mult(5f);
					}
					compGrad.mult(0.1f*mds.compEps);
				}
				MyPVector step = new MyPVector(sameWordGrad.x + compGrad.x + overGrad.x, 
						sameWordGrad.y + compGrad.y + overGrad.y); 

				int maxStep = 30;
				if(step.dist(new MyPVector())>maxStep){
					step.normalize();
					step.mult(maxStep);
				}

				MyPVector newLoc = new MyPVector();
				newLoc.x = curLoc.x - step.x;
				newLoc.y = curLoc.y - step.y;			
				mds.updateGradient(step.dist(new MyPVector()));

				//SIZE WORD
				//				float sizeWordEps = 0.00000001f;
				//				float sizeWordGrad = 0;
				//				for(int ii = 0; ii < overlapped.size(); ++ii){
				//					if(ii == word.cloudIndex) continue;
				//					float otherWeight = 0;
				//					if(prop.getCloudIndices().contains(ii)){
				//						otherWeight= prop.getWord(ii).weight;
				//					}
				//					sizeWordGrad += sizeWordEps*mds.S[ii][word.cloudIndex]*(word.weight - otherWeight);	
				//				}
				//				float aux = word.weight; 

				//				word.weight -= sizeWordGrad;
				//				System.out.println(word + " "+word.weight+" "+word.origWeight);
				//				word.weight -= 0.01f*(word.weight - word.origWeight);
				//				word.weight = Math.min(Math.max(word.weight, 0.1f),1.5f);
				//if(Math.abs(1-word.weight/aux)>0.001) 
				//System.out.println(word +" " +(-sizeWordGrad)+" "+word.weight/aux);
				//				word.refactorSize(word.weight/aux);			
				return newLoc;
			}

			private MyPVector genOverlapDirection(Word word, Rectangle2D wRect, String other){				
				MyPVector oLoc = index.get(other).getCurrentLocation(word.cloudIndex);
				Rectangle2D auxRect = index.get(other).getWord(word.cloudIndex).getBounds();
				Rectangle2D oRect = new Rectangle2D.Double(oLoc.x, oLoc.y, auxRect.getWidth(), 
						auxRect.getHeight());
				return mds.minAxisOverlapDirection(wRect, oRect);
				//				return mds.minAxisOverlapDirectionNoise(wRect, oRect); // not improving
				//				return mds.minOverlapDirectionRed(wRect, oRect);
			}
			private int getOneUnder(float upperLimit, float stdev) {
				return MyPApplet.round(MyPApplet.map((float) r.nextGaussian()
						* stdev, -2, 2, 0, upperLimit));
			}
		};
	}

	public WordColorer sameColor(final MyPApplet host){
		return new HsbWordColorer(host) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 690771431780590347L;

			public int getColorFor(Word w) {

				CoordProp prop = index.get(w.word);
				int color = prop.getColor();
				if( color  == -1){
					float hue = random(256);
					float sat = 200;//156 + host.random(100);
					float bri = 50 + random(150);
					//					if(tfIdf == 2 || tfIdf == 3 || tfIdf == 4)
					//						bri = 30+120*w.idf;
					//					color = host.color(hue, sat, bri);
					float alpha = 255;
					if(tfIdf == 2) {
						float A = 40;
						float K = 100;
						float S = 20;
						float x0 = 0.2f;
						float idf = prop.getIdf();
						alpha = (255/K)*(A + (float) ((K-A)/(1+Math.exp(-S*(idf-x0)))));
					}
					MyPApplet temp = new MyPApplet();
					try {
						temp.g = pg;
					} catch (Exception e){
						
					}

					color = temp.colorer(hue, sat, bri, alpha);
					prop.setColor(color);
				}
				return color;
			}
		};
	}

	public WordAngler sameAngle() {
		return new WordAngler() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -517498624866844146L;

			public float angleFor(Word w) {
				CoordProp prop = index.get(w.word);
				float angle = prop.getAngle();
				if( Math.abs(angle + 1f)< 0.1 ){
					float[] aux = {0f, 0f, 0f, 0f, 0f, PConstants.HALF_PI, -PConstants.HALF_PI};
					angle = aux[r.nextInt(aux.length)];
					prop.setAngle(angle);
				}
				return angle;
			}
		};
	}
}
