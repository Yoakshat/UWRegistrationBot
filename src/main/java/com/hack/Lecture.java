package com.hack;

public class Lecture {
    
    private String slnCode; 
    private TimeRange timeRange;

    public Lecture(String slnCode, TimeRange timeRange){
        this.slnCode = slnCode; 
        this.timeRange = timeRange; 
    }

    public String getSLN(){
        return this.slnCode; 
    }

    public boolean overlaps(Lecture other){
        return this.timeRange.overlaps(other.timeRange);
    }

}
