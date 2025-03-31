package com.hack;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;

public class Helper {
    public void login(WebDriver driver){
        WebElement netId = driver.findElement(By.id("weblogin_netid"));
        WebElement pass = driver.findElement(By.id("weblogin_password"));
        WebElement submit = driver.findElement(By.id("submit_button"));
        
        // enter your following netid and password
        netId.sendKeys("abcd");
        pass.sendKeys("1234");
        submit.click();
    }

    public void waitTillPresent(WebDriver driver, By by){
        while(true){
            try{
                driver.findElement(by);
                // wait one second just to be sure
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
                break;
            } catch (Exception e){

            }
        }
    }

    public void clickWhenPresent(WebDriver driver, By by){
        while(true) { 
            try {
                driver.findElement(by).click();
                // if successful
                break;
            } catch (Exception e){

            }
        }
    }

    // if we want to press at 5am, time will be 04:59
    // scrolls up and down to keep page active
    public void keepPageActive(WebDriver driver, String time){
        try{
            JavascriptExecutor jse = (JavascriptExecutor)driver; 
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm") ;
            String fixed = dateFormat.format(dateFormat.parse(time));
            while(true){
                Date date = new Date() ;
                // move on to next stage
                if(dateFormat.format(date).equals(fixed)){
                    break; 
                } else {
                    // scroll up and down
                    jse.executeScript("scroll(0, -15)");
                    jse.executeScript("scroll(0, 15)");
                    // wait 5 seconds
                    Thread.sleep(5000);
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    } 

}




