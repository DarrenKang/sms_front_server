package ph.sinonet.vg.live.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONException;
import ph.sinonet.vg.live.model.sms.NexmoResponse;
import ph.sinonet.vg.live.service.interfaces.SmsService;
@Controller
@Scope("prototype")
public class SmsRecieveAction extends SubActionSupport{
	private static Logger log = Logger.getLogger(SmsRecieveAction.class);
	@Autowired
	private SmsService smsService;
	public void receiveMsgResponse(){
		log.info("开始接收Nexmo的响应");
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			HttpServletResponse response= ServletActionContext.getResponse();
			log.info("请求为"+request);
			String msisdn=request.getParameter("msisdn");
			String to=request.getParameter("to");
			String network_code=request.getParameter("network-code");
			String messageId=request.getParameter("messageId");
			String price=request.getParameter("price");
			String status=request.getParameter("status");
			String scts=request.getParameter("scts");
			String err_code=request.getParameter("err-code");
			String message_timestamp=request.getParameter("message-timestamp");
		    log.info("Nexmo的响应为：msisdn: "+msisdn+"to: "+to+"network-code: "+network_code+"messageId: "+messageId+
		    		"price: "+price+"status: "+status+"scts: "+scts+"err-code: "+err_code+"message-timestamp: "+message_timestamp);
		    NexmoResponse nexmoResponse1=new NexmoResponse();
		    nexmoResponse1.setMsisdn(msisdn);
		    nexmoResponse1.setTo(to);
		    nexmoResponse1.setNetwork_code(network_code);
		    nexmoResponse1.setMessageId(messageId);
		    nexmoResponse1.setPrice(price);
		    nexmoResponse1.setStatus(status);
		    nexmoResponse1.setScts(scts);
		    nexmoResponse1.setErr_code(err_code);
		    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date date=f.parse(message_timestamp);
		    Calendar ca = Calendar.getInstance();
		    ca.setTime(date);
		    ca.add(Calendar.HOUR, 8);
		    date=ca.getTime();
		    //message_timestamp=f.format(date);
		    nexmoResponse1.setMessage_timestamp(date);
//		    NexmoResponse nexmoResponse=smsService.findByMessageId(messageId);
//		    if(nexmoResponse!=null){
//		    	smsService.update(nexmoResponse1);
//		    }else{
//		    	 smsService.save(nexmoResponse1);
//		    }
		    smsService.saveOrUpdate(nexmoResponse1);
		    PrintWriter out= response.getWriter();
		    out.print("ok");
		    out.flush();
		    out.close();
		   // reader.close();
		    log.info("Nexmo的响应为ok,且输出流关闭。");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("Nexmo响应json异常，异常信息为"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("Nexmo响应IO异常，异常信息为"+e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Nexmo响应异常，异常信息为"+e.getMessage());
			e.printStackTrace();
		}
	}
}
