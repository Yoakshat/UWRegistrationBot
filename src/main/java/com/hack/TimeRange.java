package com.hack;
import java.util.*;
import java.text.*;
import java.time.*;

public class TimeRange {
    public Date start; 
    public Date end; 
    public List<String> days;

    private DateFormat dateFormat;

    private boolean early; 
    private boolean backToBack; 

    // early: are you okay with early classes
    // backToBack: are you okay with backToBack classes; 
    public TimeRange(String startDate, String endDate, List<String> days, boolean early, boolean backToBack) throws ParseException{
        // convert startDate to date
        this.dateFormat = new SimpleDateFormat("hh:mm a");
        this.start = dateFormat.parse(startDate); 
        this.end = dateFormat.parse(endDate);
        this.days = days;

        this.early = early; 
        this.backToBack = backToBack;
    }   

    private boolean daysShared(TimeRange other){
        for(String day: other.days){
            if(days.contains(day)){
                return true; 
            }
        }
        return false;
    }

    // NO pls not an 8:30 class
    public boolean tooEarly() throws ParseException{
        if(!early){
            return this.start.before(this.dateFormat.parse("8:31 AM"));
        }

        return false; 
    }

    public boolean overlaps(TimeRange other){
        
        // no back-to-backs (8:30 - 9:20 9:30 - 10:30)
        Date otherStart = other.start; 
        Date otherEnd = other.end;

        if(!backToBack){
            otherStart = Date.from(other.start.toInstant().minusSeconds(11 * 60));
            otherEnd = Date.from(other.end.toInstant().plusSeconds(11 * 60));
        }

        
        

        // 8:20 - 9:30 

        // first check if any days are shared 
        if (daysShared(other)){
            return start.before(otherEnd) && otherStart.before(end);
        } 
        return false;
    }
}
