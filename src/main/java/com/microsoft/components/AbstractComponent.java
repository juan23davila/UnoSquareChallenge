package com.microsoft.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class AbstractComponent {

    WebDriver driver;
    WebElement sectionElement;

    public AbstractComponent(WebDriver driver, WebElement sectionElement) {
        this.driver = driver;
        this.sectionElement = sectionElement;
        WebDriverWait eWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        eWait.until(ExpectedConditions.visibilityOf(sectionElement));
    }

    public WebElement findElementInSection(By elementSelector){
        return sectionElement.findElement(elementSelector);
    }
}
