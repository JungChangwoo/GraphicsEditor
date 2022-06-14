package frames;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private MenuBar menuBar;
	private ToolBar toolBar;
	private DrawingPanel drawingPanel;
	
	public MainFrame() {
		this.setSize(800, 800);
		
		BorderLayout layoutManager = new BorderLayout();
		this.setLayout(layoutManager);
		
		this.menuBar = new MenuBar();
		this.setJMenuBar(this.menuBar);
		
		this.toolBar = new ToolBar();
		this.add(this.toolBar, BorderLayout.NORTH);
		
		this.drawingPanel = new DrawingPanel();
		this.add(this.drawingPanel, BorderLayout.CENTER);
		
		this.menuBar.associate(this.drawingPanel);
		this.toolBar.associate(this.drawingPanel);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				menuBar.windowClosing();
			}
		});
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void initialize() {
		this.menuBar.initialize();
		this.toolBar.initialize();
		this.drawingPanel.initialize();
	}

}
