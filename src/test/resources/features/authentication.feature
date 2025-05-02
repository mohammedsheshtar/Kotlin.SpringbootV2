Feature: Authentication

  Scenario: Access hello endpoint with a valid JWT token
    Given a valid JWT token for user "momo1234"
    When I send a GET request to "/hello" with the token
    Then the response status should be 200
    And the response body should be "Hello World"
