package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;

import shapes.TAnchors.EAnchors;

public class TAnchors implements Serializable{

	private final int WIDTH = 15;
	private final int HEIGHT = 15;
	
	public enum EAnchors {
		eNW,
		eWW,
		eSW,
		eSS,
		eSE,
		eEE,
		eNE,
		eNN,
		eRR,
		eMove
	}
	private Ellipse2D anchors[];
	private EAnchors eSelectedAnchor;
	private EAnchors eResizeAnchor;
	
	public EAnchors geteResizeAnchor() {
		return this.eResizeAnchor;
	}
	public void seteResizeAnchor(EAnchors eResizeAnchor) {
		this.eResizeAnchor = eResizeAnchor;
	}
	public EAnchors geteSelectedAnchor() {
		return this.eSelectedAnchor;
	}
	public void setSelectedAnchor(EAnchors eSelectedAnchor) {
		this.eSelectedAnchor = eSelectedAnchor;
	}
	
	// constructors
	public TAnchors() {
		this.anchors = new Ellipse2D[EAnchors.values().length-1];
		for (int i=0; i<EAnchors.values().length-1; i++) {
			this.anchors[i] = new Ellipse2D.Double();
		}
	}
	
	// methods
	public boolean contains(int x, int y) {
		for (int i=0; i<EAnchors.values().length-1; i++) {			
			if (this.anchors[i].contains(x, y)) {
				this.eSelectedAnchor = EAnchors.values()[i];
				return true;
			}
		}
		return false;
	}
	
	public void draw(Graphics2D graphics2D, Rectangle boundingRectangle, AffineTransform affineTransform) {
		
		for (int i=0; i<EAnchors.values().length-1; i++) {
			EAnchors eAnchor = EAnchors.values()[i];
			int x =  boundingRectangle.x;
			int y =  boundingRectangle.y;
			int w =  boundingRectangle.width;
			int h =  boundingRectangle.height;
			
			switch (eAnchor) {
				case eNW:							break;
				case eWW:				y = y+h/2;	break;				
				case eSW:				y = y+h;	break;				
				case eSS:	x = x+w/2;	y = y+h;	break;				
				case eSE:	x = x+w;	y = y+h;	break;				
				case eEE:	x = x+w;	y = y+h/2;	break;				
				case eNE:	x = x+w;				break;				
				case eNN:	x = x+w/2;				break;				
				case eRR:	x = x+w/2;	y = y-h/2;	break;
				default:							break;
			}
			x = x - WIDTH/2;
			y = y - HEIGHT/2;
			
			this.anchors[eAnchor.ordinal()].setFrame(x, y, WIDTH, HEIGHT);
			Shape transformedShape = affineTransform.createTransformedShape(this.anchors[eAnchor.ordinal()]);
			graphics2D.setColor(Color.WHITE);
			graphics2D.fill(transformedShape);
			graphics2D.setColor(Color.BLACK);
			graphics2D.draw(transformedShape);
		}
	}
	public Point2D getResizeAnchorPoint() {
		this.eResizeAnchor = null;
		switch (this.eSelectedAnchor) {
			case eNW:	eResizeAnchor = EAnchors.eSE;		break;
			case eWW:	eResizeAnchor = EAnchors.eEE;		break;				
			case eSW:	eResizeAnchor = EAnchors.eNE;		break;				
			case eSS:	eResizeAnchor = EAnchors.eNN;		break;				
			case eSE:	eResizeAnchor = EAnchors.eNW;		break;				
			case eEE:	eResizeAnchor = EAnchors.eWW;		break;				
			case eNE:	eResizeAnchor = EAnchors.eSW;		break;				
			case eNN:	eResizeAnchor = EAnchors.eSS;		break;				
			default:										break;
		}
		double cx = this.anchors[eResizeAnchor.ordinal()].getCenterX();
		double cy = this.anchors[eResizeAnchor.ordinal()].getCenterY();
		return new Point2D.Double(cx, cy);
	}
	public void clearAnchor(Graphics2D graphics2D, Rectangle boundingRectangle, AffineTransform affineTransform) {
		for (int i=0; i<this.anchors.length; i++) {
			int x = (int) this.anchors[i].getX();
			int y = (int) this.anchors[i].getY();
			int w = (int) this.anchors[i].getWidth();
			int h = (int) this.anchors[i].getHeight();
			graphics2D.clearRect(x, y, w, h);
			graphics2D.setXORMode(Color.WHITE);
			graphics2D.setColor(Color.WHITE);
			graphics2D.fill(this.anchors[i]);
		}
	}

}
