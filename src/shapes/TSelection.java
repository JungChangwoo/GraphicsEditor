package shapes;

import java.awt.Rectangle;

public class TSelection extends TShape {	
	private static final long serialVersionUID = 1L;

	public TSelection() {
		this.shape = new Rectangle();
	}
	
	public TShape clone() {
		return new TSelection();
	}
	
	public void prepareDrawing(int x, int y) {
		Rectangle rectangle = (Rectangle) this.shape;
		rectangle.setBounds(x, y, 0, 0);
	}
	
	@Override
	public void keepDrawing(int x, int y) {
		Rectangle rectangle = (Rectangle) this.shape;
		rectangle.setSize(x-rectangle.x, y-rectangle.y);
	}
}
