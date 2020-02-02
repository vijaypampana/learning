@smoke @first @first  @Module_Login
  Feature: First Feature file
    This is the first feature file created using the BDD framework

    @Regression @first
    Scenario Outline: Open the Google and search for BDD

      Given I want to open chrome browser
      And I open the google Home page
      When I search for "<Query>"
      And I read the data table values
        | val1 | val2 | val3 | val4 |
        | dat1 | dat2 | dat3 | dat4 |
        | dat5 | dat6 | dat7 | dat8 |
      And I read the map value
        | key1 | data1 |
        | key2 | data2 |
        | key3 | data3 |
      And I read data table and saved it to list of map
        | val1 | val2 | val3 | val4 |
        | dat1 | dat2 | dat3 | dat4 |
        | dat5 | dat6 | dat7 | dat8 |
      Then BDD search results are shown

      @Dev
      Examples:
        | Query |
        | DEV |

      @Sit
      Examples:
        | Query |
        | DEV |
