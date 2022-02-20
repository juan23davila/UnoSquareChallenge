package com.microsoft.pages;

import com.microsoft.selenium.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.function.Function;

public class ExplorePage extends Base {

    @FindBy(id = "R1MarketRedirect-close")
    WebElement stayMexicoElement;

    @FindBy(xpath = "//header[@role='tablist']/a[contains(text(),'Comprar')]")
    WebElement comprarElement;

    public ExplorePage(WebDriver driver) {
        super(driver);

        //Wait to close popUp
        Wait<WebDriver> fWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        WebElement stayInRegionButton = fWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                if(driver.findElement(By.id("R1MarketRedirect-close")).isDisplayed())
                    return driver.findElement(By.id("R1MarketRedirect-close"));
                else
                    return null;
            }
        });

        if(stayInRegionButton != null){
            click(stayInRegionButton);
        }

        PageFactory.initElements(driver, this);
    }

    public void goToBuy(){
        click(comprarElement);
    }
}
