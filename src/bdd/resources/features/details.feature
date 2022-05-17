Feature: Details and Addition-Modification Views
  Specifications of the behavior of the Details and Addition-Modification Views
  
  Background: The Lists, Details, Addition-Modification and User Views
		Given The database contains a user with the following values
      | name     | Mario               |
    	| surname  | Rossi               |
    	| email    | mario.rossi@test.it |
    	| password | password            |
    And The datatase contains a log with the following values
      | in         | 16/05/2022 18:34:11 CEST |
      | out        | 16/05/2022 19:13:59 CEST |
      | user_email | mario.rossi@test.it      |
    And The database contains a list with the following values
      | name       | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    And The database contains a detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    When The user runs An Advanced Todo App
    And The user enters "mario.rossi@test.it" in the Email login input field
    And The user enters "password" in the Password login input field
    And The user clicks the Login button
    And The user clicks the "TEST-LIST" label in list view of Lists

	Scenario: Successful detail addition
    When The user clicks the Add detail button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "OTHER-TEST-DETAIL" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "disabled"
    And The Save addition-modification button is "disabled"
    And The addition-modification input field is "disabled"
    And The list view of Details contains a detail with todo "OTHER-TEST-DETAIL"
    And Between the details in the datatase there is the detail with the following values
      | todo       | OTHER-TEST-DETAIL   |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
  
  Scenario: Unsuccessful detail addition: detail with same todo exists
    When The user clicks the Add detail button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "TEST-DETAIL" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    And The addition-modification error text shows "Already exists a detail in the selected list with same todo"
    
  Scenario: Unsuccessful detail addition: empty field
    When The user clicks the Add detail button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    And The addition-modification error text shows "The field is empty"
    
	Scenario: Successful detail todo modification
    When The user clicks the "TEST-DETAIL" label in list view of Details
    And The user clicks the "TEST-DETAIL" modify icon in list view of Details
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "-NEW" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "disabled"
    And The Save addition-modification button is "disabled"
    And The addition-modification input field is "disabled"
    And The list view of Details contains a detail with todo "TEST-DETAIL-NEW"
    And The list view of Details does not contain a detail with todo "TEST-DETAIL"
    And Between the details in the datatase there is the detail with the following values
      | todo       | TEST-DETAIL-NEW     |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    And Between the details in the datatase there is not the detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
      
  Scenario: Successful detail done modification
    When The user clicks the "TEST-DETAIL" label in list view of Details
    And The user clicks the "TEST-DETAIL" checkbox in list view of Details
    Then Between the details in the datatase there is the detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | true                |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
      
  Scenario: Unsuccessful detail todo modification: detail no longer exists
  	Given From the database is deleted the detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    When The user clicks the "TEST-DETAIL" label in list view of Details
    And The user clicks the "TEST-DETAIL" modify icon in list view of Details
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "-NEW" in the addition-modification input field
    And The user clicks the Save addition-modification button
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    And The list view of Details does not contain a detail with todo "TEST-DETAIL-NEW"
    And The list view of Details contains a detail with todo "TEST-DETAIL"
    And Between the details in the datatase there is not the detail with the following values
      | todo       | TEST-DETAIL-NEW     |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    And The addition-modification error text shows "The selected detail no longer exists; try Refresh button!"
  
  Scenario: Unsuccessful detail todo modification: canceled operation
    When The user clicks the "TEST-DETAIL" label in list view of Details
    And The user clicks the "TEST-DETAIL" modify icon in list view of Details
    Then The Cancel addition-modification button is "enabled"
    And The Save addition-modification button is "enabled"
    And The addition-modification input field is "enabled"
    When The user enters "-NEW" in the addition-modification input field
    And The user clicks the Cancel addition-modification button
    Then The Cancel addition-modification button is "disabled"
    And The Save addition-modification button is "disabled"
    And The addition-modification input field is "disabled"
    And The list view of Details does not contain a detail with todo "TEST-DETAIL-NEW"
    And The list view of Details contains a detail with todo "TEST-DETAIL"
    And Between the details in the datatase there is not the detail with the following values
      | todo       | TEST-DETAIL-NEW     |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    And Between the details in the datatase there is the detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    
	Scenario: Successful detail deletion
    When The user clicks the "TEST-DETAIL" label in list view of Details
    And The user clicks the "TEST-DETAIL" delete icon in list view of Details
    Then The list view of Details does not contain a detail with todo "TEST-DETAIL"
    And Between the details in the datatase there is not the detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
    
  Scenario: Unsuccessful detail deletion: detail no longer exists
  	Given From the database is deleted the detail with the following values
      | todo       | TEST-DETAIL         |
      | done       | false               |
      | list_name  | TEST-LIST           |
      | user_email | mario.rossi@test.it |
  	When The user clicks the "TEST-DETAIL" label in list view of Details
    And The user clicks the "TEST-DETAIL" delete icon in list view of Details
    Then The list view of Details contains a detail with todo "TEST-DETAIL"
    And The details error text shows "The selected detail no longer exists; try Refresh button!"