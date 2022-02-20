package com.microsoft;

import com.microsoft.components.ComponentsManager;
import com.microsoft.components.NavigatorMenu;
import com.microsoft.pages.CartPage;
import com.microsoft.pages.ExplorePage;
import com.microsoft.pages.HomePage;
import com.microsoft.pages.ShopPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class sumAppsAndBuyOneTest {

    private WebDriver driver;

    private NavigatorMenu navigatorMenu;

    private HomePage homePage;
    private ShopPage shopPage;
    private CartPage cartPage;

    @BeforeClass
    public void setUp(){
        homePage = new HomePage(driver);
        driver = homePage.chromeDriverConnection();
    }

    @DataProvider(name = "readJson")
    public Object[][] readJson(Method method) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(".\\src\\test\\java\\com\\microsoft\\resources\\sumAppsAndBuyOneScenarios.json");
        Object obj = jsonParser.parse(reader);
        JSONObject dataObj = (JSONObject) obj;
        JSONArray dataArray = (JSONArray)dataObj.get("dataToTest");

        String[][] data = new String[dataArray.size()][1];

        for (int i = 0; i<dataArray.size(); i++){
            JSONObject currentData = (JSONObject)dataArray.get(i);

            String url = (String) currentData.get("url");
            String itemToSearch = (String) currentData.get("itemToSearch");
            String pagesToSome = (String)currentData.get("pagesToSum");
            String messageToAssert = (String)currentData.get("messageToAssert");

            switch (method.getName()){
                case "openPage":{
                    data[i][0] = url;
                    return data;
                }
                case "openWindowsSectionAndSearchItem":{
                    data[i][0] = itemToSearch;
                    return data;
                }case "countTitlesAndSumPrices":{
                    data[i][0] = pagesToSome;
                    return data;
                } case "DeleteItemFromShoppingCar":{
                    data[i][0] = messageToAssert;
                    return data;
                }
            }
        }
        return null;
    }

    @Test(dataProvider = "readJson", priority = 0)
    public void openPage(String url){
        homePage = new HomePage(driver);
        homePage.openURL(url);
    }

    @Test(dataProvider = "readJson", priority = 1)
    public void openWindowsSectionAndSearchItem(String itemToSearch){
        ComponentsManager componentsManager = new ComponentsManager(driver);
        navigatorMenu = componentsManager.getNavigatorMenu();
        navigatorMenu.clickOnWindowsOption();
        navigatorMenu.search(itemToSearch);
    }

    @Test(dataProvider = "readJson", priority = 2)
    public void countTitlesAndSumPrices(String pagesToSum){
        ExplorePage explorePage = new ExplorePage(driver);
        explorePage.goToBuy();

        shopPage = new ShopPage(driver);
        shopPage.clickAppsDepartment();

        shopPage.countAndSumPricesOfItemsForNPages(true, Integer.parseInt(pagesToSum));
    }

    @Test(priority = 3)
    public void selectFirstNonFreeOption(){
        shopPage.goFirstPage();
        shopPage.selectFirstNonFreeOpt();
    }

    @Test(priority = 4)
    public void comparePricesBeforeAndAfterAddToCart(){
        double priceBeforeAddToCart = shopPage.addToCarAnyItem();
        cartPage = new CartPage(driver);
        int itemsInCar = navigatorMenu.countItemFromShoppingCar();
        Assert.assertTrue(itemsInCar > 0, "You are not in the Shopping Cart Page or there are not items in the car");
        double priceAfterAddToCart = cartPage.getTotalPrice();
        Assert.assertEquals(priceBeforeAddToCart, priceAfterAddToCart, "The prices before and after add item to cart are different.");
    }

    @Test(dataProvider = "readJson", priority = 5)
    public void DeleteItemFromShoppingCar(String messageToAssert){
        String emptyCartMessage = cartPage.deleteItems();
        Assert.assertEquals(emptyCartMessage, messageToAssert);
        int itemsInCar = navigatorMenu.countItemFromShoppingCar();
        Assert.assertTrue(itemsInCar == 0, "The item was not deleted from cart");
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
