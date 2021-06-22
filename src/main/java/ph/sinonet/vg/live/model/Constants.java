package ph.sinonet.vg.live.model;

/**
 * Created by jay on 11/15/16.
 */
public class Constants {

    public static final String ENCODING = "UTF-8";

    public static final Integer FLAG_TRUE = 0;
    public static final Integer FLAG_FALSE = 1;
    public static final Integer ENABLE = 0;
    public static final Integer DISABLE = 1;
    public static final String SESSION_CUSTOMERID = "customer";

    public static String getExceptionContent(Exception e) {
        String resultContent = "";
        if (e != null) {
            resultContent = e.getClass().getName() + ":" + e.getMessage();
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++)
                resultContent += "\n" + stackTrace[i];
        }
        return resultContent;
    }

}
