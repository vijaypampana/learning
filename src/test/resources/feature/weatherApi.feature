@Weather_API
Feature: This test is to run weather API

  Scenario Outline: This test will take inputs and get the details from weather API

    Given I set API Server as "https://api.meteomatics.com/"
#    And I set API Base Path as "/"
    And I set API Basic Authentication using "none_pampana" and "ysqHm54a1IFUB"
    And I set API headers using below table
    | content-type | application/json |
    Then I make API GET call using URL "/<TimeZone>/<temp>,<precipitation>,<cloud>/<latitude>,<longitude>/<format>"
    # The below step is important as it verify the response code and at the same time set ValidatableResponse for subsequent processing
    Then I verify API response status code is 200
    Then I set API JSON root as "data.find {it.parameter == 't_2m:C'}" and verify response using below table
    | lat | <latitude> |
#    | lon | <longitude> |

    Examples:
      | TimeZone             | temp   | precipitation | cloud               | latitude  | longitude | format |
      | 2020-03-15T00:00:00Z | t_2m:C | precip_1h:mm  | total_cloud_cover:p | 45.842875 | 6.863229  | json   |