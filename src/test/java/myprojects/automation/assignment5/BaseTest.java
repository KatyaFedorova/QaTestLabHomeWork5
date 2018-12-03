package myprojects.automation.assignment5;

import com.google.gson.Gson;
import myprojects.automation.assignment5.model.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Base script functionality, can be used for all Selenium scripts.
 */
public abstract class BaseTest {
    protected EventFiringWebDriver driver;
    protected GeneralActions actions;
    protected boolean isMobileTesting;

    /**
     * Prepares {@link WebDriver} instance with timeout and browser window configurations.
     *
     * Driver type is based on passed parameters to the automation project,
     * creates {@link ChromeDriver} instance by default.
     *
     */
    @BeforeClass
    @Parameters({"selenium.browser", "selenium.grid"})
    public void setUp(@Optional("chrome") String browser, @Optional("") String gridUrl) {
        // TODO create WebDriver instance according to passed parameters
        // driver = new EventFiringWebDriver(....);
        // driver.register(new EventHandler());
        // ...

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        // unable to maximize window in mobile mode
        if (!isMobileTesting(browser))
            driver.manage().window().maximize();

        isMobileTesting = isMobileTesting(browser);

        actions = new GeneralActions(driver);
    }

    /**
     * Closes driver instance after test class execution.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     *
     * @return Whether required browser displays content in mobile mode.
     */
    private boolean isMobileTesting(String browser) {
        switch (browser) {
            case "android":
                return true;
            case "firefox":
            case "ie":
            case "internet explorer":
            case "chrome":
            default:
                return false;
        }
    }

    @DataProvider(name = "loginData")
    protected Object[][] loginData () {
        return new Object[][]{{"webinar.test@gmail.com","Xcg7299bnSmMuRLp9ITw"}};
    }

    @DataProvider(name = "User")
    protected Object[] getUserData () {
        return new Object[][] {{getDataFromFile("user.json")}};
    }

    private static User getDataFromFile(String accountFileName) {
        Gson gson = new Gson();
        User user;
        try (FileReader reader = new FileReader("data/" + accountFileName)) {
            user = gson.fromJson(reader, User.class);
        } catch (Exception e) {
            System.err.println("ОШИБКА! " + e.getMessage());
            throw new RuntimeException(e);
        }
        return user;
    }
}
