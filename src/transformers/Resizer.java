package transformers;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import shapes.TShape;
import shapes.TAnchors.EAnchors;

public class Resizer extends Transformer {
	private double xScale, yScale;
	private double cx, cy;

	public Resizer(TShape shape) {
		super(shape);
	}

	@Override
	public void prepare(int x, int y) {
		this.px = x;
		this.py = y;
		Point2D resizeAnchorPoint = this.anchors.getResizeAnchorPoint();
		this.cx = resizeAnchorPoint.getX();
		this.cy = resizeAnchorPoint.getY();
	}

	@Override
	public void keepTransforming(int x, int y) {
		this.getResizeScale(x, y);
		this.affineTransform.translate(this.cx, this.cy);
		this.affineTransform.scale(this.xScale, this.yScale);
		this.affineTransform.translate(-this.cx, -this.cy);
		this.px = x;
		this.py = y;
	}
	
	@Override
	public void finalize(int x, int y) {
		Shape shape = this.affineTransform.createTransformedShape(this.shape.getShape());
		this.shape.setShape(shape);
		this.affineTransform.setToIdentity();
	}
	private void getResizeScale(int x, int y) {
		EAnchors eResizeAnchor = this.anchors.geteResizeAnchor();
		double w1 = this.px - this.cx;
		double w2 = x - this.cx;
		double h1 = this.py - this.cy;
		double h2 = y - this.cy;
		
		switch (eResizeAnchor) {
			case eNW:	xScale = w2/w1; yScale = h2/h1;		break;
			case eWW:	xScale = w2/w1; yScale = 1;			break;				
			case eSW:	xScale = w2/w1; yScale = h2/h1;		break;				
			case eSS:	xScale = 1; 	yScale = h2/h1;		break;				
			case eSE:	xScale = w2/w1; yScale = h2/h1;		break;				
			case eEE:	xScale = w2/w1; yScale = 1;			break;				
			case eNE:	xScale = w2/w1; yScale = h2/h1;		break;				
			case eNN:	xScale = 1; 	yScale = h2/h1;		break;				
			default:										break;
		}
	}
		
}