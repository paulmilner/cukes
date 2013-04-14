@edit_item
Feature: Edit items
    As a user
    I would like to edit items
    So that the item can be kept up to date

Background:
    Given I log into Test App as "administrator"

Scenario: Add a item to a item list
    When I navigate to "Create Item List"
    And I create a item list
    And I navigate to "Create Item"
    And I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    And I enter the following Item tab data:
      | item-code    | 10100                     |
      | item-type    | Type 1                    |
    And I add the item to the item list
    And I click Save
    And I click OK in the Save Successful box
    Then the item is in the item list
    And I can edit the item from the list
    And I see the Item Details page for the item

