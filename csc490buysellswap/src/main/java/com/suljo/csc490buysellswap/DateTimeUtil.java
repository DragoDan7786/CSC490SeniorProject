package com.suljo.csc490buysellswap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final DateTimeFormatter yearMonthDay_12HoursMinutesSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    private static final DateTimeFormatter datetime2_DatetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter datetime2_DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * Returns a human-readable string representation of an MSSQL datetime2 value.
     * @param mssqlDatetime2String a string in the MSSQL datetime2 default format.
     * @param outputFormatter a DateTimeFormatter
     * @return the human-readable string
     */
    public static String mssqlDatetime2ToHumanReadable(String mssqlDatetime2String, DateTimeFormatter outputFormatter){
        //Get rid of the fractional seconds present in the default string returned by MSSQL JDBC for a datetime2 value.
        LocalDateTime datetime = LocalDateTime.parse(mssqlDatetime2String.substring(0, 19), datetime2_DatetimeFormatter);
        return datetime.format(outputFormatter);
    }

    /**
     * Returns a LocalDateTime parsed from the default string representation of a T-SQL datetime2 value.
     * Returns null if the provided string is null.
     * @param mssqlDatetime2String T-SQL datetime2 string
     * @return LocalDateTime representation of the date or null.
     */
    public static LocalDateTime mssqlDatetime2StringToLocalDateTime(String mssqlDatetime2String){
        if (mssqlDatetime2String != null){
            return LocalDateTime.parse(mssqlDatetime2String.substring(0, 19), datetime2_DatetimeFormatter);
        } else {
            return null;
        }
    }

    public static LocalDate mssqlDatetime2StringToLocalDate(String mssqlDatetime2String){
        if (mssqlDatetime2String != null){
            return LocalDate.parse(mssqlDatetime2String.substring(0, 10), datetime2_DateFormatter);
        } else {
            return null;
        }
    }
}
