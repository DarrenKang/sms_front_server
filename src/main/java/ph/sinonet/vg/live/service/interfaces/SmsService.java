package ph.sinonet.vg.live.service.interfaces;

import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.send.SendMsgReq;
import ph.sinonet.vg.live.model.bean.websocket.resp.send.SendMsgResp;
import ph.sinonet.vg.live.model.sms.NexmoResponse;
import ph.sinonet.vg.live.model.sms.SmsAccount;
import ph.sinonet.vg.live.model.sms.SmsConfig;
import ph.sinonet.vg.live.model.sms.SmsMessages;
import ph.sinonet.vg.live.utils.Page;

import java.util.Date;
import java.util.List;

public interface SmsService {
	public NexmoResponse findByMessageId(String messageId);
	SendMsgResp sendSms63Ws(String server, int userID, String account, String password, String mobile, String content);

	String modifyAccount(String loginname, String password);

	String addSmsAccount(String loginname, String password, String[] platformIds);

	SmsAccount getSmsAccount(String loginname, String password);
	String logout();
	String login (String loginname, String password);

	ResponseMessage processWSReq(RequestMessage req);

//	String sendSmsForNow(String smsServer, Integer smsPort, String smsUserID, String smsPassword, String phoneNo, String msg);

	String sendSmsForNowHttp(String smsServer, Integer smsPort, String smsUserID, String smsPassword, String phoneNo, String msg);

	SendMsgResp sendSmsForNowWs(String smsServer, Integer smsPort, String smsUserID, String smsPassword, String phoneNo, String msg, String name, String provider);
	
//	String sendSmsForDodoca(String smsServer, String smsUserID, String smsPassword, String phoneNo, String msg);

	String sendSmsForDodocaHttp(String smsServer, String smsUserID, String smsPassword, String phoneNo, String msg);

	SendMsgResp sendSmsForDodocaWs(String smsServer, String smsUserID, String smsPassword, String phoneNo, String msg, String name, String provider);
	
//	String sendSmsForEmay(String softwareSerialNo, String key, String mobile, String smsContent);

	String sendSmsForEmayHttp(String softwareSerialNo, String key, String mobile, String smsContent);

	SendMsgResp sendSmsForEmayWs(String softwareSerialNo, String key, String mobile, String smsContent, String name, String provider);

//	String sendSmsForEntinfo(String smsServer, Integer smsPort, String username, String password, String phones, String content);

	String sendSmsForEntinfoHttp(String smsServer, Integer smsPort, String username, String password, String phones, String content);

	SendMsgResp sendSmsForEntinfoWs(String smsServer, Integer smsPort, String username, String password, String phones, String content, String name, String provider);
	
//	String sendSmsForC123(String smsServer, Integer smsPort, String username, String password, String phones, String content);

	String sendSmsForC123Http(String smsServer, Integer smsPort, String username, String password, String phones, String content);

	SendMsgResp sendSmsForC123Ws(String smsServer, Integer smsPort, String username, String password, String phones, String content, String name, String provider);
	
//	String sendSmsForQixintong(String serviceURL, String username, String password, String mobile, String content);

	String sendSmsForQixintongHttp(String serviceURL, String username, String password, String mobile, String content);

	SendMsgResp sendSmsForQixintongWs(String serviceURL, String username, String password, String mobile, String content, String name, String provider);
	
//	String sendSmsForAblesms(String smsServer, Integer smsPort, String username, String password, String content, Integer countryCode, String telephone, String userDefineNo);

	String sendSmsForAblesmsHttp(String smsServer, Integer smsPort, String username, String password, String content, Integer countryCode, String telephone, String userDefineNo);

	SendMsgResp sendSmsForAblesmsWs(String smsServer, Integer smsPort, String username, String password, String content, Integer countryCode, String telephone, String userDefineNo, String name, String provider);
	
//	String sendForBayouSms(String username, String password, String message, String phone, String requestAddress);

	String sendForBayouSmsHttp(String username, String password, String message, String phone, String requestAddress);

	SendMsgResp sendForBayouSmsWs(String username, String password, String message, String phone, String requestAddress, String name, String provider);

//	String sendForAccessYou(String accountno, String password, String message, String phone, String baseUrl);

	String sendForAccessYouHttp(String accountno, String password, String message, String phone, String baseUrl);

	SendMsgResp sendForAccessYouWs(String accountno, String password, String message, String phone, String baseUrl, String name, String provider);

//	String sendSmsForHCT(String accountno, String password, String message, String phone, String baseUrl);

	String sendSmsForHCTHttp(String accountno, String password, String message, String phone, String baseUrl);

	SendMsgResp sendSmsForHCTWs1(String accountno, String password, String message, String phone, String baseUrl, String name, String provider);
	SendMsgResp sendSmsForHCTWs(String accountno, String password, String message, String phone, String baseUrl, String name, String provider);
//	String sendSmsForChinaSmsGroup(String account, String password, String phone, String message, String baseUrl);

	String sendSmsForChinaSmsGroupHttp(String account, String password, String phone, String message, String baseUrl);

	SendMsgResp sendSmsForChinaSmsGroupWs(String account, String password, String phone, String message, String baseUrl, String name, String provider);

//	String sendSmsForWinic(String id, String password, String phone, String content, String baseUrl );

	String sendSmsForWinicHttp(String id, String password, String phone, String content, String baseUrl);

	SendMsgResp sendSmsForWinicWs(String id, String password, String phone, String content, String baseUrl, String name, String provider);

//	String sendSmsOpenPlatform(String id, String password, String phone, String content, String baseUrl );

	String sendSmsOpenPlatformHttp(String account, String authKey, String phone, String content, String baseUrl);

	SendMsgResp sendSmsOpenPlatformWs(String account, String authKey, String phone, String content, String baseUrl, String name, String provider);

//	String sendSmsSphPlatform(String id, String password, String phone, String content, String baseUrl );

	String sendSmsSphPlatformHttp(String id, String password, String phone, String content, String baseUrl);

	SendMsgResp sendSmsSphPlatformWs(String id, String password, String phone, String content, String baseUrl, String name, String provider);

//	String sendSmsForBdt360(String account,String password,String phone,String content,String baseurl);

	String sendSmsForBdt360Http(String account, String password, String phone, String content, String baseUrl);

	SendMsgResp sendSmsForBdt360Ws(String account, String password, String phone, String content, String baseUrl, String name, String provider);

//	String sendSmsForSynHey(String account,String password,String phone,String content,String baseurl);

	String sendSmsForSynHeyHttp(String account, String password, String phone, String content, String baseurl);

	SendMsgResp sendSmsForSynHeyWs(String account, String password, String phone, String content, String baseurl, String name, String provider,String priority,RequestMessage msg);

	void saveSmsMessage(String product, String provider, String userId,  String phone, String content, String result);

	String addSmsConfig(String type, String user, String server, String remark, Integer port, String[] platformIds, String password, String name, Integer flag, Integer channel,String priority,String totalCounts);

	String deleteSmsConfig(String username, String name);

	String updateSmsConfig(String type, String user, String server, String remark, Integer port, String[] platformIds, String password, String name, Integer flag, Integer channel,String priority,String totalCounts);

	String enableConfig(String name);

	String disableConfig(String name);

	Page querySmsMessages(String product, String code, Date start, Date end, Integer pageIndex, Integer size, String accountId,String phonenumber);

	List<SmsConfig> querySmsConfigByProject(String name, String type, Integer flag, String platoformIds,Integer channel,String sortField,String orderRule);
	String cancelDisableSmsConfig(String name);
	String notParticipateAutoSwitch(String name);
	SendMsgResp sendSmsForGJDXWs(String user, String password, String message, String tell, String server,String name, String type);

	public String channelonesensitive();

	public String updateAds(String advertise);

	public ResponseMessage processGroupWSReq(RequestMessage req) ;
		// TODO Auto-generated method stub
	public SendMsgResp sendSmsForLXWs(String user, String password, String message, String tell, String server,
			String name, String type);
	public String sendSmsForLX(String accName, String accPwd, String content, String aimcodes, String server,
			String name, String type);
	public  SendMsgResp sendSmsForNEXMOWs(String user, String password, String message, String tell, String server,String name, String type);
	public  void sendSmsForNEXMO(String user, String password, String message, String tell, String server);
	public void saveOrUpdate(NexmoResponse nexmoResponse1);
	public void update(NexmoResponse nexmoResponse1);
	public void save(NexmoResponse nexmoResponse1);
	public List<SmsMessages> getSmsMessage(String product,String string, String string2, String string3);
	public Page checkNexmoMessage(String phoneNum, Date start1, Date end1, Integer pageIndex, Integer size1,String status);
	public Integer queryNumberOfMessage(String accountId, String type, String projectname,  Date start1,
			Date end1);
	public Integer querySumNumberOfMessage(String projectname, Integer channel, Date start, Date end);
	public SendMsgResp sendSmsForTECHTOWs(String user, String password, String message, String tell, String server,String name, String type);
}
