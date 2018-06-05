package com.webmajstr.gpstrackerexample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class LogEntity {

    @Id
    public long id;
    public String log;
    public Date date;

    public LogEntity(String log) {
        this.log = log;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%d;%s;%s", id, getDateString(), log);
    }

    private String getDateString() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }
}