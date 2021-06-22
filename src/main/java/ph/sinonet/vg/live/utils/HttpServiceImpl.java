package ph.sinonet.vg.live.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public class HttpServiceImpl {
	
	public static void main(String[] args) {
//		System.out.println("=========");
//		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
//		jsonResponse.setCode("11");
//		jsonResponse.setMsg("11111111111");
//		//String a = HttpServiceImpl.httpRequest("http://127.0.0.1:8080/netpay/weixinpay.do","GET","out_trade_no=100445660737");
//		//String a = HttpServiceImpl.httpRequest("http://65.52.166.205/payment.php","GET","pInfo=db");
//		EncryptionUtil en = new EncryptionUtil();
//		String value = "111#222#succ#123#test1";
//		String md5 = en.encryptThis(value);
//		
//		Map<String, Object> content = new HashMap<String, Object>();
//		content.put("proposalId", "111");
//		content.put("billNo", "222");
//		content.put("status", "succ");
//		content.put("amount", "123");
//		content.put("username", "test1");
//		content.put("md5", md5);
//		jsonResponse.setContent(content);
//		String pInfo = "pInfo="+JsonUtils.toJson(jsonResponse);
		//String improutStr = HttpServiceImpl.httpRequest("http://65.52.166.205/payment.php?"+pInfo,"GET","","");
		//System.out.println("===========:"+improutStr);
	}
	// 处理http请求 requestUrl为请求地址 requestMethod请求方式，值为"GET"或"POST"
	private static Integer num =0;
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr,String proposalId) {

		StringBuffer buffer = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod(requestMethod);
			conn.connect();
			// 往服务器端写内容 也就是发起http请求需要带的参数
			if (null != outputStr) {
				OutputStream os = conn.getOutputStream();
				os.write(outputStr.getBytes("utf-8"));
				os.close();
			}
			// 读取服务器端返回的内容
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			num=0;
		} catch (Exception e) {
			e.printStackTrace();
			num++;
		}finally {
			System.out.println("proposalId:"+proposalId);
			if(num>0 && num<4){
				System.out.println("订单号:"+proposalId+",尝试弟"+num+"次回调...........");
				httpRequest(requestUrl,requestMethod,outputStr,proposalId);
			}
			System.out.println("proposalId:"+proposalId+";回调结束,总共回调次数:"+num);
		}
		return buffer.toString();
	}

}