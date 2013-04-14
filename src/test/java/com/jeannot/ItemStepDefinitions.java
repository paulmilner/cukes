package com.jeannot;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.jeannot.CommonStepDefinitions;
import com.jeannot.GwtIds;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ItemStepDefinitions {

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private WebDriver driver;
    private String testAppURL;
    private String itemName;
    private String possibleDuplicateItemName;
    private String itemListName;
    private String reportTitle;

    @Before
    public void setUp() throws Exception {
        driver = commonStepDefinitions.getDriver();
        testAppURL = commonStepDefinitions.getTestAppURL();
        itemName = makeFairlyRandomItemName();
        possibleDuplicateItemName = makeFairlyRandomItemName();
        itemListName = "TL-" + System.currentTimeMillis(); //the Item list name for this test
        reportTitle = "REP-" + System.currentTimeMillis(); //Item Folder report title for this test
    }

    @Given("^I navigate to \"Create Item\"$")
    public void I_navigate_to_create_item() {
        WebElement itemManagementMenuElement = driver.findElement(By.id("gwt-debug-tgtMenumenuItem"));
        assertNotNull(itemManagementMenuElement);
        itemManagementMenuElement.click();
        WebElement createItemMenuElement = driver.findElement(By.id("gwt-debug-tgtMenuCreateItemItem"));
        assertNotNull(createItemMenuElement);
        createItemMenuElement.click();
    }

    @When("^I enter a Item Name, Country Code and Item Type of \"([^\"]*)\" and click \"OK\"$")
    public void I_enter_Item_Name_and_Country_Code_and_Item_Type_and_click(String itemType) {
        WebElement createItemOKButton = driver.findElement(By.id("gwt-debug-create_item_ok"));
        assertNotNull(createItemOKButton);
        assertTrue(!createItemOKButton.isEnabled());
        WebElement createItemName = driver.findElement(By.id("gwt-debug-create_item_name-textbox"));
        assertNotNull(createItemName);
        createItemName.sendKeys(itemName);
        WebElement createItemCountry = driver.findElement(By.id("gwt-debug-create_item_country_code-textbox"));
        assertNotNull(createItemCountry);
        createItemCountry.sendKeys("AF");
        WebElement createItemType = driver.findElement(By.id("gwt-debug-create_item_type-inner-textbox"));
        assertNotNull(createItemType);
        createItemType.clear();
        createItemType.sendKeys(itemType);
        String createItemTypeValue = createItemType.getAttribute("value");
        assertEquals("The type should be set to correctly",itemType,createItemTypeValue);
        assertTrue(createItemOKButton.isEnabled());
        createItemOKButton.click();
    }

    @When("^I enter a second Item Name, Country Code and Item Type of \"([^\"]*)\" and click \"OK\"$")
    public void I_enter_a_second_Item_Name_and_Country_Code_and_Item_Type_and_click(String itemType) {
        WebElement createItemOKButton = driver.findElement(By.id("gwt-debug-create_item_ok"));
        assertNotNull(createItemOKButton);
        assertTrue(!createItemOKButton.isEnabled());
        WebElement createItemName = driver.findElement(By.id("gwt-debug-create_item_name-textbox"));
        assertNotNull(createItemName);
        createItemName.sendKeys(possibleDuplicateItemName);
        WebElement createItemCountry = driver.findElement(By.id("gwt-debug-create_item_country_code-textbox"));
        assertNotNull(createItemCountry);
        createItemCountry.sendKeys("AF");
        WebElement createItemType = driver.findElement(By.id("gwt-debug-create_item_type-inner-textbox"));
        assertNotNull(createItemType);
        createItemType.clear();
        createItemType.sendKeys(itemType);
        String createItemTypeValue = createItemType.getAttribute("value");
        assertEquals("The type should be set to correctly",itemType,createItemTypeValue);
        assertTrue(createItemOKButton.isEnabled());
        createItemOKButton.click();
    }

    @Then("^I see the Item Details page for the item$")
    public void the_item_is_created_and_I_see_the_Item_Details_page_for_the_item() throws Exception {
        WebElement elem = driver.findElement(By.id("gwt-debug-Tab:Item-tab")); //just need this to wait for the dialog box
        assertTrue("We should be on the Item page", driver.getPageSource().contains("Desired Effect"));
        WebElement idTab = driver.findElement(By.id("gwt-debug-Tab:Identification-tab"));
        idTab.click(); // click to the Identification tab
        String itemName = driver.findElement(By.id("gwt-debug-itemName-textbox")).getAttribute("value");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(commonStepDefinitions.getTestAppDataSource());
        int rowCount = jdbcTemplate.queryForInt("select count(*) from dbo.item_view where item_name = '" + itemName + "'");
        jdbcTemplate.getDataSource().getConnection().close();
        assertTrue("There should be exactly 1 row for this item in the DB", rowCount == 1);
        WebElement itemTab = driver.findElement(By.id("gwt-debug-Tab:Item-tab"));
        itemTab.click();
    }

    @When("^I click Save$")
    public void I_click_Save() {
        WebElement saveItemButton = driver.findElement(By.id("gwt-debug-Test AppButton-save"));
        assertNotNull(saveItemButton);
        assertTrue(saveItemButton.isEnabled());
        saveItemButton.click();
    }

    @Then("^I should see a Validation Error$")
    public void I_see_a_Validation_Error() throws Throwable {
        WebElement elem = driver.findElement(By.id("gwt-debug-closeValidation")); //just need this to wait for the dialog box
        assertTrue(driver.getPageSource().contains("Validation Error"));
    }

    @Then("^the save should succeed$")
    public void the_save_should_succeed() throws Throwable {
        WebElement elem = driver.findElement(By.id("gwt-debug-close")); //just need this to wait for the dialog box
        assertTrue(driver.getPageSource().contains("Save successful!"));
    }

    @When("^I enter the following Item tab data:$")
    public void I_enter_the_following_item_tab_data(DataTable arg1) throws Throwable {
         for (DataTableRow row : arg1.getGherkinRows()) {
            String gwtId = GwtIds.getGwtId(row.getCells().get(0));
            String value = row.getCells().get(1);
            WebElement elem = driver.findElement(By.id(gwtId));
            elem.click();
            elem.clear();
            elem.sendKeys(value);
         }
    }

    @When("^I enter the following Item tab rich text field data:$")
    public void I_enter_the_following_item_tab_rich_text_field_data(DataTable arg1) throws Throwable {
         for (DataTableRow row : arg1.getGherkinRows()) {
            String gwtId = GwtIds.getGwtId(row.getCells().get(0));
            String value = row.getCells().get(1);
            WebDriver frame = driver.switchTo().frame(driver.findElement(By.id(gwtId)));
            WebElement elem = frame.findElement(By.xpath("//*"));
            elem.click();
            elem.sendKeys(value);
            driver.switchTo().defaultContent();
         }
    }

    @When("^I click OK in the Save Successful box$")
    public void I_click_OK_in_the_Save_Successful_box() throws Throwable {
        WebElement okButton = driver.findElement(By.id("gwt-debug-close"));
        assertNotNull(okButton);
        okButton.click();
    }

    @Then("^I see the following saved Item tab data:$")
    public void I_see_the_following_saved_Item_tab_data(DataTable arg1) throws Throwable {
        for (DataTableRow row : arg1.getGherkinRows()) {
           String fieldId = row.getCells().get(0);
           String gwtId = GwtIds.getGwtId(fieldId);
           String value = row.getCells().get(1);
           WebElement elem = driver.findElement(By.id(gwtId));
           assertEquals("Text for the field \"" + fieldId + "\" should be: " + value, value, elem.getAttribute("value"));
        }
    }

    @Then("^I see the following saved Item tab rich text field data:$")
    public void I_see_the_following_saved_Item_tab_rich_text_field_data(DataTable arg1) throws Throwable {
        for (DataTableRow row : arg1.getGherkinRows()) {
           String fieldId = row.getCells().get(0);
           String gwtId = GwtIds.getGwtId(fieldId);
           String value = row.getCells().get(1);
           WebDriver frame = driver.switchTo().frame(driver.findElement(By.id(gwtId)));
           WebElement elem = frame.findElement(By.xpath("//*"));
           String val = elem.getText();
           String val2 = elem.getAttribute("value");
           assertEquals("Text for the RTF field \"" + fieldId + "\" should be: " + value, value, elem.getText());
           driver.switchTo().defaultContent();
        }
    }

    @When("^I navigate to \"Create Item List\"$")
    public void I_navigate_to_create_item_list() throws Throwable {
        WebElement itemManagementMenuElement = driver.findElement(By.id("gwt-debug-tgtMenumenuItem"));
        assertNotNull(itemManagementMenuElement);
        itemManagementMenuElement.click();
        WebElement itemManagementItemListsMenuElement = driver.findElement(By.id("gwt-debug-tgtMenuViewTgtListItemmenuItem"));
        assertNotNull(itemManagementItemListsMenuElement);
        itemManagementItemListsMenuElement.click();
        WebElement createItemListMenuElement = driver.findElement(By.id("gwt-debug-MenuItem-createItemList"));
        assertNotNull(createItemListMenuElement);
        createItemListMenuElement.click();
    }

    @When("^I create a item list$")
    public void I_create_a_item_list() throws Throwable {
        WebElement itemListNameElement = driver.findElement(By.id("gwt-debug-Test AppTextBox-item-list-name-textbox"));
        itemListNameElement.sendKeys(itemListName);
        WebElement itemListTypeElement = driver.findElement(By.id("gwt-debug-Test AppComboBox-item-list-type-inner-textbox"));
        itemListTypeElement.clear();
        itemListTypeElement.sendKeys("RTL");
        WebElement itemListSaveElement = driver.findElement(By.id("gwt-debug-EditButtonBar-save"));
        itemListSaveElement.click();
    }

    @When("^I add the item to the item list$")
    public void I_add_the_item_to_the_item_list() throws Throwable {
        WebElement addItemToListButton = driver.findElement(By.id("gwt-debug-addItemToList"));
        addItemToListButton.click();
        WebElement itemListNameBox = driver.findElement(By.id("gwt-debug-Test AppComboBox-addToItemListDialogView-inner-textbox"));
        itemListNameBox.sendKeys(itemListName);
        WebElement addToItemListButton = driver.findElement(By.id("gwt-debug-Test AppButton-addToItemListDialogView-add"));
        addToItemListButton.click();
        WebElement okButton = driver.findElement(By.id("gwt-debug-close")); //wait for OK button
        assertTrue("We should see the successful addition to list message",driver.getPageSource().contains("All Items were successfully added to the List"));
        okButton.click();
    }

    @Then("^the item is in the item list$")
    public void the_item_is_in_the_item_list() throws Throwable {
        WebElement listMembershipTab = driver.findElement(By.id("gwt-debug-Tab:List Membership-tab"));
        listMembershipTab.click();
        List<WebElement> elements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        boolean found = false;
        for (WebElement element : elements) {
            if (element.getText().equals(itemListName)) {
                found = true;
                break;
            }
            assertTrue("We should have found our item list in the list membership", found);
        }
    }

    @Then("^I can edit the item from the list$")
    public void I_can_edit_the_item_from_the_list() throws Throwable {
        //This gets the Item Lists
        List<WebElement> itemListelements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        for (WebElement itemListElement : itemListelements) {
            if (itemListElement.getText().equals(itemListName)) {
                Actions itemListAction = new Actions(driver);
                itemListAction.doubleClick(itemListElement);
                itemListAction.perform();
                //This gets the Items in the chosen list, from which you can edit the one we want
                List<WebElement> itemElements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
                for (WebElement itemElement : itemElements) {
                    if (itemElement.getText().equals(itemName)) {
                        Actions itemAction = new Actions(driver);
                        itemAction.doubleClick(itemElement);
                        itemAction.perform();
                        break;
                    }
                }
                break;
            }
        }
    }

    @When("^I remove the item from the item list$")
    public void I_remove_the_item_from_the_item_list() throws Throwable {
        //Navigate to our item list
        WebElement itemManagementMenuElement = driver.findElement(By.id("gwt-debug-tgtMenumenuItem"));
        assertNotNull(itemManagementMenuElement);
        itemManagementMenuElement.click();
        WebElement itemListsMenuElement = driver.findElement(By.id("gwt-debug-tgtMenuViewTgtListItem"));
        assertNotNull(itemListsMenuElement);
        itemListsMenuElement.click();
        WebElement viewAllItemListsMenuElement = driver.findElement(By.id("gwt-debug-MenuItem-viewAllItemLists"));
        assertNotNull(viewAllItemListsMenuElement);
        viewAllItemListsMenuElement.click();

        //Click "View item list" on the current one
        List<WebElement> itemElements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        for (WebElement itemElement : itemElements) {
            if (itemElement.getText().equals(itemListName)) {
                WebElement parent = itemElement.findElement(By.xpath("..")).findElement(By.xpath(".."));
                WebElement image = parent.findElement(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-cells-ClickableImageResouceCell-Css-cellBase')]"));
                image.click(); //Clicks the "View Item list" image
                break;
            }
        }

        //We should now be viewing the item list. Check the item to remove.
        List<WebElement> items = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        for (WebElement item : items) {
            if (item.getText().equals(itemName)) {
                WebElement parent = item.findElement(By.xpath("..")).findElement(By.xpath(".."));
                WebElement checkBox = parent.findElement(By.tagName("input"));
                checkBox.click();
                break;
            }
        }

        //Click Remove from Item List button
        WebElement removeButton = driver.findElement(By.id("gwt-debug-Test AppButton-removeFromItemList"));
        removeButton.click();
    }

    @Then("^the item is no longer seen in the item list$")
    public void the_item_is_no_longer_seen_in_the_item_list() throws Throwable {
        //Item is no longer in the list - does this work??
    }

    @Then("^the item list is no longer seen in the item's list membership tab$")
    public void the_item_list_is_no_longer_seen_in_the_item_s_list_membership_tab() throws Throwable {
        //Navigate to the item (but how? It's not in the list anymore!
        //Item list membership shouldn't contain the current item list
        //Maybe this needs more thinking out...
    }

    @When("^I click on \"Item Folder\"$")
    public void I_click_on_item_folder() throws Throwable {
        WebElement itemFolderElement = driver.findElement(By.id("gwt-debug-itemFolderButton"));
        assertNotNull(itemFolderElement);
        itemFolderElement.click();
    }

    @Then("^I am on the Item Folder page$")
    public void I_am_on_the_Item_Folder_page() throws Throwable {
        WebElement itemFolderElement = driver.findElement(By.id("gwt-debug-iteming-metaDataBanner"));
        assertNotNull(itemFolderElement);
    }

    @When("^I click on Add New on the Item Folder page$")
    public void I_click_on_Add_New_on_the_Item_Folder_page() throws Throwable {
        WebElement addNewElement = driver.findElement(By.id("gwt-debug-RepositoryFolderTableButtonBar-addNew"));
        assertNotNull(addNewElement);
        addNewElement.click();
    }

    @When("^I fill in the Item Folder item page$")
    public void I_fill_in_the_Item_Folder_item_page() throws Throwable {
        WebElement intelligenceTypeElement = driver.findElement(By.id("gwt-debug-intelligence_type-inner-textbox"));
        assertNotNull(intelligenceTypeElement);
        intelligenceTypeElement.clear();
        intelligenceTypeElement.sendKeys("TEXT");
        WebElement reportTitleElement = driver.findElement(By.id("gwt-debug-item_folder_report_title-textbox"));
        reportTitleElement.sendKeys(reportTitle);
        WebElement reportDtgElement = driver.findElement(By.id("gwt-debug-report_date-inner-textbox"));
        assertNotNull(reportDtgElement);
        reportDtgElement.clear();
        reportDtgElement.sendKeys("130000ZMar2013");
        WebElement informationDtgElement = driver.findElement(By.id("gwt-debug-information_date-inner-textbox"));
        assertNotNull(informationDtgElement);
        informationDtgElement.clear();
        informationDtgElement.sendKeys("140000ZMar2013");
    }

    @When("^I click Save on the Item Folder item page$")
    public void I_click_Save_on_the_Item_Folder_item_page() throws Throwable {
        WebElement saveButtonElement = driver.findElement(By.id("gwt-debug-Test AppButton-save"));
        assertNotNull(saveButtonElement);
        saveButtonElement.click();
    }

    @Then("^the report is listed on the Item Folder page$")
    public void the_report_is_listed_on_the_Item_Folder_page() throws Throwable {
        WebElement closeButtonElement = driver.findElement(By.id("gwt-debug-RepositoryFolderTableButtonBar-cancel")); //Check for the Item Folder close button, so we know we are on the right page
        assertTrue("The new report title should be on the list", driver.getPageSource().contains(reportTitle));

    }

    @When("^I re-enter the report$")
    public void I_re_enter_the_report() throws Throwable {
        List<WebElement> elements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]"));
        boolean reportTitleWasFound = false;
        for (WebElement element : elements) {
            if (element.getText().equals(reportTitle)) {
                Actions action = new Actions(driver);
                action.doubleClick(element);
                action.perform();
                reportTitleWasFound = true;
                break;
            }
        }
        assertTrue(reportTitleWasFound);
    }

    @When("^I edit the report data$")
    public void I_edit_the_report_data() throws Throwable {
        WebElement reportTitleElement = driver.findElement(By.id("gwt-debug-item_folder_report_title-textbox"));
        reportTitleElement.clear();
        reportTitleElement.sendKeys(reportTitle + "-V2");
    }

    @When("^the edited report data is visible on the page$")
    public void the_edited_report_data_is_visible_on_the_page() throws Throwable {
        WebElement closeButtonElement = driver.findElement(By.id("gwt-debug-RepositoryFolderTableButtonBar-cancel")); //Check for the Item Folder close button, so we know we are on the right page
        assertTrue("The edited report title should be on the list", driver.getPageSource().contains(reportTitle + "-V2"));
    }

    @When("^I click Delete on the Item Folder item page$")
    public void I_click_Delete_on_the_Item_Folder_item_page() throws Throwable {
        WebElement deleteButtonElement = driver.findElement(By.id("gwt-debug-Test AppButton-delete"));
        deleteButtonElement.click();
        WebElement confirmButtonElement = driver.findElement(By.id("gwt-debug-confirmationBtnConfirm"));
        confirmButtonElement.click();
    }

    @Then("^the deleted report is not visible on the page$")
    public void the_deleted_report_is_not_visible_on_the_page() throws Throwable {
        WebElement closeButtonElement = driver.findElement(By.id("gwt-debug-RepositoryFolderTableButtonBar-cancel")); //Check for the Item Folder close button, so we know we are on the right page
        assertFalse("The deleted report title should not be on the list", driver.getPageSource().contains(reportTitle));
    }

    @When("^I click the map button$")
    public void I_click_the_map_button() throws Throwable {
        WebElement mapButtonElement = driver.findElement(By.id("gwt-debug-tableToolbarButton-inner"));
        assertNotNull(mapButtonElement);
        mapButtonElement.click();
    }

    @Then("^I see the map for the item$")
    public void I_see_the_map_for_the_item() throws Throwable {
//        WebElement mapComponentElement = driver.findElement(By.id("MapComponent"));
//        assertNotNull(mapComponentElement);
    }

    @When("^I enter search for the existing item$")
    public void I_enter_search_for_the_existing_item() throws Throwable {
        WebElement searchElement = driver.findElement(By.id("gwt-debug-searchTextBox-textbox"));
        assertNotNull(searchElement);
        searchElement.sendKeys(itemName);
        WebElement searchButtonElement = driver.findElement(By.id("gwt-debug-searchTextBox-magnifier"));
        assertNotNull(searchButtonElement);
        searchButtonElement.click();
    }

    @Then("^I see only the existing item in the search results$")
    public void I_see_only_the_existing_item_in_the_search_results() throws Throwable {
        List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class,'com-jeannot-testapp-client-common-widgets-search-SearchCssResources-searchResultsContent')][contains(@id,'gwt-debug-testapp-SearchTable')]"));
        boolean itemWasFound = false;
        for (WebElement element : elements) {
            if (element.getText().equals(itemName)) {
                itemWasFound = true;
                break;
            }
        }
        assertTrue(itemWasFound);
        assertEquals(1, elements.size());
    }

    @When("^I enter search for a non-existent item$")
    public void I_enter_search_for_a_non_existent_item() throws Throwable {
        WebElement searchElement = driver.findElement(By.id("gwt-debug-searchTextBox-textbox"));
        assertNotNull(searchElement);
        searchElement.sendKeys("NONEXISTENT001"); //item with this or similar name must never exist!
        WebElement searchButtonElement = driver.findElement(By.id("gwt-debug-searchTextBox-magnifier"));
        assertNotNull(searchButtonElement);
        searchButtonElement.click();
    }

    @Then("^I see no search results$")
    public void I_see_no_search_results() throws Throwable {
        List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class,'com-jeannot-testapp-gwt-client-widgets-layout-Test AppScrollPanel-Css-scrollPanel')]"));
        assertTrue(driver.getPageSource().contains("No Results Found."));
    }

    @Then("^I see a possible duplicate warning triangle$")
    public void I_see_a_possible_duplicate_warning_triangle() throws Throwable {
        WebElement warningTriangleElement = driver.findElement(By.id("gwt-debug-iteming-duplication-widget-image"));
        assertNotNull(warningTriangleElement);
    }

    @Then("^the warning triangle refers to the other of the two items$")
    public void the_warning_triangle_refers_to_the_other_of_the_two_items() throws Throwable {
        WebElement warningTriangleElement = driver.findElement(By.id("gwt-debug-iteming-duplication-widget-image"));
        warningTriangleElement.click();
        List<WebElement> elements = driver.findElements(By.xpath("//td[contains(@class,'com-jeannot-testapp-gwt-client-widgets-tables-Test AppDataTable-Css-cellTableCell')]/div"));
        boolean itemWasFound = false;
        for (WebElement element : elements) {
            if (element.getText().equals(itemName)) {
                itemWasFound = true;
                break;
            }
        }
        assertTrue(itemWasFound);
    }

    @Then("^the created item is in Draft state$")
    public void the_created_item_is_in_Draft_state() throws Throwable {
        //The "Draft" image isn't accessible via ID/CSS class, so I go to the database instead...
        //WebElement draftElement = driver.findElement(By.id("????"));
        //assertNotNull(draftElement);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(commonStepDefinitions.getTestAppDataSource());
        String state = jdbcTemplate.queryForObject("select lifecycle_state from dbo.item_view where item_name = '" + itemName + "'",String.class);
        jdbcTemplate.getDataSource().getConnection().close();
        assertEquals("Item should be in Draft state","DRAFT", state);

    }

    private String makeFairlyRandomItemName() {
        //can be up to 54 chars long, but I'll just work with 20 for now.
        //I have split up the timestamp elements
        //with some random alphabetic chars in order to make duplicate check less likely
        //to find something...
        String timestamp = Long.toString(System.nanoTime());
        Random randomGenerator1 = new Random();
        int r1 = randomGenerator1.nextInt(25);
        int r2 = randomGenerator1.nextInt(25);
        int r3 = randomGenerator1.nextInt(25);
        int r4 = randomGenerator1.nextInt(25);
        int r5 = randomGenerator1.nextInt(25);
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        sb.append(letters.charAt(r1));
        sb.append(timestamp.substring(0,2));
        sb.append(letters.charAt(r2));
        sb.append(timestamp.substring(2,4));
        sb.append(letters.charAt(r3));
        sb.append(timestamp.substring(4,7));
        sb.append(letters.charAt(r4));
        sb.append(timestamp.substring(7));
        sb.append(letters.charAt(r5));
        String result = sb.toString();
        return result;
    }

}
