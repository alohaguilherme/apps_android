package com.example.guilherme.organize.activity.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dateNow(){

       long date = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }
}
