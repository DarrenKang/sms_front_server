package ph.sinonet.vg.live.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ph.sinonet.vg.live.model.Constants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@Scope("prototype")
public class SubActionSupport extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public String getIp() {
		String forwaredFor = getRequest().getHeader("X-Forwarded-For");
		String remoteAddr = getRequest().getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)) {
			String[] ipArray = forwaredFor.split(",");
			return ipArray[0];
		} else {
			return remoteAddr;
		}
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}

	public ServletContext getServltContext() {
		return ServletActionContext.getServletContext();
	}
	
	public void responseWriter(String content) {
		try {
			getResponse().setContentType("text/plain;charset=" + Constants.ENCODING + "");
			PrintWriter writer = getResponse().getWriter();
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
