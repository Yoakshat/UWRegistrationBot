package com.hack;

import java.util.*; 

public class Class implements Comparable<Class> {
    
    private String name; 
    // everything is a type of quiz section
    private List<QuizSection> sections; 

    public Class(String name, List<QuizSection> sections){
        this.name = name; 
        this.sections = sections;
    }

    public List<LecMod> removeOverlap(QuizSection picked){
        // we want a way to store (index, object)
        // we have to store two arrays -> toRemove, and index

        List<QuizSection> toRemove = new ArrayList<>();
        List<LecMod> addBack = new ArrayList<>(); 

        int i = 0; 
    
        
        for(QuizSection section: sections){
            SecMod lecOver = null; 
            QuizMod quizOver = null; 

            // quiz removeOverlap
            if(!section.isQuiz()){
                Lecture sec = (Lecture)(section);
                // should return List<QuizMod> 
                quizOver = sec.removeQuizOverlap(picked);
            }
            
            // section removeOverlap
            if(section.overlaps(picked)){
                // remove lecture
                toRemove.add(section);
                // create a sectionMod
                lecOver = new SecMod(i, section);
            } 

            // order of above doesn't actually matter bcoz of reference semantics
            addBack.add(new LecMod(lecOver, quizOver));

            i += 1; 
        }

        sections.removeAll(toRemove);

        return addBack;
    }

    // given the List of LecMods, add back
    public void addOverlap(List<LecMod> lecs){
        
        for(LecMod lec: lecs){
            // add back to sections
            if(lec.section != null){
                lec.addSec(sections);
            }

            // add back to quiz sections
            if(lec.quizSecs != null){
                // tell quizMod to add it back
                lec.addQuiz();
            }
        }
    }

    public QuizSection get(int i){
        return sections.get(i);
    }

    public int numSections(){
        return sections.size();
    }

    @Override
    public String toString(){
        return name;
    }

    // will be ascending order 
    @Override
    public int compareTo(Class other) {
        return this.numSections() - other.numSections();
    }


}
