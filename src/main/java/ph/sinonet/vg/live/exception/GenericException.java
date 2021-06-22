package ph.sinonet.vg.live.exception;


/**
 * 
 * @author Win
 * 
 * @version 2011-4-9 下午04:36:59
 * 
 */
public class GenericException extends Exception {

	private static final long serialVersionUID = 0x2b0a5bba2e52a69fL;
	private String error;

	public GenericException() {
		error = "Unknown exception.";
	}

	public GenericException(String error) {
		super(error);
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public static void main(String[] args) {

	}
}
