package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import frames.DrawingPanel;
import global.Constants.EFileMenu;

public class FileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private File defaultDirectoryFolder;
	private String defaultDirectoryFilePath;
	private String defaultDirectoryPath;
	private File file;
	private DrawingPanel drawingPanel;
	
	public FileMenu(String title) {
		super(title);
		
		readLocalStorage();
		this.file = null;
		
		ActionHandler actionHandler = new ActionHandler();
		for (EFileMenu eMenuItem: EFileMenu.values()) {
			JMenuItem menuItem = new JMenuItem(eMenuItem.getLabel());
			menuItem.setActionCommand(eMenuItem.name());
			menuItem.addActionListener(actionHandler);			
			this.add(menuItem);			
		}
	}

	public void associate(DrawingPanel drawingPanel) {this.drawingPanel = drawingPanel;}
	
	private void load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object object = ois.readObject();
			this.drawingPanel.setShapes(object);
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private boolean saveConfirmation() {
		int answer = JOptionPane.showConfirmDialog(this, "저장하지 않았습니다. 저장할까요?", "확인", JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}
	private boolean closeConfirmation() {
		int answer = JOptionPane.showConfirmDialog(this, "정말로 닫으시겠습니까?", "닫기", JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}
	private boolean quitConfirmation() {
		int answer = JOptionPane.showConfirmDialog(this, "정말로 종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}
	private void store(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this.drawingPanel.getShapes());
			oos.close();
			
			this.drawingPanel.setUpdated(false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void newPanel() {
		if(this.drawingPanel.isUpdated()) {
			if(saveConfirmation()) {
				save();
			}
		}
		this.drawingPanel.setShapes(null);
		this.file = null;
	}
	private void open() {
		if(this.drawingPanel.isUpdated()) {
			if(saveConfirmation()) {
				save();
			}
		}
		JFileChooser chooser = new JFileChooser(this.defaultDirectoryPath);
		int returnVal = chooser.showOpenDialog(this.drawingPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.file = chooser.getSelectedFile();
			this.load(this.file);
		}
	}
	
	private void save() {
		if(this.file == null) {
			this.saveAs();
		} else {
			this.store(this.file);
		}
	}
	
	private void saveAs() {
		JFileChooser chooser = new JFileChooser(this.defaultDirectoryPath);
		int returnVal = chooser.showSaveDialog(this.drawingPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.file = chooser.getSelectedFile();
			this.store(this.file);
			
			writeLocalStorage();
		}
	}
	private void close() {
		if(this.drawingPanel.isUpdated()) {
			if(saveConfirmation()) {
				save();
			}
		}
		if(closeConfirmation()) {
			System.exit(0);
		}
	}

	private void quit() {
		if(this.drawingPanel.isUpdated()) {
			if(saveConfirmation()) {
				save();
			}
		}
		if(quitConfirmation()) {
			System.exit(0);
		}
	}
	private void writeLocalStorage() {
		try {
			FileWriter rw = new FileWriter(this.defaultDirectoryFilePath);
			BufferedWriter bw = new BufferedWriter(rw);
			bw.write(this.file.getAbsolutePath());
			bw.close();
			this.defaultDirectoryPath = this.file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readLocalStorage() {
		try {
			this.defaultDirectoryFilePath = "C:\\ProgramData\\GraphicsEditor\\defaultDirectory.txt";
			this.defaultDirectoryFolder = new File("C:\\ProgramData\\GraphicsEditor");
			if(!this.defaultDirectoryFolder.exists()) {
				this.defaultDirectoryFolder.mkdir();
			}
			File file = new File(this.defaultDirectoryFilePath);
			if(!file.exists()) {
				FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			}
			FileReader rw = new FileReader(this.defaultDirectoryFilePath);
			BufferedReader br = new BufferedReader(rw);
			this.defaultDirectoryPath = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(EFileMenu.eNew.name())) {
				newPanel();
			} else if (e.getActionCommand().equals(EFileMenu.eOpen.name())) {
				open();
			} else if (e.getActionCommand().equals(EFileMenu.eClose.name())) {
				close();
			} else if (e.getActionCommand().equals(EFileMenu.eSave.name())) {
				save();
			} else if (e.getActionCommand().equals(EFileMenu.eSaveAs.name())) {
				saveAs();
			} else if (e.getActionCommand().equals(EFileMenu.eQuit.name())) {
				quit();
			}		
		}
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing() {
		this.quit();
	}
}
