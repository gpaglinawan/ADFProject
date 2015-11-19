package com.fda.aps;

import oracle.jbo.domain.Date;

import java.sql.Timestamp;

import java.util.Calendar;

public class DateUtils {
    public DateUtils() {
        super();
    }
    static final long MILI_SECONDS_PER_DAY = 86400000;

    public static Date dateAfterNDays(Date startDate, int nDays) {
        if (startDate == null)
            startDate = new Date(Date.getCurrentDate()); // assume today

        Timestamp ts = startDate.timestampValue();
        long nextDatesSecs = ts.getTime() + (MILI_SECONDS_PER_DAY * nDays);
        return new Date(new Timestamp(nextDatesSecs));
    }
    
    public static oracle.jbo.domain.Date convertUtilToJboDate(java.util.Date utildate){
        
        oracle.jbo.domain.Date newDate = new oracle.jbo.domain.Date (new java.sql.Timestamp( utildate.getTime() ));
        return newDate;
    }
    
    private static java.util.Date getUtilDateFromDomainDate(oracle.jbo.domain.Date selectedDate){
            java.util.Date finalDate = null;
            if (selectedDate != null) {
            try{

              java.sql.Date sqldate = selectedDate.dateValue();
              finalDate = new java.util.Date(sqldate.getTime());
            }catch(Exception e){
                e.printStackTrace();
            }
            }
            return finalDate;
        }
    
    public static int getJboDateYear(oracle.jbo.domain.Date selectedDate){
            java.util.Date d=getUtilDateFromDomainDate(selectedDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int year = cal.get(Calendar.YEAR);
            return year;
        }
        
        public static int getJboDateMonth(oracle.jbo.domain.Date selectedDate){
            java.util.Date d=getUtilDateFromDomainDate(selectedDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int month = cal.get(Calendar.MONTH);
            return month;
        }
        
        public static int getJboDateDay(oracle.jbo.domain.Date selectedDate){
            java.util.Date d=getUtilDateFromDomainDate(selectedDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return day;
        }
}
