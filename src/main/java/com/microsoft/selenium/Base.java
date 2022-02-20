package com.microsoft.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Base {
    private WebDriver driver;

    public Base(WebDriver driver){
        this.driver = driver;
    }

    public WebDriver chromeDriverConnection(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\juan.davila\\Documents\\browserDrivers\\chromedriver98.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    public WebElement findElement(By locator){
        return driver.findElement(locator);
    }

    public List<WebElement> findElements(By locator){
        return driver.findElements(locator);
    }

    public String getText(WebElement element){
        return element.getText();
    }

    public String getText(By locator){
        return findElement(locator).getText();
    }

    public void type(String inputText, WebElement element){
        element.sendKeys(inputText);
    }

    public void click(WebElement element){
        element.click();
    }

    public Boolean isDisplayed(WebElement element){
        try {
            return element.isDisplayed();
        }catch (org.openqa.selenium.NoSuchElementException e){
            return false;
        }
    }

    public void openURL(String url){
        driver.get(url);
    }

    public String getURL(){
        return driver.getCurrentUrl();
    }

    public void waitUntilElementIsVisible(WebElement element, int maxTimeWait){
        WebDriverWait eWait = new WebDriverWait(driver, Duration.ofSeconds(maxTimeWait));
        eWait.until(ExpectedConditions.visibilityOf(element));
    }

    public void scrollUntilWebElement(WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(false);", webElement);
    }
}
