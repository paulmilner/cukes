@create_item
Feature: Create items
    As a user
    I would like to create items
    So that the item is available for use in Test App

#Background:
#    Given I log into Test App as "administrator"
#    And I navigate to "Create Item"

@wip
Scenario: Search Google for something...
    When I search Google for "Cucumber"
    Then I see "Cucumber" in the search results

Scenario: Initiate item creation
    When I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    Then I see the Item Details page for the item

Scenario: Able to save a item with only the basic name, country and type
    When I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    And I click Save
    And I click OK in the Save Successful box
    Then I see the Item Details page for the item

Scenario: Save a item
    When I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    And I enter the following Item tab data:
        | item-code    | 10100                     |
        | item-type    | Type 1                    |
    And I click Save
    And I click OK in the Save Successful box
    Then I see the Item Details page for the item
    And I see the following saved Item tab data:
        #copied from the data setup above
      | item-code    | 10100                     |
      | item-type    | Type 1                    |

Scenario: Save a item with all Item tab fields
    When I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    And I enter the following Item tab data:
      | item-code    | 10100                     |
      | item-type    | Type 1                    |
    And I enter the following Item tab rich text field data:
        | item-description                       | Test Item               |
    And I click Save
    And I click OK in the Save Successful box
    Then I see the Item Details page for the item
    And I see the following saved Item tab data:
        #copied from the data setup above
      | item-code    | 10100                     |
      | item-type    | Type 1                    |
    And I see the following saved Item tab rich text field data:
        #copied from the data setup above
        | item-description                       | Test Item               |

Scenario: A item is created in Draft state
    When I enter a Item Name, Country Code and Item Type of "Organisation" and click "OK"
    And I click Save
    Then the created item is in Draft state

