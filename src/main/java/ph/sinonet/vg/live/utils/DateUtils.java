package ph.sinonet.vg.live.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


/**
 * Created by Tom
 * on 10/25/16.
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    private static DateTimeFormatter standardFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static String fmtDateForBetRecods(Date date) {
        return new DateTime(date).toString(standardFmt);
    }

    public static Date parseDateForStandard() {
        Date date = new Date();
        return standardFmt.parseDateTime(fmtDateForBetRecods(date)).toDate();
    }
}
