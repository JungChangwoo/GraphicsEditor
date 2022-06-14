package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import shapes.TAnchors.EAnchors;

abstract public class TShape implements Serializable {
	
	// attributes
	private static final long serialVersionUID = 1L;
	private boolean bSelected;
	
	// components
	protected Shape shape;
	private AffineTransform affineTransform;
	private TAnchors anchors;
	private Color color;
	private double thickness;
	private String text;
	
	// setters and getters
	public Shape getShape() {return this.shape;}
	public void setShape(Shape shape) {this.shape = shape;}
	public TAnchors getAnchors() {return anchors;}
	public AffineTransform getAffineTransform() {return affineTransform;}
	public boolean isSelected() {return this.bSelected;}
	public void setSelected(boolean bSelected) {this.bSelected = bSelected;}
	public EAnchors getSelectedAnchor() {return this.anchors.geteSelectedAnchor();}
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}
	public double getThickness() {return this.thickness;}
	public void setThickness(double thickness) {this.thickness = thickness;}
	
	// constructor
	public TShape() {
		this.affineTransform = new AffineTransform();
		this.affineTransform.setToIdentity();
		this.anchors = new TAnchors();
		this.bSelected = false;
		this.color = Color.BLACK;
	}
	
	public abstract TShape clone();
	public void initialize() {}
	
	public double getCenterX() {return this.shape.getBounds2D().getCenterX();}
	public double getCenterY() {return this.shape.getBounds2D().getCenterY();}	
	
	// methods
	public boolean contains(int x, int y) {
		if (isSelected()) {
			if (this.anchors.contains(x, y)) {
				return true;
			};
		}
		Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
		if(transformedShape.getBounds().contains(x, y)) {
			this.anchors.setSelectedAnchor(EAnchors.eMove);
			return true;
		}
		return false;
	}	
	public void draw(Graphics2D graphics2D) {
		Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
		graphics2D.setStroke(new BasicStroke((float) this.getThickness()));
		graphics2D.setColor(this.getColor());
		graphics2D.draw(transformedShape);
		graphics2D.setColor(Color.BLACK);
		graphics2D.setStroke(new BasicStroke(1));
		if (isSelected()) {
			this.anchors.draw(graphics2D, this.shape.getBounds(), this.affineTransform);
		}
	}
	
	public abstract void prepareDrawing(int x, int y);
	public abstract void keepDrawing(int x, int y);
	public void addPoint(int x, int y) {}
	public void clearAnchor(Graphics2D graphics2D) {
		if (isSelected()) {
			this.anchors.clearAnchor(graphics2D, this.shape.getBounds(), this.affineTransform);
		}
	}

}

