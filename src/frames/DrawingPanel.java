package frames;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import global.Constants.ETools;
import global.Constants.ETransformationStyle;
import shapes.TAnchors.EAnchors;
import shapes.TSelection;
import shapes.TShape;
import transformers.Drawer;
import transformers.Mover;
import transformers.Resizer;
import transformers.Rotater;
import transformers.Transformer;

public class DrawingPanel extends JPanel {

	// attributes
	private static final long serialVersionUID = 1L;
	
	// components
	private Vector<TShape> shapes;
	private BufferedImage bufferedImage;
	private Graphics2D graphics2DBufferedImage;
	
	// associated attribute
	private TShape selectedShape;
	private ETools selectedTool;
	private TShape currentShape;
	private Transformer transformer;
	
	// working variables
	private enum EDrawingState {
		eIdle,
		e2PointTransformation,
		eNPointTransformation
	}
	EDrawingState eDrawingState;
	
	public DrawingPanel() {
		this.setBackground(Color.WHITE);
		this.eDrawingState = EDrawingState.eIdle;
		
		this.shapes = new Vector<TShape>();
		
		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addMouseWheelListener(mouseHandler);
	}
	
	public void initialize() {
		this.bufferedImage = (BufferedImage) this.createImage(this.getWidth(), this.getHeight());
		this.graphics2DBufferedImage = (Graphics2D) this.bufferedImage.getGraphics();
	}
	
	// file open/save
	public Object getShapes() {	
		return this.shapes;	
	}
	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) { 
		this.shapes = (Vector<TShape>) shapes;
		this.repaint();
	}
		
	public void setSelectedTool(ETools selectedTool) {
		this.selectedTool = selectedTool;		
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		if (this.selectedTool == ETools.eSelection) {
			cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		}
		this.setCursor(cursor);
	}

	// overriding
	public void paint(Graphics graphics) {
		super.paint(graphics);
		this.graphics2DBufferedImage.clearRect(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
		for (TShape shape: this.shapes) {
			shape.draw(this.graphics2DBufferedImage);
		}
		graphics.drawImage(this.bufferedImage, 0, 0, this);
		
	}	

	private void prepareTransformation(int x, int y) {
		if (selectedTool == ETools.eSelection) {
			currentShape = onShape(x, y);
			if (currentShape != null) {
				if(currentShape.getSelectedAnchor() == EAnchors.eMove) {
					this.transformer = new Mover(currentShape);
				} else if (currentShape.getSelectedAnchor() == EAnchors.eRR) {
					this.transformer = new Rotater(currentShape);
				} else {
					this.transformer = new Resizer(currentShape);
				}
			} else {
				this.currentShape = this.selectedTool.newShape();
				this.transformer = new Drawer(currentShape);
			}
		} else {
			this.currentShape = this.selectedTool.newShape();
			this.transformer = new Drawer(currentShape);
		}
		
		this.transformer.prepare(x, y);
		this.graphics2DBufferedImage.setXORMode(this.getBackground());
	}
	
	private void keepTransformation(int x, int y) {
		// erase
		this.currentShape.draw(this.graphics2DBufferedImage);
		this.getGraphics().drawImage(this.bufferedImage, 0, 0, this);
		// draw
		this.transformer.keepTransforming(x, y);
		this.currentShape.draw(this.graphics2DBufferedImage);
		this.getGraphics().drawImage(this.bufferedImage, 0, 0, this);
		
	}
	
	private void continueTransformation(int x, int y) {
		this.currentShape.addPoint(x, y);
	}
	
	private void finishTransformation(int x, int y) {
		this.graphics2DBufferedImage.setPaintMode();
		this.transformer.finalize(x, y);
		
		if(this.selectedShape != null) {
			this.selectedShape.setSelected(false);
		}
		if (!(this.currentShape instanceof TSelection)) {
			this.shapes.add(this.currentShape);
			this.selectedShape = this.currentShape;
			this.selectedShape.setSelected(true);
		}
		
		this.repaint();
	}	

	private TShape onShape(int x, int y) {
		for (TShape shape: this.shapes) {
			if (shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}
	private void changeSelection(int x, int y) {
		// erase previous selection
		if(this.selectedShape != null) {
			this.selectedShape.setSelected(false);
		}
		// draw new selection
		this.selectedShape = this.onShape(x, y);
		if (this.selectedShape != null) {
			this.selectedShape.setSelected(true);
			this.selectedShape.draw((Graphics2D) this.getGraphics());
		}
		this.repaint();
	}
	
	private void changeCursor(int x, int y) {
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		if (this.selectedTool == ETools.eSelection) {
			cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		}
		
		this.currentShape = onShape(x, y);
		if (this.currentShape != null) {
			cursor = new Cursor(Cursor.MOVE_CURSOR);
			if (this.currentShape.isSelected()) {
				EAnchors eAnchor = this.currentShape.getSelectedAnchor();
				switch (eAnchor) {
					case eRR: cursor = new Cursor(Cursor.HAND_CURSOR);				break;
					case eWW: cursor = new Cursor(Cursor.W_RESIZE_CURSOR);			break;
					case eNW: cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);			break;
					case eSW: cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);			break;
					case eSS: cursor = new Cursor(Cursor.S_RESIZE_CURSOR);			break;
					case eSE: cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);			break;
					case eEE: cursor = new Cursor(Cursor.E_RESIZE_CURSOR);			break;
					case eNE: cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);			break;
					case eNN: cursor = new Cursor(Cursor.N_RESIZE_CURSOR);			break;
					default: break;
				}
			}
		}
		this.setCursor(cursor);
	}
	
	private class MouseHandler implements MouseInputListener, MouseWheelListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) {
					this.lButtonClicked(e);
				} else if (e.getClickCount() == 2) {
					this.lButtonDoubleClicked(e);
				}
			}
		}
		
		private void lButtonClicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				changeSelection(e.getX(), e.getY());
				if (selectedTool.getETransformationStyle() == ETransformationStyle.eNPoint) {
					prepareTransformation(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNPointTransformation;
				}
			} else if (eDrawingState == EDrawingState.eNPointTransformation) {
				continueTransformation(e.getX(), e.getY());
			}
		}
		private void lButtonDoubleClicked(MouseEvent e) {			
			if (eDrawingState == EDrawingState.eNPointTransformation) {
				finishTransformation(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNPointTransformation) {
				keepTransformation(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (selectedTool.getETransformationStyle() == ETransformationStyle.e2Point) {
					prepareTransformation(e.getX(), e.getY());
					eDrawingState = EDrawingState.e2PointTransformation;
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2PointTransformation) {
				keepTransformation(e.getX(), e.getY());
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2PointTransformation) {
				finishTransformation(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	
	}

}
