package com.jeannot;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CommonStepDefinitions {

    @Autowired
    private DriverManagerDataSource testAppDataSource;

    private static WebDriver driver;
    private String testAppURL;

    @Before(order = 0) //should run first
    public void setUp() throws Exception {
        System.setProperty("jna.nosys", "true"); //not sure why I need this, but it gets rid of an error msg...
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream("/testing.properties");
        props.load(in);
        in.close();
        testAppURL = props.getProperty("test.url");

        //NB this will use the embedded .dll version of the IE Driver, even though we get a warning message the whole time:
        //"The path to the driver executable must be set by the webdriver.ie.driver system property..."
        //We are currently using Selenium 2.24.1, but at some point after that version they stopped embedding
        //the DLL. This means that the IEDriverServer.exe needs to be downloaded, which of course may be difficult
        //to do in the secure Test App environment. So we have to stick to one of the earlier versions for now...
        if (driver==null) {
            driver = new InternetExplorerDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //set global polling wait for elements to appear

              //Shutdown hook not needed, we close and recreate the browser on each scenario
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        driver.quit();
//                    }
//                    catch (Exception e) {
//                        System.out.println("ShutdownHook: threw exception");
//                        e.printStackTrace();
//                    }
//                }
//            });
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        //Take a screenshot if the scenario has failed...
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/jpeg");
            } catch (WebDriverException wde) {
                System.err.println(wde.getMessage());
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
        }
        driver.quit();
        driver = null;
    }

    @Given("^I am using Internet Explorer$")
    public void i_am_using_internet_explorer() {
        assertNotNull(driver);
    }

    @When("^I navigate to the homepage$")
    public void i_navigate_to_the_homepage() {
        driver.get(testAppURL);
    }

    @When("^I navigate to the Dashboard$")
    public void i_navigate_to_the_Dashboard() {
        WebElement generalMenuElement = driver.findElement(By.id("gwt-debug-generalMenumenuItem"));
        assertNotNull(generalMenuElement);
        generalMenuElement.click();
        WebElement dashboardMenuElement = driver.findElement(By.id("gwt-debug-dashBoardMenu"));
        assertNotNull(dashboardMenuElement);
        dashboardMenuElement.click();
    }

    @When("^I click OK on the confirmation box$")
    public void I_click_OK_on_the_confirmation_box() throws Throwable {
        WebElement saveConfirmElement = driver.findElement(By.id("gwt-debug-confirmationBtnConfirm"));
        assertNotNull("We should have a confirmation box",saveConfirmElement);
        saveConfirmElement.click();
    }

    @Given("^I log into Test App as \"([^\"]*)\"$")
    public void I_log_into_Test_App_as(String userType) throws Throwable {
        String user = getUseridForUserType(userType);
        String password = getPasswordForUserType(userType);
        assertNotNull("we should have a user for user type: " + userType,userType);
        assertNotNull("we should have a password for user type: " + userType,userType);
        driver.get(testAppURL);
        By byTagInput = By.tagName("input");
        List<WebElement> elements = driver.findElements(byTagInput); //get the username/password input fields
        assertTrue("We should have 2 input elements on the login form", elements.size()==2);
        WebElement usernameElement = elements.get(0);
        WebElement passwordElement = elements.get(1);
        usernameElement.sendKeys(user);
        passwordElement.sendKeys(password);
        By byTagButton = By.tagName("button"); //get the Login button
        WebElement button = driver.findElement(byTagButton);
        button.click();
        WebElement adminMenuElement = driver.findElement(By.id("gwt-debug-adminMenumenuItem"));
        assertNotNull(adminMenuElement);
        String expectedPageTitle = "Test App";
        String expectedDashboardText = "Dashboard";
        String title = driver.getTitle();
        assertTrue("The expected result was: " + expectedPageTitle + ", but the title actually was: " + title, title.equals(expectedPageTitle));
        assertTrue("The following text was expected: " + expectedDashboardText, driver.getPageSource().contains(expectedDashboardText));
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getTestAppURL() {
        return testAppURL;
    }

    public DriverManagerDataSource getTestAppDataSource() {
        return testAppDataSource;
    }

    public String getUseridForUserType(String userType) {
        String result = null;
        if (userType.equalsIgnoreCase("administrator")) {
            result = "a";
        }
        return result;
    }

    public String getPasswordForUserType(String userType) {
        String result = null;
        //TODO move this to a properties file
        if (userType.equalsIgnoreCase("administrator")) {
            result = "a";
        }
        return result;
    }

}
