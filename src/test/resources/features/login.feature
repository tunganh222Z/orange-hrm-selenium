Feature: Login feature

  @smoke
  Scenario: Successful login
    Given User opens login page
    Given Do nothing

    @api
    Scenario: Call api
      Given Call api get /books

  @api
  Scenario: Call api
    Given Call api get /status