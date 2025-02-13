package com.hack;

import java.time.Duration;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// create a possible schedule
public class Plan {
    
    public void createPlan(String[] courses, WebDriver driver){
        Helper help = new Helper();
        // first get to page
        driver.get("https://myplan.uw.edu/course/");
        // then click seattle campus
        help.clickWhenPresent(driver, By.id("seattle-campus-selection"));

        // then search for keys and press button search
        for(String course: courses){
            driver.findElement(By.className("form-control")).sendKeys(course); 
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // then check box for spring quarter
            help.clickWhenPresent(driver, By.id("term__Spring 2025"));

            // then get the first link in the search results
            String selector = String.format("a[title*='%s']", course);

            driver.findElement(By.cssSelector(selector)).click();

            // for now implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            
            // get all rows that say lecture
            // id = spring-a, spring-b.....
            int lectIdx = 0;
            while(true){
                // try getting id
                String getID = "spring-" + String.valueOf((char)('a' + lectIdx));
        
                try {
                    WebElement tRow = driver.findElement(By.id(getID));
                    WebElement info = tRow.findElement(By.xpath("tr[1]/td[4]/div/div[2]"));

                    // then get day
                    String days = info.findElement(By.xpath("div[1]/span[1]")).getText();

                    
                    // now get time
                    List<WebElement> timeElems = info.findElement(By.xpath("div[2]"))
                        .findElements(By.tagName("time"));
                    
                    TimeRange timeClass = new TimeRange(timeElems.get(0).getText(), 
                                                        timeElems.get(1).getText(), 
                                                        Arrays.asList(days.split("[, ]+")));

                    // TODO: create a new class with 
                    // Lecture(sln code, name, TimeRange)
                    
                    lectIdx += 1; 
                } catch (Exception e) {
                    // System.out.println(e);
                    break; 
                }
            }

            // then get the lecture time and the days 
            
        }
    }
}   
