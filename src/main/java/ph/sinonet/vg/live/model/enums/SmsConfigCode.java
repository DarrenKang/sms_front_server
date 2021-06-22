package ph.sinonet.vg.live.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum SmsConfigCode {
	NOW("NOW", "NOW时代互联"), 
	DODOCA("DODOCA", "DODOCA点点客"),
	EMAY("EMAY","EMAY亿美"),
	EMAYVOICE("EMAYVOICE", "EMAYVOICE"),
	ENTINFO("ENTINFO","ENTINFO信达传媒"),
	C123("C123","C123中国短信通"),
	QIXINTONG("QIXINTONG","QIXINTONG企信通"),
	ABLESMS("ABLESMS","ABLESMS"),
	BAYOUSMS("BAYOUSMS", "BAYOUSMS"),
	ACCESSYOU("ACCESSYOU", "ACCESSYOU"),
	HCTCOM("HCTCOM", "HCTCOM"),
	HCTCOM1("HCTCOM1", "HCTCOM1"),
	CHINA_SMS_GROUP("CHINA_SMS_GROUP","(China SMS)中国短信群发"),
	WINIC("WINIC","WINIC"),
	OPEN_API("OPEN_API","OPEN_API"),
	SPH_API("SPH_API","SPH_API"),
	SYNHEY("SYNHEY","SYNHEY"),
	BDT360("BDT360","BDT360"),
	SMSGET("SMSGET","SMSGET"),
	GJDX("GJDX","GJDX"),
	BULK("BULK","BULK"),
	LX("LX","LX"),
	NEXMO("NEXMO","NEXMO"),
	ASIAROUTE("ASIAROUTE","ASIAROUTE"),
	TECHTO("TECHTO","TECHTO"),
	SUBMAIL("SUBMAIL", "SUBMAIL"),
	DAIYI("DAIYI","DAIYI"),
	SMS63("SMS63", "SMS63");
	

	private String code;
	private String text;

	private SmsConfigCode(String code, String text) {
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
		SmsConfigCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			SmsConfigCode type = p[i];
			if (type.getCode().equals(code)) {
				return type.getText();
			}
		}
		return null;
	}

	public static List<SmsConfigCode> getActiveSmsApi(){
		List<SmsConfigCode> list = new ArrayList<SmsConfigCode>();
		list.add(SmsConfigCode.ACCESSYOU);
		list.add(SmsConfigCode.BDT360);
		list.add(SmsConfigCode.CHINA_SMS_GROUP);
		list.add(SmsConfigCode.HCTCOM);
		list.add(SmsConfigCode.OPEN_API);
		list.add(SmsConfigCode.SMSGET);
		list.add(SmsConfigCode.SPH_API);
		list.add(SmsConfigCode.SYNHEY);
		list.add(SmsConfigCode.SMS63);
		list.add(SmsConfigCode.HCTCOM1);
		list.add(SmsConfigCode.GJDX);
		list.add(SmsConfigCode.BULK);
		list.add(SmsConfigCode.LX);
		list.add(SmsConfigCode.NEXMO);
		list.add(SmsConfigCode.TECHTO);
		list.add(SmsConfigCode.ASIAROUTE);
		list.add(SmsConfigCode.SUBMAIL);
		list.add(SmsConfigCode.DAIYI);
		return list;
	}
}
