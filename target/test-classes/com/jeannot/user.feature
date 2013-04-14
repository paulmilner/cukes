@user
Feature: Manage users
  As a Test App Administrator,
  I would like to view and manage user definitions,
  So that users and their accounts can be administered.

Background:
Given I log into Test App as "administrator"

@wip
Scenario: View the list of users
When I navigate to Users
Then I see the User list

Scenario: I should be unable to add a user who is not listed in Active Directory
When I navigate to Users
And I see the User list
And I choose to add a new user
And I enter the new user details
And I click Save on the user
And I click OK on the confirmation box
Then I see an error message telling me the username could not be authenticated

Scenario: I should be unable to add a duplicate user
When I navigate to Users
And I see the User list
And I choose to add a new user
And I enter the new user details with a username that already exists
And I click Save on the user
And I click OK on the confirmation box
Then I see an error message telling me the username already exists

