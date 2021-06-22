package ph.sinonet.vg.live.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karl on 10/4/16.
 */
public class OpenApi {
//    private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
//    private static String sAccount = "";
//    private static String sAuthKey = "";
    private static int nCgid = 7254;
    private static int nCsid = 0;
   private  static HashMap<String,PlatformLimit> openApiMap=new HashMap<String,PlatformLimit>();
    public OpenApi() {
    }

    public static void initialzeAccount(String url, String account, String authkey, int cgid, int csid) {
//        sOpenUrl = url;
//        sAccount = account;
//        sAuthKey = authkey;
        nCgid = cgid;
        nCsid = csid;
    }
    public static String SmsOpenPlatformLimit(String phoneNum){
		String message="";
		if(!openApiMap.containsKey(phoneNum)){
			Date startTime=new Date();
			PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime, 1);
			openApiMap.put(phoneNum, platformLimit);
		}else{
			int i=openApiMap.get(phoneNum).getCount();
			Date startTime=openApiMap.get(phoneNum).getStartTime();
			if((new Date().getTime()-startTime.getTime())/(1000*60)>=60){
			    startTime=new Date();
				PlatformLimit platformLimit=new PlatformLimit(phoneNum,startTime,1);
				openApiMap.put(phoneNum, platformLimit);
			}else{
				if(i>=3){
					message="同一个号码一个小时只能发送3条信息！"+phoneNum+"目前已经超出发送数量，该短信被运营商拦截，请过一段时间再次发送";
				}else{
					PlatformLimit platformLimit=openApiMap.get(phoneNum);
					int count=platformLimit.getCount();
					platformLimit.setCount(++count);
					openApiMap.put(phoneNum, platformLimit);
				}
			}
		}
		return message;
		
	}
    public static String querySendOnce(String account, String authKey, String baseUrl,String mobile, String content, int cgid, int csid, String time) throws Exception {
    	 StringBuffer sbu=new StringBuffer();
    	 StringBuffer s=new StringBuffer();
         if(mobile!=null && mobile!=""){
      	   String[] mobiles=mobile.split("\\,");
      	   if(mobiles.length>=1){
      		   for(String p:mobiles){
      			   s.append(SmsOpenPlatformLimit(p));
      			   sbu.append((p.startsWith("86"))?p.substring(2):p);
      			   sbu.append(",");
      		   }
      		   mobile=sbu.toString();
      		   mobile=mobile.substring(0,mobile.length()-1);
      	   }
         }
         if(mobile!=null && mobile!=""){
      	   String[] mobiles=mobile.split("\\,");
      	   if(mobiles.length>=1){
      		   for(String p:mobiles){
      			   s.append(SmsOpenPlatformLimit(p));
      		   }
      	   }
         }
    	StringBuilder sb = new StringBuilder();
        sb.append("action=sendOnce&ac=");
        sb.append(account);
        sb.append("&authkey=");
        sb.append(authKey);
        sb.append("&m=");
        sb.append(mobile);
        sb.append("&c=");
        sb.append(HttpUtilsOpenApi.UrlEncode(content, (String)null));
        if(cgid > 0 || nCgid > 0) {
            sb.append("&cgid=");
            sb.append(cgid > 0?cgid:nCgid);
        }

        if(csid > 0 || nCsid > 0) {
            sb.append("&csid=");
            sb.append(csid > 0?csid:nCsid);
        }

        if(time != null) {
            sb.append("&t=");
            sb.append(time);
        }
        System.out.println(sb.toString());
        return HttpUtilsOpenApi.post(baseUrl, sb.toString(), "POST", "UTF-8")+","+s.toString();
    }

    public static String querySendBatch(String account, String authKey, String baseUrl,String mobile, String content, int cgid, int csid, String time) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("action=sendBatch&ac=");
        sb.append(account);
        sb.append("&authkey=");
        sb.append(authKey);
        sb.append("&m=");
        sb.append(mobile);
        sb.append("&c=");
        sb.append(HttpUtilsOpenApi.UrlEncode(content, (String)null));
        if(cgid > 0 || nCgid > 0) {
            sb.append("&cgid=");
            sb.append(cgid > 0?cgid:nCgid);
        }

        if(csid > 0 || nCsid > 0) {
            sb.append("&csid=");
            sb.append(csid > 0?csid:nCsid);
        }

        if(time != null) {
            sb.append("&t=");
            sb.append(time);
        }

        return HttpUtilsOpenApi.post(baseUrl, sb.toString(), "POST", "UTF-8");
    }

    public static String querySendParam(String account, String authKey, String baseUrl,String mobile, String content, String[] paramArray, int cgid, int csid, String time) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("action=sendParam&ac=");
        sb.append(account);
        sb.append("&authkey=");
        sb.append(authKey);
        sb.append("&m=");
        sb.append(mobile);
        sb.append("&c=");
        sb.append(HttpUtilsOpenApi.UrlEncode(content, (String)null));
        int nCount = Math.min(paramArray.length, 10);

        for(int i = 0; i < nCount; ++i) {
            if(paramArray[i] != null) {
                sb.append("&p");
                sb.append(i + 1);
                sb.append("=");
                sb.append(HttpUtilsOpenApi.UrlEncode(paramArray[i], (String)null));
            }
        }

        if(cgid > 0 || nCgid > 0) {
            sb.append("&cgid=");
            sb.append(cgid > 0?cgid:nCgid);
        }

        if(csid > 0 || nCsid > 0) {
            sb.append("&csid=");
            sb.append(csid > 0?csid:nCsid);
        }

        if(time != null) {
            sb.append("&t=");
            sb.append(time);
        }

        return HttpUtilsOpenApi.post(baseUrl, sb.toString(), "POST", "UTF-8");
    }

    public static String queryBalance(String account, String authKey, String baseUrl) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("action=getBalance&ac=");
        sb.append(account);
        sb.append("&authkey=");
        sb.append(authKey);
        return HttpUtilsOpenApi.post(baseUrl, sb.toString(), "GET", "UTF-8");
    }

    public static String postUpdateKey(String account, String authKey, String baseUrl) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("action=updateKey&ac=");
        sb.append(account);
        sb.append("&authkey=");
        sb.append(authKey);
        return HttpUtilsOpenApi.post(baseUrl, sb.toString(), "GET", "UTF-8");
    }




}
