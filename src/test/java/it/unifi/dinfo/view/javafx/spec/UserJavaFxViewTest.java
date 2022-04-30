package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.UserJavaFxView.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserJavaFxViewTest extends ApplicationTest {

	@Mock
	private ToDoController toDoController;
	
	@Mock
	private DetailsJavaFxView detailsJavaFxView;
	
	@Mock
	private ListsJavaFxView listsJavaFxView;
	
	@Mock
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	
	@Mock
	private LoginJavaFxView loginJavaFxView;
	
	@Mock
	private RegistrationJavaFxView registrationJavaFxView;
	
	private UserJavaFxView userJavaFxView;
	
	@Override
	public void start(Stage stage) throws Exception {
		MockitoAnnotations.openMocks(this);
		userJavaFxView = new UserJavaFxView(toDoController, listsJavaFxView, detailsJavaFxView, 
				additionModificationJavaFxView, loginJavaFxView, registrationJavaFxView);
		
		FlowPane appRoot = new FlowPane();
		/* https://stackoverflow.com/questions/67893273 */
		appRoot.setStyle(FONT_FAMILY);
		Scene scene = new Scene(appRoot, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = userJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		((HBox) vBox.getChildren().get(0)).setAlignment(Pos.CENTER);
		appRoot.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@Test
	public void shouldViewContainUserAndLogTextsAndRefreshAndLogoutButtons() {
		Node userTextNode = lookup("#" + USER_TEXT_ID).tryQuery().orElse(null);
		assertThat(userTextNode).isNotNull().isOfAnyClassIn(Text.class);
		
		Node logTextNode = lookup("#" + LOG_TEXT_ID).tryQuery().orElse(null);
		assertThat(logTextNode).isNotNull().isOfAnyClassIn(Text.class);
		
		Node refreshButtonNode = lookup("#" + REFRESH_BUTTON_ID).tryQuery().orElse(null);
		assertThat(refreshButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button refreshButton = (Button) refreshButtonNode;
		assertThat(refreshButton.isVisible()).isTrue();
		assertThat(refreshButton.getText()).isEqualTo(REFRESH_BUTTON_TEXT);
		
		Node logoutButtonNode = lookup("#" + LOGOUT_BUTTON_ID).tryQuery().orElse(null);
		assertThat(logoutButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button logoutButton = (Button) logoutButtonNode;
		assertThat(logoutButton.getText()).isEqualTo(LOGOUT_BUTTON_TEXT);
	}
	
	@Test
	public void shouldUserTextBeInitiallyEmpty() {
		assertThat(lookup("#" + USER_TEXT_ID).queryText().getText()).isEmpty();
	}
	
	@Test
	public void shouldLogTextNotReportInitiallyADate() {
		assertThat(lookup("#" + LOG_TEXT_ID).queryText().getText()
				.replaceFirst(LOG_STARTING_TEXT, "")).isEmpty();
	}
	
	@Test
	public void shouldUserAndLogTextsAndLogoutButtonBeInitiallyInvisible() {
		assertThat(lookup("#" + USER_TEXT_ID).queryText().isVisible()).isFalse();
		assertThat(lookup("#" + LOG_TEXT_ID).queryText().isVisible()).isFalse();
		assertThat(lookup("#" + LOGOUT_BUTTON_ID).queryButton().isVisible()).isFalse();
	}
	
	@Test
	public void shouldRefreshButtonBeInitiallyDisabled() {
		assertThat(lookup("#" + REFRESH_BUTTON_ID).queryButton().isDisable()).isTrue();
	}
	
	@Test
	public void shouldUserLoggedInCallResetGUIOnLoginAndRegistrationViewsEnableListsViewAndDisableDetailsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		userJavaFxView.userLoggedIn(user, log, null);
		verify(loginJavaFxView).resetGUI();
		verify(registrationJavaFxView).resetGUI();
		verify(listsJavaFxView).enableArea();
		verify(detailsJavaFxView).disableArea();
	}
	
	@Test
	public void shouldUserLoggedInEnableAllOnThisView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		userJavaFxView.userLoggedIn(user, log, null);
		assertThat(lookup("#" + USER_TEXT_ID).queryText().isVisible()).isTrue();
		assertThat(lookup("#" + LOG_TEXT_ID).queryText().isVisible()).isTrue();
		assertThat(lookup("#" + LOGOUT_BUTTON_ID).queryButton().isVisible()).isTrue();
		assertThat(lookup("#" + REFRESH_BUTTON_ID).queryButton().isDisable()).isFalse();
	}
	
	@Test
	public void shouldUserLoggedInSetCurrentUserAndCurrentLog() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		userJavaFxView.userLoggedIn(user, log, null);
		assertThat(userJavaFxView.getCurrentUser()).isEqualTo(user);
		assertThat(userJavaFxView.getCurrentLog()).isEqualTo(log);
	}
	
	@Test
	public void shouldUserLoggedInModifyTextInUserAndLogTexts() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		Calendar calMin2H = Calendar.getInstance();
		calMin2H.add(Calendar.HOUR, -2);
		Log lastLog = new Log(calMin2H.getTime(), user);
		calMin2H.add(Calendar.MINUTE, +1);
		lastLog.setOut(calMin2H.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat(SDF_PATTERN, Locale.ITALIAN);
		userJavaFxView.userLoggedIn(user, log, lastLog);
		assertThat(lookup("#" + USER_TEXT_ID).queryText().getText()).isEqualTo(
				user.getName() + " " + user.getSurname());
		assertThat(lookup("#" + LOG_TEXT_ID).queryText().getText()).isEqualTo(
				LOG_STARTING_TEXT + sdf.format(lastLog.getIn()));
	}
	
	@Test
	public void shouldUserLoggedInCallGetAllListsOnController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		userJavaFxView.setCurrentUser(user);
		Log log = new Log(new Date(), user);
		userJavaFxView.userLoggedIn(user, log, null);
		verify(toDoController).getAllLists(user.getId());
	}
	
	@Test
	public void shouldUserLoggedInNotReportADateButNAInLogTextWhenIsNotGivenALastLog() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		userJavaFxView.userLoggedIn(user, log, null);
		assertThat(lookup("#" + LOG_TEXT_ID).queryText().getText()).isEqualTo(
				LOG_STARTING_TEXT + LOG_NOT_AVAILABLE_TEXT);
	}
	
	@Test
	public void shouldResetGUIClearUserAndLogTextAndDisableAllOnThisView() {
		Text userText = lookup("#" + USER_TEXT_ID).queryText();
		userText.setText("Mario Rossi");
		userText.setVisible(true);
		Text logText = lookup("#" + LOG_TEXT_ID).queryText();
		logText.setText(logText.getText() + "10/03/2022 21:05:20 UTC");
		logText.setVisible(true);
		Button logoutButton = lookup("#" + LOGOUT_BUTTON_ID).queryButton();
		logoutButton.setVisible(true);
		Button refreshButton = lookup("#" + REFRESH_BUTTON_ID).queryButton();
		refreshButton.setDisable(false);
		userJavaFxView.resetGUI();
		assertThat(userText.getText()).isEmpty();
		assertThat(logText.getText().replaceFirst(LOG_STARTING_TEXT, "")).isEmpty();
		assertThat(userText.isVisible()).isFalse();
		assertThat(logText.isVisible()).isFalse();
		assertThat(logoutButton.isVisible()).isFalse();
		assertThat(refreshButton.isDisable()).isTrue();
	}
	
	@Test
	public void shouldClickOnLogoutButtonResetGUIOnThisAndAllOthersViewsAndCallLogoutOnController() {
		Text userText = lookup("#" + USER_TEXT_ID).queryText();
		userText.setText("Mario Rossi");
		userText.setVisible(true);
		Text logText = lookup("#" + LOG_TEXT_ID).queryText();
		logText.setText(logText.getText() + "10/03/2022 21:05:20 UTC");
		logText.setVisible(true);
		Button logoutButton = lookup("#" + LOGOUT_BUTTON_ID).queryButton();
		logoutButton.setVisible(true);
		Button refreshButton = lookup("#" + REFRESH_BUTTON_ID).queryButton();
		refreshButton.setDisable(false);
		clickOn(logoutButton);
		verify(listsJavaFxView).resetGUI();
		verify(detailsJavaFxView).resetGUI();
		verify(additionModificationJavaFxView).resetGUI();
		verify(toDoController).logout(null, false);
		assertThat(userText.getText()).isEmpty();
		assertThat(logText.getText().replaceFirst(LOG_STARTING_TEXT, "")).isEmpty();
		assertThat(userText.isVisible()).isFalse();
		assertThat(logText.isVisible()).isFalse();
		assertThat(logoutButton.isVisible()).isFalse();
		assertThat(refreshButton.isDisable()).isTrue();
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView, 
				additionModificationJavaFxView));
	}
	
	@Test
	public void shouldClickOnRefreshCallClearAllOnListsAndDetailsViewEnableListsViewDisableDetailsViewAndCallGetAllListsOnController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		userJavaFxView.setCurrentUser(user);
		Button refreshButton = lookup("#" + REFRESH_BUTTON_ID).queryButton();
		refreshButton.setDisable(false);
		clickOn(refreshButton);
		verify(listsJavaFxView).clearAll();
		verify(detailsJavaFxView).clearAll();
		verify(listsJavaFxView).enableArea();
		verify(detailsJavaFxView).disableArea();
		verify(toDoController).getAllLists(user.getId());
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView, 
				additionModificationJavaFxView));
	}
	
	@Test
	public void shouldGetCurrentUserReturnCurrentUser() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		userJavaFxView.setCurrentUser(user);
		assertThat(userJavaFxView.getCurrentUser()).isEqualTo(user);
	}
	
	@Test
	public void shouldSetCurrentLogSetEffectivelyTheCurrentLog() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		userJavaFxView.setCurrentLog(log);
		assertThat(userJavaFxView.getCurrentLog()).isEqualTo(log);
	}
	
}
