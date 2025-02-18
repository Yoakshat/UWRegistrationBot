package com.hack;

import java.util.*;

public class QuizSection {
    protected String slnCode; 
    protected TimeRange timeRange;

    public QuizSection(String slnCode, TimeRange timeRange){
        this.slnCode = slnCode; 
        this.timeRange = timeRange; 
    }

    public String getSLN(){
        return this.slnCode; 
    }

    public boolean overlaps(QuizSection other){
        return this.timeRange.overlaps(other.timeRange);
    }

    public boolean isQuiz(){
        return true; 
    }

    public List<QuizSection> getQuizzes(){
        return null;
    }

}
