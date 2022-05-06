package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationJavaFxViewStep extends ApplicationTest {

	@When("The user enters {string} in the Name registration input field")
	public void theUserEntersNameInTheNameRegistrationInputField(String name) {
		clickOn("#" + RegistrationJavaFxView.NAME_TEXTFIELD_ID);
		write(name);
	}
	
	@When("The user enters {string} in the Surname registration input field")
	public void theUserEntersSurnameInTheSurnameRegistrationInputField(String surname) {
		clickOn("#" + RegistrationJavaFxView.SURNAME_TEXTFIELD_ID);
		write(surname);
	}
	
	@When("The user enters {string} in the Email registration input field")
	public void theUserEntersEmailInTheEmailRegistrationInputField(String email) {
		clickOn("#" + RegistrationJavaFxView.EMAIL_TEXTFIELD_ID);
		write(email);
	}
	
	@When("The user enters {string} in the Password registration input field")
	public void theUserEntersPasswordInThePasswordRegistrationInputField(String password) {
		clickOn("#" + RegistrationJavaFxView.PASSWORD_FIELD_ID);
		write(password);
	}
	
	@When("The user enters {string} in the Confirm Password registration input field")
	public void theUserEntersPasswordInTheConfirmPasswordRegistrationInputField(
			String password) {
		clickOn("#" + RegistrationJavaFxView.CONFIRM_PASSWORD_FIELD_ID);
		write(password);
	}
	
	@When("The user clicks the Register button")
	public void theUserClicksTheRegisterButton() {
		clickOn("#" + RegistrationJavaFxView.REGISTER_BUTTON_ID);
	}
	
	@Then("The registration error text shows {string}")
	public void theRegistrationErrorTextShowsErrorMessage(String errorMessage) {
		String registrationErrorText = lookup("#" + RegistrationJavaFxView.ERROR_TEXT_ID)
				.queryText().getText();
		assertThat(registrationErrorText).isEqualTo(errorMessage);
	}
	
	@Then("The Name registration input field is empty")
	public void theNameRegistrationInputFieldIsEmpty() {
		String emailText = lookup("#" + RegistrationJavaFxView.NAME_TEXTFIELD_ID)
				.queryAs(TextField.class).getText();
		assertThat(emailText).isNullOrEmpty();
	}
	
	@Then("The Surname registration input field is empty")
	public void theSurnameRegistrationInputFieldIsEmpty() {
		String emailText = lookup("#" + RegistrationJavaFxView.SURNAME_TEXTFIELD_ID)
				.queryAs(TextField.class).getText();
		assertThat(emailText).isNullOrEmpty();
	}
	
	@Then("The Email registration input field is empty")
	public void theEmailRegistrationInputFieldIsEmpty() {
		String emailText = lookup("#" + RegistrationJavaFxView.EMAIL_TEXTFIELD_ID)
				.queryAs(TextField.class).getText();
		assertThat(emailText).isNullOrEmpty();
	}
	
	@Then("The Password registration input field is empty")
	public void thePasswordRegistrationInputFieldIsEmpty() {
		String passwordText = lookup("#" + RegistrationJavaFxView.PASSWORD_FIELD_ID)
				.queryAs(PasswordField.class).getText();
		assertThat(passwordText).isNullOrEmpty();
	}
	
	@Then("The Confirm Password registration input field is empty")
	public void theConfirmPasswordRegistrationInputFieldIsEmpty() {
		String passwordText = lookup("#" + RegistrationJavaFxView.CONFIRM_PASSWORD_FIELD_ID)
				.queryAs(PasswordField.class).getText();
		assertThat(passwordText).isNullOrEmpty();
	}
	
}
