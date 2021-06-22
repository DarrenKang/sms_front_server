package ph.sinonet.vg.live.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import ph.sinonet.vg.live.model.Constants;
import ph.sinonet.vg.live.model.bisystem.SmsMessages;
import ph.sinonet.vg.live.model.enums.ProjectType;
import ph.sinonet.vg.live.model.enums.SmsConfigCode;
import ph.sinonet.vg.live.model.sms.NexmoResponse;
import ph.sinonet.vg.live.model.sms.SmsAccount;
import ph.sinonet.vg.live.service.interfaces.SmsService;
import ph.sinonet.vg.live.utils.Page;
import ph.sinonet.vg.live.model.sms.SmsConfig;
import ph.sinonet.vg.live.model.sms.SmsConfigLog;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 
 * @author cha
 * 2011-10-24  15:11:34
 *
 */

@Controller
@Scope("prototype")
public class SmsConfigAction extends SubActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(SmsConfigAction.class);

	private static final String KEY = "bce268630ebd27ba87129df56d99017c";

	private static final String CHANNEL2 = "channel2-";
	private static final String BOTH = "both-";
	private static final String SENSITIVEONE="channelonesensitive.txt";
	@Autowired
	private SmsService smsService;

	@Resource(name = "props")
	private Properties prop;

	public String addAccount() {

		if(!password1.equals(password2)){
			getRequest().setAttribute("errormsg", "password not match");
			return INPUT;
		}

		SmsAccount smsAccount = smsService.getSmsAccount(smsUserId, password1);
		System.out.println("sms? "+smsUserId+" "+smsPassword);
		if(smsAccount != null){
			getRequest().setAttribute("errormsg", "account already exist");
		} else {
			smsService.addSmsAccount(smsUserId, password1, platformIds);
			getRequest().setAttribute("errormsg", "Successfully added new account");
			return SUCCESS;
		}
		return INPUT;
	}

	public String modifyPassword() {

		SmsAccount smsAccount = (SmsAccount) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		System.out.println(smsAccount.toString());
		System.out.println(oldpassword+" "+password3+" "+password4);
		if(smsAccount == null){
			getRequest().setAttribute("errormsg", "please login first");
			return INPUT;
		}


		if(!oldpassword.equals(smsAccount.getPassword())){
			getRequest().setAttribute("errormsg", "old password not match");
			return INPUT;
		}

		if(!password3.equals(password4)){
			getRequest().setAttribute("errormsg", "password not match");
			return INPUT;
		}
		if(smsAccount != null){
			smsService.modifyAccount(smsAccount.getLoginname(), password3);
			getRequest().setAttribute("errormsg", "modify password success");
			return SUCCESS;
		}

		return INPUT;
	}


	public String login() {
		String msg = smsService.login(smsUserId, smsPassword);
		System.out.println("sms? "+smsUserId+" "+smsPassword);
		SmsAccount smsAccount=smsService.getSmsAccount(smsUserId, smsPassword);
		if("LOGINSUCCESS".equals(msg) & smsAccount!=null){
			System.out.println("sessionTrue "+smsAccount.toString());
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, smsAccount);

			System.out.println(getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID));
			return SUCCESS;
		}

		getRequest().setAttribute("errormsg", "invalid username/password");
		return INPUT;
	}

	public String logout() {
		String msg=smsService.logout();
		System.out.println(msg);
		Object obj = getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (obj != null) {
			getHttpSession().removeAttribute(Constants.SESSION_CUSTOMERID);
		}

		return SUCCESS;
	}


	/**
	 * add sms config
	 */
	public void validateAddSmsConfig(){
		if (StringUtils.isEmpty(name))
			addFieldError("name", "Empty Name");
		if (StringUtils.isEmpty(server))
			addFieldError("server", "Empty Server");
		if (StringUtils.isEmpty(user))
			addFieldError("user", "Empty User");
		if (StringUtils.isEmpty(password))
			addFieldError("password", "Empty Password");
	}
	
	public String addSmsConfig(){
		try {
			String msg = smsService.addSmsConfig(type, user, server, remark, port, platformIds, password, name, flag, channel, priority,totalCounts);
			getRequest().setAttribute("errormsg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	
	/**
	 * delete sms config
	 */
	public String deleteSmsConfig(){
		try{
			String msg = smsService.deleteSmsConfig(name, type);
			
			getRequest().setAttribute("errormsg", msg);
		}catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	
	/**
	 * update sms config
	 */
	public String updateSmsConfig(){
		try {

			
			String msg = smsService.updateSmsConfig(type, user, server, remark, port, platformIds, password, name, flag, channel,priority,totalCounts);
			getRequest().setAttribute("errormsg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	
	/**
	 * query sms config by project/product
	 */
	public String querySmsConfigByProject(){
		System.out.println("querySmsConfigByProject");
		String orderRule=null;
		try {
			//List<SmsConfig> smsConfigList=smsService.querySmsConfigByProject(name, type, flag, projectname);
			//for(SmsConfig s:smsConfigList){
				//s.setPassword("");
			//}
			//getRequest().setAttribute("smsConfigList", smsConfigList);
			if(StringUtils.isEmpty(projectname)){
				SmsAccount smsAccount=(SmsAccount)getRequest().getSession().getAttribute(Constants.SESSION_CUSTOMERID);
				if(smsAccount!=null){
				projectname=smsAccount.getPlatformIds();
				if("999".equals(projectname)){
					projectname=null;
				}
			}
			}
			if(StringUtils.isNotEmpty(sortField)){
				Map application=(Map)ActionContext.getContext().getApplication();
				if(application!=null){
				if(application.get(sortField)!=null){
					String sortField1=(String)application.get(sortField);
					if("asc".equals(sortField1)){
						 orderRule="desc";
					}else{
						orderRule="asc";
					}
				}else{
					orderRule="asc";
				}
				
				application.put(sortField, orderRule);
			}
			}
			getRequest().setAttribute("smsConfigList", smsService.querySmsConfigByProject(name, type, flag, projectname,channel,sortField,orderRule));
		}catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	public String querychannelonesensitive() {
		try {
			log.info("开始查询敏感词");
			String ads=smsService.channelonesensitive();
			getRequest().setAttribute("ads",ads);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("查询敏感词出错了，出错信息是:"+e.getMessage());
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String updateAds(){
		try {
			log.info("接收到的广告词汇为："+advertise);
			String msg = smsService.updateAds(advertise);
			getRequest().setAttribute("errormsg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	/**
	 * enable sms config
	 */
	public String enableConfig(){
		try{
			String msg = smsService.enableConfig(name);
			
			getRequest().setAttribute("errormsg", msg);
		}catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	/**
	 * 将失效的配置生效
	 * @return
	 */
	public String cancelDisableSmsConfig(){
		try{
			String msg=smsService.cancelDisableSmsConfig(name);
			getRequest().setAttribute("errormsg", msg);
		}catch(Exception e){
			e.printStackTrace();
			String errorContent=Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	/**
	 * disable sms config
	 */
	public String disableConfig(){
		try{
			String msg = smsService.disableConfig(name);
			
			getRequest().setAttribute("errormsg", msg);
		}catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}


	public static void main(String[] args) {
		String numbers = "1077";
//		numbers = numbers.substring(0, firstEndIndex)+ "*****" + numbers.substring((numbers.length()-2), numbers.length());

//		System.out.println(numbers);
		numbers = "12345678910";
		int firstEndIndex = numbers.length() == 11 ? 4 : 6;
		if(numbers!= null && !numbers.isEmpty() && numbers.length() >= 11){
			numbers = numbers.substring(0, firstEndIndex)+ "*****" + numbers.substring((numbers.length()-2), numbers.length());

		}

		System.out.println(numbers);


	}

	private String hideNumbers(String numbers){
		if(numbers!= null && !numbers.isEmpty() && numbers.length() >= 11){
			int firstEndIndex = numbers.length() == 11 ? 4 : 6;
			numbers = numbers.substring(0, firstEndIndex)+ "*****" + numbers.substring((numbers.length()-2), numbers.length());
			return numbers;

		}

		return numbers;
	}

	private String removePrefixBothOrChannel2(String belongProject){
		if(belongProject.contains(BOTH))
			belongProject = belongProject.replace(BOTH,"");
		else if (belongProject.contains(CHANNEL2))
			belongProject = belongProject.replace(CHANNEL2,"");

		return belongProject;
	}


	public String viewSMS(){
		getRequest().setAttribute("smsProvider", SmsConfigCode.getActiveSmsApi());
		getRequest().setAttribute("products", ProjectType.getActiveProducts());
		return INPUT;
	}

	public String querySentSMS(){
		if(StringUtils.isEmpty(projectname)){
			SmsAccount smsAccount=(SmsAccount)getRequest().getSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if(smsAccount!=null){
			projectname=smsAccount.getPlatformIds();
			if("999".equals(projectname)){
				projectname=null;
			}
		}
		}
		System.out.println(start);
		System.out.println(end);
		SimpleDateFormat sfend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date start1=null;
		Date end1=null;
		if(StringUtils.isNotEmpty(start)){
			try {
				start1 = sfend.parse(start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("开始时间解析异常");
			}
		}
		if(StringUtils.isNotEmpty(end)){
			try {
				end1 = sfend.parse(end);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("结束时间解析异常");
			}
		}
		Page page = smsService.querySmsMessages(projectname, smsprovider, start1, end1, pageIndex, size1,accountId,phonenumber);
		List<ph.sinonet.vg.live.model.sms.SmsMessages> list = (List<ph.sinonet.vg.live.model.sms.SmsMessages>)page.getPageContents();
			for(ph.sinonet.vg.live.model.sms.SmsMessages msg : list){
				msg.setPhonenumber(hideNumbers(msg.getPhonenumber()));
				msg.setSmsLength(msg.getContent().length());
			}
		getRequest().setAttribute("messages", (List<ph.sinonet.vg.live.model.sms.SmsMessages>)page.getPageContents());
		getRequest().setAttribute("page", page.getJsPageCode());
		return viewSMS();
	}
	public String notParticipateAutoSwitch(){
		try {
			String msg=smsService.notParticipateAutoSwitch(name);
			getRequest().setAttribute("errormsg", msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String errorContents=Constants.getExceptionContent(e);
			log.error(errorContents);
		}
		return INPUT;
	}
	public String queryNexmoMessage(){
		System.out.println(start);
		System.out.println(end);
		SimpleDateFormat sfend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date start1=null;
		Date end1=null;
		if(StringUtils.isNotEmpty(start)){
			try {
				start1 = sfend.parse(start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotEmpty(end)){
			try {
				end1 = sfend.parse(end);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Page page=smsService.checkNexmoMessage(phoneNum,start1,end1,pageIndex,size1,status1);
		List<ph.sinonet.vg.live.model.sms.NexmoResponse> list = (List<ph.sinonet.vg.live.model.sms.NexmoResponse>)page.getPageContents();
		for(ph.sinonet.vg.live.model.sms.NexmoResponse msg : list){
			msg.setMsisdn(hideNumbers(msg.getMsisdn()));
			msg.setErr_code(setErrorCode(msg.getErr_code()));
		}
		getRequest().setAttribute("messages", (List<NexmoResponse>)page.getPageContents());
		getRequest().setAttribute("page", page.getJsPageCode());
		return INPUT;
	}
	public String checkNexmoMessage(){
		return INPUT;
	}
	public String setErrorCode(String errcode){
		if("0".equals(errcode)){
			return "发送成功";
		}else if("1".equals(errcode)){
			return "请求当中的to可能不合法";
		}else if("2".equals(errcode)){
			return "暂时发送失败，请稍后再试";
		}else if("3".equals(errcode)){
			return "to不再有效了";
		}else if("4".equals(errcode)){
			return "携号转网，请用户联系网络运营商4";
		}else if("5".equals(errcode)){
			return "携号转网，请用户联系网络运营商5";
		}else if("6".equals(errcode)){
			return "消息可能被不正当阻挡，请给平台发nex邮件";
		}else if("7".equals(errcode)){
			return "手机占线";
		}else if("8".equals(errcode)){
			return "网络错误";
		}else if("9".equals(errcode)){
			return "资金不足";
		}else if("10".equals(errcode)){
			return "请求参数不对";
		}else if("11".equals(errcode)){
			return "请给平台nex发邮件";
		}else if("12".equals(errcode)){
			return "查看发送的手机号码是否合法";
		}else if("13".equals(errcode)){
			return "内容不合法，被阻止";
		}else if("14".equals(errcode)){
			return "接收人可能账户被暂停";
		}else if("15".equals(errcode)){
			return "用户不希望接收消息";
		}else if("99".equals(errcode)){
			return "所选路由发生错误，请给nex发送邮件";
		}
		return "未知错误";
	}
	public String checkNumberOfMessage(){
		return INPUT;
	}
	public String queryNumberOfMessage(){
		System.out.println(start);
		System.out.println(end);
		SimpleDateFormat sfend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date start1=null;
		Date end1=null;
		if(StringUtils.isNotEmpty(start)){
			try {
				start1 = sfend.parse(start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("开始时间解析异常");
			}
		}
		if(StringUtils.isNotEmpty(end)){
			try {
				end1 = sfend.parse(end);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("结束时间解析异常");
			}
		}
		Integer numberOfMessage = smsService.queryNumberOfMessage(accountId, type, projectname,  start1, end1);
		getRequest().setAttribute("numberOfMessage", numberOfMessage);
		return INPUT;
	}
	public String querySumNumberOfMessage(){
		SimpleDateFormat sfend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date start1=null;
		Date end1=null;
		if(StringUtils.isNotEmpty(start)){
			try {
				start1 = sfend.parse(start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("开始时间解析异常");
			}
		}
		if(StringUtils.isNotEmpty(end)){
			try {
				end1 = sfend.parse(end);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("结束时间解析异常");
			}
		}
		Integer sumNumberOfMessage = smsService.querySumNumberOfMessage( projectname, channel, start1, end1);
		getRequest().setAttribute("sumNumberOfMessage", sumNumberOfMessage);
		return INPUT;
	}
	
	private String phonenumber;
	private String status1;
	private String phoneNum;
	private String errormsg;
	private String name;
	private String code;
	private Integer flag;
	private String smsServer;
	private Integer smsPort;
	private String smsUserId;
	private String smsPassword;
	private String[] belongProject;
	private String projectname;
	private String type;
	private Integer channelType;
	private String phoneNo;
	private String msg;

	//private Date start;
	//private Date end;
	private String start;
	private String end;
	private Integer pageIndex;
	private Integer size1;
	private String smsprovider;

	private String product;

	private Integer channel;
	private String priority;
	private String totalCounts;
	private String areadyCounts;
	private String password3;
	private String password4;
	private String sortField;
	String user;
	String server;
	String remark;
	Integer port;
	String[] platformIds;
	String password;
	String oldpassword;
	String password1;
	String password2;
	String priority1;
	private String advertise;
	private String accountId;
	
	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getStatus1() {
		return status1;
	}

	public void setStatus1(String status1) {
		this.status1 = status1;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getPlatformIds() {
		return platformIds;
	}

	public void setPlatformIds(String[] platformIds) {
		this.platformIds = platformIds;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
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

	public String getSmsUserId() {
		return smsUserId;
	}

	public void setSmsUserId(String smsUserId) {
		this.smsUserId = smsUserId;
	}

	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}

	public String[] getBelongProject() {
		return belongProject;
	}

	public void setBelongProject(String[] belongProject) {
		this.belongProject = belongProject;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getChannelType() {
		return channelType;
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	
	public String getSmsprovider() {
		return smsprovider;
	}

	public void setSmsprovider(String smsprovider) {
		this.smsprovider = smsprovider;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPriority1() {
		return priority1;
	}

	public void setPriority1(String priority1) {
		this.priority1 = priority1;
	}

	public String getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(String totalCounts) {
		this.totalCounts = totalCounts;
	}

	public String getAreadyCounts() {
		return areadyCounts;
	}

	public void setAreadyCounts(String areadyCounts) {
		this.areadyCounts = areadyCounts;
	}

	public String getPassword3() {
		return password3;
	}

	public void setPassword3(String password3) {
		this.password3 = password3;
	}

	public String getPassword4() {
		return password4;
	}

	public void setPassword4(String password4) {
		this.password4 = password4;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getAdvertise() {
		return advertise;
	}

	public void setAdvertise(String advertise) {
		this.advertise = advertise;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getSize1() {
		return size1;
	}

	public void setSize1(Integer size1) {
		this.size1 = size1;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	
	
	

	
}
