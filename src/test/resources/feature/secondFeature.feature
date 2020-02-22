@regression @SubModule_second
  Feature: This is second Test feature

    Scenario: This is second test scenario

      Given I want to open chrome browser
      When I open the google Home page
      When I search for "BDD"
      Then BDD search results are shown