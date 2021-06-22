package ph.sinonet.vg.live.utils;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by karl on 10/4/16.
 */
public class HttpUtilsOpenApi {

    public HttpUtilsOpenApi(){}
    public static String post(String urlPost, String param, String method, String encoding) throws Exception {
        System.out.println("url post:"+urlPost);
        System.out.println("url params:"+ param);

        URL url = new URL(urlPost);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setRequestMethod(method);
        http.setUseCaches(false);
        http.setConnectTimeout(10000);
        http.setReadTimeout(10000);
        http.setInstanceFollowRedirects(true);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(http.getOutputStream(), encoding));
        out.write(param);
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), encoding));
        String line = null;
        StringBuilder sb = new StringBuilder();

        while((line = in.readLine()) != null) {
            sb.append(line);
        }

        in.close();
        http.disconnect();
        return sb.toString();
    }

    public static String UrlEncode(String sName, String encodeing) {
        String sValue = "";
        if(sName == null) {
            return sValue;
        } else {
            try {
                if(encodeing == null) {
                    sValue = URLEncoder.encode(sName, "UTF-8");
                } else {
                    sValue = URLEncoder.encode(sName, encodeing);
                }
            } catch (UnsupportedEncodingException var4) {
                ;
            }

            return sValue;
        }
    }

    public static List<String> parseReply(String xml) {
        StringReader read = new StringReader(xml);
        InputSource source = new InputSource(read);
        SAXBuilder builder = new SAXBuilder();

        List<String> stringList = new ArrayList<String>();
        try {
            ArrayList listReply = new ArrayList();
            Document doc = builder.build(source);
            Element root = doc.getRootElement();
            if(root != null) {
                Attribute result = root.getAttribute("result");
                if(result != null && parseInt(result.getValue(), -102) > 0) {
                    List listItem = root.getChildren("Item");
                    Iterator var10 = listItem.iterator();

//                    while(var10.hasNext()) {
//                        Element item = (Element)var10.next();
//                        stringList.add(item.getAttributeValue("id",0));
//                        t.setId(parseInt(item.getAttributeValue("id"), 0));
//                        t.setMsgId(parseLong(item.getAttributeValue("msgid"), 0L));
//                        long lTime = parseLong(item.getAttributeValue("time"), 0L);
//                        if(lTime > 0L) {
//                            t.setReplyTime(new Date(lTime));
//                        }
//
//                        t.setMobile(item.getAttributeValue("mobile"));
//                        t.setContent(item.getAttributeValue("content"));
//                        listReply.add(t);
//                    }
                }
            }

            return listReply;
        } catch (JDOMException var14) {
            ;
        } catch (IOException var15) {
            ;
        }

        return null;
    }

    public static int parseInt(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (Exception var3) {
            return def;
        }
    }
}
