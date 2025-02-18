package com.hack;

import java.util.*;

// composes a SecMod and a QuizMod together
public class LecMod {
    // secMod for the actual lecture
    public SecMod section;
    // for quiz sections 
    public QuizMod quizSecs; 

    public LecMod(SecMod section, QuizMod quizSecs){
        this.section = section; 
        this.quizSecs = quizSecs; 
    }

    public void addSec(List<QuizSection> sections){
        sections.add(section.i, section.sec);
    }

    public void addQuiz(){
        quizSecs.addBack();
    }
}
