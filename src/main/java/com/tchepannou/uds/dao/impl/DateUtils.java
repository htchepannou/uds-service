package com.tchepannou.uds.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtils(){

    }

    public static Timestamp asTimestamp (Date date) {
        if (date == null) {
            return null;
        } else if (date instanceof Timestamp) {
            return (Timestamp) date;
        } else {
            return new Timestamp(date.getTime());
        }
    }

    public static java.sql.Date asSqlDate (Date date) {
        if (date == null) {
            return null;
        } else if (date instanceof java.sql.Date) {
            return (java.sql.Date) date;
        } else {
            return new java.sql.Date(date.getTime());
        }
    }

    public static String asString (Date date) {
        return date != null ? new SimpleDateFormat(DATETIME_PATTERN).format(date) : null;
    }
}
