@item_folder
Feature: Item folder handling
    As a user
    I would like to manage items in item folders
    So that relevant items are easy to find, bundled with the item

Background:
    Given I log into Test App as "administrator"
    And I navigate to "Create Item"
    And I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"

Scenario: Access the item folder page
    When I click on "Item Folder"
    Then I am on the Item Folder page

Scenario: Add a textual report to the item folder
    When I click on "Item Folder"
    And I am on the Item Folder page
    And I click on Add New on the Item Folder page
    And I fill in the Item Folder item page
    And I click Save on the Item Folder item page
    Then I am on the Item Folder page
    And the report is listed on the Item Folder page

Scenario: Edit a textual report in the item folder
    When I click on "Item Folder"
    And I am on the Item Folder page
    And I click on Add New on the Item Folder page
    And I fill in the Item Folder item page
    And I click Save on the Item Folder item page
    And I am on the Item Folder page
    And the report is listed on the Item Folder page
    And I re-enter the report
    And I edit the report data
    And I click Save on the Item Folder item page
    And I am on the Item Folder page
    And the edited report data is visible on the page

Scenario: Remove a textual report from the item folder
    When I click on "Item Folder"
    And I am on the Item Folder page
    And I click on Add New on the Item Folder page
    And I fill in the Item Folder item page
    And I click Save on the Item Folder item page
    And I am on the Item Folder page
    And the report is listed on the Item Folder page
    And I re-enter the report
    And I click Delete on the Item Folder item page
    Then I am on the Item Folder page
    And the deleted report is not visible on the page
