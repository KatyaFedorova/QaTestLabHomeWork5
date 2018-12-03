package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.Product;
import myprojects.automation.assignment5.model.User;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement element;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() {
        click(By.cssSelector(".all-product-link"));
        int productCount = getElementCount(By.cssSelector(".products.row [data-id-product]"));
        Random random = new Random();
        //actions.click(By.cssSelector(String.format(".products.row [data-id-product = \"%s\"]", Math.round(random.nextInt(100_00) + productCount) / 100)));
        click(By.cssSelector(String.format(".products.row [data-id-product = \"%s\"].product-title", 1)));
    }

    public Product getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        String productName = getText(By.cssSelector(".col-md-6 [itemprop = \"name\"]"));
        String productQty = getAttribute(By.cssSelector(".col-md-6 [itemprop = \"price\"]"), "content");
        String productPrice = getText(By.cssSelector(".product-quantities span")).split(" ")[0];
        return new Product(productName, Integer.valueOf(productQty), Float.valueOf(productPrice));
    }

    public void verifyBasket(Product product) {
        click(By.cssSelector(".modal-body .btn.btn-primary"));
        Assert.assertEquals(1, getElementCount(By.cssSelector(".card.cart-container")));
        String basketProductTotal = getAttribute(By.cssSelector(".label.js-subtotal"), "value");
        Assert.assertEquals(1, DataConverter.parseStockValue(basketProductTotal));
        String basketProductName = getAttribute(By.cssSelector(".product-line-info:nth-of-type(1) a"), "value");
        Assert.assertEquals(product.getName(), basketProductName);
        String basketProductPrise = getAttribute(By.cssSelector(".product-line-info:nth-of-type(2) a"), "value");
        Assert.assertEquals(product.getPrice(), DataConverter.parsePriceValue(basketProductPrise));
    }

    public void setOrderData(User user) {
        set(By.name("firstname"), user.getFirstName());
        set(By.name("lastname"), user.getLastName());
        set(By.name("email"), user.getEmail());
        click(By.className(".continue.btn"));
        set(By.name("address1"), String.format("%s, %s", user.getAddress().getStreet(), user.getAddress().getHouse()));
        set(By.name("postcode"), user.getAddress().getPostCode());
        set(By.name("city"), user.getAddress().getCity());
        click(By.className(".continue.btn"));
        click(By.className(".continue.btn"));
        click(By.id("payment-option-1"));
        click(By.name("conditions_to_approve[terms-and-conditions]"));
        click(By.cssSelector("#payment-confirmation button"));
    }

    public void verifyOrderConfirmation(Product product) {
        verifyElementWithTextPresent(By.id("order_confirmation"), "Ваш заказ подтверждён");
        verifyElementWithTextPresent(By.className("font-weight-bold"), product.getPrice() + " ₴");
        verifyElementWithTextPresent(By.cssSelector(".details"), product.getName());
        verifyElementWithTextPresent(By.cssSelector(".col-xs-2"), "1");
    }

    public void verifyLeftQuntity(Product product) {
        click(By.cssSelector(".logo.img-responsive"));
        set(By.cssSelector("#search_widget input[type = \"hidden\"]"), product.getName());
        click(By.cssSelector("#search_widget .material-icons.search"));
        click(By.cssSelector(String.format(".products.row [data-id-product = \"%s\"].product-title", 1)));
        String productQty = getAttribute(By.cssSelector(".col-md-6 [itemprop = \"price\"]"), "content");
        Assert.assertEquals(Integer.valueOf(productQty), Integer.valueOf(product.getQty() - 1));
    }



    //===================================== BaseMethods ================================================================

    private void openUrl(String url) {
        driver.get(url);
    }

    private void getElement(By locator) {
        element = driver.findElement(locator);
    }

    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    public String getAttribute(By locator, String attributeName) {
        element = driver.findElement(locator);
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getAttribute(attributeName);
    }

    public String getText(By locator) {
        element = driver.findElement(locator);
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    public void click(By locator) {
        element = driver.findElement(locator);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void set(By locator, String text) {
        element = driver.findElement(locator);
        wait.until(ExpectedConditions.visibilityOf(element));
        if (!element.getAttribute("value").isEmpty()) {
            element.clear();
        }
        element.sendKeys(text);
    }

    public void verifyElementWithTextPresent(By locator, String text) {
        element = driver.findElement(locator);
        if (!element.getAttribute("value").equals(text)) {
            throw new RuntimeException(String.format("Element %s doesn`t conteince text \"%s\"", element.getLocation(), text));
        }
    }
}
