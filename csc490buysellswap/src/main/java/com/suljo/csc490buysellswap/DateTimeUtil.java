package com.suljo.csc490buysellswap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final DateTimeFormatter yearMonthDay_12HoursMinutesSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    private static final DateTimeFormatter datetime2Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Returns a human-readable string representation of an MSSQL datetime2 value.
     * @param mssqlDatetime2String a string in the MSSQL datetime2 default format.
     * @param outputFormatter a DateTimeFormatter
     * @return the human-readable string
     */
    public static String mssqlDatetime2ToHumanReadable(String mssqlDatetime2String, DateTimeFormatter outputFormatter){
        //Get rid of the fractional seconds present in the default string returned by MSSQL JDBC for a datetime2 value.
        LocalDateTime datetime = LocalDateTime.parse(mssqlDatetime2String.substring(0, 19), datetime2Formatter);
        return datetime.format(outputFormatter);
    }
}
