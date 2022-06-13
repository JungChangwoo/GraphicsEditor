package transformers;

import shapes.TShape;

public class Drawer extends Transformer {

	public Drawer(TShape shape) {
		super(shape);
	}

	@Override
	public void prepare(int x, int y) {
		this.shape.prepareDrawing(x, y);
	}

	@Override
	public void keepTransforming(int x, int y) {
		this.shape.keepDrawing(x, y);
	}

	@Override
	public void finalize(int x, int y) {
		
	}
	
}