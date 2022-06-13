package menus;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class EditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	
	private JMenuItem cutItem;
	private JMenuItem copyItem;
	private JMenuItem pasteItem;
	
	private JMenuItem groupItem;
	private JMenuItem unGroupItem;
	
	public EditMenu(String title) {
		super(title);
		
		this.undoItem = new JMenuItem("undo");
		this.add(this.undoItem);		
		this.redoItem = new JMenuItem("redo");
		this.add(this.redoItem);
		
		this.cutItem = new JMenuItem("cut");
		this.add(this.cutItem);		
		this.copyItem = new JMenuItem("copy");
		this.add(this.copyItem);		
		this.pasteItem = new JMenuItem("paste");
		this.add(this.pasteItem);
		
		this.groupItem = new JMenuItem("group");
		this.add(this.groupItem);		
		this.unGroupItem = new JMenuItem("ungroup");
		this.add(this.unGroupItem);
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
