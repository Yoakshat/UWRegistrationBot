package com.hack;

import java.text.ParseException;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;



public class Main {

    // enable setting to keep desktop on 
    // keep computer charged
    
    public static void main(String[] args) throws ParseException{
        WebDriver driver = new ChromeDriver();

        // FOR PLANNING 
        String[] courseArray = {"MATH 126", "CSE 121", "NUTR 200"};
        Plan plan = new Plan(courseArray, driver, "winter", true, true); 
        // 27 possibilities with 3 classes
        plan.createPlans(1000); 

        /* FOR REGISTRATION */
        /*String[] slns = {"12908", "12909", "12923", "12924", "13076", "13078", "22074"};
        List<String> slnList = Arrays.asList(slns);

        Optimal obj = new Optimal(driver, slnList);
        try{
            action(driver, obj); 
        } catch (Exception e){
            AlarmSound.sound();
        }*/

    }

    public static void action(WebDriver driver, Optimal obj){
        // full url needed
        driver.get("https://my.uw.edu/");


        Helper help = new Helper(); 
        help.login(driver); 
            
        By acd = By.cssSelector("a[href='/academics/']");
        help.clickWhenPresent(driver, acd);

        By reg = By.cssSelector("a[href='https://sdb.admin.uw.edu/students/uwnetid/register.asp']");
        help.clickWhenPresent(driver, reg);

        // comment out this line day-of
        help.keepPageActive(driver, "05:57");

        obj.registerEverything();
        
    }

    



    

    
}