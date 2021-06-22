package ph.sinonet.vg.live.exception;

/**
 * 
 * @author Win
 * 
 * @version 2011-4-9 下午04:37:10
 * 
 */
public class PostFailedException extends GenericException {

	public PostFailedException() {
		super("Post failed,please check the network connection");
	}

	public PostFailedException(String error) {
		super(error);
	}
}
