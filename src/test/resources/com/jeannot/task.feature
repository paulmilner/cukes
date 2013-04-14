@task
Feature: Manage tasks, plans and resources
  As a Test App Planner,
  I would like to view and manage tasks, plans and resources,
  So that logistical goals can be achieved.

Background:
Given I log into Test App as "administrator"
And I navigate to Planning

Scenario: Create a plan
Given I navigate to Create Plan
And I name the plan
And I fill in the following plan data:
        | plan-priority                       | 2                         |
        | plan-poc-name                       | Stephen Redmond           |
        | plan-poc-phone                      | +44-7700-233455           |
And I click Save on the plan
Then I see the plan in the list of plans

Scenario: Select a plan on the timeline
Given I navigate to Create Plan
And I name the plan
And I fill in the following plan data:
        | plan-priority                       | 2                         |
        | plan-poc-name                       | Stephen Redmond           |
        | plan-poc-phone                      | +44-7700-233455           |
And I click Save on the plan
And I enter the plan
And I click on View Timeline
Then the plan is selectable in the timeline

Scenario: Add a resource on the timeline
Given I navigate to Create Plan
And I name the plan
And I fill in the following plan data:
        | plan-priority                       | 2                         |
        | plan-poc-name                       | Stephen Redmond           |
        | plan-poc-phone                      | +44-7700-233455           |
And I click Save on the plan
And I enter the plan
And I click on View Timeline
And I add resource from the timeline
Then I am on the New Resource form
