package ph.sinonet.vg.live.exception;

/**
 * 
 * @author Win
 * 
 * @version 2011-4-9 下午04:37:21
 * 
 */
public class ResponseFailedException extends GenericException {

	public ResponseFailedException() {
		super("fail to get the http response,it's sucessful to send http request");
	}

	public ResponseFailedException(String error) {
		super("fail to get the http response,it's sucessful to send http request;" + error);
	}
}
