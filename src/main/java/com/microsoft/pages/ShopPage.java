package com.microsoft.pages;

import com.microsoft.selenium.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopPage extends Base {

    @FindBy(xpath = "//a[@aria-label='Refine by Aplicaciones']")
    WebElement appsDepartmentSection;

    @FindAll(@FindBy(className = "m-channel-placement-item"))
    List<WebElement> itemsForSale;

    @FindBy(xpath = "//li[@aria-label='Aplicaciones']")
    WebElement tittleChoiceElement;

    @FindBy(css = "a[aria-label='página siguiente']")
    WebElement nextPageBtnElement;

    @FindBy(css = "a[aria-label='página 1']")
    WebElement firstPageBtnElement;

    @FindBy(css = "a[name='refine'][aria-label='Refine by Menos de MXN$65.00']")
    WebElement firstNonFreeOptionElement;

    @FindBy(id = "ButtonPanel_buttonPanel_OverflowMenuTrigger")
    WebElement threeDotBtnElement;

    @FindBy(id = "buttonPanel_AddToCartButton")
    WebElement addToCarBtnElement;

    @FindBy(xpath = "//div[@aria-label='Regístrate al boletín Microsoft Store']")
    WebElement registerDialogElement;

    @FindBy(xpath = "//div[@aria-label='Regístrate al boletín Microsoft Store']/div[@role='button']")
    WebElement closeRegisterDialogElement;

    public ShopPage(WebDriver driver) {
        super(driver);
        WebDriverWait eWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        eWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("refine-by-menu-title-Categorías")));
        PageFactory.initElements(driver, this);
    }

    public void clickAppsDepartment(){
        click(appsDepartmentSection);
        waitUntilElementIsVisible(tittleChoiceElement, 5);
    }

    public void countAndSumPricesOfItemsForNPages(boolean printTitles, int pagesToSum){
        int amountOfTitles = 0;
        double sumPriceTitles = 0;
        boolean isAllPagesVisited;
        int i = 0;

        if(printTitles)
            System.out.println("The list of the tittles are:\n");

        do {
            double[] dataOfThePage = countAndSumPricesOfItemsForPage(printTitles);
            amountOfTitles += dataOfThePage[0];
            sumPriceTitles += dataOfThePage[1];
            i++;
            isAllPagesVisited = pagesToSum > i;
            if(isAllPagesVisited)
                goNextPage();
        }while (isAllPagesVisited);

        System.out.println("\nThe sum of the price of the "+amountOfTitles+" titles is MXN$"+sumPriceTitles+" found in the first "+pagesToSum+" pages");
    }

    private double[] countAndSumPricesOfItemsForPage(boolean printTitles){
        double[] dataOfThePage = new double[2];
        double totalTitles = 0;
        double sumPrices = 0;

        if(printTitles){
            for (WebElement element : itemsForSale) {
                String[] titleAndPrice = element.findElement(By.className("c-channel-placement-content")).getText().split("\n");
                sumPrices += getPriceAndPrintTittle(titleAndPrice);
                totalTitles++;
            }
        }else {
            for (WebElement element : itemsForSale) {
                String[] titleAndPrice = element.findElement(By.className("c-channel-placement-content")).getText().split("\n");
                sumPrices += getPrice(titleAndPrice);
            }
        }

        dataOfThePage[0] = totalTitles;
        dataOfThePage[1] = sumPrices;
        return dataOfThePage;
    }

    private double getPrice(String[] titleAndPrice) {
        if(titleAndPrice[titleAndPrice.length -1].contains("MXN$")){
            String price = titleAndPrice[titleAndPrice.length -1].replace("MXN$", "");
            return Double.parseDouble(price);
        }else {
            return 0;
        }
    }

    private double getPriceAndPrintTittle(String[] titleAndPrice) {

        if(titleAndPrice[titleAndPrice.length -1].contains("Gratis")){
            printTitle(titleAndPrice);
            return 0;
        }else if(titleAndPrice[titleAndPrice.length -1].contains("MXN$")){
            printTitle(titleAndPrice);
            String price = titleAndPrice[titleAndPrice.length - 1].replace("MXN$", "");
            return Double.parseDouble(price);
        }else if(titleAndPrice[titleAndPrice.length -1].contains("+")){
            List<String> shortTittleAndPrice = new ArrayList<>();
            Collections.addAll(shortTittleAndPrice, titleAndPrice);
            int indexToRemove = shortTittleAndPrice.size() -1;
            shortTittleAndPrice.remove(indexToRemove);
            printTitle(shortTittleAndPrice.toArray(new String[0]));
            return 0;
        }

        return 0;
    }

    private void printTitle(String[] titleAndPrice) {
        StringBuilder title = new StringBuilder();

        if(titleAndPrice[0].contains("Ahorra")){
            for (int i = 1; i < titleAndPrice.length -4; i++) {
                title.append(titleAndPrice[i]).append(" ");
            }
        }else {
            for (int i = 0; i < titleAndPrice.length -1; i++) {
                title.append(titleAndPrice[i]).append(" ");
            }
        }
        System.out.println("* "+title.substring(0, title.length()-1));
    }


    public void goNextPage() {
        click(nextPageBtnElement);
    }

    public void goFirstPage(){
        click(firstPageBtnElement);
    }

    public void selectFirstNonFreeOpt(){
        if(!isDisplayed(firstNonFreeOptionElement)) {
            scrollUntilWebElement(firstNonFreeOptionElement);
        }
        click(firstNonFreeOptionElement);
    }


    public double addToCarFirstItem() {
        WebElement itemForSale = itemsForSale.get(0);
        String [] titleAndPrice = itemForSale.findElement(By.className("c-channel-placement-content")).getText().split("\n");
        double pricesItem = getPrice(titleAndPrice);
        click( itemForSale );

        waitUntilElementIsVisible(registerDialogElement, 3);

        if(isDisplayed(registerDialogElement)){
            click(closeRegisterDialogElement);
        }
        waitUntilElementIsVisible(threeDotBtnElement, 5);
        click(threeDotBtnElement);
        click(addToCarBtnElement);

        return pricesItem;
    }
}
