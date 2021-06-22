package ph.sinonet.vg.live.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import ph.sinonet.vg.live.constants.SmsType;
import ph.sinonet.vg.live.model.enums.ErrorCode;
import ph.sinonet.vg.live.security.EncryptionUtil;
import ph.sinonet.vg.live.service.interfaces.SmsService;

@Controller
@Scope("prototype")
public class SmsAction extends SubActionSupport{
	
	private final static Log log = LogFactory.getLog(SmsAction.class);
	
	@Autowired
	private SmsService smsService;
	
	public void sendSms(){
		System.out.println("SmsAction.sendSms()");
		printRequest();
		processSendSms();
	}
	
	public void sendSmsForChannelTwo(){
		System.out.println("SmsAction.sendSmsForChannelTwo()");
		printRequest();
		processSendSms();
	}

	public void testsavemessage(){
		belongProject = "test product";
		smsUserId = "test id";
		phoneNo = "test number";
		msg = "test message";
		String response ="<result>" +
							"<code>-1</code>" +
							"<msg>" + msg + "</msg>" +
				          "</result>";
		smsService.saveSmsMessage(belongProject, "provider", smsUserId, phoneNo, msg, response);
	}


	private void processSendSms() {
		String resp;
		if (SmsType.NOW.getCode().equals(code)) {
			resp = smsService.sendSmsForNowHttp(smsServer, smsPort, smsUserId, smsPassword, phoneNo, msg);
		} else if (SmsType.DODOCA.getCode().equals(code)) {
			resp = smsService.sendSmsForDodocaHttp(smsServer, smsUserId, smsPassword, phoneNo, msg);
		} else if (SmsType.EMAY.getCode().equals(code)) {
			resp = smsService.sendSmsForEmayHttp(smsUserId, smsPassword, phoneNo, msg);
		} else if (SmsType.ENTINFO.getCode().equals(code)) {
			resp = smsService.sendSmsForEntinfoHttp(smsServer, smsPort, smsUserId, smsPassword, phoneNo, msg);
		} else if (SmsType.C123.getCode().equals(code)) {
			resp = smsService.sendSmsForC123Http(smsServer, smsPort, smsUserId, smsPassword, phoneNo, msg);
		} else if (SmsType.QIXINTONG.getCode().equals(code)) {
			resp = smsService.sendSmsForQixintongHttp(smsServer, smsUserId, smsPassword, phoneNo, msg);
		} else if (SmsType.ABLESMS.getCode().equals(code)) {
			resp = smsService.sendSmsForAblesmsHttp(smsServer, smsPort,smsUserId, smsPassword,msg,86, phoneNo, null);
		} else if (SmsType.BAYOUSMS.getCode().equals(code)){
			resp = smsService.sendForBayouSmsHttp(smsUserId, smsPassword, msg, phoneNo, smsServer);
		} else if (SmsType.HCTCOM.getCode().equals(code)) {
			resp = smsService.sendSmsForHCTHttp(smsUserId, smsPassword, msg, phoneNo, smsServer);
		} else if (SmsType.ACCESSYOU.getCode().equals(code)) {
			resp = smsService.sendForAccessYouHttp(smsUserId, smsPassword, msg, phoneNo, smsServer);
		} else if (SmsType.CHINA_SMS_GROUP.getCode().equals(code)) {
			resp = smsService.sendSmsForChinaSmsGroupHttp(smsUserId,smsPassword,phoneNo,msg,smsServer);
		} else if (SmsType.WINIC.getCode().equals(code)) {
			resp = smsService.sendSmsForWinicHttp(smsUserId,smsPassword,phoneNo,msg,smsServer);
		} else if (SmsType.OPEN_API.getCode().equals(code)){
			resp = smsService.sendSmsOpenPlatformHttp(smsUserId,smsPassword,phoneNo,msg,smsServer);
		} else if (SmsType.SPH_API.getCode().equals(code)){
			resp = smsService.sendSmsSphPlatformHttp(smsUserId,smsPassword,phoneNo,msg,smsServer);
		} else  if(SmsType.BDT360.getCode().equals(code)){
			resp = smsService.sendSmsForBdt360Http(smsUserId,smsPassword,phoneNo,msg,smsServer);
		} else if(SmsType.SYNHEY.getCode().equals(code)){
			resp = smsService.sendSmsForSynHeyHttp(smsUserId,smsPassword,phoneNo,msg,smsServer);
		}
//		else if(SmsType.SMS63.getCode().equals(code)){
//			resp = smsService.sendSms63(smsServer, smsPort, smsUserId, smsPassword, phoneNo, msg);
//		}
		else {
			resp =  buildResult("未找到对应的短信提供商");
		}
		smsService.saveSmsMessage(belongProject, code, smsUserId,  phoneNo, msg, resp);//add for saving sms addition

		responseWriter(resp);
	}

	private String buildResult(String msg){
		
		Integer errorCode = ErrorCode.SUCCESS.getCode();
		
		if(msg != null || !(msg.isEmpty())){
			errorCode = -1;
		}
		String xml = "<result>" +
						"<code>" + errorCode +"</code>" +
						"<msg>" + msg + "</msg>" +
					 "</result>";
		return xml;
	}
	
	private void printRequest() {
		log.info("sms request params :");
		System.out.println("code : " + code);
		System.out.println("server : " + smsServer);
		System.out.println("port : " + smsPort);
		System.out.println("user id : " + smsUserId);
		System.out.println("password : " + smsPassword);
		System.out.println("phone : " + phoneNo);
		System.out.println("msg : " + msg);
		
	}
	

	private String name;
	private String code;
	private Integer flag;
	private String smsServer;
	private Integer smsPort;
	private String smsUserId;
	private String smsPassword;
	private String belongProject;
	private String phoneNo;
	private String msg;
	private Document doc;

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getSmsServer() {
		return smsServer;
	}
	public void setSmsServer(String smsServer) {
		this.smsServer = smsServer;
	}
	public Integer getSmsPort() {
		return smsPort;
	}
	public void setSmsPort(Integer smsPort) {
		this.smsPort = smsPort;
	}
	public String getsmsUserId() {
		return smsUserId;
	}
	public void setsmsUserId(String smsUserId) {
		this.smsUserId = smsUserId;
	}
	public String getSmsPassword() {
		return smsPassword;
	}
	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}
	public String getBelongProject() {
		return belongProject;
	}
	public void setBelongProject(String belongProject) {
		this.belongProject = belongProject;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSmsUserId() {
		return smsUserId;
	}

	public void setSmsUserId(String smsUserId) {
		this.smsUserId = smsUserId;
	}

	public static void main(String[] args) {
		System.out.println(EncryptionUtil.encryptPassword("1001@501339830001|7265h111222333"));
		System.out.println(EncryptionUtil.encryptPassword("1001@501339830001|7265|h111222333"));
		System.out.println(EncryptionUtil.encryptPassword("smsDEV!@2cc"));
	}
}
