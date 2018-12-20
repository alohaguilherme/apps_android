package com.example.guilherme.organize.activity.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dateNow(){

       long date = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String mesAnoDateSelect(String data){

        String dateRutern[] = data.split("/");
        //String day = dateRutern[0];
        String month = dateRutern[1];
        String year = dateRutern[2];
        String monthYear = month + year;
        return monthYear;
    }
}
