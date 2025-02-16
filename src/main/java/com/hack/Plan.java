package com.hack;

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
    public void createPlans(){
        createPlans(new ArrayList<String>(), this.classes, 3);

        System.out.println(this.plans.toString());
    }

    // whenever successful add to plan
    // delete classes whenever we've used them
    // recursive backtracking

    // TODO: fix error getting all lectures
    private void createPlans(List<String> plan, List<Class> classes, int n){
        // generated enough plans
        if(this.plans.size() >= n){
            return; 
        }

        // successful picked all classes
        if(classes.size() == 0){
            this.plans.add(plan);
            return; 
        }

        Collections.sort(classes);

        Class pick = classes.get(0);
        int lecs = pick.numLectures();

        // fails if we get to a class and no classes left
        if(lecs == 0){
            return;
        }

        // go in order
        for(int i = 0; i < lecs; i++){
            Lecture picked = pick.get(i);
            
            // choose
            Class first = classes.remove(0);
            plan.add(picked.getSLN());
            // take the rest of classes and remove the overlap
            List<List<LecMod>> stored = new ArrayList<>();
            for(Class c: classes){
                stored.add(c.removeOverlap(picked));
            }

            // do
            createPlans(plan, classes, n);

            // unchoose
            int j = 0; 
            for(Class c: classes){
                c.addBack(stored.get(j));
                j += 1; 
            }
            classes.add(0, first);
            
        }


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
            help.clickWhenPresent(driver, By.id("term__Spring 2025"));

            // then get the first link in the search results
            String selector = String.format("a[title*='%s']", course);
            help.clickWhenPresent(driver, By.cssSelector(selector));

            // for now implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            
            // get all rows that say lecture
            // id = spring-a, spring-b.....
            int lectIdx = 0;
            
            // arrayList of lectures
            ArrayList<Lecture> lectures = new ArrayList<>();

            while(true){
                // try getting id
                String getID = "spring-" + String.valueOf((char)('a' + lectIdx));
        
                try {
                    WebElement tRow = driver.findElement(By.id(getID));
                    String slnCode = tRow.findElement(By.xpath("tr[1]/td[6]")).getText().split("\\n")[1];
                    System.out.println(slnCode);

                    WebElement info = tRow.findElement(By.xpath("tr[1]/td[4]/div/div[2]"));

                    // then get day
                    String days = info.findElement(By.xpath("div[1]/span[1]")).getText();

                    
                    
                    // now get time
                    List<WebElement> timeElems = info.findElement(By.xpath("div[2]"))
                        .findElements(By.tagName("time"));
                    
                    TimeRange timeClass = new TimeRange(timeElems.get(0).getText(), 
                                                        timeElems.get(1).getText(), 
                                                        Arrays.asList(days.split("[, ]+")));


                    lectures.add(new Lecture(slnCode, timeClass));
                    
                    lectIdx += 1; 
                } catch (Exception e) {
                    // System.out.println(e);
                    break; 
                }
            }

            // then get the lecture time and the days 
            classes.add(new Class(course, lectures));
            
        }

        return classes; 
    }
}   
