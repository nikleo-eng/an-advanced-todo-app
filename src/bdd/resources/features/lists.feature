Feature: Lists and Addition-Modification Views
  Specifications of the behavior of the Lists and Addition-Modification Views
  
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
    And The database contains a list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    When The user runs An Advanced Todo App
    And The user enters "mario.rossi@test.it" in the Email login input field
    And The user enters "password" in the Password login input field
    And The user clicks the Login button

	Scenario: Successful list addition
    When The user clicks the Add list button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "OTHER-TEST-LIST" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "disabled"
    And The Save addition-modification button is "disabled"
    And The addition-modification input field is "disabled"
    And The list view of Lists contains a list with name "OTHER-TEST-LIST"
    And Between the lists in the datatase there is the list with the following values
      | name       | OTHER-TEST-LIST     |
      | user_email | mario.rossi@test.it |
  
  Scenario: Unsuccessful list addition: list with same name exists
    When The user clicks the Add list button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "TEST-LIST" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    And The addition-modification error text shows "Already exists a list with same name"
    
  Scenario: Unsuccessful list addition: empty field
    When The user clicks the Add list button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    And The addition-modification error text shows "The field is empty"
    
	Scenario: Successful list modification
    When The user clicks the "TEST-LIST" label in list view of Lists
    And The user clicks the "TEST-LIST" modify icon in list view of Lists
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "-NEW" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "disabled"
    And The Save addition-modification button is "disabled"
    And The addition-modification input field is "disabled"
    And The list view of Lists contains a list with name "TEST-LIST-NEW"
    And The list view of Lists does not contain a list with name "TEST-LIST"
    And Between the lists in the datatase there is the list with the following values
      | name       | TEST-LIST-NEW       |
      | user_email | mario.rossi@test.it |
    And Between the lists in the datatase there is not the list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
      
  Scenario: Unsuccessful list modification: list no longer exists
  	Given From the database is deleted the list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    When The user clicks the "TEST-LIST" label in list view of Lists
    And The user clicks the "TEST-LIST" modify icon in list view of Lists
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "-NEW" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    And The list view of Lists does not contain a list with name "TEST-LIST-NEW"
    And The list view of Lists contains a list with name "TEST-LIST"
    And Between the lists in the datatase there is not the list with the following values
      | name       | TEST-LIST-NEW       |
      | user_email | mario.rossi@test.it |
    And The addition-modification error text shows "The selected list no longer exists; try Refresh button!"
  
  Scenario: Unsuccessful list modification: canceled operation
    When The user clicks the "TEST-LIST" label in list view of Lists
    And The user clicks the "TEST-LIST" modify icon in list view of Lists
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "-NEW" in the addition-modification input field
    And The user clicks the Cancel addition-modification button
    Then The Cancel addition-modification button is "disabled"
    And The Save addition-modification button is "disabled"
    And The addition-modification input field is "disabled"
    And The list view of Lists does not contain a list with name "TEST-LIST-NEW"
    And The list view of Lists contains a list with name "TEST-LIST"
    And Between the lists in the datatase there is not the list with the following values
      | name       | TEST-LIST-NEW       |
      | user_email | mario.rossi@test.it |
    And Between the lists in the datatase there is the list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    
	Scenario: Successful list deletion
    When The user clicks the "TEST-LIST" label in list view of Lists
    And The user clicks the "TEST-LIST" delete icon in list view of Lists
    Then The list view of Lists does not contain a list with name "TEST-LIST"
    And Between the lists in the datatase there is not the list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    
  Scenario: Unsuccessful list deletion: list no longer exists
  	Given From the database is deleted the list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
  	When The user clicks the "TEST-LIST" label in list view of Lists
    And The user clicks the "TEST-LIST" delete icon in list view of Lists
    Then The list view of Lists contains a list with name "TEST-LIST"
    And The lists error text shows "The selected list no longer exists; try Refresh button!"