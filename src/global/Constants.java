package global;

import shapes.TShape;
import shapes.TRectangle;
import shapes.TOval;
import shapes.TLine;
import shapes.TPolygon;
import shapes.TSelection;

public class Constants {
	public enum ETransformationStyle {
		e2Point,
		eNPoint, 
		eText
	}
	public enum ETools { 
		eSelection("����", "selection.PNG", "�����Դϴ�.", new TSelection(), ETransformationStyle.e2Point),
		eOval("���׶��", "ovalIcon.PNG", "�����Դϴ�.", new TOval(), ETransformationStyle.e2Point),
		eRectanble("�׸�", "rectangleIcon.PNG", "�簢���Դϴ�.", new TRectangle(), ETransformationStyle.e2Point),
		eLine("����", "lineIcon.PNG", "�����Դϴ�.", new TLine(), ETransformationStyle.e2Point),
		ePolygon("�ٰ���", "polygonIcon.PNG", "������ �����Դϴ�.", new TPolygon(), ETransformationStyle.eNPoint), 
		eText("�ؽ�Ʈ", "text.PNG", "�ؽ�Ʈ�Դϴ�.", new TRectangle(), ETransformationStyle.eText);
		
		private String label;
		private String iconPath;
		private String toolTipText;
		private TShape tool;
		private ETransformationStyle eTransformationStyle;
		
		private ETools(String label, String iconPath, String toolTipText, TShape tool, ETransformationStyle eTransformationStyle) {
			this.label = label;
			this.iconPath = iconPath;
			this.toolTipText = toolTipText;
			this.tool = tool;
			this.eTransformationStyle = eTransformationStyle;
		}
		public String getLabel() {
			return this.label;
		}
		public String getIconPath() {
			return this.iconPath;
		}
		public String getToolTipText() {
			return this.toolTipText;
		}
		public TShape newShape() {
			return this.tool.clone();
		}
		public ETransformationStyle getETransformationStyle() {
			return this.eTransformationStyle;
		}
	}
	
	public enum EThicknessTools {
		eNormal("����", 1),
		eBold3("���� 3", 3),
		eBold5("���� 5", 5),
		eBold7("���� 7", 7);
		
		private String label;
		private double width;
		
		private EThicknessTools(String label, double width) {
			this.label = label;
			this.width = width;
		}
		public String getLabel() {
			return this.label;
		}
		public double getWidth() {
			return this.width;
		}
	}
	
	public enum EFileMenu {
		eNew("���� �����"),
		eOpen("����"),
		eClose("�ݱ�"),
		eSave("����"),
		eSaveAs("�ٸ��̸�����"),
		ePrint("����Ʈ"),
		eQuit("����");
		
		private String label;
		
		private EFileMenu(String label) {
			this.label = label;
		}
		public String getLabel() {
			return this.label;
		}
	}
	
	public enum EEditMenu {
		eCut("�ڸ���"),
		eCopy("����"),
		ePaste("�ٿ��ֱ�"),
		eGroup("�׷�ȭ"),
		eUnGroup("�׷�ȭ ����"),
		eUndo("���� ���"),
		eRedo("�ٽ� ����");
		
		private String label;
		private EEditMenu(String label) {
			this.label = label;
		}
		public String getLabel() {
			return this.label;
		}
	}
}
