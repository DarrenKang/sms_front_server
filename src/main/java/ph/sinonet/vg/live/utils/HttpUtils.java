package ph.sinonet.vg.live.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ph.sinonet.vg.live.exception.PostFailedException;
import ph.sinonet.vg.live.exception.ResponseFailedException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by francis on 9/13/16.
 */
public class HttpUtils {
    private static Log log = LogFactory.getLog(HttpUtils.class);
    public static String ENCODE_TYPE = "UTF-8";
    private static Integer TIME_OUT = Integer.valueOf(300000);

    public HttpUtils() {
    }

    public static HttpClient createHttpClient() {
        HttpClient httpclient = new HttpClient();
        HttpClientParams params = new HttpClientParams();
        params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
        params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
        params.setParameter("http.socket.timeout", TIME_OUT);
        params.setParameter("http.protocol.allow-circular-redirects", true);
        httpclient.setParams(params);
        return httpclient;
    }

    public static HttpClient createHttpClientNoTimeout() {
        HttpClient httpclient = new HttpClient();
        HttpClientParams params = new HttpClientParams();
        params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
        params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
        params.setParameter("http.socket.timeout", 5*60 * 1000);
        httpclient.setParams(params);
        return httpclient;
    }

    public static String get(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException, Exception {
        boolean statusCode = true;
        String url = getURL(action, params);
        System.out.println("url="+url);
        GetMethod method = new GetMethod(url);
        System.out.println("method="+method);
        if(url == null) {
            throw new PostFailedException("URL is null");
        } else {
            int statusCode1;
            try {
                log.info("TOTAL URL:" + method.getURI());
                statusCode1 = createHttpClient().executeMethod(method);
                System.out.println("statusCode1 ="+statusCode1);
            } catch (HttpException var7) {
                statusCode1 = 503;
                log.info(var7.getStackTrace());
                var7.printStackTrace();
                throw new Exception("HttpException: "+var7.getMessage());
            } catch (SocketTimeoutException var8) {
                log.info(var8.getStackTrace());
                var8.printStackTrace();
                throw new ResponseFailedException(var8.getMessage());
            } catch (IOException var9) {
                log.info(var9.getStackTrace());
                var9.printStackTrace();
                throw new PostFailedException();
            }

            if(statusCode1 != 200) {
                log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
                throw new PostFailedException("the post result status is not OK");
            } else if(statusCode1 == 503){
                log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
                throw new PostFailedException("Service Unavailable");
            } else {
                try {
                    log.info("send successfully");
                    String e = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
                    log.info("get response sucessfully,reponse:" + e);
                    method.releaseConnection();
                    return e;
                } catch (Exception var6) {
                    log.error(var6.getStackTrace());
                    var6.printStackTrace();
                    throw new ResponseFailedException(var6.getMessage());
                }
            }
        }
    }

    public static String getMethodAllowRedirect(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        boolean statusCode = true;
        String url = getURL(action, params);
        System.out.println("url="+url);
        GetMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
        System.out.println("method="+method);
        if(url == null) {
            throw new PostFailedException("URL is null");
        } else {
            int statusCode1;
            try {
                log.info("TOTAL URL:" + method.getURI());
                statusCode1 = createHttpClient().executeMethod(method);
                System.out.println("statusCode1 ="+statusCode1);
            } catch (HttpException var7) {
                log.error(var7.getStackTrace());
                var7.printStackTrace();
                throw new PostFailedException();
            } catch (SocketTimeoutException var8) {
                log.error(var8.getStackTrace());
                var8.printStackTrace();
                throw new ResponseFailedException(var8.getMessage());
            } catch (IOException var9) {
                log.error(var9.getStackTrace());
                var9.printStackTrace();
                throw new PostFailedException();
            }

            if(statusCode1 != 200) {
                log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
                throw new PostFailedException("the post result status is not OK");
            } else {
                try {
                    log.info("send successfully");
                    String e = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
                    log.info("get response sucessfully,reponse:" + e);
                    method.releaseConnection();
                    return e;
                } catch (Exception var6) {
                    log.error(var6.getStackTrace());
                    var6.printStackTrace();
                    throw new ResponseFailedException(var6.getMessage());
                }
            }
        }
    }

    public static String getWithNoTimeout(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        boolean statusCode = true;
        String url = getURL(action, params);
        GetMethod method = new GetMethod(url);
        if(url == null) {
            throw new PostFailedException("URL is null");
        } else {
            int statusCode1;
            try {
                log.info("TOTAL URL:" + method.getURI());
                statusCode1 = createHttpClientNoTimeout().executeMethod(method);
            } catch (HttpException var7) {
                log.error(var7.getStackTrace());
                var7.printStackTrace();
                throw new PostFailedException();
            } catch (SocketTimeoutException var8) {
                log.error(var8.getStackTrace());
                var8.printStackTrace();
                throw new ResponseFailedException(var8.getMessage());
            } catch (IOException var9) {
                log.error(var9.getStackTrace());
                var9.printStackTrace();
                throw new PostFailedException();
            }

            if(statusCode1 != 200) {
                log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
                throw new PostFailedException("the post result status is not OK");
            } else {
                try {
                    log.info("send successfully");
                    String e = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
                    log.info("get response sucessfully,reponse:" + e);
                    method.releaseConnection();
                    return e;
                } catch (Exception var6) {
                    log.error(var6.getStackTrace());
                    var6.printStackTrace();
                    throw new ResponseFailedException(var6.getMessage());
                }
            }
        }
    }
    public static String getWithNoTimeout1(String action, Map<String, String> params) {
    	int statusCode = -999;
    	String url = getURL(action, params);
    	GetMethod method = new GetMethod(url);
    	if (url == null)
    	return null;
    	try {
    	statusCode = createHttpClientNoTimeout().executeMethod(method);
    	} catch (HttpException e) {
    	return null;
    	} catch (SocketTimeoutException e) {
    	return null;
    	} catch (IOException e) {
    	return null;
    	}
    	if (statusCode != 200) {
    	return null;
    	}
    	try {
    	String responseBody = method.getResponseBodyAsString();
    	return responseBody;
    	} catch (Exception e) {
    	return null;
    	}
    	}

    public static String getURL(String action, Map<String, String> params) {
        String url = "";
        if(action == null) {
            return url;
        } else {
            if(params != null && params.size() > 0) {
                url = action + "?";
                Iterator it = params.keySet().iterator();

                while(it.hasNext()) {
                    String key = (String)it.next();
                    String value = (String)params.get(key);

                    try {
                        value = URLEncoder.encode(value, ENCODE_TYPE);
                    } catch (Exception var7) {
                        log.error(var7.getStackTrace());
                        var7.printStackTrace();
                    }

                    if(!StringUtils.isEmpty(value)) {
                        if(url.endsWith("?")) {
                            url = url + key + "=" + value;
                        } else {
                            url = url + "&" + key + "=" + value;
                        }
                    }
                }
            } else {
                url = action;
            }

            return url;
        }
    }
    public static String getNEXURL(String action, Map<String, String> params) {
        String url = "";
        if(action == null) {
            return url;
        } else {
            if(params != null && params.size() > 0) {
            	url = action;
                Iterator it = params.keySet().iterator();

                while(it.hasNext()) {
                    String key = (String)it.next();
                    String value = (String)params.get(key);
                    if(!StringUtils.isEmpty(value)) {
                        if(url.endsWith("?")) {
                            url = url + key + "=" + value;
                        } else {
                            url = url + "&" + key + "=" + value;
                        }
                    }
                }
            } else {
                url = action;
            }

            return url;
        }
    }
    public static String post(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        boolean statusCode = true;
        PostMethod post = new PostMethod(action);
        if(params != null) {
            Iterator e = params.keySet().iterator();

            while(e.hasNext()) {
                String key = (String)e.next();
                String value = (String)params.get(key);
                post.setParameter(key, value);
            }
        }

        int statusCode1;
        try {
            log.info("POST URL:" + post.getURI());
            statusCode1 = createHttpClient().executeMethod(post);
            log.info("nex执行的状态码是"+statusCode1);
        } catch (HttpException var8) {
            log.error(var8.getStackTrace());
            var8.printStackTrace();
            throw new PostFailedException();
        } catch (SocketTimeoutException var9) {
            log.error(var9.getStackTrace());
            var9.printStackTrace();
            throw new ResponseFailedException(var9.getMessage());
        } catch (IOException var10) {
            log.error(var10.getStackTrace());
            var10.printStackTrace();
            throw new PostFailedException();
        }

        if(statusCode1 != 200) {
            log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
            throw new PostFailedException("the post result status is not OK");
        } else {
            try {
                log.info("post successfully");
                String e1 = IOUtils.toString(post.getResponseBodyAsStream(), "UTF-8");
                log.info("get response sucessfully,reponse:" + e1);
                post.releaseConnection();
                return e1;
            } catch (Exception var7) {
                log.error(var7.getStackTrace());
                var7.printStackTrace();
                throw new ResponseFailedException(var7.getMessage());
            }
        }
    }

    public static String postWithNoCache(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        boolean statusCode = true;
        PostMethod post = new PostMethod(action);
        post.setRequestHeader("Pragma","no-cache");
        post.setRequestHeader("Cache-Control","no-store");
        if(params != null) {
            Iterator e = params.keySet().iterator();

            while(e.hasNext()) {
                String key = (String)e.next();
                String value = (String)params.get(key);
                post.setParameter(key, value);
            }
        }

        int statusCode1;
        try {
            log.info("POST URL:" + post.getURI());
            statusCode1 = createHttpClient().executeMethod(post);
        } catch (HttpException var8) {
            log.error(var8.getStackTrace());
            var8.printStackTrace();
            throw new PostFailedException();
        } catch (SocketTimeoutException var9) {
            log.error(var9.getStackTrace());
            var9.printStackTrace();
            throw new ResponseFailedException(var9.getMessage());
        } catch (IOException var10) {
            log.error(var10.getStackTrace());
            var10.printStackTrace();
            throw new PostFailedException();
        }

        if(statusCode1 != 200) {
            log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
            throw new PostFailedException("the post result status is not OK");
        } else {
            try {
                log.info("post successfully");
                String e1 = IOUtils.toString(post.getResponseBodyAsStream(), "UTF-8");
                log.info("get response sucessfully,reponse:" + e1);
                post.releaseConnection();
                return e1;
            } catch (Exception var7) {
                log.error(var7.getStackTrace());
                var7.printStackTrace();
                throw new ResponseFailedException(var7.getMessage());
            }
        }
    }

    public static String customGetReq(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        int statusCode = -999;
        String url = getURL(action, params);
        GetMethod method = new GetMethod(url);
        method.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        if (url == null)
            throw new PostFailedException("URL is null");
        try {
            log.info("TOTAL URL:" + method.getURI());
            statusCode = createHttpClient().executeMethod(method);
        } catch (HttpException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new PostFailedException();
        } catch (SocketTimeoutException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        } catch (IOException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new PostFailedException();
        }
        if (statusCode != 200) {
            log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
            throw new PostFailedException("the post result status is not OK");
        }
        try {
            log.info("send successfully");
            String responseBody = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            log.info("get response sucessfully,reponse:" + responseBody);
            method.releaseConnection();
            return responseBody;
        } catch (Exception e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        }
    }
    public static String customGetNEXReq(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        int statusCode = -999;
        String url = getNEXURL(action, params);
        log.info("nex的请求url为"+url);
        GetMethod method=null;
        if (url == null)
            throw new PostFailedException("URL is null");
        try {
        	  method = new GetMethod(url);
             method.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            log.info("TOTAL URL:" + method.getURI());
            statusCode = createHttpClient().executeMethod(method);
        } catch (HttpException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new PostFailedException();
        } catch (SocketTimeoutException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        } catch (IOException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new PostFailedException();
        }
        if (statusCode != 200) {
            log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
            throw new PostFailedException("the post result status is not OK");
        }
        try {
            log.info("send successfully");
            String responseBody = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            log.info("get response sucessfully,reponse:" + responseBody);
            method.releaseConnection();
            return responseBody;
        } catch (Exception e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        }
    }
}
