Feature: User View
  Specifications of the behavior of the User View
  
  Background: The Lists, Details, Addition-Modification and User Views
		Given The database contains a user with the following values
      | name     | Mario               |
    	| surname  | Rossi               |
    	| email    | mario.rossi@test.it |
    	| password | password            |
    And The datatase contains a log with the following values
      | in         | 03/05/2022 19:40:01 CEST |
      | out        | 03/05/2022 19:41:11 CEST |
      | user_email | mario.rossi@test.it      |
    When The user runs An Advanced Todo App
    And The user enters "mario.rossi@test.it" in the Email login input field
    And The user enters "password" in the Password login input field
    And The user clicks the Login button

	Scenario: Successful logout
    When The user clicks the Logout button
    Then The Login and Registration Views are showed
    And The Email login input field is empty
    And The Password login input field is empty
    And The Name registration input field is empty
    And The Surname registration input field is empty
    And The Email registration input field is empty
    And The Password registration input field is empty
    And The Confirm Password registration input field is empty
    And Between the logs in the datatase there is an other log for the user with the email "mario.rossi@test.it"
    
	Scenario: Successful lists refresh
	  Given The database contains a list with the following values
      | name       | LIST                |
      | user_email | mario.rossi@test.it |
    When The user clicks the Refresh button
    Then The Lists, Details, Addition-Modification and User Views are showed
    And The list view of Lists contains a list with name "LIST"