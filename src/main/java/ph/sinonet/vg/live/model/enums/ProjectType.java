package ph.sinonet.vg.live.model.enums;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Sun
 * 
 * @version 2011-11-19 上午11:57:02 change
 * @version 2012-02-03 09:59:05 update (jay)
 */
public enum ProjectType {
	RUIBO("RUIBO", "RUIBO"), //
	RUIBOPHONE("RUIBOPHONE", "RUIBOPHONE"), //
	BAIBO("BAIBO", "BAIBO"), //
	BBETASIA("BBETASIA", "BBETASIA"), //
	JINSHENG("JINSHENG", "JINSHENG"), //
	HAOMEN("HAOMEN", "HAOMEN"), //
	BOLEBA("BOLEBA", "BOLEBA"), //
	HONGLIHUI("HONGLIHUI", "HONGLIHUI"), //
	RUIBOHUI("RUIBOHUI", "RUIBOHUI"),
	SUNCITY("SUNCITY", "SUNCITY"),
	SUNRISE("SUNRISE", "SUNRISE"),
	SALAN("SALAN", "SALAN"),
	EU("EU","EU"),
	DUOJIN("DUOJIN", "DUOJIN"),
//	YIJIBO("YIJIBO", "YIJIBO"),
//	JINSANJIAO("JINSANJIAO", "JINSANJIAO"),
//	JINSANJIAO_PARTNER("JINSANJIAO_PARTNER", "JINSANJIAO_PARTNER"),
	EBET("EBET","EBET"),
	PRE_SALES("PRE_SALES","PRE_SALES"),
	XBET("XBET","XBET"),
	LONGFA("LONGFA","LONGFA"),
	V68("V68","V68"),
	MSGREEN("MSGREEN","MSGREEN"),
	BUYUHANG("BUYUHANG","BUYUHANG"),
	;
//	STAGING("STAGING","STAGING");

	private String code;
	private String text;

	private ProjectType(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public String getCode() {
		return this.code;
	}

	public static String getText(String code) {
		ProjectType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ProjectType type = p[i];
			if (type.getCode().equals(code)) {
				return type.getText();
			}
		}
		return null;
	}

	public static boolean isExistText(String code) {
		ProjectType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ProjectType type = p[i];
			if (type.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static ProjectType getProjectType(String code) {
		ProjectType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ProjectType type = p[i];
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}
	
	public static ProjectType[] getNetpayProducts(){
		return new ProjectType[]{
								RUIBO,
								BAIBO,
								BBETASIA,
								JINSHENG,
								HAOMEN,
								BOLEBA,
								HONGLIHUI,
								SUNCITY,
								SALAN,
//								YIJIBO
								DUOJIN,
								EU,
								XBET,
								V68,
								MSGREEN,
								};
		
	}

	public static List<ProjectType> getActiveProducts(){
		List<ProjectType> list = new ArrayList<ProjectType>();
		list.add(ProjectType.BAIBO);
		list.add(ProjectType.BBETASIA);
		list.add(ProjectType.BOLEBA);
		list.add(ProjectType.EU);
		list.add(ProjectType.HAOMEN);
		list.add(ProjectType.JINSHENG);
		list.add(ProjectType.MSGREEN);
		list.add(ProjectType.RUIBO);
		list.add(ProjectType.XBET);
		return list;
	}
}
