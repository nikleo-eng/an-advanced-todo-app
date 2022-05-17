package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;

public class UserJavaFxViewStep extends ApplicationTest {

	@Then("The user text shows {string}")
	public void theUserTextShowsNameSurname(String nameAndSurname) {
		String userText = lookup("#" + UserJavaFxView.USER_TEXT_ID).queryText().getText();
		assertThat(userText).isEqualTo(nameAndSurname);
	}
	
	@Then("The log text shows that the last login is happened at {string}")
	public void theLogTextShowsThatTheLastLoginIsHappenedAtIn(String in) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(UserJavaFxView.SDF_PATTERN, Locale.ITALIAN);
		sdf.setLenient(true);
		Date inDate = sdf.parse(in);
		String lastLogText = lookup("#" + UserJavaFxView.LOG_TEXT_ID).queryText().getText();
		assertThat(lastLogText).startsWith(UserJavaFxView.LOG_STARTING_TEXT);
		Date logDate = sdf.parse(lastLogText.substring(UserJavaFxView.LOG_STARTING_TEXT.length()));
		assertThat(inDate).isEqualTo(logDate);
	}
	
	@When("The user clicks the Logout button")
	public void theUserClicksTheLogoutButton() {
		clickOn("#" + UserJavaFxView.LOGOUT_BUTTON_ID);
	}
	
	@When("The user clicks the Refresh button")
	public void theUserClicksTheRefreshButton() {
		clickOn("#" + UserJavaFxView.REFRESH_BUTTON_ID);
	}
	
}
