@search_item
Feature: Search items

Background:
    Given I log into Test App as "administrator"

Scenario: Search for an individual existing item
    When I navigate to "Create Item"
    And I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    And I click Save
    And I click OK in the Save Successful box
    And I see the Item Details page for the item
    And I navigate to the Dashboard
    And I enter search for the existing item
    Then I see only the existing item in the search results

Scenario: Search for a non-existent item
    When I enter search for a non-existent item
    Then I see no search results
