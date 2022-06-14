package frames;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import global.Constants.EThicknessTools;
import global.Constants.ETools;
import shapes.TShape;

public class ToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	private DrawingPanel drawingPanel;
	private JTextField tf;
	
	public ToolBar() {
		ButtonGroup buttonGroup = new ButtonGroup();
		ActionHandler actionHandler = new ActionHandler();
		
		String resourcePath = "/resource/";
		
		int count = 0;
		// Drawing Tool
		for (ETools eTool: ETools.values()) {
			if (count == 5) {
				break;
			}
			JRadioButton drawingTool = new JRadioButton();
			drawingTool.setActionCommand(eTool.name());
			drawingTool.addActionListener(actionHandler);
			setIcon(resourcePath, eTool, drawingTool);
			drawingTool.setToolTipText(eTool.getToolTipText());
			this.add(drawingTool);
			buttonGroup.add(drawingTool);
			count += 1;
		}
		// Color Tool
		ColorActionHandler colorActionHandler = new ColorActionHandler();
		JRadioButton colorTool = new JRadioButton();
		colorTool.addActionListener(colorActionHandler);
		setIcon(resourcePath, colorTool);
		colorTool.setToolTipText("색깔입니다.");
		this.add(colorTool);
		buttonGroup.add(colorTool);
		
		// Thickness Tool
		JComboBox<String> thicknessComboBox = new JComboBox();
		thicknessComboBox.setBounds(10, 10, 10, 25);
		for (EThicknessTools eThicknessTool: EThicknessTools.values()) {
			thicknessComboBox.addItem(eThicknessTool.getLabel());
		}
		thicknessComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = thicknessComboBox.getSelectedIndex();
				drawingPanel.setSelectedThicknessTool(EThicknessTools.values()[index]);
			}
			
		});
		this.add(thicknessComboBox);
		
		// Text Tool
		this.tf = new JTextField();
		this.add(tf);
		TextActionHandler textActionHandler = new TextActionHandler();
		JButton tfBtn = new JButton("확인");
		tfBtn.addActionListener(textActionHandler);
		this.add(tfBtn);
	}
	
	private void setIcon(String resourcePath, JRadioButton colorTool) {
		ImageIcon icon = new ImageIcon(getClass().getResource(resourcePath + "color.PNG"));
		Image img = icon.getImage();
		Image changeImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		colorTool.setIcon(changeIcon);
	}
	private void setIcon(String resourcePath, ETools eTool, JRadioButton drawingTool) {
		ImageIcon icon = new ImageIcon(getClass().getResource(resourcePath + eTool.getIconPath()));
		Image img = icon.getImage();
		Image changeImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		drawingTool.setIcon(changeIcon);
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		JRadioButton defaultButton = (JRadioButton) this.getComponent(ETools.eRectanble.ordinal());
		defaultButton.doClick();
	}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			drawingPanel.setSelectedTool(ETools.valueOf(e.getActionCommand()));
		}	
	}
	
	private class ColorActionHandler implements ActionListener {
		JColorChooser chooser = new JColorChooser();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Color selectedColor = chooser.showDialog(null, "Color", Color.WHITE);
			if(selectedColor != null) {
				drawingPanel.setSelectedColor(selectedColor);
			}
		}
		
	}
	
	private class TextActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String input = tf.getText();
			if(input != null) {
				drawingPanel.setInputedText(input);
				drawingPanel.seteDrawingState();
				drawingPanel.setSelectedTool(ETools.eText);
			}
		}
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
