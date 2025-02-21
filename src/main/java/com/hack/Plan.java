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
        createPlans(new ArrayList<String>(), n);

        System.out.println(this.plans.toString());
        System.out.println(this.plans.size());
    }

    // whenever successful add to plan
    // delete classes whenever we've used them
    // recursive backtracking
    private void createPlans(List<String> plan, int n){

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
        
        // sort from classes downwards
        Collections.sort(classes);

        Class pick = classes.get(0);
        int secs = pick.numSections();


        // fails if we get to a class and no classes left
        if(secs == 0){
            return;
        }

        // go in order
        // should not give permutations

        for(int i = 0; i < secs; i++){
            QuizSection picked = pick.get(i);
            Class first = classes.remove(0);

            boolean addQuizClass = !picked.isQuiz() && picked.getQuizzes().size() > 0;

            // check if picked is a lecture or a quiz section
            if(addQuizClass){
                // add a new class filled with quiz sections
                classes.add(new Class(pick.toString(), picked.getQuizzes()));
            }
            
            // CHOOSE

            plan.add(picked.getSLN());

            List<List<LecMod>> stored = new ArrayList<>();
            for(Class c: classes){
                stored.add(c.removeOverlap(picked));
            }


            // DO
            createPlans(plan, n);

            // UNCHOOSE
            
            // if you added a quiz section, just remove it!
            if(addQuizClass){
                classes.remove(classes.size() - 1);
            }
            
            // for Class c, add overlap back
            int j = 0; 
            for(Class c: classes){
                c.addOverlap(stored.get(j));
                j += 1; 
            }

            classes.add(0, first);

            // remove added plan
            plan.remove(picked.getSLN());
            
        }



    }   

    // get time data from timebox
    private TimeRange getTimeRange(WebElement tRow) throws ParseException{
        WebElement timeBox = tRow.findElement(By.xpath("tr[1]/td[4]/div/div[2]"));

        String days = timeBox.findElement(By.xpath("div[1]/span[1]")).getText();
        List<WebElement> timeElems = timeBox.findElement(By.xpath("div[2]"))
            .findElements(By.tagName("time"));

        return new TimeRange(timeElems.get(0).getText(), 
                    timeElems.get(1).getText(), 
                    Arrays.asList(days.split("[, ]+")));
    }

    private QuizSection createQuiz(WebElement tRow) throws ParseException{

        // take a tRow and create a lecture                    
        String slnCode = tRow.findElement(By.xpath("tr[1]/td[6]")).getText().split("\\n")[1];
        
        TimeRange timeClass = getTimeRange(tRow);

        return new QuizSection(slnCode, timeClass);

    }

    private boolean isValid(WebElement tRow, boolean lecture) throws ParseException{
        // check for in-person
        String arrangement = tRow.findElement(By.xpath("tr[1]/td[4]/div/div[1]/span[2]")).getText();
        System.out.println(arrangement);
        if(!arrangement.contains("In-person")){
            return false; 
        }

        // check for open
        String open = tRow.findElement(By.xpath("tr[1]/td[7]/div/span")).getText(); 
        System.out.println(open);
        if(!open.contains("Open")){
            return false; 
        }

        // check if not too early
        if(getTimeRange(tRow).tooEarly()){
            return false;
        }

        // check if number of seats > 20 
        // (otherwise most likely another special class)
        if(lecture){
            String seatString = tRow.findElement(By.xpath("tr[1]/td[7]/small")).getText();
            //  get the last element
            String[] seatArr = seatString.split("\\s+");
            int numSeats = Integer.parseInt(seatArr[seatArr.length - 1]);

            if(numSeats < 20){
                return false; 
            }
        }



        // if fulfilled all other conditions
        return true; 
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

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            WebElement search = driver.findElement(By.className("search-results"));
            int numRows = search.findElements(By.cssSelector("tr")).size() - 1;

            // check box for spring quarter only if multiple classes
            if (numRows > 1){
                help.clickWhenPresent(driver, By.id("term__Spring 2025"));
            }

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
            
            // TODO: instead go backwards (go from latest time to earliest time)
            // I WANT LATE CLASSES!
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
                            if(isValid(qr, false)){
                                quizSecs.add(createQuiz(qr));
                            }
                        }
    
                    }
                    
                    // everything starts of as a lecture
                    if(isValid(tRow, true)){
                        sections.add(new Lecture(createQuiz(tRow), quizSecs));
                    }
                    
                    
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
