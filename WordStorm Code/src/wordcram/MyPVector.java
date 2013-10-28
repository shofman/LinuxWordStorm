package wordcram;
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import processing.core.PVector;
import java.io.Serializable;

public class MyPVector extends PVector implements Serializable {
	float xvalue;
	float yvalue;
	
	public MyPVector(int oneUnder, int oneUnder2) {
		// TODO Auto-generated constructor stub
		super(oneUnder, oneUnder2);
	}

	public MyPVector(float x, float y) {
		// TODO Auto-generated constructor stub
		super(x, y);
	}

	public MyPVector() {
		// TODO Auto-generated constructor stub
		super();
	}

	public MyPVector(int i, int j, int k) {
		// TODO Auto-generated constructor stub
		super(i,j,k);
	}

	/**
	 * 
	 */
	
	@Override
	public MyPVector get() {
		MyPVector returner = new MyPVector(super.get().x, super.get().y);
		if(returner.x != xvalue) {
			xvalue = returner.x;
		}
		if (returner.y != yvalue) {
			yvalue = returner.y;
		}
		return returner;
	}
	
	public MyPVector add(MyPVector x, MyPVector y) {
		return new MyPVector(super.add(x, y).get().x, super.add(x,y).get().y);
	}

	private static final long serialVersionUID = -1382958699833533463L;

}