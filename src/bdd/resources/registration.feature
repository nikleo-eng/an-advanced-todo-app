Feature: Registration View
  Specifications of the behavior of the Registration View
  
  Background: The Login and Registration Views
		Given The user runs An Advanced Todo App

	Scenario Outline: Successful registration
    When The user enters "<name>" in the Name registration input field
    And The user enters "<surname>" in the Surname registration input field
    And The user enters "<email>" in the Email registration input field
    And The user enters "<password>" in the Password registration input field
    And The user enters "<password>" in the Confirm Password registration input field
    And The user clicks the Register button
    Then The Lists, Details, Addition-Modification and User Views are showed
    And The user text shows "<name> <surname>"
    And Between the users in the database there is a user with the following values
      | name     | <name>     |
    	| surname  | <surname>  |
    	| email    | <email>    |
    	| password | <password> |
    And Between the logs in the datatase there is a log for the user with the email "<email>"
    
    Examples: 
      | name     | surname | email                  | password   |
      | Mario    | Rossi   | mario.rossi@test.it    | passwordMR |
      | Giuseppe | Verdi   | giuseppe.verdi@test.it | passwordGV |
    
	Scenario: Unsuccessful registration: field empty
		When The user clicks the Register button
    Then The Login and Registration Views are showed
    And The registration error text shows "A field in the form is empty"
    
	Scenario: Unsuccessful registration: passwords not matching
		When The user enters "Mario" in the Name registration input field
    And The user enters "Rossi" in the Surname registration input field
    And The user enters "m.rossi@email.it" in the Email registration input field
    And The user enters "passwordMR" in the Password registration input field
    And The user enters "pswrdMR" in the Confirm Password registration input field
    And The user clicks the Register button
    Then The Login and Registration Views are showed
    And The registration error text shows "The passwords entered do not match"
    
	Scenario: Unsuccessful registration: user already found
	  Given The database contains a user with the following values
      | name     | Mario            |
    	| surname  | Rossi            |
    	| email    | m.rossi@email.it |
    	| password | pswrdMR          |
    And The datatase contains a log with the following values
      | in         | 04/05/2022 18:05:56 CEST |
      | out        | 04/05/2022 18:07:03 CEST |
      | user_email | m.rossi@email.it         |
		When The user enters "Mario" in the Name registration input field
    And The user enters "Rossi" in the Surname registration input field
    And The user enters "m.rossi@email.it" in the Email registration input field
    And The user enters "pswrdMR" in the Password registration input field
    And The user enters "pswrdMR" in the Confirm Password registration input field
    And The user clicks the Register button
    Then The Login and Registration Views are showed
    And The registration error text shows "Already exists a user with same email"