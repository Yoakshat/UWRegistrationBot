package com.hack;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class Typer extends TimerTask {

    private WebDriver driver; 
    public int i = 0; 

    public Typer(WebDriver driver){
        this.driver = driver; 
    }

    @Override
    public void run() {
        new Actions(driver)
            .sendKeys("a")
            .perform();

        i++;
    }
    
}
