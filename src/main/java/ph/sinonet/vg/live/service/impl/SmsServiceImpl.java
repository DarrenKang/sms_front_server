package ph.sinonet.vg.live.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;
import com.nineorange.service.BusinessService;
import com.todaynic.client.mobile.SMS;

import cn.emay.sdk.client.api.Client;
import net.sf.json.JSONObject;
import ph.sinonet.vg.live.constants.SmsType;
import ph.sinonet.vg.live.dao.ServerInFoDao;
import ph.sinonet.vg.live.dao.SmsDao;
import ph.sinonet.vg.live.exception.PostFailedException;
import ph.sinonet.vg.live.exception.ResponseFailedException;
import ph.sinonet.vg.live.model.bean.websocket.RequestMessage;
import ph.sinonet.vg.live.model.bean.websocket.ResponseMessage;
import ph.sinonet.vg.live.model.bean.websocket.req.send.SendMsgReq;
import ph.sinonet.vg.live.model.bean.websocket.resp.send.SendMsgResp;
import ph.sinonet.vg.live.model.enums.ErrorCode;
import ph.sinonet.vg.live.model.sms.NexmoResponse;
import ph.sinonet.vg.live.model.sms.ServerInFo;
import ph.sinonet.vg.live.model.sms.SmsAccount;
import ph.sinonet.vg.live.model.sms.SmsConfig;
import ph.sinonet.vg.live.model.sms.SmsMessages;
import ph.sinonet.vg.live.security.EncryptionUtil;
import ph.sinonet.vg.live.service.BaseServiceImpl;
import ph.sinonet.vg.live.service.interfaces.SmsService;
import ph.sinonet.vg.live.utils.BizNumberUtil;
import ph.sinonet.vg.live.utils.DateUtils;
import ph.sinonet.vg.live.utils.DomOperator;
import ph.sinonet.vg.live.utils.HttpServiceImpl;
import ph.sinonet.vg.live.utils.HttpUtils;
import ph.sinonet.vg.live.utils.HttpUtilsGB2312;
import ph.sinonet.vg.live.utils.MD5;
import ph.sinonet.vg.live.utils.OpenApi;
import ph.sinonet.vg.live.utils.OuterIp;
import ph.sinonet.vg.live.utils.Page;
import ph.sinonet.vg.live.utils.PlatformLimit;
import ph.sinonet.vg.live.utils.SendSMS;
import ph.sinonet.vg.live.utils.StringUtil;
import ph.sinonet.vg.live.utils.Util;

@Service("smsService")
public class SmsServiceImpl extends BaseServiceImpl implements SmsService {

	private final static Log log = LogFactory.getLog(SmsServiceImpl.class);
	private static Client emayClient = null;
	private static String COUNTRY_CODE_CHINA = "86";
	private  static HashMap<String,PlatformLimit> Bdt360Map=new HashMap<String,PlatformLimit>();
	private  static HashMap<String,PlatformLimit> SphPlatformMap=new HashMap<String,PlatformLimit>();
	private  static HashMap<String,PlatformLimit> SmsForHCTMap=new HashMap<String,PlatformLimit>();
	private  static HashMap<String,PlatformLimit> BULKMap=new HashMap<String,PlatformLimit>();
	private Date startSwitchTime=new Date(); 
	@Autowired
	private SmsDao smsDao;
	@Autowired
	private ServerInFoDao serverInFoDao;
	private String sendSms63(String server, int userID, String account, String password, String mobile, String content) {

		StringBuffer sb = new StringBuffer(server);

		//mobile = mobile.startsWith("86") ? mobile.replaceFirst("86","") : mobile;
		StringBuffer sbu=new StringBuffer();
		if(mobile!=null && mobile!=""){
			String[] mobiles=mobile.split("\\,");
			if(mobiles.length>=1){
				for(String str:mobiles){
					sbu.append((str.startsWith("86"))?str.substring(2):str);
					sbu.append(",");
				}
				mobile=sbu.toString();
				mobile=mobile.substring(0,mobile.length()-1);
			}
		}
		try {
			sb.append("userid="+userID);
			sb.append("&account="+account);
			sb.append("&password="+password);
			sb.append("&mobile="+mobile);  //18390571077
			sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
			sb.append("&sendTime=");
			sb.append("&action=send");
			sb.append("&extno=");

			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			System.out.println(sb.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			String inputLine;
			String result = StringUtil.EMPTY;

			while (null != (inputLine = in.readLine())) {
				result = result + inputLine;
			}

			log.info("SMS63 response:" + result);

			in.close();
			return getReturnXmlForSms63(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
//	private String sendSms63Back(String server,int userID,String account,String password,String mobile){
//		StringBuffer sb=new StringBuffer(server);
//		String response=StringUtils.EMPTY;
//		try {
//			sb.append("action=query");
//			sb.append("&userid="+userID);
//			sb.append("&account="+account);
//			sb.append("&password="+password);
//			URL url=new URL(sb.toString());
//			log.info(sb.toString());
//			System.out.println(sb.toString());
//			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//			connection.setRequestMethod("post");
//			BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
//			String inputLine="";
//			String result=StringUtils.EMPTY;
//			while((inputLine=br.readLine())!=null){
//				result+=inputLine;
//			}
//			System.out.println(result);
//			return response;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return "";
//	}
	@Override
	public SendMsgResp sendSms63Ws(String server, int userID, String account, String password, String mobile, String content) {

		SendMsgResp data = new SendMsgResp();
		data.setTel(mobile);

	//	data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		String msg = sendSms63(server, userID, account, password, mobile, content);
//		if (StringUtil.isBlank(msg)) {
//			data.setStatusAndMsg(ErrorCode.SUCCESS);
//			data.setStat("Sucess");
//		} else {
//			data.setStat(msg);
//		}
		String[] msgs=msg.split(",");
		if("Success".equals(msgs[0])){
			//String msg1=sendSms63Back(server,userID,account,password,mobile);
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			data.setStatusAndMsg(ErrorCode.FAIL_SEND);
			data.setErrorMsg(data.getErrorMsg()+",the reason is "+msgs[1]);
		}

		log.info("sendSms63Ws() tel " + mobile + " status : " + data.getStat() + " msg : " + msg);

		return data;


	}


	@Override
	public String modifyAccount(String loginname, String password) {

		return smsDao.modifyAccount(loginname, password);
	}

	@Override
	public String addSmsAccount(String loginname, String password, String[] platformIds) {
		String projects = "";
		if(platformIds != null){
			projects = StringUtil.arrayToString(platformIds);
		}
		SmsAccount smsAccount = new SmsAccount(loginname, projects, password);
		return smsDao.addAccount(smsAccount);
	}

	@Override
	public SmsAccount getSmsAccount(String loginname, String password){
		return smsDao.getSmsAccount(loginname, password);
	}

	@Override
	public ResponseMessage processWSReq(RequestMessage msg) {
		log.info("????????????processWSReq??????,????????????????????????");
		SendMsgReq req = (SendMsgReq) msg.getData();
		String tell=req.getTel();
		tell=tell.trim();
		SendMsgResp data = null;
		String reg3="^(86){0,1}(13[0-9]|14[0-4,5-9]|15[0-4,5-9]|16[6]|17[0-1,3-8]|18[0-9]|19[1,89])\\d{8}";
		String reg4="^(86){0,1}(13[0-9]|14[0-4,5-9]|15[0-4,5-9]|16[6]|17[0-1,3-8]|18[0-9]|19[1,89])\\d{8}(;(86){0,1}(13[0-9]|14[0-4,5-9]|15[0-4,5-9]|16[6]|17[0-1,3-8]|18[0-9]|19[1,89])\\d{8})*";
		String ads=channelonesensitive();
		String[] addarr=ads.split("\\,");
		log.info("processWSReq() " + req);
		ArrayList<SmsConfig> configss = smsDao.getSmsConfig(req);
		ArrayList<SmsConfig> configs=new ArrayList<SmsConfig>();
		if(configss!=null){
			for(SmsConfig config : configss){
				if(StringUtils.isNotEmpty(config.getPriority()) &&  !"1".equals(config.getPriority()) ){
					List<SmsConfig> ss=smsDao.getsmsConfig(req, "1");
					if(ss.size()==0){
						configs=configss;
						break;
					}
					SmsConfig s=ss.get(0);
					//SmsMessages smsMessages=smsDao.querySmsContent(req.getTel(),s.getUser() );
					//Date createtime=smsMessages.getCreatetime();
					if((new Date().getTime()-s.getSwitchTime().getTime())/(1000*60)>=10){
						config.setFlag(1);
						config.setSwitchTime(DateUtils.parseDateForStandard());
						smsDao.updateSmsConfigFlag(config.getName(), config.getFlag(),config.getSwitchTime(),config.getAreadyCounts(),config.getFlag1());
						s.setFlag(0);
						s.setSwitchTime(DateUtils.parseDateForStandard());
						smsDao.updateSmsConfigFlag(s.getName(), s.getFlag(),s.getSwitchTime(),s.getAreadyCounts(),s.getFlag1());
//						configs.remove(config);
//						configs.add(s);
						configs.add(s);
				}else{
					configs.add(config);
				}
			}else{
				configs.add(config);
			}
			}
		}
		String userConfig = null;
		String configType = null;
		String product = null;
		String name = null;
		Integer port = null;
		String type = null;
		String password = null;
		String server = null;
		String user = null;
		String priority=null;
		String remark=null;
		 if("1".equals(req.getChannel())){
				for(String addStr:addarr){
					if(req.getMessage().contains(addStr)){
						data = new SendMsgResp();
						data.setTel(tell);
						data.setStatusAndMsg(ErrorCode.CONTENT_ERROR);
						saveWsSentMessage(req.getPlatformId(), type, user, data.getTel(), req.getMessage(), data.getStatusAndMsg());
						return createResponse(msg, data);
					}
				}
		 }
		if (StringUtil.isBlank(req.getMessage())) {
			data = new SendMsgResp();
			data.setStatusAndMsg(ErrorCode.EMPTY_MESSAGE);
			data.setTel(tell);
		} else if (req.getMessage().length() > 100) {
			data = new SendMsgResp();
			data.setTel(tell);
			data.setStatusAndMsg(ErrorCode.MSG_LENGTH_EXCEED);
		} else if (StringUtils.isEmpty(tell)) {
			data = new SendMsgResp();
			data.setTel(tell);
			data.setStatusAndMsg(ErrorCode.EMPTY_CELLPHONE);
		} else if (!tell.matches(reg3) && !tell.matches(reg4)) {
			data = new SendMsgResp();
			data.setTel(tell);
			data.setStatusAndMsg(ErrorCode.FORMATERROE_CELLPHONE);
		} else {

			for (SmsConfig config : configs) {
				log.info(config);
				// TODO :SMSREQ for all sending message
				// TODO :store data to database (req, resp)
//			// move this line of code
//			SmsReq smsReq = SmsReq.parse(config);
//			smsReq.setMsgInfo(req.getTel(), req.getMessage());

				name = config.getName();
				type = config.getType();
				server = config.getServer();
				port = config.getPort() != null ? config.getPort() : 0;
				user = config.getUser();
				password = config.getPassword();
				product = config.getPlatformIds();
				priority=config.getPriority();
				remark=config.getRemark();
				log.info("??????????????????????????????");
				if (SmsType.NOW.getCode().equals(config.getType())) {
					data = sendSmsForNowWs(server, port, config.getUser(), password, tell, req.getMessage(), name, type);
				} else if (SmsType.DODOCA.getCode().equals(config.getType())) {
					data = sendSmsForDodocaWs(server, user, password, tell, req.getMessage(), name, type);
				} else if (SmsType.EMAY.getCode().equals(config.getType())) {
					data = sendSmsForEmayWs(user, password, tell, req.getMessage(), name, type);
				} else if (SmsType.ENTINFO.getCode().equals(config.getType())) {
					data = sendSmsForEntinfoWs(server, port, user, password, tell, req.getMessage(), name, type);
				} else if (SmsType.C123.getCode().equals(config.getType())) {
					data = sendSmsForC123Ws(server, port, user, password, tell, req.getMessage(), name, type);
				} else if (SmsType.QIXINTONG.getCode().equals(config.getType())) {
					data = sendSmsForQixintongWs(server, user, password, tell, req.getMessage(), name, type);
				} else if (SmsType.ABLESMS.getCode().equals(config.getType())) {
					data = sendSmsForAblesmsWs(server, port, user, password, req.getMessage(), 86, req.getTel(), null, name, type);
				} else if (SmsType.BAYOUSMS.getCode().equals(config.getType())) {
					data = sendForBayouSmsWs(user, password, req.getMessage(), req.getTel(), server, name, type);
				} else if (SmsType.HCTCOM1.getCode().equals(config.getType())) {
					data = sendSmsForHCTWs1(user, password, req.getMessage(), tell, server, name, type);
				} else if (SmsType.ACCESSYOU.getCode().equals(config.getType())) {
					data = sendForAccessYouWs(user, password, req.getMessage(), tell, server, name, type);
				} else if (SmsType.CHINA_SMS_GROUP.getCode().equals(config.getType())) {
					data = sendSmsForChinaSmsGroupWs(user, password, req.getTel(), req.getMessage(), server, name, type);
				} else if (SmsType.WINIC.getCode().equals(config.getType())) {
					data = sendSmsForWinicWs(user, password, req.getTel(), req.getMessage(), server, name, type);
				} else if (SmsType.OPEN_API.getCode().equals(config.getType())) {
					data = sendSmsOpenPlatformWs(user, password, tell, req.getMessage(), server, name, type);
				} else if (SmsType.SPH_API.getCode().equals(config.getType())) {
					data = sendSmsSphPlatformWs(user, password, tell, req.getMessage(), server, name, type);
				} else if (SmsType.BDT360.getCode().equals(config.getType())) {
					data = sendSmsForBdt360Ws(user, password, tell, req.getMessage(), server, name, type);
				} else if (SmsType.SYNHEY.getCode().equals(config.getType())) {
					data = sendSmsForSynHeyWs(user, password, tell, req.getMessage(), server, name, type,priority, msg);
				} else if (SmsType.SMS63.getCode().equals(config.getType())) {
					data = sendSms63Ws(server, port, user, password, tell, req.getMessage());
				}else if(SmsType.HCTCOM.getCode().equals(config.getType())){
					data = sendSmsForHCTWs(user, password, req.getMessage(), tell, server, name, type);
				}else if(SmsType.GJDX.getCode().equals(config.getType())){
					data = sendSmsForGJDXWs(user, password, req.getMessage(), tell, server, name, type);
				}else if(SmsType.BULK.getCode().equals(config.getType())){
					data = sendSmsForBULKWs(user, password, req.getMessage(), tell, server, name, type);
				}else if(SmsType.LX.getCode().equals(config.getType())){
					data=sendSmsForLXWs(user,password,req.getMessage(),tell,server,name,type);
				}else if(SmsType.NEXMO.getCode().equals(config.getType())){
					data=sendSmsForNEXMOWs(user,password,req.getMessage(),tell,server,name,type);
				}else if(SmsType.TECHTO.getCode().equals(config.getType())){
					data=sendSmsForTECHTOWs(user,password,req.getMessage(),tell,server,name,type);
				}else if(SmsType.ASIAROUTE.getCode().equals(config.getType())){
					data=sendSmsForASIAROUTEWs(user,password,req.getChannel(),config.getPlatformIds(),req.getMessage(),tell,server,name,type);
				}else if(SmsType.SUBMAIL.getCode().equals(config.getType())){
					data=sendSmsForSUBMAILWs(user,password,req.getMessage(),tell,server,name,type);
				} else if (SmsType.DAIYI.getCode().equals(config.getType())) {
					data = sendSmsForDaiyiWs(server, remark, user,password,req.getMessage(),tell,name,type);
				} else {
					continue;
				}

				break;

			}
		}
		log.info("??????????????????????????????");
		saveWsSentMessage(req.getPlatformId(), type, user, data.getTel(), req.getMessage(), data.getStatusAndMsg());
		data.setPlatformId(req.getPlatformId());
		if(data.getStatus()!=200 && StringUtil.isNotEmpty(priority)){
			List<SmsConfig> smsConfigs=smsDao.getSmsConfig(req, priority);
			if(smsConfigs.size()>0){
				for(SmsConfig smsConfig:smsConfigs){
					if(data.getErrorMsg().startsWith("???????????????")){
						return createResponse(msg, data);
					}
					smsConfig.setSwitchTime(DateUtils.parseDateForStandard());
					smsConfig.setAreadyCounts(String.valueOf(Integer.parseInt(smsConfig.getAreadyCounts())+1));
					smsConfig.setFlag(1);
					log.info(smsConfig);
					if(smsConfig.getAreadyCounts().equals(smsConfig.getTotalCounts())){
						sendMail(smsConfig);
						smsConfig.setFlag1(1);
					}
					smsDao.updateSmsConfigFlag(smsConfig.getName(),smsConfig.getFlag(),smsConfig.getSwitchTime(),smsConfig.getAreadyCounts(),smsConfig.getFlag1());
				}
			}
			smsConfigs=smsDao.getAllSmsConfig(req);
			if(smsConfigs.size()>0){
			List<Integer> pros=new ArrayList<Integer>();
			for(SmsConfig s:smsConfigs){
				pros.add(Integer.parseInt(s.getPriority()));
			}
			Integer[] pro=pros.toArray(new Integer[pros.size()]);
			Arrays.sort(pro);
			int length=pro.length;
			int nextPriority=0 ;
			for(int i=0;i<length;i++){
				if(pro[i]>Integer.parseInt(priority)){
					nextPriority=pro[i];
					break;
				}
			}
			if(Integer.parseInt(priority)<pro[length-1]){
				List<SmsConfig> smsCons=smsDao.getsmsConfig(req, String.valueOf(nextPriority));
				if(smsCons.size()>0){
					for(SmsConfig smsConfig:smsCons){
						smsConfig.setFlag(0);
						smsConfig.setSwitchTime(DateUtils.parseDateForStandard());
						smsDao.updateSmsConfigFlag(smsConfig.getName(),smsConfig.getFlag(),smsConfig.getSwitchTime(),smsConfig.getAreadyCounts(),smsConfig.getFlag1());
						processWSReq(msg);
					}
				}
			}else{
				List<SmsConfig> smsCons=smsDao.getsmsConfig(req, "1");
				if(smsCons.size()>0){
					for(SmsConfig smsConfig:smsCons){
						smsConfig.setFlag(0);
						smsConfig.setSwitchTime(DateUtils.parseDateForStandard());
						smsDao.updateSmsConfigFlag(smsConfig.getName(),smsConfig.getFlag(),smsConfig.getSwitchTime(),smsConfig.getAreadyCounts(),smsConfig.getFlag1());
					}
				}else{
					smsCons=smsDao.getsmsConfig(req, String.valueOf(pro[0]));
					if(smsCons.size()>0){
						for(SmsConfig smsConfig:smsCons){
							smsConfig.setFlag(0);
							smsConfig.setSwitchTime(DateUtils.parseDateForStandard());
							smsDao.updateSmsConfigFlag(smsConfig.getName(),smsConfig.getFlag(),smsConfig.getSwitchTime(),smsConfig.getAreadyCounts(),smsConfig.getFlag1());
						}
					}
				}
//	
		}
		}
		}

		if(data.getStatus()==200 && StringUtil.isNotEmpty(priority)){
			List<SmsConfig> smsConfigs=smsDao.getSmsConfig(req, priority);
			if(smsConfigs.size()>0){
				for(SmsConfig smsConfig:smsConfigs){
					smsConfig.setAreadyCounts("0");
					smsConfig.setFlag1(0);
					smsDao.updateSmsConfigFlag(smsConfig.getName(),smsConfig.getFlag(),smsConfig.getSwitchTime(),smsConfig.getAreadyCounts(),smsConfig.getFlag1());
				}
			}
		}
		
		return createResponse(msg, data);
//		return new ResponseMessage("sending", SendMsgReq.NAME, data);
		 }
	
	private SendMsgResp sendSmsForDaiyiWs(String server, String remark, String user, String password, String message,
			String tell,  String name, String type) {
		// TODO Auto-generated method stub
		SendMsgResp sendMsgResp=new SendMsgResp();
		sendMsgResp.setTel(tell);
		 String result=sendSmsForDaiyi(server, remark, user,password,message,tell,name,type) ;
		 log.info("????????????????????????"+result);
		 String[] strs=result.split("\\,");
		 if("0".equals(strs[0])){
			 sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
		 }else{
			 String msg=getDaiyiErrorMsg(strs[0]);
			 sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
			sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+",???????????????"+msg);
		 }
		return sendMsgResp;
	}
	
	private String sendSmsForDaiyi(String server, String remark, String user, String password, String message,
			String tell, String name, String type) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("name="+user); 
			sb.append("&pwd="+password);
			sb.append("&content="+URLEncoder.encode(message,"UTF-8"));
			tell=tell.startsWith("86")?tell.substring(2):tell;
			sb.append("&mobile="+tell);
			sb.append("&sign="+remark);
			sb.append("&type=pt");
			log.info("?????????????????????url??????"+server+sb.toString());
			 URL localURL = new URL(server);
		        URLConnection connection = localURL.openConnection();
		        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
		        
		        httpURLConnection.setDoOutput(true);
		        httpURLConnection.setRequestMethod("POST");
		        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(sb.length()));
		        httpURLConnection.setConnectTimeout(20000);
		        httpURLConnection.setReadTimeout(2000);
		        OutputStream outputStream = null;
		        OutputStreamWriter outputStreamWriter = null;
		        InputStream inputStream = null;
		        InputStreamReader inputStreamReader = null;
		        BufferedReader reader = null;
		        String resultBuffer = "";
		        
		        try {
		            outputStream = httpURLConnection.getOutputStream();
		            outputStreamWriter = new OutputStreamWriter(outputStream);
		            
		            outputStreamWriter.write(sb.toString());
		            outputStreamWriter.flush();
		            
		            if (httpURLConnection.getResponseCode() >= 300) {
		                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
		            }
		            
		            inputStream = httpURLConnection.getInputStream();
		            resultBuffer = convertStreamToString(inputStream);
		            
		        } finally {
		            
		            if (outputStreamWriter != null) {
		                outputStreamWriter.close();
		            }
		            
		            if (outputStream != null) {
		                outputStream.close();
		            }
		            
		            if (reader != null) {
		                reader.close();
		            }
		            
		            if (inputStreamReader != null) {
		                inputStreamReader.close();
		            }
		            
		            if (inputStream != null) {
		                inputStream.close();
		            }
		            
		        }

		        return resultBuffer;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("???????????????????????????????????????"+e.getMessage());
		}
		return null;
	}
	/**
	 * ????????????????????????UTF-8??????.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                is.close();    
            } catch (IOException e) {    
               e.printStackTrace();    
            }    
        }    
        return sb1.toString();    
    }

	public String getDaiyiErrorMsg(String code){
		if("1".equals(code)){
			return "??????????????????";
		}else if("2".equals(code)){
			return "????????????";
		}else if("3".equals(code)){
			return "????????????";
		}else if("4".equals(code)){
			return "??????sql??????";
		}else if("10".equals(code)){
			return "???????????????";
		}else if("11".equals(code)){
			return "????????????";
		}else if("12".equals(code)){
			return "????????????";
		}else if("13".equals(code)){
			return "IP????????????";
		}else if("14".equals(code)){
			return "????????????";
		}else if("-1".equals(code)){
			return "????????????";
		}else{
			return null;
		}
	}
	public SendMsgResp sendSmsForSUBMAILWs(String appId, String appey, String message, String phone, String baseurl,
			String name, String type) {
		// TODO Auto-generated method stub
		SendMsgResp sendMsgResp=new SendMsgResp();
		 String result=sendSmsForSUBMAIL( appId,  appey,  message,  phone,  baseurl,name,  type) ;
		 log.info("SUBMAIL??????????????????"+result);
		 JSONObject jsonObject = JSONObject.fromObject(result);
			String status=jsonObject.getString("status");
			if("success".equals(status)){
				sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
				String money_account=jsonObject.getString("money_account");
				sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+",?????????"+money_account);
			}else{
				String msg=jsonObject.getString("msg");
				sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
				sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+",???????????????"+msg);
			}
			sendMsgResp.setTel(phone);
			return sendMsgResp;
		
	}
	public String sendSmsForSUBMAIL(String appId, String appey, String message, String phone, String baseurl,
			String name, String type) {
		String response = "";
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("appid="+appId); 
			phone=phone.startsWith("86")?phone.substring(2):phone;
			sb.append("&to="+phone);
			sb.append("&code="+Integer.parseInt(message));
			sb.append("&signature="+appey);
			sb.append("&sign_type=normal");
			log.info("SUBMAIL?????????????????????????????????"+sb.toString());
			URL url = new URL(baseurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String requestContent = sb.toString();
			DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
			// ??????write(requestContent.getBytes())?????????????????????????????????
			ds.write(requestContent.getBytes());
			ds.flush();
			try {
				// ??????URL?????????
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				String s = "";
				String temp = "";
				while ((temp = reader.readLine()) != null) {
					s += temp;
				}
				response = s;
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("No response get!!!");
			}
			ds.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Request failed!");
		}
		return response;
	}
	public SendMsgResp sendSmsForASIAROUTEWs(String user,String password,String channel,String plartFormId,String message, String tell, String server, String name, String type) {
		// TODO Auto-generated method stub
		log.info("?????????sendSmsForASIAROUTEWs??????");
		SendMsgResp sendMsgResp=new SendMsgResp();
		String result=sendSmsForASIAROUTE(user,password,channel,plartFormId,message, tell, server);
		log.info("ASIAROUTE??????????????????"+result);
		if(StringUtils.isNotEmpty(result) && result.startsWith("{")){
//			JSONObject jsonObject = JSONObject.fromObject(result);
//			String message_id=jsonObject.getString("message_id");
//			log.info("?????????message_id???"+message_id);
			sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
		}
		sendMsgResp.setTel(tell);
		return sendMsgResp;
	}
	public String sendSmsForASIAROUTE(String user,String password,String channel,String plartFormId,String message, String tell, String server) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(server);
		try {
//			String ip=OuterIp.getOuterIp();
//			ip=ip.substring(1, ip.length()-1);
//			log.info("??????????????????ip??????"+ip);
//			List<ServerInFo> serverInFos=getServerInFo(plartFormId,channel);
//			for(ServerInFo s:serverInFos){
//				log.info("???????????????????????????ServerInFo???"+s.toString());
//				log.info("???????????????????????????ServerInFo?????????ip???"+s.getServerIp());
//				if(s.getServerIp().equals(ip)){
//					sb.append("?username="+s.getUsername()); 
//					sb.append("&password="+s.getPassword());
//				}
//			}
			sb.append("?username="+user); 
			sb.append("&password="+password);
			sb.append("&ani=852678967893");
			tell=tell.startsWith("86")?tell:"86"+tell;
			sb.append("&dnis="+tell);
			sb.append("&message="+URLEncoder.encode(message,"UTF-8"));
			sb.append("&command=submit");
			sb.append("&serviceType");
			log.info("ASIAROUTE???????????????url??????"+sb.toString());
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
//			 int statusCode = connection.getResponseCode();
//			 log.info("?????????????????????"+statusCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			String result="";
			String line="";
			while((line=in.readLine())!=null){
				result+=line;
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("???????????????????????????????????????"+e.getMessage());
		}
		return null;
	}
	public List<ServerInFo> getServerInFo(String plartFormId) {
		// TODO Auto-generated method stub
		return serverInFoDao.getServerInFo(plartFormId);
	}
	public SendMsgResp sendSmsForTECHTOWs(String user, String password, String message, String tell, String server,
			String name, String type) {
		// TODO Auto-generated method stub
		SendMsgResp sendMsgResp=new SendMsgResp();
		String result=sendSmsForTECHTO(user,password,message, tell, server);
		log.info("TECHTO??????????????????"+result);
		
		if(StringUtils.isNotEmpty(result) && result.length()>4){
			JSONObject jsonObject = JSONObject.fromObject(result);
			String code=jsonObject.getString("code");
			if(judgeContainsStr(code)){
				sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
			}else{
				sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
				String errorMessage=getTECHTOErrorMessage(code);
				sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+errorMessage);
			}
		}else{
			String code=result;
			sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
			String errorMessage=getTECHTOErrorMessage(code);
			sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+"the reason is"+errorMessage);
		}
		
		sendMsgResp.setTel(tell);
		return sendMsgResp;
	}
	public String getTECHTOErrorMessage(String code){
		if(" -1".equals(code)){
			return "????????????";
		}else if(" -2".equals(code)){
			return "????????????";
		}else if(" -3".equals(code)){
			return "???????????????";
		}else if(" -4".equals(code)){
			return "??????????????????";
		}else if(" -5".equals(code)){
			return "????????????";
		}else if("-6".equals(code)){
			return "???????????????????????????";
		}else if("-7".equals(code)){
			return "??????????????????";
		}else if(" -8".equals(code)){
			return "????????????";
		}else if(" -9".equals(code)){
			return "IP??????";
		}else if(" -10".equals(code)){
			return "????????????access_token??????100???";
		}else if(" -11".equals(code)){
			return "?????????????????????????????????????????????";
		}else if(" -12".equals(code)){
			return "access_token ?????? / ??????";
		}else{
			return null;
		}
	}
	public  boolean judgeContainsStr(String cardNum) {
    	String regex=".*[a-zA-Z]+.*";
	    Matcher m=Pattern.compile(regex).matcher(cardNum);
	    return m.matches();
	}
	public String sendSmsForTECHTO(String user, String password, String content, String tell, String server){
		StringBuffer sb = new StringBuffer(server);
		try {
			sb.append("username="+user); 
			sb.append("&password="+password);
			sb.append("&targets="+tell);
			sb.append("&type=gbsms");
			sb.append("&subject=message");
			sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
			log.info("TECHTO???????????????url??????"+sb.toString());
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			String result="";
			String line="";
			while((line=in.readLine())!=null){
				result+=line;
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("???????????????????????????????????????"+e.getMessage());
		}
		return null;
	}
	public  SendMsgResp sendSmsForNEXMOWs(String user, String password, String message, String tell, String server,
			String name, String type) {
		// TODO Auto-generated method stub
		sendSmsForNEXMO(user, password, tell, message, server);
		SendMsgResp sendMsgResp=new SendMsgResp();
		sendMsgResp.setTel(tell);
		//sendSmsForNEXMO(user,password,message,tell,server);
		sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
		//log.info("??????????????????"+result);
		sendMsgResp.setNeedWaiting(true);
		return sendMsgResp;
	}
	public void sendSmsForNEXMO(String api_key, String api_secret, String tell, String message, String server){
		StringBuffer sb = new StringBuffer(server);
		try {
			log.info("Mexmo?????????????????????????????????????????????????????????????????????111???");
			sb.append("&api_key="+api_key); 
			sb.append("&api_secret="+api_secret); 
			tell=tell.startsWith("86")?tell:"86"+tell;
			sb.append("&to="+tell);
			sb.append("&from=NEXMO");
			sb.append("&text="+message);
			sb.append("&type=unicode");
			log.info("Mexmo?????????url??????"+sb.toString());
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, null, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			URL url = new URL(sb.toString());
			HttpsURLConnection httpsConnection = (HttpsURLConnection) url.openConnection();
			httpsConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
			StringBuilder body = new StringBuilder();
			while (reader.ready()) {
				body.append(reader.readLine());
			}
			httpsConnection.disconnect();
			log.info("?????????????????????"+body.toString());
//			log.info("??????111???"+tell);
//			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
//			log.info("??????111???"+tell);
//			httpConnection.setRequestMethod("POST");
//			//input to server
//			httpConnection.setDoInput(true);
//			//this method implies that we intend to receive data from server
//			httpConnection.setDoOutput(true);
//			//implies do not use cached data
////			httpConnection.setUseCaches(false);
////			log.info("Mexmo?????????url???"+sb.toString()+"api_key="+api_key+"&api_secret="+api_secret+"&to="+tell+"&from=NEXMO&text"+message+"&type=unicode");
////			//data that will be sent over the stream to the server
////			DataOutputStream dataStreamToServer=new DataOutputStream(httpConnection.getOutputStream());
////			dataStreamToServer.writeBytes("api_key="+api_key+"&api_secret="+api_secret+"&to="+tell+"&from=NEXMO&text"+message+"&type=unicode");
////			dataStreamToServer.flush();
////			dataStreamToServer.close();
//			
//			httpConnection.connect();
//			log.info("??????222???"+tell);
//			OutputStream os = httpConnection.getOutputStream();
//			log.info("??????222???"+tell);
//			os.close();
//			log.info("Mexmo?????????????????????????????????????????????????????????????????????111???");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("???????????????????????????????????????"+e.getMessage());
		}
	}
//	public  String sendSmsForNEXMO(String api_key, String api_secret, String tell, String message, String server) {
//		String resultMessage = null;
//		try{
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("api_key",api_key);
//			params.put("api_secret",api_secret);
//			tell=tell.startsWith("86")?tell:"86"+tell;
//			params.put("to",tell);
//			params.put("from","NEXMO");
//			params.put("text",message);
//			params.put("type","unicode");
//			//String response = HttpUtils.getWithNoTimeout1(server,params);
//			log.info("server??????https://rest.nexmo.com");
//			String str=HttpServiceImpl.httpRequest("https://rest.nexmo.com", "GET", null,"1234");
//			log.info("????????????????????????"+str);
//		} catch(Exception e) {
//			e.printStackTrace();
//			resultMessage = e.getMessage();
//		}
//		return resultMessage;
//	}
	public String getUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);
                 
            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
	public SendMsgResp sendSmsForLXWs(String accName, String accPwd, String content, String aimcodes, String server,
			String name, String type) {
		// TODO Auto-generated method stub
		SendMsgResp sendMsgResp=new SendMsgResp();
		sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
		sendMsgResp.setTel(aimcodes);
		String result=sendSmsForLX(accName,  accPwd,  content,  aimcodes,  server,
				 name,  type);
		log.info("?????????????????????:"+result);
		String[] results=result.split("\\;");
		if("1".equals(results[0])){
			sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+results[1]);
		}
		return sendMsgResp;
	}
	public String sendSmsForLX(String accName, String accPwd, String content, String aimcodes, String server,
			String name, String type) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(server);
		try {
			sb.append("&accName="+accName); 
			sb.append("&accPwd="+MD5.getMd5String(accPwd)); 
			aimcodes=aimcodes.startsWith("86")?aimcodes.substring(2):aimcodes;
			sb.append("&aimcodes="+aimcodes);
			content=content+"???XBET???";
			sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
			sb.append("&bizId="+BizNumberUtil.createBizId());
			sb.append("&dataType=string");
			log.info("?????????????????????url??????"+sb.toString());
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			return in.readLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("???????????????????????????????????????"+e.getMessage());
		}
		return null;
	}
	
	private SendMsgResp sendSmsForBULKWs(String user, String password, String message, String tell, String server,
			String name, String type) {
		// TODO Auto-generated method stub
		
		SendMsgResp sendMsgResp=new SendMsgResp();
		sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
		sendMsgResp.setTel(tell);
		String msg=sendSmsForBULK(server,type,user,password,tell,message);
		if (StringUtil.isBlank(msg))
			sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
		if(StringUtil.isNotBlank(msg)){
			sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+",the reason is "+msg);
		}
		return sendMsgResp;
	}
	public String sendSmsForBULK(String server, String type, String user, String password, String tell,String message) {
		// TODO Auto-generated method stub
		String resultMessage=StringUtils.EMPTY;
		StringBuffer sb2=new StringBuffer();
		StringBuffer middleMessage=new StringBuffer();
		String midPhone="";
		try {
			URL sendUrl =new URL(server);
			HttpURLConnection httpConnection=(HttpURLConnection)sendUrl.openConnection();
			httpConnection.setRequestMethod("POST");
			//input to server
			httpConnection.setDoInput(true);
			//this method implies that we intend to receive data from server
			httpConnection.setDoOutput(true);
			//implies do not use cached data
			httpConnection.setUseCaches(false);
			//data that will be sent over the stream to the server
			DataOutputStream dataStreamToServer=new DataOutputStream(httpConnection.getOutputStream());
			if(tell!=null && tell!=""){
			String[] phones=tell.split("\\,");
			if(phones.length>0){
				for(String p:phones){
					midPhone=(p.startsWith("86"))?p:("86"+p);
					middleMessage.append(bulkplatformlimit(midPhone));
					 sb2.append(midPhone);
					 sb2.append(",");
			}
				tell=sb2.toString();
				tell=tell.substring(0,tell.length()-1);
			}
			}
			
			log.info("??????????????????"+"username="+user+"&password="+password+"&type=2&dlr=1&destination="+tell+"&source=85261231234&message="+convertToUnicode(message));
											
			log.info("BULK?????????url???:"+server+"?username="+user+"&password="+password+"&type=2&dlr=1&destination="+tell+"&source=85261231234&message="+convertToUnicode(message));
			dataStreamToServer.writeBytes("username="+user+"&password="+password+"&type=2&dlr=1&destination="+tell+"&source=85261231234&message="+convertToUnicode(message));
			dataStreamToServer.flush();
			dataStreamToServer.close();
			//here take the output value of the server
			BufferedReader dataStreamFromUrl=new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl="",dataBuffer="";
			//writing information from the stream to the buffer
			while((dataBuffer=dataStreamFromUrl.readLine())!=null){
				dataFromUrl+=dataBuffer;
			}
			/**
			 * now dataFromUrl variable contains the response received from the server
			 * so we can parse the response and process it accordingly
			 */
			dataStreamFromUrl.close();
			System.out.println("BULK?????????response???:"+dataFromUrl);
			log.info("BULK?????????response???:"+dataFromUrl);
			String[] backdatas=dataFromUrl.split("\\|");
			if(backdatas.length>0){
				if("1701".equals(backdatas[0]) && StringUtils.isNoneEmpty(middleMessage)){
					resultMessage=middleMessage.toString();
				}else{
					resultMessage= getFailMessageForBULK(backdatas[0]);
				}
				
				log.info("BULK?????????????????????"+backdatas[0]);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			log.info("??????????????????"+e.getStackTrace());
			e.printStackTrace();
			resultMessage=e.getMessage();
		}catch(IOException e){
			log.info("??????????????????"+e.getStackTrace());
			e.printStackTrace();
			resultMessage=e.getMessage();
		}
		return resultMessage;
	}
	public String bulkplatformlimit(String phoneNum) {
		// TODO Auto-generated method stub
		String message="";
		if(!BULKMap.containsKey(phoneNum)){
			Date startTime=new Date();
			PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
			BULKMap.put(phoneNum, platformLimit);
		}else{
			int i=BULKMap.get(phoneNum).getCount();
			Date startTime=BULKMap.get(phoneNum).getStartTime();
			if((new Date().getTime()-startTime.getTime())/(1000*60)>=60*24){
			    startTime=new Date();
				PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime,1);
				BULKMap.put(phoneNum, platformLimit);
			}else{
				if(i>=2){
					message="?????????????????????????????????2????????????"+phoneNum+"?????????????????????????????????????????????????????????????????????????????????????????????";
				}else{
					PlatformLimit platformLimit=BULKMap.get(phoneNum);
					int count=platformLimit.getCount();
					platformLimit.setCount(++count);
					BULKMap.put(phoneNum, platformLimit);
				}
			}
		}
		return message;
	}
	private static String convertToUnicode(String regText){
		char[] chars=regText.toCharArray();
		StringBuffer hexString=new StringBuffer();
		for(int i=0;i<chars.length;i++){
			String iniHexString=Integer.toHexString((int)chars[i]);
			if(iniHexString.length()==1){
				iniHexString="000"+iniHexString;
			}else if(iniHexString.length()==2){
				iniHexString="00"+iniHexString;
			}else if(iniHexString.length()==3){
				iniHexString="0"+iniHexString;
			}
			hexString.append(iniHexString);
		}
		System.out.println("????????????unicode???????????????"+hexString.toString());
		log.info("????????????unicode???????????????"+hexString.toString());
		return hexString.toString();
	}
	private String getFailMessageForBULK(String errorcode) {
		// TODO Auto-generated method stub
		if("1701".equals(errorcode)){
			return "";
		}else if("1702".equals(errorcode)){
			return "????????????url???????????????????????????????????????????????????????????????????????????,Invalid URL Error";
		}else if("1703".equals(errorcode)){
			return "???????????????????????????,Invalid value in username or password field";
		}else if("1704".equals(errorcode)){
			return "type???????????????,Invalid value in type field";
		}else if("1705".equals(errorcode)){
			return "?????????????????????,Invalid Message";
		}else if("1706".equals(errorcode)){
			return "?????????????????????,Invalid Destination";
		}else if("1707".equals(errorcode)){
			return "source?????????,Invalid Source ";
		}else if("1708".equals(errorcode)){
			return "dlr?????????,Invalid value for dlr field";
		}else if("1709".equals(errorcode)){
			return "??????????????????,User validation failed";
		}else if("1710".equals(errorcode)){
			return "Internal Error";
		}else if("1025".equals(errorcode)){
			return "Insufficient Credit???????????? ";
		}else{
			return "??????????????????????????????";
		}
	}
	public  SendMsgResp sendSmsForGJDXWs(String user, String password, String message, String tell, String server,
			String name, String type) {
		// TODO Auto-generated method stub
		SendMsgResp sendMsgResp=new SendMsgResp();
		sendMsgResp.setStatusAndMsg(ErrorCode.FAIL_SEND);
		sendMsgResp.setTel(tell);
		String msg=sendSmsForGJDX(server,type,user,password,tell,message);
		if (StringUtil.isBlank(msg))
			sendMsgResp.setStatusAndMsg(ErrorCode.SUCCESS);
		if(StringUtil.isNotBlank(msg)){
			sendMsgResp.setErrorMsg(sendMsgResp.getErrorMsg()+",the reason is "+msg);
		}
		return sendMsgResp;
	}
	
	public String sendSmsForGJDX(String smsServer, String type, String smsUserId, String smsPassword, String phoneNo, String msg) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				StringBuffer sb1=new StringBuffer(smsServer);
				StringBuffer sb2=new StringBuffer();
				String resultMessage=StringUtils.EMPTY;
				String midPhone="";
				sb1.append("?Cmd=1");
				try {
					sb1.append("&username=" + smsUserId);
					sb1.append("&password=" + smsPassword);
					String[] phones=phoneNo.split("\\,");
					if(phones.length>0){
						for(String p:phones){
							midPhone=(p.startsWith("86"))?p:("86"+p);
							 sb2.append(midPhone);
							 sb2.append(",");
					}
					phoneNo=sb2.toString();
					phoneNo=phoneNo.substring(0,phoneNo.length()-1);
					sb1.append("&mobile=" + phoneNo);
					
					try {
						log.info("before url encoder msg=" + msg);
						log.info("after url encoder msg=" + URLEncoder.encode(msg, "UTF-8"));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					sb1.append("&content=" + URLEncoder.encode(msg, "UTF-8"));
					//log.info("????????????GJDX???????????????"+sb1.toString()+"??????");
					URL url=new URL(sb1.toString());
					log.info("GJDX??????url???"+url.toString());
					HttpURLConnection connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					log.info("??????????????????");
					BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
					log.info("??????????????????");
					String line=StringUtils.EMPTY;
					String mess=StringUtils.EMPTY;
					while((line=br.readLine())!=null){
						mess+=line;
					}
					br.close();
					log.info("????????????GJDX response:"+mess);
					String[] strs=mess.split("\\:");
					if(strs.length>0){
						if(Integer.parseInt(strs[0])<0){
							System.out.println("????????????GJDX??????????????????"+strs[0]);
							log.info("????????????GJDX??????????????????"+strs[0]);
							resultMessage =  getFailMessageForGJDX(strs[0]);
						}
					}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					resultMessage=e.getMessage();
				}
				return resultMessage;
	}
private String getFailMessageForGJDX(String statusCode){
		
		if("-1".equals(statusCode)){
			return "????????????????????????";
		}else if("-9".equals(statusCode)){
			return "?????????????????????????????????";
		}else if("-2".equals(statusCode)){
			return "????????????";
		}else if("-99".equals(statusCode)){
			return "????????????";
		}else{
			return "??????????????????????????????";
		}
		
	}
	private String buildResult(String msg){
		
		Integer errorCode = 200;
		
		if(msg != null && !(msg.isEmpty())){
			errorCode = -1;
		}
		String xml = "<result>" +
						"<code>" + errorCode +"</code>" +
						"<msg>" + msg + "</msg>" +
					 "</result>";
		return xml;
	}

	private SMS sendSmsForNow(String smsServer, Integer smsPort, String smsUserID, String smsPassword, String phoneNo, String msg) {
		Hashtable config = new Hashtable();
		config.put("VCPSERVER", smsServer);
		config.put("VCPSVPORT", smsPort);
		config.put("VCPUSERID", smsUserID);
		config.put("VCPPASSWD", smsPassword);

		SMS sms = new SMS(config);
		sms.setEncodeType("GBK");
		log.info((new StringBuilder("send sendSmsForDodoca to ")).append(phoneNo).toString());
		log.info("send  msg:" + msg);
		try {
			sms.sendSMS(phoneNo, msg, "");
			System.out.println(sms.getRecieveXml());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sms;
	}

	@Override
	public String sendSmsForNowHttp(String smsServer, Integer smsPort, String smsUserID, String smsPassword, String phoneNo, String msg) {
		String resp = null;
		try {
			SMS sms = sendSmsForNow(smsServer, smsPort, smsUserID, smsPassword, phoneNo, msg);
			if ("2000".equals(sms.getCode()))
				return buildResult(resp);
			else
				return buildResult(sms.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			return buildResult(e.getMessage());
		}
	}

	@Override
	public SendMsgResp sendSmsForNowWs(String smsServer, Integer smsPort, String smsUserID, String smsPassword, String phoneNo, String msg, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setTel(phoneNo);
		try {
			SMS sms = sendSmsForNow(smsServer, smsPort, smsUserID, smsPassword, phoneNo, msg);
			if ("2000".equals(sms.getCode())) {
				data.setStatusAndMsg(ErrorCode.SUCCESS);
			}
			smsDao.saveSmsLog(sms.getCode()+ " " + sms.getMsg(), msg, phoneNo, "", "", "");
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		return data;
	}

	private int sendSmsForDodoca(String smsServer, String smsUserID, String smsPassword, String phoneNo, String msg) {
		int nRet = 0;
		try {
			log.info((new StringBuilder("send sendSmsForDodoca to ")).append(phoneNo).toString());
			log.info("send msg:" + msg);
			BusinessService bs = new BusinessService();
			bs.setWebService(smsServer);
			nRet = bs.validateUser(smsUserID, smsPassword);
			if (nRet == 1) {
				nRet = bs.sendBatchMessage(smsUserID, smsPassword, phoneNo.replaceAll(",", ";"), msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		log.info(msg);
		return nRet;
//		return buildResult(msg);
	}

	@Override
	public String sendSmsForDodocaHttp(String smsServer, String smsUserID, String smsPassword, String phoneNo, String msg) {
		try {
			int nRet =  sendSmsForDodoca(smsServer, smsUserID, smsPassword, phoneNo, msg);
			if (nRet != 1) {
				msg = "????????????";
			} else {
				if (nRet <= 0) {
					msg = "??????????????????,?????????:" + nRet;
				} else {
					msg = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		log.info(msg);

		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForDodocaWs(String smsServer, String smsUserID, String smsPassword, String phoneNo, String msg, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		try {
			int nRet =  sendSmsForDodoca(smsServer, smsUserID, smsPassword, phoneNo, msg);
			if (nRet != 1) {
				log.info("sendSmsForDodocaWs() ????????????");
				data.setStatusAndMsg(ErrorCode.FAIL_SEND);
			} else {
				if (nRet <= 0) {
					log.info("sendSmsForDodocaWs() ??????????????????,?????????: " + nRet);
					data.setStatusAndMsg(ErrorCode.FAIL_SEND);
				} else {
					msg = null;
					data.setStatusAndMsg(ErrorCode.SUCCESS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		log.info(msg);
		return data;
	}

	private String sendSmsForEmay(String softwareSerialNo, String key, String mobile, String smsContent) {
		String msg = null;
		int result = 0;

		try {
			result = getEmayClient(softwareSerialNo, key).sendSMS(mobile.split(","), smsContent, 5);// 5????????????????????????
			if (result == 0)
				msg = null;
			else if (result == 10)
				msg = "?????????????????????";
			else if (result == 101)
				msg = "?????????????????????";
			else if (result == 305)
				msg = "?????????????????????????????????????????????????????????????????????????????????";
			else if (result == 307)
				msg = "?????????????????????????????????(1)?????????11??????13???,(2)13????????????86?????????3??????1,11???????????????1,(3)????????????????????????????????????";
			else if (result == 999)
				msg = "????????????";
			else
				msg = "?????????????????????";

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return msg;
	}

	@Override
	public String sendSmsForEmayHttp(String softwareSerialNo, String key, String mobile, String smsContent) {
		String msg = sendSmsForEmay(softwareSerialNo, key, mobile, smsContent);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForEmayWs(String softwareSerialNo, String key, String mobile, String smsContent, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		String msg = sendSmsForEmay(softwareSerialNo, key, mobile, smsContent);

		if (StringUtil.isBlank(msg)) {
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}
		log.info("sendSmsForEmayWs() tel " + mobile + " status : " + data.getStat() + " msg : " + msg);

		return data;
	}
	
	private synchronized static Client getEmayClient(String softwareSerialNo, String key) {
		if (emayClient == null) {
			try {
				emayClient = new Client(softwareSerialNo, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return emayClient;
	}

	private String sendSmsForEntinfo(String smsServer, Integer smsPort, String username, String password, String phones, String content) {
		String msg = null;
		String soapAction = "http://tempuri.org/mt";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mt xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + username + "</sn>";
		xml += "<pwd>" + EncryptionUtil.encryptPassword(username + password).toUpperCase() + "</pwd>";
		xml += "<mobile>" + phones + "</mobile>";
		xml += "<content>" + content + "</content>";
		xml += "<ext></ext>";
		xml += "<stime></stime>";
		xml += "<rrid>0</rrid>";
		xml += "</mt>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";
		URL url;
		System.out.println(xml);
		try {
			url = new URL(smsServer);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.flush();
			out.close();
			InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			String result = "????????????";
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mtResult>(.*)</mtResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find())
					result = matcher.group(1);
			}
			in.close();
			isr.close();
			if (StringUtil.equals(result, "-2"))
				msg =  "??????/???????????????";
			else if (StringUtil.equals(result, "-3"))
				msg =  "????????????";
			else if (StringUtil.equals(result, "-4"))
				msg =  "????????????";
			else if (StringUtil.equals(result, "-5"))
				msg =  "??????????????????";
			else if (StringUtil.equals(result, "-6"))
				msg =  "????????????";
			else if (StringUtil.equals(result, "-7"))
				msg =  "????????????";
			else if (StringUtil.equals(result, "-8"))
				msg =  "??????????????????";
			else if (StringUtil.equals(result, "-9"))
				msg =  "?????????????????????";
			else if (StringUtil.equals(result, "-10"))
				msg =  "???????????????";
			else if (StringUtil.equals(result, "-11"))
				msg =  "???????????????";
			else if (StringUtil.equals(result, "-12"))
				msg =  "?????????????????????";
			else if (StringUtil.equals(result, "-13"))
				msg =  "????????????????????????";
			else if (StringUtil.equals(result, "-14"))
				msg =  "????????????????????????";
			else if (StringUtil.equals(result, "-15"))
				msg =  "????????????base64????????????";
			
			
		} catch (Exception e) {
			e.printStackTrace();
//			return buildResult(e.getMessage());
			msg = e.getMessage();
		}

		return msg;
	}

	@Override
	public String sendSmsForEntinfoHttp(String smsServer, Integer smsPort, String username, String password, String phones, String content) {
		String msg = sendSmsForEntinfo(smsServer, smsPort, username, password, phones, content);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForEntinfoWs(String smsServer, Integer smsPort, String username, String password, String phones, String content, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phones);
		String msg = sendSmsForEntinfo(smsServer, smsPort, username, password, phones, content);
		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);

		return data;
	}

	private String sendSmsForC123(String smsServer, Integer smsPort, String username, String password, String phones, String content) {
		int count = 0;
		StringBuffer sb = new StringBuffer(smsServer);
		sb.append("uid=" + username + "");
		sb.append("&pwd=" + EncryptionUtil.encryptPassword(password).toUpperCase() + "");
		sb.append("&encode=utf8");
		sb.append("&mobile=" + phones + "");
		sb.append("&content=" + URLEncoder.encode(content));
		String msg = null;
		URL url;
		try {
			url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			count = Integer.valueOf(in.readLine());
			in.close();
			if (count == 101) {
				msg = "????????????";
			} else if (count == 102) {
				msg = "????????????";
			} else if (count == 103) {
				msg = "????????????";
			} else if (count == 104) {
				msg = "????????????";
			} else if (count == 105) {
				msg = "????????????";
			} else if (count == 106) {
				msg = "????????????";
			} else if (count == 107) {
				msg = "????????????";
			} else if (count == 108) {
				msg = "???????????????";
			} else if (count == 109) {
				msg = "????????????";
			} else if (count == 110) {
				msg = "????????????????????????";
			} else if (count == 111) {
				msg = "??????????????????";
			} else if (count == 112) {
				msg = "????????????";
			} else if (count == 113) {
				msg = "????????????????????????";
			} else if (count == 114) {
				msg = "???????????????10???????????????";
			} else if (count == 115) {
				msg = "????????????";
			} else if (count == 116) {
				msg = "??????????????????";
			} else if (count == 117) {
				msg = "??????IP?????????";
			} else if (count == 118) {
				msg = "????????????";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			msg = e.getMessage();
		} catch (ProtocolException e) {
			e.printStackTrace();
			msg = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		return msg;
	}

	@Override
	public String sendSmsForC123Http(String smsServer, Integer smsPort, String username, String password, String phones, String content) {
		String msg = sendSmsForC123(smsServer, smsPort, username, password, phones, content);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForC123Ws(String smsServer, Integer smsPort, String username, String password, String phones, String content, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phones);
		String msg = sendSmsForC123(smsServer, smsPort, username, password, phones, content);

		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);

		return data;
	}

	private String sendSmsForQixintong(String serviceURL, String username, String password, String mobile, String content) {
		String code = null;
		String resultMsg = null;
		StringBuffer sb = new StringBuffer(serviceURL);
		sb.append("mark=send");
		sb.append("&username=" + username + "");
		sb.append("&password=" + password + "");
		sb.append("&mobile=" + mobile + "");
		sb.append("&content=" + URLEncoder.encode(content));
		sb.append("&fstd=5");
		URL url;
		try {
			url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			code = in.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!StringUtil.equals(code, "???0")) {
			if (StringUtil.equals(code, "-1")) {
				resultMsg = "?????????????????????????????????";
			} else if (StringUtil.equals(code, "-2")) {
				resultMsg = "????????????";
			} else if (StringUtil.equals(code, "???-3")) {
				resultMsg = "???????????????????????????????????????";
			} else if (StringUtil.equals(code, "-4")) {
				resultMsg = "???????????????";
			} else if (StringUtil.equals(code, "???-5")) {
				resultMsg = "????????????????????????";
			} else if (StringUtil.equals(code, "???-6")) {
				resultMsg = "?????????API??????";
			} else if (StringUtil.equals(code, "-7")) {
				resultMsg = "???????????????";
			} else if (StringUtil.equals(code, "-8")) {
				resultMsg = "???????????????????????????";
			} else if (StringUtil.equals(code, "-88")) {
				resultMsg = "??????????????????";
			}
		}
		return resultMsg;
	}

	@Override
	public String sendSmsForQixintongHttp(String serviceURL, String username, String password, String mobile, String content) {
		String msg = sendSmsForQixintong(serviceURL, username, password, mobile, content);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForQixintongWs(String serviceURL, String username, String password, String mobile, String content, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(mobile);
		String msg = sendSmsForQixintong(serviceURL, username, password, mobile, content);

		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);

		return data;
	}

	private String sendSmsForAblesms(String smsServer, Integer smsPort, String username, String password, String content, Integer countryCode, String telephone, String userDefineNo) {
		String msg = null;
		System.out.println("content before "+content);
		System.out.println("content after "+URLEncoder.encode(content));
		System.out.println("telephone "+telephone);
		StringBuffer sb = new StringBuffer(smsServer);
		sb.append("Username=" + username + "");
		sb.append("&Password=" + password + "");
		sb.append("&Message=" +URLEncoder.encode(content) + "");
		sb.append("&Telephone=" + telephone + "");
		sb.append("&CountryCode=" + countryCode + "");
		sb.append("&UserDefineNo=" + userDefineNo + "");
		URL url;
		try {
			url = new URL(sb.toString());
			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			String result = StringUtil.EMPTY;
			while (null != (inputLine = in.readLine())) {
				result = result + inputLine;
			}
			String xml = result.replace("&lt;", "<").replace("&gt;", ">");
			System.out.println(xml);
			String code = getReturnXml(xml);
			if (StringUtil.isNotEmpty(code)) {
				if (StringUtil.equals(code, "1")) {
					msg = null;
				} else if (StringUtil.equals(code, "0")) {
					msg = "Missing ??Values";
				} else if (StringUtil.equals(code, "10")) {
					msg = "Incorrect ??Username ??or ??Password ";
				} else if (StringUtil.equals(code, "20")) {
					msg = "??Message ??content ??too ??long";
				} else if (StringUtil.equals(code, "30")) {
					msg = "Message ??content ??too ??long ";
				} else if (StringUtil.equals(code, "40")) {
					msg = "Telephone ??number ??too ??long ";
				} else if (StringUtil.equals(code, "60")) {
					msg = "Incorrect ??Country ??Code ";
				} else if (StringUtil.equals(code, "80")) {
					msg = "Incorrect ??date ??time";
				} else if (StringUtil.equals(code, "100")) {
					msg = "System ??error, ??please ??try ??again ";
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			msg = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}

	@Override
	public String sendSmsForAblesmsHttp(String smsServer, Integer smsPort, String username, String password, String content, Integer countryCode, String telephone, String userDefineNo) {
		String msg = sendSmsForAblesms(smsServer, smsPort, username, password, content, countryCode, telephone, userDefineNo);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForAblesmsWs(String smsServer, Integer smsPort, String username, String password, String content, Integer countryCode, String telephone, String userDefineNo, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(telephone);
		String msg = sendSmsForAblesms(smsServer, smsPort, username, password, content, countryCode, telephone, userDefineNo);
		if (StringUtil.isBlank(msg)) {
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}
		return data;
	}

	private static String getReturnXml(String xml) {
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element data = doc.getRootElement();
			Iterator it = data.elements().iterator();
			if (it.hasNext()) {
				Element ele = (Element) it.next();
				if (ele.getName().equals("ReturnValue")) {
					Iterator itCode = ele.elements().iterator();
					while (itCode.hasNext()) {
						Element eleCode = (Element) itCode.next();
						if (eleCode.getName().equals("State")) {
							return eleCode.getText();
						}
					}
				}

			}
		}
		return "";

	}
	
	private String sendForBayouSms(String username, String password, String message, String phone, String requestAddress) {
		String msg = null;
		SendSMS sendSMS = new SendSMS();
		sendSMS.setUsername(username);
		sendSMS.setPassword(EncryptionUtil.encrypt(password));
		sendSMS.setMessage(message);
		sendSMS.setMobiles(phone);
		sendSMS.setServicesRequestAddRess(requestAddress);
		sendSMS.setSmstype(0);
		sendSMS.setTimerid("0");
		sendSMS.setTimertype(0);
		
		Map<String, String> responseError = sendSMS.sendSMS();
		log.info("Error Code: " + responseError.get("errorcode") + " Message: " + responseError.get("errordescription"));
		
		if(!(StringUtil.equals(StringUtil.trimToEmpty(responseError.get("errorcode")), "0"))){
			msg = responseError.get("errordescription");
		} 
		return msg;
	}

	@Override
	public String sendForBayouSmsHttp(String username, String password, String message, String phone, String requestAddress) {
		String msg = sendForBayouSms(username, password, message, phone, requestAddress);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendForBayouSmsWs(String username, String password, String message, String phone, String requestAddress, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendForBayouSms(username, password, message, phone, requestAddress);
		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);

		return data;
	}
	
	private String sendForAccessYou(String accountno, String password, String message, String phone, String baseUrl){
		String resultMessage = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountno", accountno);
		params.put("pwd", password);
		params.put("msg", message);
		if(phone!=null && phone!=""){
			phone=(phone.startsWith("86"))?phone:"86"+phone;
		}
		params.put("phone",phone);
		//params.put("phone", "86"+phone);
		params.put("size", "l");

		try {
			log.info("params base url "+baseUrl+" accountNo "+accountno+" pwd "+password+" msg"+message+" phone"+phone);
			
			String response = HttpUtils.get(baseUrl, params);
			log.info("Response: " + response);
			if(NumberUtils.isNumber(StringUtil.trimToEmpty(response))){
				return resultMessage;
			}
			resultMessage = response;
		} catch (PostFailedException e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		} catch (ResponseFailedException e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}
		return resultMessage;
	}

	@Override
	public String sendForAccessYouHttp(String accountno, String password, String message, String phone, String baseUrl) {
		String msg = sendForAccessYou(accountno, password, message, phone, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendForAccessYouWs(String accountno, String password, String message, String phone, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendForAccessYou(accountno, password, message, phone, baseUrl);
		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		if(StringUtil.isNotBlank(msg)){
			data.setErrorMsg(data.getErrorMsg()+",the reason is "+msg);
		}
		return data;
	}
	 public static String SmsForHCTLimit1(String phoneNum,String content){
			String message="";
			String key=phoneNum+content;
			if(!SmsForHCTMap.containsKey(key)){
				Date startTime=new Date();
				PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
				SmsForHCTMap.put(key, platformLimit);
			}else{
				int i=SmsForHCTMap.get(key).getCount();
				Date startTime=SmsForHCTMap.get(key).getStartTime();
				if((new Date().getTime()-startTime.getTime())/(1000*60)>=60*24){
				    startTime=new Date();
					PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
					SmsForHCTMap.put(key, platformLimit);
				}else{ 
					if(i>=10){
						Integer min=Math.round(60*24-(new Date().getTime()-startTime.getTime())/(1000*60));
						message="????????????????????????????????????????????????10??????"+phoneNum+"????????????????????????????????????????????????????????????????????????"+min+"??????????????????????????????????????????????????????";
					}else{
						PlatformLimit platformLimit=SmsForHCTMap.get(key);
						int count=platformLimit.getCount();
						platformLimit.setCount(++count);
						SmsForHCTMap.put(key, platformLimit);
					}
				}
			}
			return message;
			
		}
	 private String sendSmsForHCT1(String accountno, String password, String message, String phone, String baseUrl) {
		 String resultMessage=StringUtils.EMPTY;
		 StringBuffer sb=new StringBuffer(baseUrl);
		 StringBuffer sb1=new StringBuffer();
		 StringBuffer sb2=new StringBuffer();
		 String midPhone=null;
		 try {
				log.info("before URLEncoder : message="+message);
				log.info("after URLEncoder : message="+URLEncoder.encode(message,"UTF-8"));
			}catch (Exception ex){
				System.out.println("error:"+ex.getMessage());
			}
		 try {
			sb.append("?action=send");
			 String[] strs=accountno.split("\\|");
			 sb.append("&userid="+strs[1]);
			 sb.append("&account="+strs[0]);
			 sb.append("&password="+password);
			 String[] phones=phone.split("\\,");
			 if(phones.length>0){
				 for(String p:phones){
					 midPhone=(p.startsWith("86"))?p.substring(2):p;
					 sb1.append(midPhone);
					 String str1=SmsForHCTLimit1(midPhone,message);
					 if(StringUtils.isNoneEmpty(str1)){
						 sb2.append(str1);
					 }
					 sb1.append(",");
				 }
				 sb1.deleteCharAt(sb1.length()-1);
				 phone=sb1.toString();
			 }
			 sb.append("&mobile="+phone);
			 sb.append("&content="+URLEncoder.encode(message, "UTF-8"));
			 sb.append("&sendTime=");
			 sb.append("&extno=");
			 System.out.println(sb.toString());
			 log.info("???????????????????????????url???"+sb.toString());
			 URL url=new URL(sb.toString());
			 HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			 connection.setRequestMethod("POST");
			 BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			 String line=StringUtils.EMPTY;
			 while((line=br.readLine())!=null){
				 resultMessage+=line;
			 }
			 log.info("HCTCOM response:"+resultMessage);
			 br.close();
			 if(StringUtils.isNoneEmpty(sb2.toString())){
				 return getReturnXmlForhctcom(resultMessage)+","+sb2.toString();
			 }
			 return getReturnXmlForhctcom(resultMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return "";
	 }
	 private  String getReturnXmlForhctcom(String resultMessage){
		 String response=StringUtils.EMPTY;
		 Document doc=DomOperator.string2Document(resultMessage);
		 Element root=doc.getRootElement();
		 Iterator it=root.elements().iterator();
		 while(it.hasNext()){
			 Element e=(Element)it.next();
			 if("returnstatus".equals(e.getName())){
				 response+=e.getText();
				 response+=",";
			 }
			 if("message".equals(e.getName())){
				 response+=e.getText();
			 }
		 }
		 return response;
	 }
//	private String sendSmsForHCT(String accountno, String password, String message, String phone, String baseUrl) {
//		String resultMessage = null;
//		StringBuffer phoneStr=new StringBuffer("");
//		String midStr=null;
//		StringBuffer sb1=new StringBuffer();
//		StringBuffer s=new StringBuffer();
//		String str1=null;
//		try {
//			Map<String, String> params = new HashMap<String, String>();
//
//			try {
//				log.info("before URLEncoder : message="+message);
//				log.info("after URLEncoder : message="+URLEncoder.encode(message,"GB2312"));
//			}catch (Exception ex){
//				System.out.println("error:"+ex.getMessage());
//			}
//
//			params.put("id", accountno);
//			params.put("pwd", password);
//			if(phone!=null && phone!=""){
//				String[] phones=phone.split("\\,");
//				if(phones.length>=1){
//					for(String str:phones){
//						midStr=(str.startsWith("86"))?str.substring(2):str;
//						phoneStr.append(midStr);
//						phoneStr.append(",");
//					}
//					 phone=phoneStr.toString();
//					 phone=phone.substring(0, phone.length()-1);
//				}
//			}
//			if(phone!=null && phone!=""){
//				String[] phones=phone.split("\\,");
//				if(phones.length>=1){
//					for(String p:phones){
//						str1=SmsForHCTLimit(p);
//						if(StringUtils.isNotEmpty(str1)){
//							s.append(str1);
//					}
//					}
//				}
//			}
//			params.put("to", phone);
//			params.put("content", message);
//			params.put("time", "");
//
//			System.out.println(HttpUtilsGB2312.getURL(baseUrl, params));
//			String xml = HttpUtilsGB2312.get(baseUrl, params);
//			System.out.println(xml);
//			String[] responseArray = xml.split("/");
//
//			if (StringUtils.equalsIgnoreCase(responseArray[0],"000") && StringUtils.isEmpty(s.toString())) {
////				buildResult(null);
//			}else if(StringUtils.equalsIgnoreCase(responseArray[0],"000") && StringUtils.isNotEmpty(s.toString())){
//				resultMessage=s.toString();
//			} else{
//				resultMessage =  getFailMessageForWinic(responseArray[0]);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultMessage = e.getMessage();
//		}
//
//		return resultMessage;
//	}

	@Override
	public String sendSmsForHCTHttp(String accountno, String password, String message, String phone, String baseUrl) {
		String msg = sendSmsForHCT1(accountno, password, message, phone, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForHCTWs1(String accountno, String password, String message, String phone, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsForHCT1(accountno, password, message, phone, baseUrl);
		String[] msgs=msg.split("\\,");
		if ("Success".equals(msgs[0]) && msgs.length==2){
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else if("Success".equals(msgs[0]) && msgs.length==3){
			data.setErrorMsg(msgs[2]);
		}else{
			//data.setErrorMsg(data.getErrorMsg()+msg);
			data.setErrorMsg(msgs[1]);
		}
		log.info("HCTCOM result status:"+data.getStat()+"response message:"+data.getErrorMsg());
		return data;
	}

	private String sendSmsForChinaSmsGroup(String account, String password, String phone, String message, String baseUrl) {
		String resultMessage = null;
		log.info("sendSmsForChinaSmsGroup");
		try {
			log.info("before URLEncoder : message="+message);
			log.info("after URLEncoder : message="+URLEncoder.encode(message,"UTF-8"));
//			message = message.replace(" ", "%20");
//			log.info("after remote "+"s : message="+URLEncoder.encode(message,"UTF-8"));
		} catch (Exception ex) {
			System.out.println("error:"+ex.getMessage());
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("zh", account);
		params.put("mm", password);
		params.put("hm", phone);
		params.put("nr", message);
		params.put("sms_type", "41");//id
		try {
			String url = HttpUtils.getURL(baseUrl, params);
			System.out.println("get : url="+url);
			String response = ph.sinonet.vg.live.utils.HttpUtils.getMethodAllowRedirect(baseUrl, params);
			response = response.substring(response.indexOf(":")+1,response.length());

			if (!StringUtil.isNumeric(response)) {
//				return buildResult(response);
				return response;
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}

		return resultMessage;
	}

	@Override
	public String sendSmsForChinaSmsGroupHttp(String account, String password, String phone, String message, String baseUrl) {
		String msg = sendSmsForChinaSmsGroup(account, password, phone, message, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForChinaSmsGroupWs(String account, String password, String phone, String message, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsForChinaSmsGroup(account, password, phone, message, baseUrl);
		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);

		return data;
	}
	
	private static Long getHctResponse(String xml){
		org.jsoup.nodes.Document document = Jsoup.parse(xml, StringUtil.EMPTY, Parser.xmlParser());
		return Long.parseLong(document.select("string").text());
	}
	
	private static String getFailMessageForHCT(Long code){
		switch (code.intValue()) {
			case -1:
				return "????????????";
			case -2:
				return "????????????";
			case -3:
				return "????????????";
			case -4:
				return "?????????????????????, ??????, ????????????";
			case -9:
				return "????????????";
			case -12:
				return "????????????";
			case -13:
				return "????????????";
			default:
				return "API Error";
		}
	}

	private Map<String,String> getBorderSmsResponse(String jsonResponse){
		Gson gson = new Gson();

		Type jsonStringToMap = new TypeToken<Map<String, String>>(){}.getType();

		Map<String,String> zz =  gson.fromJson(jsonResponse, jsonStringToMap);

		return zz;
	}

	private static String getFailMessageForWinic(String statusCode){



		if(StringUtils.equalsIgnoreCase(statusCode,"-01")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-02")){
			return "?????????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-03")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-04")){
			return "??????????????????????????????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-110")){
			return "IP?????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-12")){
			return "????????????";
		}

		return "API Error";

	}

	private static String getFailMessageForBdt360(String statusCode){

		if(StringUtils.equalsIgnoreCase(statusCode,"-1")){
			return "????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"0")){
			return "??????????????????????????????";
		}


		if(StringUtils.equalsIgnoreCase(statusCode,"-2")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-3")){
			return "?????????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-4")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-30")){
			return "?????????IP";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-5")){
			return "????????????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-6")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-7")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-8")){
			return "????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-9")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-10")){
			return "????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-13")){
			return "????????????????????????";
		}
		if(StringUtils.equalsIgnoreCase(statusCode,"-14")){
			return "???????????????????????????????????????";
		}
		if(StringUtils.equalsIgnoreCase(statusCode,"-15")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-16")){
			return "??????????????????";
		}

		if(StringUtils.equalsIgnoreCase(statusCode,"-100")){
			return "???????????????????????????(????????????)";
		}
		return "API Error";

	}

	private String sendSmsForWinic(String id, String password, String phone, String content, String baseUrl) {
		String resultMessage = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", id);
			params.put("pwd", password);
			params.put("to", phone);
			params.put("content", content);
			params.put("time", "");

			System.out.println(HttpUtils.getURL(baseUrl, params));
			String xml = HttpUtils.get(baseUrl, params);
			System.out.println(xml);
			String[] responseArray = xml.split("/");


			if (StringUtils.equalsIgnoreCase(responseArray[0],"000")) {
//				buildResult(null);
			} else {
				resultMessage =  getFailMessageForWinic(responseArray[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}
		return resultMessage;
	}

	@Override
	public String sendSmsForWinicHttp(String id, String password, String phone, String content, String baseUrl) {
		String msg = sendSmsForWinic(id, password, phone, content, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForWinicWs(String id, String password, String phone, String content, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsForWinic(id, password, phone, content, baseUrl);
		if (StringUtil.isBlank(msg))
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		return data;
	}
	
	private String sendSmsOpenPlatform(String account, String authKey, String phone, String content, String baseUrl) {
		String resultMessage = null;
        String[] accountANDchannel = account.split("\\|");
		try {
			String xmlResults = OpenApi.querySendOnce(accountANDchannel[0],authKey,baseUrl,phone,content,Integer.parseInt(accountANDchannel[1]),0,null);
			String[] xmlResult=xmlResults.split(",");
			Integer result = getOpenApiReturnXml(xmlResult[0]);
			log.info("phone:" + phone +",content:" + content + ",result:" + result);

			if (result != null && result.intValue() == 1 && xmlResult.length==1) {
//				buildResult(null);
			}else if(result != null && result.intValue() == 1 && xmlResult.length>1){
				resultMessage = xmlResult[1];
			} else {
				resultMessage = getOpenApiErrorMsg(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}

		return resultMessage;

	}

	@Override
	public String sendSmsOpenPlatformHttp(String account, String authKey, String phone, String content, String baseUrl) {
		String msg = sendSmsOpenPlatform(account, authKey, phone, content, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsOpenPlatformWs(String account, String authKey, String phone, String content, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsOpenPlatform(account, authKey, phone, content, baseUrl);
		if (StringUtil.isBlank(msg)){
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			data.setErrorMsg(msg);
		}
		return data;
	}

	 public static String SmsSphPlatformLimit(String phoneNum){
			String message="";
			if(!SphPlatformMap.containsKey(phoneNum)){
				Date startTime=new Date();
				PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
				SphPlatformMap.put(phoneNum, platformLimit);
			}else{
				int i=SphPlatformMap.get(phoneNum).getCount();
				Date startTime=SphPlatformMap.get(phoneNum).getStartTime();
				if((new Date().getTime()-startTime.getTime())/(1000*60)>=60*24){
				    startTime=new Date();
					PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime,1);
					SphPlatformMap.put(phoneNum, platformLimit);
				}else{
					if(i>=3){
						message="???????????????24?????????????????????3????????????"+phoneNum+"?????????????????????????????????????????????????????????????????????????????????";
					}else{
						PlatformLimit platformLimit=SphPlatformMap.get(phoneNum);
						int count=platformLimit.getCount();
						platformLimit.setCount(++count);
						SphPlatformMap.put(phoneNum, platformLimit);
					}
				}
			}
			return message;
			
		}
	private String sendSmsSphPlatform(String id, String password, String phone, String content, String baseUrl) {
		String resultMessage = null;
		StringBuffer sb=new StringBuffer("");
		StringBuffer s=new StringBuffer();
		StringBuffer sb1=new StringBuffer();
		String str1=null;
		try{
			Map<String, String> params = new HashMap<String, String>();
			params.put("name",id);
			params.put("pwd",password);
			if(phone!=null && phone!=""){
				String[] phones=phone.split("\\,");
				if(phones.length>=1){
					for(String p:phones){
						sb.append((p.startsWith("86"))?p.substring(2):p);
						sb.append(",");
					}
					phone=sb.toString();
					phone=phone.substring(0,phone.length()-1);
				}
			}
			if(phone!=null && phone!=""){
				String[] phones=phone.split("\\,");
				if(phones.length>=1){
					for(String p:phones){
						str1=SmsSphPlatformLimit(p);
						if(StringUtils.isNotEmpty(str1)){
							s.append(str1);
						}
					}
				}
			}
			params.put("dst",phone);
			params.put("msg",content);
			params.put("time","??????");
			params.put("sender","????????????");

			System.out.println(HttpUtilsGB2312.getURL(baseUrl,params));
			String response = HttpUtilsGB2312.get(baseUrl,params);
			String finalResponse = new String(response.getBytes("GBK"), "GB2312");

			String errid = finalResponse.split("&")[4].split("=")[1];
			String errmsg = finalResponse.split("&")[3].split("=")[1];

			if(!errid.equals("0")) {
				resultMessage = backMessage(Integer.parseInt(errid));
			}
			if(errid.equals("0") && StringUtil.isNotEmpty(s.toString())){
				resultMessage=s.toString();
			}
			System.out.println("errid = " + errid + " errmsg = " + resultMessage + " finalResponse " + finalResponse);
		} catch(Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}
		return resultMessage;
//		return buildResult(resultMessage);
	}
	public static String backMessage(int errid){
		switch(errid){
			case 6001:
				return "?????????????????????";
			case 6002:
				return "??????????????????????????????";
			case 6003:
				return "?????????????????????";
			case 6004:
				return "????????????????????????????????????";
			case 6005:
				return "??????????????????????????????";
			case 6006:
				return "????????????????????????????????????";
			case 6007:
				return "???????????????????????????????????????";
			case 6008:
				return "?????????????????????";
			case 6009:
				return "????????????????????????";
			case 6010:
				return "????????????????????????";
			case 6011:
				return "???????????????????????????????????????";
			case 6012:
				return "????????????????????????ip??????";
			case 6013:
				return "???????????????????????????";
			case 6014:
				return "??????????????????????????????";
			case 6015:
				return "???????????????????????????????????????";
			case 6016:
				return "????????????????????????4?????????";
			case 6017:
				return "??????????????????????????????";
			case 6018:
				return "??????????????????SDK????????????????????????";
			case 6019:
				return "???????????????????????????";
			case 6020:
				return "????????????vip??????????????????";
			case 6021:
				return "?????????????????????";
			default:
				return "????????????";
		}
	}
	@Override
	public String sendSmsSphPlatformHttp(String id, String password, String phone, String content, String baseUrl) {
		String msg = sendSmsSphPlatform(id, password, phone, content, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsSphPlatformWs(String id, String password, String phone, String content, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsSphPlatform(id, password, phone, content, baseUrl);
		if (StringUtil.isBlank(msg)){
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			data.setErrorMsg(msg);
		}
		return data;
	}

	public static String SmsForBdt360PlatformLimit(String phoneNum){
		String message="";
		if(!Bdt360Map.containsKey(phoneNum)){
			Date startTime=new Date();
			PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
			Bdt360Map.put(phoneNum, platformLimit);
		}else{
			int i=Bdt360Map.get(phoneNum).getCount();
			Date startTime=Bdt360Map.get(phoneNum).getStartTime();
			if((new Date().getTime()-startTime.getTime())/(1000*60)>=24*60){
			    startTime=new Date();
				PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime,1);
				Bdt360Map.put(phoneNum, platformLimit);
			}else{
				if(i>=8){
					message="?????????????????????????????????8????????????"+phoneNum+"????????????????????????????????????????????????????????????????????????????????????????????????";
				}else{
					PlatformLimit platformLimit=Bdt360Map.get(phoneNum);
					int count=platformLimit.getCount();
					platformLimit.setCount(++count);
					Bdt360Map.put(phoneNum, platformLimit);
				}
			}
		}
		return message;
	}
	private String sendSmsForBdt360(String account, String password, String phone, String content, String baseUrl) {
		String resultMessage = null;
		StringBuffer sb=new StringBuffer("");
		StringBuffer s=new StringBuffer();
		try {

			Map<String, String> params = new HashMap<String, String>();
			params.put("Id", "300");
			params.put("Name", account);
			params.put("Psw", password);
			if(phone!=null && phone!=""){
				String[] phones=phone.split("\\;");
				if(phones.length>=1){
					for(String p:phones){
						sb.append((p.startsWith("86"))?p.substring(2):p);
						sb.append(";");
					}
					phone=sb.toString();
					phone=phone.substring(0,phone.length()-1);
				}
			}
			if(phone!=null && phone!=""){
				String[] phones=phone.split("\\;");
				if(phones.length>=1){
					for(String p:phones){
						s.append(SmsForBdt360PlatformLimit(p));
					}
				}
			}
			params.put("Phone", phone);
			params.put("Message", content);
			params.put("Timestamp","0");
			log.info(HttpUtils.getURL(baseUrl, params));
			String response = HttpUtils.post(baseUrl, params);
			log.info(response);
			String[] responseArray = response.split(",");

			if (StringUtils.contains(responseArray[0],"State:1") && StringUtil.isEmpty(s.toString())) {
//				buildResult(null);
			}else if(StringUtils.contains(responseArray[0],"State:1") && StringUtil.isNotEmpty(s.toString())){
				resultMessage=s.toString();
			} else {
				resultMessage =  getFailMessageForBdt360(StringUtils.replace(responseArray[0],"State:",""));
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}
		return resultMessage;

//		return null;
	}

	@Override
	public String sendSmsForBdt360Http(String account, String password, String phone, String content, String baseUrl) {
		String msg = sendSmsForBdt360(account, password, phone, content, baseUrl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForBdt360Ws(String account, String password, String phone, String content, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsForBdt360(account, password, phone, content, baseUrl);
		if (StringUtil.isBlank(msg)){
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			data.setErrorMsg(msg);
		}
		return data;
	}


	public String sendSmsForSynHey(String account, String password, String phone, String content, String baseurl) {
		String resultMessage = null;
		try{
			Map<String, String> params = new HashMap<String, String>();
			params.put("key",account);
			params.put("secret",password);
			params.put("to",StringUtils.startsWith(phone,"86") ? phone : "86" + phone);
			//params.put("to",phone);
			params.put("text",content);
	//http://api.synhey.com/json?key=2693624034@qq.com&secret=123456&to=8618390571077&text=Hello+world
//			String testUrl ="http://api.synhey.com/json?";
//			String paramStr = "key=yboawd" + "&secret=mYhUBPKL" + "&to=8618390571077" + "&text=test";

			log.info(HttpUtils.getURL(baseurl,params));

			String response = HttpUtils.customGetReq(baseurl,params);
			log.info("response:" + response);
			Map<String,String> responseMap = getBorderSmsResponse(response);

			if (StringUtils.equalsIgnoreCase("0",responseMap.get("status"))) {
//				buildResult(null);
			} else {
				resultMessage = responseMap.get("status_code");
			}

		} catch(Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}
		return resultMessage;
	}

	@Override
	public String sendSmsForSynHeyHttp(String account, String password, String phone, String content, String baseurl) {
		String msg = sendSmsForSynHey(account, password, phone, content, baseurl);
		return buildResult(msg);
	}

	@Override
	public SendMsgResp sendSmsForSynHeyWs(String account, String password, String phone, String content, String baseurl, String name, String provider,String priority,RequestMessage msgReq) {
		SendMsgReq req = (SendMsgReq) msgReq.getData();
		SendMsgResp data = new SendMsgResp();
	    data.setTel(phone);
		//phone = "86"+ phone;
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);

		String msg = sendSmsForSynHey(account, password, phone, content, baseurl);
		if (StringUtil.isBlank(msg)){
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			data.setErrorMsg(data.getErrorMsg()+",the reason is "+msg);
		}
		
//		}else{
//			if("1".equals(priority)){
//				List<SmsConfig> smsConfigs=smsDao.getSmsConfig(req, priority);
//				for(SmsConfig smsConfig:smsConfigs){
//					smsConfig.setFlag(1);
//					smsConfig.setSwitchTime(new Date());
//					log.info(smsConfig);
//					smsDao.updateSmsConfigFlag(smsConfig.getName(),smsConfig.getFlag(),smsConfig.getSwitchTime());
//				}
//				smsConfigs=smsDao.getAllSmsConfig(req);
//				for(int i=2;i<=smsConfigs.size();i++){
//					List<SmsConfig> smsConfig=smsDao.getsmsConfig(req,String.valueOf(i) );
//					SmsConfig smsConfig1=smsConfig.iterator().next();
//					smsConfig1.setFlag(0);
//					smsConfig1.setSwitchTime(new Date());
//					smsDao.updateSmsConfigFlag(smsConfig1.getName(), smsConfig1.getFlag(),smsConfig1.getSwitchTime());
//					ResponseMessage responseMessage=processWSReq(msgReq);
//					if(responseMessage.getData().getStatus()==200){
//						break;
//					}
//					smsConfig1.setFlag(1);
//					smsDao.updateSmsConfigFlag(smsConfig1.getName(), smsConfig1.getFlag(),smsConfig1.getSwitchTime());
//				}
//				
//		}else{
//				List<SmsConfig> smsConfigs=smsDao.getSmsConfig(req, priority);
//				smsConfigs.iterator().next().setFlag(1);
//				smsConfigs=smsDao.getSmsConfig(req, String.valueOf(1));
//				SmsConfig smsConfig=smsConfigs.iterator().next();
//				smsConfig.setFlag(0);
//				smsConfig.setSwitchTime(new Date());
//				smsDao.updateSmsConfigFlag(smsConfig.getName(), smsConfig.getFlag(),smsConfig.getSwitchTime());
//			}
//			
		
		
		return data;
	}

	@Override
	public void saveSmsMessage(String product, String provider, String userId, String phone, String content, String result) {
		org.jsoup.nodes.Document xmlResponse = Jsoup.parse(result);
		String code = null, msg = null;

		if(SmsType.SMS63.getCode().equals(provider)){
			msg = getReturnXmlForSms63(result);
			Document doc = DomOperator.string2Document(result);
			code = doc.getStringValue();
		} else  if(xmlResponse != null){
			code = xmlResponse.getElementsByTag("code").text();
			if(!code.equals("200"))
				msg = xmlResponse.getElementsByTag("msg").text();
			else
				msg = "Success";
		}

		String parsedResult = "Code: "+ code + ", Message: " + msg;
		SmsMessages sms = new SmsMessages(product, provider, userId, phone, content, parsedResult, DateUtils.parseDateForStandard());
		smsDao.saveSentSMS(sms);
	}

	private String getReturnXmlForSms63(String xml) {
		Document doc = DomOperator.string2Document(xml);
		String resp = org.apache.commons.lang.StringUtils.EMPTY;

		if (doc != null) {
			Element data = doc.getRootElement();
			Iterator it = data.elements().iterator();
			while (it.hasNext()) {
				Element ele = (Element) it.next();

				if(ele.getName().equals("returnstatus")){
					resp += ele.getText();
					resp+=",";
				}

				if(ele.getName().equals("message")){
					resp += ele.getText();
				}
//				if(resp.equals("Faild")){
//					return buildResult(doc.getStringValue());
//
//				} else if(resp.equals("Success")){
//					return "";
//				}
			}
		}


	//	return buildResult("Faild");
		return resp;

	}


	private void saveWsSentMessage(String product, String provider, String userId, String phone, String content, String result) {
		ph.sinonet.vg.live.model.sms.SmsMessages sms = new ph.sinonet.vg.live.model.sms.SmsMessages(product, provider, userId, phone, content, result, DateUtils.parseDateForStandard());
		smsDao.saveWsSentSMS(sms);
	}

	public static void main(String[] args) {
//		System.out.println("running");
//		SmsServiceImpl smsService = new SmsServiceImpl();
//		String response = smsService.sendSmsForChinaSmsGroup("jacky112233","ljw123456","18390571077","3.dev test message","http://www.810086.com.cn/jk.aspx");
//		System.out.println("response = "+response);

//		SmsServiceImpl smsService = new SmsServiceImpl();
//		String response = smsService.sendSmsForWinic("jacky007","x821111","18390571077","3.dev test message","http://service.winic.org:8009/sys_port/gateway/index.asp");
//		String response = smsService.sendSmsForHCT("jacky007","x821111","?????????????????????","13533758475","http://service.winic.org:8009/sys_port/gateway/index.asp");
//		System.out.println(response);

//		String response = smsService.sendSmsOpenPlatform("1001@501328330001","2A295A604705F7E87FFCEBDF45F5E80C","18390571077","Java???????????????","http://smsapi.c123.cn/OpenPlatform/OpenApi");

//		System.out.println(response);

//		String xml = "<xml name=\"sendOnce\" result=\"1\"><Item cid=\"501328330001\" sid=\"1001\" msgid=\"652694368707503300\" total=\"1\" price=\"0.1\" remain=\"0.300\" /></xml>";
//
//		System.out.println(getOpenApiReturnXml(xml));


		SmsServiceImpl smsService = new SmsServiceImpl();
//		String resposne = "{\"messageid\":\"053EB571ADC6DD\",\"status\":\"0\"}";
//
//		smsService.getBorderSmsResponse(resposne);
//
		String response = smsService.sendSmsForSynHey("yboawd","mYhUBPKL",
				"18390571077",
				"??????www.eu2018.net ?????????88%????????????????????????????????????38%?????????????????????????????????1%???????????????????????????7x24??????QQ??????800167818???????????????eu2018com?????????????????????",
				"http://api.bordersms.com/json");

		System.out.println(response);


//		String result = smsService.sendSmsForBdt360("eu888","ljw123456","18390571077","????????????","http://sms.bdt360.com:8180/Service.asmx/SendMessageStr");
//		System.out.println(result);


	}


	private static int  getOpenApiReturnXml(String xml) {
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element data = doc.getRootElement();
			String result = data.attributeValue("result");
			if(StringUtils.isNotEmpty(result)) {
				return Integer.parseInt(result);
			}
		}

		return -100;

	}

	private static String getOpenApiErrorMsg(int result) {
		switch(result) {
			case -11:
				return "????????????";
			case -10:
				return "????????????";
			case -9:
				return "??????????????????";
			case -8:
				return "????????????????????????";
			case -7:
				return "???????????????????????????";
			case -6:
				return "???????????????????????????";
			case -5:
				return "????????????";
			case -4:
				return "???????????????(???????????????????????????????????????????????????????????????????????????)";
			case -3:
				return "???????????????";
			case -2:
				return "???????????????";
			case -1:
				return "???????????????";
			case 0:
				return "?????????????????????";
			default:
				return "????????????";
		}
	}

	@Override
	public String addSmsConfig(String type, String user, String server, String remark, Integer port, String[] platformIds, String password, String name, Integer flag, Integer channel,String priority,String totalCounts){
		String msg = smsDao.addSmsConfig(type, user, server, remark, port, platformIds, password, name, flag, channel,priority,totalCounts);
		return msg;
	}

	@Override
	public String deleteSmsConfig(String username, String name){
		String msg = smsDao.deleteSmsConfig(username, name);
		return msg;
	}

	@Override
	public String updateSmsConfig(String type, String user, String server, String remark, Integer port, String[] platformIds, String password, String name, Integer flag, Integer channel,String priority, String totalCounts){
		String msg = smsDao.updateSmsConfig(type, user, server, remark, port, platformIds, password, name, flag, channel,priority,totalCounts);
		return msg;
	}

	@Override
	public String enableConfig(String name){
		String msg = smsDao.enableConfig(name);
		return msg;
	}

	@Override
	public String disableConfig(String name){
		String msg = smsDao.disableConfig(name);
		return msg;
	}
	@Override
	public Page querySmsMessages(String product, String code, Date start, Date end, Integer pageIndex, Integer size,String accountId,String phonenumber) {
		return smsDao.querySmsContents(product, code, start, end, pageIndex, size,accountId,phonenumber);
	}

	@Override
	public List<SmsConfig> querySmsConfigByProject(String name, String type, Integer flag, String platoformIds,Integer channel,String sortField,String orderRule){
		return smsDao.querySmsConfigByProject(name, type, platoformIds, flag,channel,sortField,orderRule);
	}
	@Override
	public String cancelDisableSmsConfig(String name) {
		// TODO Auto-generated method stub
		String msg=smsDao.cancelDisableSmsConfig(name);
		return msg;
	}
	public void sendMail(SmsConfig smsConfig) {
			String platformIds=smsConfig.getPlatformIds();
			String[] platformIdAyyay=platformIds.split("\\,");
			if(platformIdAyyay.length>0){
				for(String platform:platformIdAyyay){
					Integer platformId=Integer.parseInt(platform);
					switch(platformId){
					case 0:
						sendTextMail(smsConfig,"0");
						continue;
					case 1:
						sendTextMail(smsConfig,"1");
						continue;
					case 2:
						sendTextMail(smsConfig,"2");
						continue;
					case 3:
						sendTextMail(smsConfig,"3");
						continue;
					case 4:
						sendTextMail(smsConfig,"4");
						continue;
					case 5:
						sendTextMail(smsConfig,"5");
						continue;
					case 6:
						sendTextMail(smsConfig,"6");
						continue;
					case 7:
						sendTextMail(smsConfig,"7");
						continue;
					case 8:
						sendTextMail(smsConfig,"8");
						continue;
					case 9:
						sendTextMail(smsConfig,"9");
						continue;
					case 10:
						sendTextMail(smsConfig,"10");
						continue;
					case 11:
						sendTextMail(smsConfig,"11");
						continue;
					case 12:
						sendTextMail(smsConfig,"12");
						continue;
					case 13:
						sendTextMail(smsConfig,"13");
						continue;
					case 14:
						sendTextMail(smsConfig,"14");
						continue;
					case 43:
						sendTextMail(smsConfig,"43");
						continue;
					case 44:
						sendTextMail(smsConfig,"44");
						continue;
					case 45:
						sendTextMail(smsConfig,"45");
						continue;
					case 101:
						sendTextMail(smsConfig,"101");
						continue;
					default:
						continue;
					}
				}
		
	}
	}
	public void sendTextMail(SmsConfig smsConfig,String platformId){
		try{
			Properties props=new Properties();
			//InputStream in=new FileInputStream("mail.properties");
			//InputStream in=SmsServiceImpl.class.getResourceAsStream("mail.properties");
			 InputStream in = SmsServiceImpl.class.getClassLoader().getResourceAsStream("mail.properties"); 
			props.load(in);
			in.close();
			Set<Entry<Object, Object>> entrys = props.entrySet();
			for(Entry<Object, Object> entry:entrys){  
	            System.out.println("key:"+entry.getKey()+",value:"+entry.getValue()); 
	        }  
			String host=props.getProperty("mail.smtp.host");
			String from=props.getProperty("mail.smtp.user");
			String pass=props.getProperty("mail.smtp.password");
			String port=props.getProperty("mail.smtp.port");
			Properties pro=System.getProperties();
			pro.put("mail.smtp.starttls.enable", "true");
		    pro.put("mail.smtp.host", host);  
			pro.put("mail.smtp.user", from);
			pro.put("mail.smtp.password", pass);
			pro.put("mail.smtp.port", "587");
			pro.put("mail.smtp.auth", "true"); 
			String to=props.getProperty(platformId);
			//String[] tos=new String[]{to};
			String[] tos=to.split("\\,");
			 Session session = Session.getDefaultInstance(pro, null); 
			 session.setDebug(true);
			 MimeMessage message = new MimeMessage(session); 
			 message.setFrom(new InternetAddress(from)); 
			 InternetAddress[] toAddress = new InternetAddress[tos.length];
			 // ???????????????array  
			    for( int i=0; i < tos.length; i++ ) { // ???while??????????????????  
			        toAddress[i] = new InternetAddress(tos[i]);  
			    }  
			    System.out.println(Message.RecipientType.TO);  
			 
			    for( int i=0; i < toAddress.length; i++) { // ???while??????????????????  
			        message.addRecipient(Message.RecipientType.TO, toAddress[i]);  
			    }  
			    message.setSubject("?????????????????????????????????????????????????????????");  
			    message.setText("name:"+smsConfig.getName()+"   type:"+smsConfig.getType()+"   flag:"+smsConfig.getFlag()+"   priority:"+smsConfig.getPriority()+"   totalCounts"
			    		+smsConfig.getTotalCounts()+"   areadyCounts"+smsConfig.getAreadyCounts()+"   Remark:"+smsConfig.getRemark()+"   sms server:"+smsConfig.getServer()+"   sms port:"+smsConfig.getPort()+"   user id:"
			    		+smsConfig.getUser()+"   Channel:"+smsConfig.getChannel()+"   ???????????????"+smsConfig.getPlatformIds());  
			    Transport transport = session.getTransport("smtp");  
			    transport.connect(host, from, pass);  
			    transport.sendMessage(message, message.getAllRecipients());  
			    transport.close();  
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@Override
	public String login(String loginname, String password) {
		// TODO Auto-generated method stub
		SmsAccount smsAccount =getSmsAccount(loginname,  password);
		System.out.println("sms? "+loginname+" "+password);
		if(smsAccount != null){
			return "LOGINSUCCESS";
		}
		return "INVALIDUSERNAMEORPASSWORD";
	}
	@Override
	public String logout() {
		// TODO Auto-generated method stub
		return "LOGOUT";
	}
	@Override
	public String notParticipateAutoSwitch(String name) {
		// TODO Auto-generated method stub
		String msg=smsDao.notParticipateAutoSwitch(name);
		return msg;
	}
	@Override
	public SendMsgResp sendSmsForHCTWs(String accountno, String password, String message, String phone, String baseUrl, String name, String provider) {
		SendMsgResp data = new SendMsgResp();
		data.setStatusAndMsg(ErrorCode.FAIL_SEND);
		data.setTel(phone);
		String msg = sendSmsForHCT(accountno, password, message, phone, baseUrl);
		if (StringUtil.isBlank(msg)){
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}else{
			data.setErrorMsg(msg);
		}
		return data;
	}
	
	private String sendSmsForHCT(String accountno, String password, String message, String phone, String baseUrl) {
		String resultMessage = null;
		StringBuffer phoneStr=new StringBuffer("");
		String midStr=null;
		StringBuffer sb1=new StringBuffer();
		StringBuffer s=new StringBuffer();
		String str1=null;
		try {
			Map<String, String> params = new HashMap<String, String>();

			try {
				log.info("before URLEncoder : message="+message);
				log.info("after URLEncoder : message="+URLEncoder.encode(message,"GB2312"));
			}catch (Exception ex){
				System.out.println("error:"+ex.getMessage());
			}

			params.put("id", accountno);
			params.put("pwd", password);
			if(phone!=null && phone!=""){
				String[] phones=phone.split("\\,");
				if(phones.length>=1){
					for(String str:phones){
						midStr=(str.startsWith("86"))?str.substring(2):str;
						phoneStr.append(midStr);
						phoneStr.append(",");
					}
					 phone=phoneStr.toString();
					 phone=phone.substring(0, phone.length()-1);
				}
			}
			if(phone!=null && phone!=""){
				String[] phones=phone.split("\\,");
				if(phones.length>=1){
					for(String p:phones){
						str1=SmsForHCTLimit(p);
						if(StringUtils.isNotEmpty(str1)){
							s.append(str1);
					}
					}
				}
			}
			params.put("to", phone);
			params.put("content", message);
			params.put("time", "");

			System.out.println(HttpUtilsGB2312.getURL(baseUrl, params));
			String xml = HttpUtilsGB2312.get(baseUrl, params);
			System.out.println(xml);
			String[] responseArray = xml.split("/");

			if (StringUtils.equalsIgnoreCase(responseArray[0],"000") && StringUtils.isEmpty(s.toString())) {
//				buildResult(null);
			}else if(StringUtils.equalsIgnoreCase(responseArray[0],"000") && StringUtils.isNotEmpty(s.toString())){
				resultMessage=s.toString();
			} else{
				resultMessage =  getFailMessageForWinic(responseArray[0]);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = e.getMessage();
		}

		return resultMessage;
	}

	 public static String SmsForHCTLimit(String phoneNum){
			String message="";
			if(!SmsForHCTMap.containsKey(phoneNum)){
				Date startTime=new Date();
				PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
				SmsForHCTMap.put(phoneNum, platformLimit);
			}else{
				int i=SmsForHCTMap.get(phoneNum).getCount();
				Date startTime=SmsForHCTMap.get(phoneNum).getStartTime();
				if((new Date().getTime()-startTime.getTime())/(1000*60)>=60){
				    startTime=new Date();
					PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime,1);
					SmsForHCTMap.put(phoneNum, platformLimit);
				}else{
					if(i>=10){
						message="???????????????1?????????????????????10????????????"+phoneNum+"????????????????????????????????????????????????????????????????????????????????????????????????";
					}else{
						PlatformLimit platformLimit=SmsForHCTMap.get(phoneNum);
						int count=platformLimit.getCount();
						platformLimit.setCount(++count);
						SmsForHCTMap.put(phoneNum, platformLimit);
					}
				}
			}
			return message;
			
		}
	@Override
	public String channelonesensitive() {
		// TODO Auto-generated method stub
		String ads=smsDao.selectchannelonesensitive();
		return ads;
	}
	@Override
	public String updateAds(String advertise) {
		// TODO Auto-generated method stub
		String msg = smsDao.updateAds(advertise);
		return msg;
	}
	@Override
	public ResponseMessage processGroupWSReq(RequestMessage msg) {
		// TODO Auto-generated method stub
		log.info("????????????processGroupWSReq??????,????????????????????????");
		SendMsgReq req = (SendMsgReq) msg.getData();
		String[] tels=null;
		RequestMessage newsms=new RequestMessage();
		SendMsgResp data = null;
		String errorMsg=null;
		StringBuffer errorPhoneNumber=new StringBuffer();
		String errorPhoneNumbers="";
		String tell=req.getTel();
		if(StringUtils.isNotEmpty(tell)){
			tels=tell.split("\\,");
			ResponseMessage[] responseMessages=new ResponseMessage[tels.length];
			for(int i=0;i<tels.length;i++){
				newsms.setService("sending");
				newsms.setFunctionName(msg.getFunctionName());
				SendMsgReq data1 = (SendMsgReq) msg.getData();
				data1.setTel(tels[i]);
				newsms.setData(data1);
				responseMessages[i]=processWSReq(newsms);
				if(responseMessages[i].getData().getStatus()!=200){
					errorPhoneNumber.append(data1.getTel());
					errorPhoneNumber.append(",");
					errorMsg=responseMessages[i].getData().getErrorMsg();
				}
			}	
		}else{
			data = new SendMsgResp();
			data.setTel(tell);
			data.setPlatformId(req.getPlatformId());
			data.setStatusAndMsg(ErrorCode.EMPTY_CELLPHONE);
			return createResponse(msg, data);
		}
		if(errorPhoneNumber.length()>0){
			errorPhoneNumbers=errorPhoneNumber.toString().substring(0, errorPhoneNumber.toString().length()-1);
		}
		
		data = new SendMsgResp();
		data.setTel(errorPhoneNumbers);
		data.setPlatformId(req.getPlatformId());
		if(StringUtils.isNotEmpty(errorMsg)){
			data.setStat("420");
			data.setErrorMsg(errorMsg);
		}else{
			data.setStatusAndMsg(ErrorCode.SUCCESS);
		}
		return  createResponse(msg, data);
	}
	@Override
	public NexmoResponse findByMessageId(String messageId) {
		// TODO Auto-generated method stub
		NexmoResponse nexmoResponse=smsDao.findByMessageId(messageId);
		return nexmoResponse;
	}
	@Override
	public void saveOrUpdate(NexmoResponse nexmoResponse1) {
		// TODO Auto-generated method stub
		log.info("??????????????????NexmoResponse??????");
		smsDao.saveOrUpdate(nexmoResponse1);
	}
	@Override
	public void update(NexmoResponse nexmoResponse1) {
		// TODO Auto-generated method stub
		log.info("??????NexmoResponse??????");
		smsDao.update(nexmoResponse1);
	}
	@Override
	public void save(NexmoResponse nexmoResponse1) {
		// TODO Auto-generated method stub
		log.info("??????NexmoResponse??????");
		smsDao.save(nexmoResponse1);
	}
	@Override
	public List<SmsMessages> getSmsMessage(String product,String smsprovider, String string2, String string3) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			Date datestart = format.parse(string2); 
			Date dateEnd = format.parse(string3);
			return smsDao.getSmsMessage(product,smsprovider,datestart,dateEnd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	@Override
	public Page checkNexmoMessage(String phoneNum, Date start1, Date end1, Integer pageIndex, Integer size1,String status1) {
		// TODO Auto-generated method stub
		
		return smsDao.checkNexmoMessage(phoneNum,start1,end1,pageIndex,size1,status1);
	}
	@Override
	public Integer queryNumberOfMessage(String accountId, String type, String projectname,  Date start,
			Date end) {
		// TODO Auto-generated method stub
		//accountId:???????????????  type???????????????  projectname???????????????  channel?????????  start???????????????  end????????????
		return smsDao.queryNumberOfMessage(accountId,type,projectname,start,end);
	}
	@Override
	public Integer querySumNumberOfMessage(String projectname, Integer channel, Date start, Date end) {
		// TODO Auto-generated method stub
		return smsDao.querySumNumberOfMessage(projectname,channel,start,end);
	}




}
