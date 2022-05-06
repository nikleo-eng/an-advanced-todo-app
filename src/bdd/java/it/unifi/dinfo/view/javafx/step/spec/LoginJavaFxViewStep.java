package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;

public class LoginJavaFxViewStep extends ApplicationTest {
	
	@When("The user enters {string} in the Email login input field")
	public void theUserEntersEmailInTheEmailLoginInputField(String email) {
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(email);
	}
	
	@When("The user enters {string} in the Password login input field")
	public void theUserEntersPasswordInThePasswordLoginInputField(String password) {
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(password);
	}
	
	@When("The user clicks the Login button")
	public void theUserClicksTheLoginButton() {
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
	}
	
	@Then("The login error text shows {string}")
	public void theLoginErrorTextShowsErrorMessage(String errorMessage) {
		String loginErrorText = lookup("#" + LoginJavaFxView.ERROR_TEXT_ID).queryText().getText();
		assertThat(loginErrorText).isEqualTo(errorMessage);
	}
	
}
