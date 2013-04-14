@availability
Feature: Website is available through a browser
  As a Test App User,
  I would like to be able to log into the Test App application,
  So that I can use it.

  Background:
    Given I am using Internet Explorer
    And I navigate to the homepage

  Scenario: The login should be served up when I initially go to the home URL
    Then I should see the login page

  Scenario: The user should not be able to log in with an invalid userid and password
    When I login with user "invalid" and password "invalid"
    Then I should see the login page with error "Error code: 401"

  Scenario: The user should not be able to log in with an invalid userid
    When I login with user "invalid" and password "a"
    Then I should see the login page with error "Error code: 401"

  Scenario: The user should not be able to log in with an invalid password
    When I login with user "a" and password "invalid"
    Then I should see the login page with error "Error code: 401"

  Scenario: The user should be able to log in with a valid userid and password
    When I login with user "a" and password "a"
    Then I see the dashboard
