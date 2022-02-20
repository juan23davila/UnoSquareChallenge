package com.microsoft.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NavigatorMenu extends AbstractComponent {


    By windowsOptionElement = By.id("shellmenu_2");
    By magnifyingGlassElement = By.id("search");
    By searchFieldElement = By.id("cli_shellHeaderSearchInput");
    By amountShoppingCar = By.className("shopping-cart-amount");

    public NavigatorMenu(WebDriver driver, WebElement navigatorMenuSectionElement) {
        super(driver, navigatorMenuSectionElement);
        WebDriverWait eWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        eWait.until(ExpectedConditions.visibilityOfElementLocated(windowsOptionElement));
    }

    public void clickOnWindowsOption(){
        findElementInSection(windowsOptionElement).click();
    }


    public void search(String inputText){
        findElementInSection(magnifyingGlassElement).click();
        findElementInSection(searchFieldElement).sendKeys(inputText);
        findElementInSection(magnifyingGlassElement).click();
    }

    public int countItemFromShoppingCar(){
        String amountFromCart =  findElementInSection(amountShoppingCar).getText();
        int amountItemsInCar = 0;
        if(amountFromCart != ""){
            amountItemsInCar = Integer.parseInt(amountFromCart);
        }
        return amountItemsInCar;
    }
}
