package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.model.Product;
import myprojects.automation.assignment5.model.User;
import myprojects.automation.assignment5.utils.Properties;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        driver.navigate().to(Properties.getBaseUrl());

    }

    @Test(dataProvider = "User")
    public void createNewOrder(User user) {
        driver.navigate().to(Properties.getBaseUrl());
        actions.openRandomProduct();
        Product product = actions.getOpenedProductInfo();

        actions.click(By.cssSelector(".add-to-cart"));
        actions.verifyBasket(product);

        actions.click(By.cssSelector(".cart-summary .btn"));
        actions.setOrderData(user);

        actions.verifyOrderConfirmation(product);

        actions.verifyLeftQuntity(product);






    }

}
