package ph.sinonet.vg.live.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONException;

public class JSONUtil {
	private  static final Log log=LogFactory.getLog(JSONUtil.class);
	 public static Object deserialize(Reader reader) throws JSONException {
		          // read content
		 log.info("reader:"+reader);
		          BufferedReader bufferReader = new BufferedReader(reader);
		         String line;
		          StringBuilder buffer = new StringBuilder();
		  
		          try {
		              while ((line = bufferReader.readLine()) != null) {
		            	  log.info("得到的行数据为"+line);
		                  buffer.append(line);
		             }
		         } catch (IOException e) {
		             throw new JSONException(e);
		         }
		          log.info("json数据为"+buffer.toString());
		         return buffer.toString();
		     }
}
