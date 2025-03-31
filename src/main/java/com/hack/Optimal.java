package com.hack;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// Optimal registration
public class Optimal {
    
    private WebDriver driver; 
    private List<String> slns; 

    // ith and (i+1) index should represent course & section
    // if no section, write element as "-1"
    public Optimal(WebDriver driver, List<String> slns){
        this.driver = driver;
        this.slns = slns; 
    }

    public void registerEverything(){
        // reload until you can register
        
        // check if you access an given element
        // if you can, then check if you can access registration
        // otherwise refresh
        Helper help = new Helper();

        while(true){
            // wait until you can access a given element
            help.waitTillPresent(driver, By.cssSelector("a[href='https://sdb.admin.uw.edu/sisStudents/uwnetid/grades.aspx']"));
            
            // can I access table 2 and enter anything in
            try{
                driver.findElement(By.xpath("//*[@id=\"regform\"]/table[2]/tbody/tr[2]/td[1]/input"));
                break;
            } catch (Exception e){
                // in this case refresh
                driver.navigate().refresh();
            }   
        }

        register(slns);
    }

    public void register(List<String> slns){
        // if available to register
        WebElement firstSln = driver.findElement(By.xpath("//*[@id=\"regform\"]/table[2]/tbody/tr[2]/td[1]/input"));
        String name = firstSln.getAttribute("name");
        int digit = Integer.parseInt(name.substring(3));
        
        // digit
        // then digit + 1
        // then digit + 2

        for(int i = 0; i < slns.size(); i++){
            WebElement slnEntry = driver.findElement(By.name("sln" + (digit+i)));
            slnEntry.clear(); 
            slnEntry.sendKeys(slns.get(i));
        }

        submit();

        // checks if you got classes, otherwise sounds alarm
        youGotClasses();
    }

    private void youGotClasses(){
        Helper help = new Helper();
        help.waitTillPresent(driver, By.cssSelector("a[href='https://sdb.admin.uw.edu/sisStudents/uwnetid/grades.aspx']"));
        // check how many elements were submitted
        WebElement registered = driver.findElement(By.cssSelector("table[class='sps_table update']"));
        // 4 extra elements (not considered classes)
        int gotClasses = registered.findElements(By.cssSelector("tr")).size() - 4;
        // System.out.println(gotClasses);
        // if less than what you wanted
        if(gotClasses < slns.size()){
            // sound alarm
            AlarmSound.sound();
        }
    }

    public void submit(){
        // don't know if s is uppercase or lowercase so just ubmit
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }





}
