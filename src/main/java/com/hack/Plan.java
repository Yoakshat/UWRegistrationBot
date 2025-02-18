package com.hack;

import java.text.ParseException;
import java.time.Duration;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// create a possible schedule
public class Plan {

    private List<Class> classes;
    private List<List<String>> plans;  

    public Plan(String[] courses, WebDriver driver){
        this.classes = getClasses(courses, driver);
        this.plans = new ArrayList<>();
    }

    // generate up to n plans using recursive backtracking
    public void createPlans(int n){
        createPlans(new ArrayList<String>(), this.classes, n);

        System.out.println(this.plans.toString());
        System.out.println(this.plans.size());
    }

    // whenever successful add to plan
    // delete classes whenever we've used them
    // recursive backtracking

    // TODO: fix this to now incorp quiz sections
    private void createPlans(List<String> plan, List<Class> classes, int n){

        // generated enough plans
        if(this.plans.size() >= n){
            return; 
        }

        // successful picked all classes
        if(classes.size() == 0){
            // Q: why can't you just do this.plans.add(plan)
            // A: reference semantics (problem when we unchoose)
            List<String> newPlan = new ArrayList<>();
            for(String p: plan){
                newPlan.add(p);
            }
            this.plans.add(newPlan);
            return; 
        }

        Collections.sort(classes);

        Class pick = classes.get(0);
        int secs = pick.numSections();

        // fails if we get to a class and no classes left
        if(secs == 0){
            return;
        }

        // go in order
        for(int i = 0; i < secs; i++){
            QuizSection picked = pick.get(i);
            // check if picked is a lecture or a quiz section
            if(!picked.isQuiz()){
                // if lecture
                classes.add(new Class(pick.toString(), picked.getQuizzes()));
            }
            
            // CHOOSE
            Class first = classes.remove(0);
            plan.add(picked.getSLN());

            List<List<LecMod>> stored = new ArrayList<>();
            for(Class c: classes){
                stored.add(c.removeOverlap(picked));
            }


            // DO
            createPlans(plan, classes, n);

            // UNCHOOSE
            
            // remove the last item if(!picked.isQuiz)
            if(!picked.isQuiz()){
                classes.remove(classes.size() - 1);
            }

            // for Class c, add overlap back
            int j = 0; 
            for(Class c: classes){
                c.addOverlap(stored.get(j));
                j += 1; 
            }
            
            // add class back
            classes.add(0, first);
            // remove added plan
            plan.remove(picked.getSLN());
            
        }


    }   

    private QuizSection createQuiz(WebElement tRow) throws ParseException{

        // take a tRow and create a lecture                    
        String slnCode = tRow.findElement(By.xpath("tr[1]/td[6]")).getText().split("\\n")[1];

        WebElement info = tRow.findElement(By.xpath("tr[1]/td[4]/div/div[2]"));

        // then get day
        String days = info.findElement(By.xpath("div[1]/span[1]")).getText();

        
        
        // unable to locate below element

        // now get time
        List<WebElement> timeElems = info.findElement(By.xpath("div[2]"))
            .findElements(By.tagName("time"));
        
        TimeRange timeClass = new TimeRange(timeElems.get(0).getText(), 
                                            timeElems.get(1).getText(), 
                                            Arrays.asList(days.split("[, ]+")));

        return new QuizSection(slnCode, timeClass);

    }


 
    
    
    public List<Class> getClasses(String[] courses, WebDriver driver){
        Helper help = new Helper();

        // do this once first
        // first get to page
        driver.get("https://myplan.uw.edu/course/");
        // then click seattle campus
        help.clickWhenPresent(driver, By.id("seattle-campus-selection"));
        
        // then search for keys and press button search
        ArrayList<Class> classes = new ArrayList<>();
        for(String course: courses){
            System.out.println(course + "\n");

            driver.get("https://myplan.uw.edu/course/");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            driver.findElement(By.className("form-control")).sendKeys(course); 
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // then check box for spring quarter
            // TODO: if necessary, fix when there's no spring or only 1 spring
            help.clickWhenPresent(driver, By.id("term__Spring 2025"));

            // then get the first link in the search results
            String selector = String.format("a[title*='%s']", course);
            help.clickWhenPresent(driver, By.cssSelector(selector));

            // for now implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            
            // get all rows that say lecture
            // id = spring-a, spring-b.....
            int lectIdx = 0;
            
            // arrayList of sections
            List<QuizSection> sections = new ArrayList<>();
            

            while(true){
                // try getting id
                String getID = "spring-" + String.valueOf((char)('a' + lectIdx));
                
                try {
                    WebElement tRow = driver.findElement(By.id(getID));
                    
                    // get all ids that start with get-id
                    String quizSelector = String.format("tbody[id^=%s]", getID);
                    List<WebElement> quizRows= driver.findElements(By.cssSelector(quizSelector));

                    int skipFirst = 0;
                    ArrayList<QuizSection> quizSecs = new ArrayList<>();
                    for(WebElement qr: quizRows){
                        // skip first because is repeat of lecture
                        if(skipFirst == 0){
                            skipFirst += 1;
                        } else {
                            // get quiz section
                            quizSecs.add(createQuiz(qr));
                        }
    
                    }

                    sections.add(new Lecture(createQuiz(tRow), quizSecs));
                    
                    
                    lectIdx += 1; 
                } catch (Exception e) {
                    break; 
                }
            }
            
            // then get the lecture time and the days 
            classes.add(new Class(course, sections));
            
        }

        return classes; 
    }
}   
