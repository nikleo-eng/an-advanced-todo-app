Feature: Login View
  Specifications of the behavior of the Login View
  
  Background: The Login and Registration Views
		When The user runs An Advanced Todo App

	Scenario Outline: Successful login
    Given The database contains a user with the following values
      | name     | <name>     |
    	| surname  | <surname>  |
    	| email    | <email>    |
    	| password | <password> |
    And The datatase contains a log with the following values
      | in         | <in>    |
      | out        | <out>   |
      | user_email | <email> |
    When The user enters "<email>" in the Email login input field
    And The user enters "<password>" in the Password login input field
    And The user clicks the Login button
    Then The Lists, Details, Addition-Modification and User Views are showed
    And The user text shows "<name> <surname>"
    And The log text shows that the last login is happened at "<in>"
    
    Examples: 
      | name     | surname | email                  | password   | in                       | out                      |
      | Mario    | Rossi   | mario.rossi@test.it    | passwordMR | 03/05/2022 19:40:01 CEST | 03/05/2022 20:10:23 CEST |
      | Giuseppe | Verdi   | giuseppe.verdi@test.it | passwordGV | 04/05/2022 15:12:00 CEST | 04/05/2022 15:16:54 CEST |
    
	Scenario: Unsuccessful login: email/password empty
		When The user clicks the Login button
    Then The Login and Registration Views are showed
    And The login error text shows "The email or password field is empty"
    
	Scenario: Unsuccessful login: user not found
		When The user enters "test@test.it" in the Email login input field
    And The user enters "passwordTest" in the Password login input field
    And The user clicks the Login button
    Then The Login and Registration Views are showed
    And The login error text shows "User with email entered not found"
    
	Scenario: Unsuccessful login: incorrect password
	  Given The database contains a user with the following values
      | name     | Mario            |
    	| surname  | Rossi            |
    	| email    | m.rossi@email.it |
    	| password | pswrdMR          |
    And The datatase contains a log with the following values
      | in         | 04/05/2022 18:05:56 CEST |
      | out        | 04/05/2022 18:07:03 CEST |
      | user_email | m.rossi@email.it         |
		When The user enters "m.rossi@email.it" in the Email login input field
    And The user enters "passwordMR" in the Password login input field
    And The user clicks the Login button
    Then The Login and Registration Views are showed
    And The login error text shows "Has been entered an incorrect password"