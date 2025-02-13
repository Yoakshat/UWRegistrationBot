package com.hack;
import java.util.*;
import java.text.*;

public class TimeRange {
    public Date start; 
    public Date end; 
    public List<String> days;

    public TimeRange(String startDate, String endDate, List<String> days) throws ParseException{
        // convert startDate to date
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        this.start = dateFormat.parse(startDate); 
        this.end = dateFormat.parse(endDate);
        this.days = days;
    }   

    private boolean daysShared(TimeRange other){
        for(String day: other.days){
            if(days.contains(day)){
                return true; 
            }
        }
        return false;
    }

    public boolean overlaps(TimeRange other){
        // first check if any days are shared 
        if (daysShared(other)){
            return start.before(other.end) && other.start.before(end);
        } 
        return false;
    }
}
