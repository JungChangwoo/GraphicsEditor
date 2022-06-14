package frames;
import javax.swing.JMenuBar;

import menus.EditMenu;
import menus.FileMenu;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	private FileMenu fileMenu;
	private EditMenu editMenu;
	
	private DrawingPanel drawingPanel;
	
	public MenuBar() {
		this.fileMenu = new FileMenu("파일");
		this.add(this.fileMenu);
		
		this.editMenu = new EditMenu("편집");
		this.add(this.editMenu);
		
	}
	
	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		
		this.fileMenu.associate(this.drawingPanel);
	}

	public void initialize() {
		this.fileMenu.initialize();
		this.editMenu.initialize();
	}

	public void windowClosing() {
		this.fileMenu.windowClosing();
	}
}
