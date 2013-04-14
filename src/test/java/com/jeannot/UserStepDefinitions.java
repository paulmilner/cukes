package com.jeannot;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import com.jeannot.CommonStepDefinitions;
import com.jeannot.GwtIds;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserStepDefinitions {

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private WebDriver driver;
    private String testAppURL;
    private String userName;

    @Before
    public void setUp() throws Exception {
        driver = commonStepDefinitions.getDriver();
        testAppURL = commonStepDefinitions.getTestAppURL();
        userName = "TU-" + System.currentTimeMillis(); //the User name for this test
    }

    @Given("^I navigate to Users$")
    public void I_click_on_the_Administration_tab() throws Throwable {
        WebElement adminMenuElement = driver.findElement(By.id("gwt-debug-adminMenumenuItem"));
        assertNotNull(adminMenuElement);
        adminMenuElement.click();
        WebElement usersMenuElement = driver.findElement(By.id("gwt-debug-adminMenuUserMgmtmenuItem"));
        assertNotNull(usersMenuElement);
        usersMenuElement.click();
        WebElement allUsersMenuElement = driver.findElement(By.id("gwt-debug-userMgmtUsersSubMenu"));
        assertNotNull(allUsersMenuElement);
        allUsersMenuElement.click();
    }

    @Then("^I see the User list$")
    public void I_should_see_the_User_list() throws Throwable {
        WebElement elem = driver.findElement(By.id("gwt-debug-userSummaryView-sfb"));
        assertTrue("We should be on the User List page", driver.getPageSource().contains("Users"));
    }

    @When("^I choose to add a new user$")
    public void I_choose_to_add_a_new_user() throws Throwable {
        WebElement addNewUserMenuElement = driver.findElement(By.id("gwt-debug-userSummaryView-tbb-addNew"));
        assertNotNull(addNewUserMenuElement);
        addNewUserMenuElement.click();
    }

    @When("^I enter the new user details$")
    public void I_enter_the_new_user_details() throws Throwable {
        WebElement userNameElement = driver.findElement(By.id("gwt-debug-userFormView-username-textbox"));
        userNameElement.sendKeys(userName);
        WebElement userEmailElement = driver.findElement(By.id("gwt-debug-userFormView-email-textbox"));
        userEmailElement.sendKeys(userName + "@test.com");
        WebElement userHQElement = driver.findElement(By.id("gwt-debug-userFormView-hqCombo-inner-textbox")); //HQ
        userHQElement.clear();
        userHQElement.sendKeys("LocalSL-HQ-A"); //default Local HQ - TODO I am assuming it must be in the DB already...
        WebElement userRoleElement = driver.findElement(By.id("gwt-debug-userFormView-roleCheckbox0-input")); //1st role checkbox
        userRoleElement.click();
        WebElement userSaveElement = driver.findElement(By.id("gwt-debug-userFormViewButtonBar-save"));
        assertTrue(userSaveElement.isEnabled());
        userSaveElement.click();
    }

    //This assumes that user "a" already exists. This is the default user all tests sign in with.
    @When("^I enter the new user details with a username that already exists$")
    public void I_enter_the_new_user_details_with_a_username_that_already_exists() throws Throwable {
        WebElement userNameElement = driver.findElement(By.id("gwt-debug-userFormView-username-textbox"));
        userNameElement.sendKeys("a");
        WebElement userEmailElement = driver.findElement(By.id("gwt-debug-userFormView-email-textbox"));
        userEmailElement.sendKeys(userName + "@test.com");
        WebElement userHQElement = driver.findElement(By.id("gwt-debug-userFormView-hqCombo-inner-textbox")); //HQ
        userHQElement.clear();
        userHQElement.sendKeys("LocalSL-HQ-A"); //default Local HQ - TODO I am assuming it must be in the DB already...
        WebElement userRoleElement = driver.findElement(By.id("gwt-debug-userFormView-roleCheckbox0-input")); //1st role checkbox
        userRoleElement.click();
        WebElement userSaveElement = driver.findElement(By.id("gwt-debug-userFormViewButtonBar-save"));
        assertTrue(userSaveElement.isEnabled());
        userSaveElement.click();
    }

    @When("^I click Save on the user$")
    public void I_click_Save_on_the_user() {
        WebElement saveUserButton = driver.findElement(By.id("gwt-debug-userFormViewButtonBar-save"));
        assertNotNull(saveUserButton);
        assertTrue(saveUserButton.isEnabled());
        saveUserButton.click();
    }

    @Then("^I see an error message telling me the username could not be authenticated$")
    public void I_see_an_error_message_telling_me_the_username_could_not_be_authenticated() throws Throwable {
        WebElement cancelButton = driver.findElement(By.id("gwt-debug-exbtncancel"));
        assertTrue(driver.getPageSource().contains("User could not be created because the username could not be authenticated against the Directory Authentication Service"));
        cancelButton.click();
    }

    @Then("^I see an error message telling me the username already exists$")
    public void I_see_an_error_message_telling_me_the_username_already_exists() throws Throwable {
        WebElement cancelButton = driver.findElement(By.id("gwt-debug-exbtncancel"));
        assertTrue(driver.getPageSource().contains("Username already exists"));
        cancelButton.click();
    }
}
