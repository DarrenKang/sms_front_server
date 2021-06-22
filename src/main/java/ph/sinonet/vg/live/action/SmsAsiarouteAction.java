package ph.sinonet.vg.live.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ph.sinonet.vg.live.model.Constants;
import ph.sinonet.vg.live.model.sms.SmsAccount;
import ph.sinonet.vg.live.model.sms.SmsConfigLog;
import ph.sinonet.vg.live.service.interfaces.SmsAsiarouteService;
import ph.sinonet.vg.live.service.interfaces.SmsConfigLogService;
import ph.sinonet.vg.live.service.interfaces.SmsService;
import ph.sinonet.vg.live.utils.Page;
@Controller
@Scope("prototype")
public class SmsAsiarouteAction extends SubActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private SmsAsiarouteService smsAsiarouteService;

	private static Logger log = Logger.getLogger(SmsAsiarouteAction.class);
	public String configuringInformation(){
		return INPUT;
	}
	public String addSmsAsiarouteConfig(){
		log.info("得到的参数是name:"+name+",serverIp:"+serverIp+",username:"+username+",password:"+password+",channel"+channel+",platformIds:"+platformIds+"remark:"+remark);
		try {
			String msg = smsAsiarouteService.addSmsAsiarouteConfig(name, serverIp, username, password,channel,platformIds,remark);
			getRequest().setAttribute("errormsg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	public String querySmsServerConfigByProject(){
		System.out.println("querySmsServerConfigByProject");
		try {
			if(StringUtils.isEmpty(projectname)){
				SmsAccount smsAccount=(SmsAccount)getRequest().getSession().getAttribute(Constants.SESSION_CUSTOMERID);
				if(smsAccount!=null){
				projectname=smsAccount.getPlatformIds();
				if("999".equals(projectname)){
					projectname=null;
				}
			}
			}
			getRequest().setAttribute("smsServerConfigList", smsAsiarouteService.querySmsServerConfigByProject(name, projectname,channel));
		}catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	public String deleteSmsServerConfig(){
		try{
			String msg = smsAsiarouteService.deleteSmsServerConfig(id);
			
			getRequest().setAttribute("errormsg", msg);
		}catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	public String updateSmsServerConfig(){
		try {
			String msg = smsAsiarouteService.updateSmsServerConfig(id,name,serverIp,username,password,channel,platformIds,remark);
			getRequest().setAttribute("errormsg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			String errorContent = Constants.getExceptionContent(e);
			log.error(errorContent);
		}
		return INPUT;
	}
	private String name;
	private String serverIp;
	private String username;
	private String password;
	private Integer channel;
	private String platformIds;
	private String remark;
	private String projectname;
	private Integer id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public String getPlatformIds() {
		return platformIds;
	}
	public void setPlatformIds(String platformIds) {
		this.platformIds = platformIds;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
}
