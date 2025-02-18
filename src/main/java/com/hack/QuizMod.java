package com.hack;

import java.util.*;

public class QuizMod {
    // lecture the quizzes belong to
    public Lecture lec;
    // the quizzes to remove
    public List<SecMod> quiz;

    public QuizMod(Lecture lec, List<SecMod> quiz){
        this.lec = lec; 
        this.quiz = quiz;
    }

    public void addBack(){
        // go inside lec and add back
        lec.addBack(quiz);
    }




}
