package com.hack;

import java.time.Duration;
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


        Optimal obj = new Optimal(driver, slns, 17);
        action(driver, obj);
    }

    public static void action(WebDriver driver, Optimal obj){
        
        
        // full url needed
        driver.get("https://my.uw.edu/");


        WebElement netId = driver.findElement(By.id("weblogin_netid"));
        WebElement pass = driver.findElement(By.id("weblogin_password"));
        WebElement submit = driver.findElement(By.id("submit_button"));

        netId.sendKeys("akshatm1");
        pass.sendKeys("Yoa$hu121805");
        submit.click();

        // for your duo login
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        
        // TODO: Keep scrolling up and down until time is reached
        
        driver.findElement(By.cssSelector("a[href='/academics/']")).click();
        driver.findElement(By.cssSelector("a[href='https://sdb.admin.uw.edu/students/uwnetid/register.asp']")).click();
        
        obj.registerEverything();
    }


    
}