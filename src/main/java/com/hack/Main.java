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

        // Duo.acceptCall();
        /*List<String> slns = new ArrayList<String>();
        slns.add("13432");
        slns.add("28719");
        slns.add("62713");
        slns.add("13232");*/

        // 5 classes -> 720 possibilities

        // ERROR with following classes
        
        String[] courseArray = {"BIOL 180", "CHEM 237", "PHYS 122"};

        Plan plan = new Plan(courseArray, driver, "autumn", false, false); 
        // 27 possibilities with 3 classes
        plan.createPlans(1000); 

        
        // this is the trick!
        // System.out.println("spring-" + String.valueOf((char)('a' + 2)));

        /*List<String> slns = new ArrayList<String>();
        slns.add("131");
        slns.add("234");
        slns.add("331");

        Optimal obj = new Optimal(driver, slns);
        try{
            action(driver, obj); 
        } catch (Exception e){
            // AlarmSound.sound();
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
        // help.keepPageActive(driver, "05:57");

        obj.registerEverything();
        
    }

    



    

    
}