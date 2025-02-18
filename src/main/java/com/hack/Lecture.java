package com.hack;
import java.util.*;

public class Lecture extends QuizSection {

    private List<QuizSection> quizzes;

    public Lecture(String slnCode, TimeRange timeRange, List<QuizSection> quizzes){
        super(slnCode, timeRange);
        this.quizzes = quizzes;
    }

    public Lecture(QuizSection quiz, List<QuizSection> quizzes){
        this(quiz.slnCode, quiz.timeRange, quizzes);
    }

    // we could remove overlap here and also add back here
    public QuizMod removeQuizOverlap(QuizSection picked){
        List<QuizSection> toRemove = new ArrayList<>();
        List<SecMod> addBack = new ArrayList<>();

        int i = 0; 
        for(QuizSection quiz: quizzes){
            if(quiz.overlaps(picked)){
                toRemove.add(quiz);
                addBack.add(new SecMod(i, quiz));
            }
            i += 1;
        }

        quizzes.removeAll(toRemove);
        return new QuizMod(this, addBack); 
    }

    public void addBack(List<SecMod> quiz){
        for(SecMod q: quiz){
            quizzes.add(q.i, q.sec);
        }
    }

    @Override
    public List<QuizSection> getQuizzes(){
        return this.quizzes;
    }

    @Override
    public boolean isQuiz(){
        return false;
    }


}
