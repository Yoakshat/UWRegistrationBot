package com.hack;

import java.util.*;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;



public class Main {

    // idea: login once, keep page active by constant scrolling up and down
    
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        // Duo.acceptCall();
        List<String> slns = new ArrayList<String>();
        slns.add("13432");
        slns.add("28719");
        slns.add("62713");
        slns.add("13232");

        
        String[] courseArray = {"CSE 351", "CSE 331"};
        Plan plan = new Plan(courseArray, driver); 
        plan.createPlans();
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