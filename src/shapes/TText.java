package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class TText {
	private String text;
	private Color color;
	private double thickness;
	private int x, y;

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}
	public double getThickness() {return thickness;}
	public void setThickness(double thickness) {this.thickness = thickness;}
	public double getX() {return x;}
	public void setX(int x) {this.x = x;}
	public double getY() {return y;}
	public void setY(int y) {this.y = y;}
	
	public void draw(Graphics2D graphics2dBufferedImage) {
		graphics2dBufferedImage.setColor(this.color);
		graphics2dBufferedImage.setStroke(new BasicStroke((float) this.getThickness()));
		graphics2dBufferedImage.drawString(this.text, this.x, this.y);
		graphics2dBufferedImage.setColor(Color.BLACK);
		graphics2dBufferedImage.setStroke(new BasicStroke(1));
	}
}
