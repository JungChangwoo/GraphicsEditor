package transformers;

import java.awt.Graphics2D;

import shapes.TShape;

public class Rotater extends Transformer {
	private double cx, cy;
	private double startAngle, endAngle;

	public Rotater(TShape shape) {
		super(shape);
	}

	@Override
	public void prepare(int x, int y) {
		this.cx = this.shape.getCenterX();
		this.cy = this.shape.getCenterY();
		this.px = x;
		this.py = y;
	}

	@Override
	public void keepTransforming(int x, int y) {
		double startAngle = Math.atan2(px-cx, py-cy);
		double endAngle = Math.atan2(x-cx, y-cy);
		double angle = startAngle - endAngle;
		
		this.affineTransform.rotate(angle, this.cx, this.cy);
		this.px = x;
		this.py = y;
	}
	
	@Override
	public void finalize(int x, int y) {
		
	}
		
}