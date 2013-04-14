package com.jeannot;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import com.jeannot.CommonStepDefinitions;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WebsiteAvailableStepDefinitions {

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private WebDriver driver;
    private String testAppURL;

    @Before
    public void setUp() throws Exception {
        driver = commonStepDefinitions.getDriver();
        testAppURL = commonStepDefinitions.getTestAppURL();
    }

    @Then("^I should see the login page$")
    public void i_should_see_the_login_page() {
        WebElement useridElement = driver.findElement(By.id("gwt-debug-loginView-username"));
        assertNotNull("We should have a login form",useridElement);
    }

    @Then("^I should see the login page with error \"([^\"]*)\"$")
    public void i_should_see_the_login_page_with_error(String errorCode) {
        WebElement useridElement = driver.findElement(By.id("gwt-debug-loginView-username"));
        assertNotNull("We should have a login form",useridElement);
        //Wait to make sure error dialog has had time to appear...
        List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class,'com-jeannot-testapp-gwt-client-widgets-dialogs-ErrorDialogBox-ErrorDialogCss-base')]"));
        assertEquals("We should have an error dialogue box",1,elements.size());
        String expectedErrorCodeText = "Username or password provided is invalid";
        assertTrue("The following error code text was expected: " + expectedErrorCodeText, driver.getPageSource().contains(expectedErrorCodeText));
    }

    @When("^I login with user \"(\\w+)\" and password \"(\\w+)\"$")
    public void i_login(String username, String password) {
        By byTagInput = By.tagName("input");
        List<WebElement> elements = driver.findElements(byTagInput); //get the username/password input fields
        assertTrue("We should have 2 input elements on the login form", elements.size()==2);
        WebElement usernameElement = elements.get(0);
        WebElement passwordElement = elements.get(1);
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        By byTagButton = By.tagName("button"); //get the Login button
        WebElement button = driver.findElement(byTagButton);
        button.click();
    }

    @Then("^I see the dashboard$")
    public void i_should_see_the_initial_logged_in_page() {
        WebElement adminMenuElement = driver.findElement(By.id("gwt-debug-adminMenumenuItem"));
        assertNotNull(adminMenuElement);
        String expectedPageTitle = "Test App";
        String expectedDashboardText = "Dashboard";
        String title = driver.getTitle();
        assertTrue("The expected result was: " + expectedPageTitle + ", but the title actually was: " + title, title.equals(expectedPageTitle));
        assertTrue("The following text was expected: " + expectedDashboardText, driver.getPageSource().contains(expectedDashboardText));
    }

}
