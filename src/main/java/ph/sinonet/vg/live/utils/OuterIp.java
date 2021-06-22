package ph.sinonet.vg.live.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class OuterIp {
	public static String getOuterIp() throws IOException {  
        InputStream inputStream = null;  
        try {  
            URL url = new URL("http://www.myip.cn/");  
            URLConnection urlconnnection = url.openConnection();  
            inputStream = urlconnnection.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GB2312");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
            StringBuffer webContent = new StringBuffer();  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                webContent.append(str);  
            }  
            int ipStart = webContent.indexOf("[") + 1;  
            int ipEnd = webContent.indexOf("]");  
            return webContent.substring(ipStart, ipEnd);  
        } finally {  
            if (inputStream != null) {  
                inputStream.close();  
            }  
        }  
    }
}
