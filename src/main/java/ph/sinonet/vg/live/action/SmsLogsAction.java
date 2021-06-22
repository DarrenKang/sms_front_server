package ph.sinonet.vg.live.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ph.sinonet.vg.live.model.Constants;
import ph.sinonet.vg.live.model.sms.SmsAccount;
import ph.sinonet.vg.live.model.sms.SmsConfigLog;
import ph.sinonet.vg.live.service.interfaces.SmsConfigLogService;
import ph.sinonet.vg.live.utils.Page;
@Controller
@Scope("prototype")
public class SmsLogsAction extends SubActionSupport{
	@Autowired
	private SmsConfigLogService smsConfigLogService;
	public String viewSMSLog(){
		return INPUT;
	}
	public String querySMSLogs(){
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
		Page page=smsConfigLogService.querySMSLogs(name,projectname,start1,end1,pageIndex,size1);
		getRequest().setAttribute("messages", (List<SmsConfigLog>)page.getPageContents());
		getRequest().setAttribute("page", page.getJsPageCode());
		return INPUT;
	}
	private String name;
	private String projectname;
	private String start;
	private String end;
	private Integer pageIndex;
	private Integer size1;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
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
	public Integer getSize1() {
		return size1;
	}
	public void setSize1(Integer size1) {
		this.size1 = size1;
	}
	
	
}
