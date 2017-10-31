package com.emotibot.xychatlib.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ldy on 2017/2/23.
 */

public class XYlibTimeUtils {
    public static Date getStartTime(int unit, int unitsBeforToday){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        todayStart.add(unit,-unitsBeforToday);
        return todayStart.getTime();
    }

    public static boolean between6and12() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour <= 12) {
            return true;
        } else {
            return false;
        }
    }
}
