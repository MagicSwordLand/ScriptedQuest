package net.brian.scriptedquests.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

public class QuestDate {

    final static DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("MM/dd", Locale.TAIWAN);

    public static String getDate(){
        return dayFormat.format(LocalDate.now());
    }

    public static String getDate(long timeMill){
        return dayFormat.format(Instant.ofEpochMilli(timeMill));
    }


}
