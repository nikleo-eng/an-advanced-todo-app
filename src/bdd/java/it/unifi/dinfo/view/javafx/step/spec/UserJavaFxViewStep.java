package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;

public class UserJavaFxViewStep extends ApplicationTest {

	@Then("The user text shows {string}")
	public void theUserTextShowsNameSurname(String nameAndSurname) {
		String userText = lookup("#" + UserJavaFxView.USER_TEXT_ID).queryText().getText();
		assertThat(userText).isEqualTo(nameAndSurname);
	}
	
	@Then("The log text shows that the last login is happened at {string}")
	public void theLogTextShowsThatTheLastLoginIsHappenedAtIn(String in) {
		String lastLogText = lookup("#" + UserJavaFxView.LOG_TEXT_ID).queryText().getText();
		assertThat(lastLogText).isEqualTo(UserJavaFxView.LOG_STARTING_TEXT + in);
	}
	
}
