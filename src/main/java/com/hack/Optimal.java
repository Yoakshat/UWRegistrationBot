package com.hack;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// Optimal registration
public class Optimal {
    
    private WebDriver driver; 
    private List<String> slns; 
    private int maxCredits;

    // ith and (i+1) index should represent course & section
    // if no section, write element as "-1"
    public Optimal(WebDriver driver, List<String> slns, int maxCredits){
        this.driver = driver;
        this.slns = slns; 
        this.maxCredits = maxCredits;
    }

    public void registerEverything(){
        int credits = 0; 

        if (credits >= maxCredits){
            return; 
        }

        for(int i = 0; i < slns.size(); i+=2){
            register(slns, i);

            // TODO: if successfull, add credits
        }
    }

    public void register(List<String> slns, int index){
        // if available to register
        WebElement firstSln = driver.findElement(By.xpath("//*[@id=\"regform\"]/table[2]/tbody/tr[2]/td[1]/input"));
        String name = firstSln.getAttribute("name");
        int digit = Integer.parseInt(name.substring(3));

        // TODO: keep reloading the page until you face no errors with above code ^

        for(int i = index; i <= index+1; i++){
            if(slns.get(i) != "-1"){
                WebElement slnEntry = driver.findElement(By.name("sln" + (digit+(i-index))));
                slnEntry.clear();
                slnEntry.sendKeys(slns.get(i));
            }
        }

        submit();

    }

    public void submit(){
        // submit when ready
        // clear first 2 entries
    }





}
