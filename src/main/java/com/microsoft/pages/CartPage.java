package com.microsoft.pages;

import com.microsoft.selenium.Base;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage extends Base {

    @FindBy(xpath = "//div[@class='greyBox--gLiKFFBr']/div[2]/div/span/span[2]")
    WebElement totalPriceElement;

    @FindBy(xpath = "//button[text()='Quitar']")
    WebElement deleteItemBtnElement;

    @FindBy(xpath = "//p[@class='c-paragraph-2']")
    WebElement itemsCartSectionElement;

    public CartPage(WebDriver driver) {
        super(driver);
        WebDriverWait eWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        eWait.until(ExpectedConditions.titleIs("Carro de la compra - Microsoft Store"));
        PageFactory.initElements(driver, this);
    }

    public String deleteItems() {
        click(deleteItemBtnElement);
        return getText(itemsCartSectionElement);
    }

    public double getTotalPrice() {
        String totalPrice = getText(totalPriceElement).replace("MXN$", "");
        double onlyPriceNumber = Double.parseDouble(totalPrice);
        return onlyPriceNumber;
    }
}
