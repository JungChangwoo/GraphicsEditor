package frames;
import java.awt.BasicStroke;
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

import global.Constants.EThicknessTools;
import global.Constants.ETools;
import global.Constants.ETransformationStyle;
import shapes.TAnchors.EAnchors;
import shapes.TSelection;
import shapes.TShape;
import shapes.TText;
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
	private Vector<TText> texts;
	
	// associated attribute
	private TShape selectedShape;
	private ETools selectedTool;
	private TShape currentShape;
	private Transformer transformer;
	private Color selectedColor;
	private EThicknessTools selectedThicknessTool;
	private String inputedText;
	private TText currentText;
	
	// working variables
	private enum EDrawingState {
		eIdle,
		e2PointTransformation,
		eNPointTransformation,
		eText;
	}
	EDrawingState eDrawingState;

	private boolean beUpdated;
	
	public DrawingPanel() {
		this.setBackground(Color.WHITE);
		this.eDrawingState = EDrawingState.eIdle;
		this.beUpdated = false;
		this.shapes = new Vector<TShape>();
		this.selectedColor = null;
		this.selectedThicknessTool = EThicknessTools.eNormal;
		this.inputedText = null;
		this.texts = new Vector<TText>();
		
		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addMouseWheelListener(mouseHandler);
	}
	
	// getters and setters
	public boolean isUpdated() {return this.beUpdated;}
	public void setUpdated(boolean beUpdated) {this.beUpdated = beUpdated;}
	public Color getSelectedColor() {return selectedColor;}
	public void setSelectedColor(Color selectedColor) {this.selectedColor = selectedColor;}
	public EThicknessTools getSelectedThicknessTool() {return selectedThicknessTool;}
	public void setSelectedThicknessTool(EThicknessTools selectedThicknessTool) {this.selectedThicknessTool = selectedThicknessTool;}
	public String getInputedText() {return inputedText;}
	public void setInputedText(String inputedText) {this.inputedText = inputedText;}
	public void seteDrawingState() {this.eDrawingState = EDrawingState.eText;}
	
	public void initialize() {
		this.bufferedImage = (BufferedImage) this.createImage(this.getWidth(), this.getHeight());
		this.graphics2DBufferedImage = (Graphics2D) this.bufferedImage.getGraphics();
		this.graphics2DBufferedImage.setBackground(Color.WHITE);
	}
	
	// file open/save
	public Object getShapes() {	
		return this.shapes;	
	}
	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) {
		if(shapes == null) {
			this.shapes = new Vector<TShape>();
			this.texts = new Vector<TText>();
			this.setUpdated(false);
		} else {
			this.shapes = (Vector<TShape>) shapes;	
		}
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
		this.initialize();
		this.graphics2DBufferedImage.clearRect(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
		for (TShape shape: this.shapes) {
			shape.draw(this.graphics2DBufferedImage);
		}
		if(this.texts != null) {
			for (TText text: this.texts) {
				text.draw(this.graphics2DBufferedImage);
			}	
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
		} else if (selectedTool == ETools.eText) {
			this.currentText = new TText();
			this.currentText.setX(x);
			this.currentText.setY(y);
			this.currentText.setText(this.inputedText);
			this.currentText.setColor(this.selectedColor);
			this.currentText.setThickness(this.selectedThicknessTool.getWidth());
			this.currentText.draw(this.graphics2DBufferedImage);
			this.texts.add(this.currentText);
			this.getGraphics().drawImage(this.bufferedImage, 0, 0, this);
			this.setUpdated(true);
		} else {
			this.currentShape = this.selectedTool.newShape();
			this.transformer = new Drawer(currentShape);
			this.currentShape.setColor(this.selectedColor);
			this.currentShape.setThickness(this.selectedThicknessTool.getWidth());
		}
		this.transformer.prepare(x, y);
		this.graphics2DBufferedImage.setXORMode(this.getBackground());
	}
	
	private void keepTransformation(int x, int y) {
		// erase
		this.graphics2DBufferedImage.setXORMode(this.getBackground());
		this.currentShape.draw(this.graphics2DBufferedImage);
//		this.getGraphics().drawImage(this.bufferedImage, 0, 0, this); 
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
		this.currentShape.clearAnchor(this.graphics2DBufferedImage);
		this.repaint();
		this.setUpdated(true);
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
			} else {
				prepareTransformation(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
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
