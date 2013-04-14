package com.jeannot;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import com.jeannot.CommonStepDefinitions;
import com.jeannot.GwtIds;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class TaskStepDefinitions {

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private WebDriver driver;
    private String testAppURL;
    private String planName;
    private String resourceName;

    @Before
    public void setUp() throws Exception {
        driver = commonStepDefinitions.getDriver();
        testAppURL = commonStepDefinitions.getTestAppURL();
        planName = "TP-" + System.currentTimeMillis(); //plan name for this test
        resourceName = "TRES-" + System.currentTimeMillis(); //resource name for this test
    }

    @Given("^I navigate to Planning$")
    public void I_navigate_to_Planning() {
        WebElement taskManagementMenuElement = driver.findElement(By.id("gwt-debug-taskMenumenuItem"));
        assertNotNull(taskManagementMenuElement);
        taskManagementMenuElement.click();
        WebElement planningMenuElement = driver.findElement(By.id("gwt-debug-taskMenuPlanningTSM"));
        assertNotNull(planningMenuElement);
        planningMenuElement.click();
    }

    @Given("^I navigate to Create Plan$")
    public void I_navigate_to_Create_Plan() throws Throwable {
        WebElement createPlanElement = driver.findElement(By.id("gwt-debug-toolbar-plan-button-inner"));
        assertNotNull(createPlanElement);
        createPlanElement.click();
    }

    @Given("^I name the plan$")
    public void I_name_the_plan() throws Throwable {
        WebElement planNameElement = driver.findElement(By.id("gwt-debug-plan_name-textbox"));
        assertNotNull(planNameElement);
        planNameElement.sendKeys(planName);
    }

    @Given("^I fill in the following plan data:$")
    public void I_fill_in_the_following_plan_data(DataTable arg1) throws Throwable {
        for (DataTableRow row : arg1.getGherkinRows()) {
           String gwtId = GwtIds.getGwtId(row.getCells().get(0));
           String value = row.getCells().get(1);
           WebElement elem = driver.findElement(By.id(gwtId));
           elem.click();
           elem.clear();
           elem.sendKeys(value);
        }
    }

    @Given("^I click Save on the plan$")
    public void I_click_Save_on_the_plan() throws Throwable {
        WebElement savePlanElement = driver.findElement(By.id("gwt-debug-editButtonBar-save"));
        assertNotNull(savePlanElement);
        savePlanElement.click();
    }

    @Then("^I see the plan in the list of plans$")
    public void I_see_the_plan_in_the_list_of_plans() throws Throwable {
        List<WebElement> elements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        boolean planNameWasFound = false;
        for (WebElement element : elements) {
            if (element.getText().equals(planName)) {
                planNameWasFound = true;
                break;
            }
        }
        assertTrue(planNameWasFound);
    }

    @Given("^I enter the plan$")
    public void I_enter_the_plan() throws Throwable {
        List<WebElement> elements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        boolean planNameWasFound = false;
        for (WebElement element : elements) {
            if (element.getText().equals(planName)) {
                Actions planAction = new Actions(driver);
                planAction.doubleClick(element);
                planAction.perform();
                planNameWasFound = true;
                break;
            }
        }
        assertTrue(planNameWasFound);
    }

    @Given("^I click on View Timeline$")
    public void I_click_on_View_Timeline() throws Throwable {
        WebElement viewTimelineElement = driver.findElement(By.id("gwt-debug-toolbar-timeline-button-inner"));
        assertNotNull(viewTimelineElement);
        viewTimelineElement.click();
    }

    @Given("^I click on Create Resource/Asset$")
    public void I_click_on_Create_Resource_Asset() throws Throwable {
        WebElement createResourceAssetElement = driver.findElement(By.id("gwt-debug-toolbar-resource-button-inner"));
        assertNotNull(createResourceAssetElement);
        assertTrue(createResourceAssetElement.isEnabled());
        createResourceAssetElement.click();
    }

    @Given("^I enter the resource name$")
    public void I_enter_the_resource_name() throws Throwable {
        WebElement resourceNameElement = driver.findElement(By.id("gwt-debug-plan_name-textbox"));
        assertNotNull(resourceNameElement);
        resourceNameElement.sendKeys(resourceName);
    }

    @Given("^I click Save on the resource$")
    public void I_click_Save_on_the_resource() throws Throwable {
        WebElement resourceSaveElement = driver.findElement(By.id("gwt-debug-reportButtonBar-save"));
        assertNotNull(resourceSaveElement);
        resourceSaveElement.click();
    }

    @Then("^the plan is selectable in the timeline$")
    public void the_resource_is_visible_in_the_timeline() throws Throwable {
        driver.manage().window().maximize();
        WebElement menuButton = driver.findElement(By.id("gwt-debug-generalMenumenuItem"));
        Actions action = new Actions(driver);
        action.moveToElement(menuButton);
        action.moveByOffset(0,140);
        action.doubleClick();
        action.perform(); //we should now be on the Plan page
        WebElement planNameElement = driver.findElement(By.id("gwt-debug-plan_name-textbox"));
        assertEquals("We should be on the plan form for the right plan",planName,planNameElement.getAttribute("value"));
    }

    @Then("^I add resource from the timeline$")
    public void I_add_resource_from_the_timeline() throws Throwable {
        driver.manage().window().maximize();
        WebElement menuButton = driver.findElement(By.id("gwt-debug-generalMenumenuItem"));
        Actions action = new Actions(driver);
        action.moveToElement(menuButton);
        action.moveByOffset(0,140);
        action.click();
        action.perform(); //we should now be on the Plan page
        WebElement addResourceElement = driver.findElement(By.id("gwt-debug-toolbar-resource-button-inner"));
        assertTrue("the AddResource button should be enabled", addResourceElement.isEnabled());
        addResourceElement.click();
    }

    @Then("^I am on the New Resource form$")
    public void I_am_on_the_New_Resource_form() throws Throwable {
        assertTrue(driver.getPageSource().contains("New Resource"));
    }
}

