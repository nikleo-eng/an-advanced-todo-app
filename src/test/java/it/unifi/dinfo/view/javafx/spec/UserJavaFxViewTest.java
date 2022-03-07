package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.UserJavaFxView.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
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
		Scene scene = new Scene(appRoot, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = userJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		((HBox) vBox.getChildren().get(0)).setAlignment(Pos.CENTER);
		appRoot.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@Test
	public void shouldViewContainUserTextAndRefreshAndLogoutButtons() {
		Node userTextNode = lookup("#" + USER_TEXT_ID).tryQuery().orElse(null);
		assertThat(userTextNode).isNotNull().isOfAnyClassIn(Text.class);
		Text userText = (Text) userTextNode;
		assertThat(userText.isVisible()).isTrue();
		
		Node refreshButtonNode = lookup("#" + REFRESH_BUTTON_ID).tryQuery().orElse(null);
		assertThat(refreshButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button refreshButton = (Button) refreshButtonNode;
		assertThat(refreshButton.isVisible()).isTrue();
		assertThat(refreshButton.getText()).isEqualTo(REFRESH_BUTTON_TEXT);
		
		Node logoutButtonNode = lookup("#" + LOGOUT_BUTTON_ID).tryQuery().orElse(null);
		assertThat(logoutButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button logoutButton = (Button) logoutButtonNode;
		assertThat(logoutButton.isVisible()).isTrue();
		assertThat(logoutButton.getText()).isEqualTo(LOGOUT_BUTTON_TEXT);
	}
	
	@Test
	public void shouldUserTextBeInitiallyEmpty() {
		assertThat(lookup("#" + USER_TEXT_ID).queryText().getText()).isEmpty();
	}
	
	@Test
	public void shouldUserLoggedInChangeTextOfCurrentUserTextAndSetCurrentUserOnAdditionModificationView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		userJavaFxView.userLoggedIn(user);
		
		assertThat(lookup("#" + USER_TEXT_ID).queryText().getText()).isEqualTo(user.getName() 
				+ " " + user.getSurname());
		verify(additionModificationJavaFxView).setCurrentUser(user);
	}
	
	@Test
	public void shouldUserLoggedInCallResetGUIOnLoginAndRegistrationViewsEnableListsViewAndDisableDetailsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		userJavaFxView.userLoggedIn(user);
		
		verify(loginJavaFxView).resetGUI();
		verify(registrationJavaFxView).resetGUI();
		verify(listsJavaFxView).enableArea();
		verify(detailsJavaFxView).disableArea();
	}
	
	@Test
	public void shouldUserLoggedInCallGetAllListsOnController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		userJavaFxView.userLoggedIn(user);
		verify(toDoController).getAllLists(user.getId());
	}
	
	@Test
	public void shouldResetGUIClearUserText() {
		userJavaFxView.resetGUI();
		assertThat(lookup("#" + USER_TEXT_ID).queryText().getText()).isEmpty();
	}
	
	@Test
	public void shouldClickOnLogoutButtonResetGUIOnThisAndAllOthersViewsAndResetUserOnAdditionModificationView() {
		clickOn("#" + LOGOUT_BUTTON_ID);
		
		assertThat(lookup("#" + USER_TEXT_ID).queryText().getText()).isEmpty();
		verify(listsJavaFxView).resetGUI();
		verify(detailsJavaFxView).resetGUI();
		verify(additionModificationJavaFxView).resetGUI();
		verify(additionModificationJavaFxView).setCurrentUser(null);
	}
	
	@Test
	public void shouldClickOnLogoutButtonCallLogoutOnController() {
		clickOn("#" + LOGOUT_BUTTON_ID);
		verify(toDoController).logout();
	}
	
	@Test
	public void shouldClickOnRefreshCallClearAllOnListsAndDetailsViewEnableListsViewDisableDetailsViewAndCallGetAllListsOnController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		
		userJavaFxView.setCurrentUser(user);
		
		clickOn("#" + REFRESH_BUTTON_ID);
		
		verify(listsJavaFxView).clearAll();
		verify(detailsJavaFxView).clearAll();
		verify(listsJavaFxView).enableArea();
		verify(detailsJavaFxView).disableArea();
		verify(toDoController).getAllLists(user.getId());
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView, 
				additionModificationJavaFxView));
	}
	
}
