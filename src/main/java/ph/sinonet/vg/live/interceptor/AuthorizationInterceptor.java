package ph.sinonet.vg.live.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ph.sinonet.vg.live.utils.SmsUtils;
import ph.sinonet.vg.live.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Scope("prototype")
public class AuthorizationInterceptor extends AbstractInterceptor {
	private Logger logger = Logger.getLogger(AuthorizationInterceptor.class);

	private String getIp(HttpServletRequest req) {
		String forwaredFor = req.getHeader("X-Forwarded-For");
		String remoteAddr = req.getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)) {
			String[] ipArray = forwaredFor.split(",");
			return ipArray[0];
		} else
			return remoteAddr;
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext actionContext = invocation.getInvocationContext();
		HttpServletRequest req = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse res = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
		String key = req.getParameter("key");
		if (StringUtil.isEmpty(key) || !key.equals(SmsUtils.APIKEY)){
			res.getWriter().write("签名错误");
			System.out.println("aaa");
		} else {
			System.out.println("bbb");
			return invocation.invoke();
		}

		return null;
	}
}
