package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import global.Constants.EEditMenu;
import menus.EditMenu.ActionHandler;

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
		
		ActionHandler actionHandler = new ActionHandler();
		for (EEditMenu eMenuItem: EEditMenu.values()) {
			JMenuItem menuItem = new JMenuItem(eMenuItem.getLabel());
			menuItem.setActionCommand(eMenuItem.name());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);
		}
		
	}
	
	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(EEditMenu.eCut.name())) {
				
			} else if (e.getActionCommand().equals(EEditMenu.eCopy.name())) {
				
			} else if (e.getActionCommand().equals(EEditMenu.ePaste.name())) {
				
			} else if (e.getActionCommand().equals(EEditMenu.eUndo.name())) {
				
			} else if (e.getActionCommand().equals(EEditMenu.eRedo.name())) {
				
			}
		}
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
