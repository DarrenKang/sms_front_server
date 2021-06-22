package ph.sinonet.vg.live.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ph.sinonet.vg.live.exception.PostFailedException;
import ph.sinonet.vg.live.exception.ResponseFailedException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jay on 7/26/16.
 */
public class HttpsUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpsUtils.class);

    public static String ENCODE_TYPE = "UTF-8";

    private static HttpClient createHttpClient() {
        Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);

        HttpClient httpclient = new HttpClient();
        HttpClientParams params = new HttpClientParams();
        params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(5,false)); //Increase retry to 5
        params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
        params.setParameter("http.socket.timeout", 5*60 * 1000);
        httpclient.setParams(params);
        return httpclient;
    }


    public static String getURL(String action, Map<String, String> params) {
        String url = "";

        if (action == null)
            return url;

        if ((params != null) && (params.size() > 0)) {
            url = action + "?";
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = params.get(key);
                try {
                    value = URLEncoder.encode(value, ENCODE_TYPE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 如果值为空，则无需传递
                if (StringUtils.isEmpty(value))
                    continue;
                if (url.endsWith("?"))
                    url = url + key + "=" + value;
                else
                    url = url + "&" + key + "=" + value;
            }
        } else {
            url = action;
        }
        return url;
    }

    public static String post(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        int statusCode = -999;
        PostMethod post = new PostMethod(action);
        if (params != null) {
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = params.get(key);
                log.info("key:" + key + " value:" + value);
                post.setParameter(key, value);
            }
        }
        try {
            log.info("POST URL:" + post.getURI());
            statusCode = createHttpClient().executeMethod(post);
        } catch (HttpException e) {
            e.printStackTrace();
            log.info("Exception found : url " + action + " paremeters");
            printParameters(params);
            throw new PostFailedException();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            log.info("Exception found : url " + action + " paremeters");
            printParameters(params);
            throw new ResponseFailedException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new PostFailedException();
        }

        if (statusCode != 200) {
            log.info("Http Status not OK : url " + action + " paremeters");
            printParameters(params);
            log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
            throw new PostFailedException("the post result status is not OK");
        }
        try {
            log.info("post successfully");
            String responseBody = post.getResponseBodyAsString();
            log.info("get response sucessfully,request URL " +  action + " params");
            printParameters(params);
            log.info("get response sucessfully,reponse:" + responseBody);
            post.releaseConnection();
            return responseBody;
        } catch (Exception e) {
            log.info("Exception found : url " + action + " paremeters");
            printParameters(params);
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        }
    }


    public static String postRaw(String action, String body) throws PostFailedException, ResponseFailedException {
        int statusCode = -999;
        PostMethod post = new PostMethod(action);
        try {
            post.setRequestEntity(new StringRequestEntity(body, "text/plain", ENCODE_TYPE));
            post.setRequestHeader("Content-type", "" + "text/plain" + ";charset=" + ENCODE_TYPE + "");
            log.info("POST URL:" + post.getURI());
            log.info("POST raw:" + body);
            statusCode = createHttpClient().executeMethod(post);
        } catch (HttpException e) {
            log.info("Exception " + action + " " + body);
            e.printStackTrace();
            throw new PostFailedException();
        } catch (SocketTimeoutException e) {
            log.info("Exception " + action + " " + body);
            throw new ResponseFailedException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new PostFailedException();
        }
        if (statusCode != 200) {
            log.info("Http Status not OK " + action + " " + body);
            log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
            throw new PostFailedException("the post result status is not OK");
        }
        try {
            log.info("post successfully");
            String responseBody = post.getResponseBodyAsString().trim();
            log.info("get response sucessfully,request: URL " +  action + "; xml " + body);
            log.info("get response sucessfully,reponse:" + responseBody);
            post.releaseConnection();
            return responseBody;
        } catch (Exception e) {
            log.info("Exception " + action + " " + body);
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        }
    }


    private static void printParameters(Map<String, String> params) {
        for (String key : params.keySet()) {
            System.out.println(key + " : " + params.get(key));
        }
    }

}
