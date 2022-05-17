package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class AdditionModificationJavaFxViewStep extends ApplicationTest {
	
	@Then("The Cancel addition-modification button is {string}")
	public void theCancelAdditionModificationButtonIsEnabledOrDisabled(String enabled) {
		Button button = lookup("#" + AdditionModificationJavaFxView.CANCEL_BUTTON_ID).queryButton();
		assertThat(button.isDisabled()).isNotEqualTo("enabled".equals(enabled));
	}
	
	@Then("The Save addition-modification button is {string}")
	public void theSaveAdditionModificationButtonIsEnabledOrDisabled(String enabled) {
		Button button = lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID).queryButton();
		assertThat(button.isDisabled()).isNotEqualTo("enabled".equals(enabled));
	}
	
	@Then("The addition-modification input field is {string}")
	public void theAdditionModificationInputFieldIsEnabledOrDisabled(String enabled) {
		TextArea textArea = lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class);
		assertThat(textArea.isDisabled()).isNotEqualTo("enabled".equals(enabled));
	}
	
	@When("The user enters {string} in the addition-modification input field")
	public void theUserEntersStringInTheAdditionModificationInputField(String inputString) {
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write(inputString);
	}
	
	@When("The user clicks the Save addition-modification button")
	public void theUserClicksTheSaveAdditionModificationButton() {
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
	}
	
	@When("The user clicks the Cancel addition-modification button")
	public void theUserClicksTheCancelAdditionModificationButton() {
		clickOn("#" + AdditionModificationJavaFxView.CANCEL_BUTTON_ID);
	}
	
	@Then("The addition-modification error text shows {string}")
	public void theAdditionModificationErrorTextShowsErrorMessage(String errorMessage) {
		String addModErrorText = lookup("#" + AdditionModificationJavaFxView.ERROR_TEXT_ID)
				.queryText().getText();
		assertThat(addModErrorText).isEqualTo(errorMessage);
	}
	
}
