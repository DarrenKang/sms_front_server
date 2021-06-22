package ph.sinonet.vg.live.constants;

import org.apache.commons.lang.ArrayUtils;

public enum SmsType {
	NOW("NOW", "时代互联"), 
	DODOCA("DODOCA", "点点客"),
	EMAY("EMAY","亿美"),
	EMAYVOICE("EMAYVOICE", "EMAYVOICE"),
	ENTINFO("ENTINFO","信达传媒"),
	C123("C123","中国短信通"),
	QIXINTONG("QIXINTONG","企信通"),
	ABLESMS("ABLESMS","ABLESMS"),
	BAYOUSMS("BAYOUSMS", "BAYOUSMS"),
	ACCESSYOU("ACCESSYOU", "ACCESSYOU"),
	HCTCOM("HCTCOM", "HCTCOM"),
	HCTCOM1("HCTCOM1", "HCTCOM1"),
	CHINA_SMS_GROUP("CHINA_SMS_GROUP","(China SMS)中国短信群发"),
	WINIC("WINIC","WINIC"),
	OPEN_API("OPEN_API","OPEN_API"),
	SPH_API("SPH_API","SPH_API"),
	BDT360("BDT360","BDT360"),
	SYNHEY("SYNHEY","SYNHEY"),
	SMSGET("SMSGET","SMSGET"),
	SUBMAIL("SUBMAIL", "SUBMAIL"),
	SUBMAILVAR("SUBMAILVAR", "SUBMAILVAR"),
	GJDX("GJDX","GJDX"),
	BULK("BULK","BULK"),
	LX("LX","LX"),
	NEXMO("NEXMO","NEXMO"),
	TECHTO("TECHTO","TECHTO"),
	ASIAROUTE("ASIAROUTE","ASIAROUTE"),
	DAIYI("DAIYI","DAIYI"),
	SMS63("SMS63", "SMS63");
	

	public static String getText(String code) {
		SmsType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			SmsType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	public static boolean isSmsWebService(String code) {
		SmsType type = valueOf(code);
		if (type != null)
			return ArrayUtils.contains(new SmsType[]{EMAYVOICE}, type);
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isSmsWebService("NOW"));
		System.out.println(isSmsWebService("EMAYVOICE"));
	}

	private String code;
	private String text;

	private SmsType(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

}
