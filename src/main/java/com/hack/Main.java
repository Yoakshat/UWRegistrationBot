package com.hack;

import java.text.ParseException;
import java.util.*;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;



public class Main {

    // idea: login once, keep page active by constant scrolling up and down
    
    public static void main(String[] args) throws ParseException{
        WebDriver driver = new ChromeDriver();

        // Duo.acceptCall();
        List<String> slns = new ArrayList<String>();
        slns.add("13432");
        slns.add("28719");
        slns.add("62713");
        slns.add("13232");

        // 5 classes -> 720 possibilities
        String[] courseArray = {"CSE 331", "MATH 208", "DRAMA 252", "CLAS 430"};


        Plan plan = new Plan(courseArray, driver); 
        // 27 possibilities with 3 classes
        plan.createPlans(1000); 

        
        // this is the trick!
        // System.out.println("spring-" + String.valueOf((char)('a' + 2)));


        // Optimal obj = new Optimal(driver, slns, 17);
        // action(driver, obj); 
        
        // example of scheduling at fixed rates in selenium
        /*Timer timer = new Timer(); 
        TimerTask task = new Typer(driver);

        timer.scheduleAtFixedRate(task, 50, 1 * 50);*/
    }

    public static void action(WebDriver driver, Optimal obj){
        
        
        // full url needed
        driver.get("https://my.uw.edu/");


        Helper help = new Helper(); 
        help.login(driver); 


        // comment out this line day-of
        // keepPageActive(driver, "4:59")
            
        By acd = By.cssSelector("a[href='/academics/']");
        help.clickWhenPresent(driver, acd);

        By reg = By.cssSelector("a[href='https://sdb.admin.uw.edu/students/uwnetid/register.asp']");
        help.clickWhenPresent(driver, reg);

    
        obj.registerEverything();
    }

    



    

    
}