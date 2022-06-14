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
		eSelection("선택", "selection.PNG", "선택입니다.", new TSelection(), ETransformationStyle.e2Point),
		eOval("동그라미", "ovalIcon.PNG", "원형입니다.", new TOval(), ETransformationStyle.e2Point),
		eRectanble("네모", "rectangleIcon.PNG", "사각형입니다.", new TRectangle(), ETransformationStyle.e2Point),
		eLine("라인", "lineIcon.PNG", "라인입니다.", new TLine(), ETransformationStyle.e2Point),
		ePolygon("다각형", "polygonIcon.PNG", "자유형 도형입니다.", new TPolygon(), ETransformationStyle.eNPoint), 
		eText("텍스트", "text.PNG", "텍스트입니다.", new TRectangle(), ETransformationStyle.eText);
		
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
		eNormal("보통", 1),
		eBold3("굵기 3", 3),
		eBold5("굵기 5", 5),
		eBold7("굵기 7", 7);
		
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
		eNew("새로 만들기"),
		eOpen("열기"),
		eClose("닫기"),
		eSave("저장"),
		eSaveAs("다른이름으로"),
		ePrint("프린트"),
		eQuit("종료");
		
		private String label;
		
		private EFileMenu(String label) {
			this.label = label;
		}
		public String getLabel() {
			return this.label;
		}
	}
	
	public enum EEditMenu {
		eCut("자르기"),
		eCopy("복사"),
		ePaste("붙여넣기"),
		eGroup("그룹화"),
		eUnGroup("그룹화 해제"),
		eUndo("실행 취소"),
		eRedo("다시 실행");
		
		private String label;
		private EEditMenu(String label) {
			this.label = label;
		}
		public String getLabel() {
			return this.label;
		}
	}
}
