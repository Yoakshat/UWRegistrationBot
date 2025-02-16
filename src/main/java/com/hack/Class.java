package com.hack;

import java.util.*; 

public class Class implements Comparable<Class> {
    
    private String name; 
    private List<Lecture> lectures; 

    public Class(String name, List<Lecture> lectures){
        this.name = name; 
        this.lectures = lectures; 
    }

    public List<LecMod> removeOverlap(Lecture picked){
        // we want a way to store (index, object)
        // we have to store two arrays -> toRemove, and index

        List<Lecture> toRemove = new ArrayList<>();
        List<LecMod> store = new ArrayList<>();

        int i = 0; 
        for(Lecture lecture: lectures){
            if(lecture.overlaps(picked)){
                // remove lecture
                toRemove.add(lecture);
                store.add(new LecMod(i, lecture));
            }
            i += 1; 
        }

        lectures.removeAll(toRemove);
        return store;
    }

    public void addBack(List<LecMod> deleted){
        for(LecMod del: deleted){
            lectures.add(del.i, del.lec);
        }
    }

    // build a compareTo to sort classes later
    public Lecture get(int i){
        return lectures.get(i);
    }

    public int numLectures(){
        return lectures.size();
    }

    @Override
    public String toString(){
        return name;
    }

    // will be ascending order 
    @Override
    public int compareTo(Class other) {
        return this.numLectures() - other.numLectures();
    }


}
