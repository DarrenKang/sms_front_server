package ph.sinonet.vg.live.utils;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
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
 * Created by karl on 9/30/16.
 */
public class HttpUtilsGB2312 extends HttpUtils {
    private static Log LOGGER = LogFactory.getLog(HttpUtilsGB2312.class);
    public static String ENCODE_TYPE = "GB2312";

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

    public static String get(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
        boolean statusCode = true;
        String url = getURL(action, params);
        GetMethod method = new GetMethod(url);
        if(url == null) {
            throw new PostFailedException("URL is null");
        } else {
            int statusCode1;
            try {
                LOGGER.info("TOTAL URL:" + method.getURI());
                statusCode1 = createHttpClient().executeMethod(method);
            } catch (HttpException var7) {
                LOGGER.error(var7.getStackTrace());
                var7.printStackTrace();
                throw new PostFailedException();
            } catch (SocketTimeoutException var8) {
                LOGGER.error(var8.getStackTrace());
                var8.printStackTrace();
                throw new ResponseFailedException(var8.getMessage());
            } catch (IOException var9) {
                LOGGER.error(var9.getStackTrace());
                var9.printStackTrace();
                throw new PostFailedException();
            }

            if(statusCode1 != 200) {
                LOGGER.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode1));
                throw new PostFailedException("the post result status is not OK");
            } else {
                try {
                    LOGGER.info("send successfully");
                    String e = IOUtils.toString(method.getResponseBodyAsStream(), ENCODE_TYPE);
                    LOGGER.info("get response sucessfully,reponse:" + e);
                    method.releaseConnection();
                    return e;
                } catch (Exception var6) {
                    LOGGER.error(var6.getStackTrace());
                    var6.printStackTrace();
                    throw new ResponseFailedException(var6.getMessage());
                }
            }
        }
    }

}
