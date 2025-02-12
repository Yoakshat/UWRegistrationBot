package com.hack;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

            driver.findElement(By.cssSelector(selector)).click();;
            
            // then get the lecture time and the days 
            
        }
    }
}   
