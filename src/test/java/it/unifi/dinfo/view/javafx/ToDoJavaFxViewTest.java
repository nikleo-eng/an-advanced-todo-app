package it.unifi.dinfo.view.javafx;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import it.unifi.dinfo.view.spec.AdditionModificationView;
import it.unifi.dinfo.view.spec.LoginView;
import it.unifi.dinfo.view.spec.RegistrationView;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ToDoJavaFxViewTest extends ApplicationTest {
	
	@Mock
	private ToDoController toDoController;
	
	@Mock
	private LoginJavaFxView loginJavaFxView;
	
	@Mock
	private RegistrationJavaFxView registrationJavaFxView;
	
	@Mock
	private ListsJavaFxView listsJavaFxView;
	
	@Mock
	private DetailsJavaFxView detailsJavaFxView;
	
	@Mock
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	
	@Mock
	private UserJavaFxView userJavaFxView;
	
	private static final String LOGIN_TEXT = "LOGIN";
	private static final String REGISTRATION_TEXT = "REGISTRATION";
	private static final String LISTS_TEXT = "LISTS";
	private static final String DETAILS_TEXT = "DETAILS";
	private static final String ADDITION_MODIFICATION_TEXT = "ADDITION_MODIFICATION";
	private static final String USER_TEXT = "USER";
	
	private ToDoJavaFxView toDoJavaFxView = new ToDoJavaFxView();
	
	@Override
	public void init() throws Exception {
		MockitoAnnotations.openMocks(this);
		toDoJavaFxView.setToDoController(toDoController);
		toDoJavaFxView.setLoginJavaFxView(loginJavaFxView);
		toDoJavaFxView.setRegistrationJavaFxView(registrationJavaFxView);
		toDoJavaFxView.setListsJavaFxView(listsJavaFxView);
		toDoJavaFxView.setDetailsJavaFxView(detailsJavaFxView);
		toDoJavaFxView.setAdditionModificationJavaFxView(additionModificationJavaFxView);
		toDoJavaFxView.setUserJavaFxView(userJavaFxView);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		when(loginJavaFxView.createGUI(anyDouble(), anyDouble())).then(ans -> 
			createVBox(ans.getArgument(0), ans.getArgument(1), LOGIN_TEXT));
		when(registrationJavaFxView.createGUI(anyDouble(), anyDouble())).then(ans -> 
			createVBox(ans.getArgument(0), ans.getArgument(1), REGISTRATION_TEXT));
		when(listsJavaFxView.createGUI(anyDouble(), anyDouble())).then(ans -> 
			createVBox(ans.getArgument(0), ans.getArgument(1), LISTS_TEXT));
		when(detailsJavaFxView.createGUI(anyDouble(), anyDouble())).then(ans -> 
			createVBox(ans.getArgument(0), ans.getArgument(1), DETAILS_TEXT));
		when(additionModificationJavaFxView.createGUI(anyDouble(), anyDouble())).then(ans -> 
			createVBox(ans.getArgument(0), ans.getArgument(1), ADDITION_MODIFICATION_TEXT));
		when(userJavaFxView.createGUI(anyDouble(), anyDouble())).then(ans -> 
			createVBox(ans.getArgument(0), ans.getArgument(1), USER_TEXT));
		
		toDoJavaFxView.start(stage);
	}
	
	private static VBox createVBox(double width, double height, String text) {
		VBox vBox = new VBox();
		vBox.setPrefSize(width, height);
		vBox.setAlignment(Pos.CENTER);
		Text inText = new Text(text);
		vBox.getChildren().add(inText);
		return vBox;
	}
	
	@Test
	public void shouldViewContainInitiallyOnlyLoginAndRegistrationAreas() {
		assertThat(lookup(LOGIN_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(REGISTRATION_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(LISTS_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(DETAILS_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(ADDITION_MODIFICATION_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(USER_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
	}
	
	@Test
	public void shouldUserLoggedInChangeRootSoThatViewContainOnlyListsDetailsAdditionModificationAndUserViews() {
		toDoJavaFxView.userLoggedIn(new User("Mario", "Rossi", "email@email.com", "password"));
		assertThat(lookup(LOGIN_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(REGISTRATION_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(LISTS_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(DETAILS_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(ADDITION_MODIFICATION_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(USER_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getAppRoot());
	}
	
	@Test
	public void shouldUserLoggedOutChangeRootSoThatViewContainOnlyLoginAndRegistrationAreas() {
		window(STAGE_TITLE).getScene().setRoot(toDoJavaFxView.getAppRoot());
		toDoJavaFxView.userLoggedOut();
		assertThat(lookup(LOGIN_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(REGISTRATION_TEXT).tryQuery().isPresent()).isTrue();
		assertThat(lookup(LISTS_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(DETAILS_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(ADDITION_MODIFICATION_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(lookup(USER_TEXT).tryQuery().isPresent()).isFalse();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
	}
	
	@Test
	public void shouldAddListCallAddOnListsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoJavaFxView.addList(list);
		verify(listsJavaFxView).add(list);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldAddDetailCallAddOnDetailsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoJavaFxView.addDetail(detail);
		verify(detailsJavaFxView).add(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldSaveListCallSaveOnListsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoJavaFxView.saveList(list);
		verify(listsJavaFxView).save(list);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldSaveDetailCallSaveOnDetailsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoJavaFxView.saveDetail(detail);
		verify(detailsJavaFxView).save(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldRenderAdditionModificationErrorCallRenderErrorOnAdditionModificationView() {
		toDoJavaFxView.renderAdditionModificationError(
				AdditionModificationView.ERRORS.DETAIL_ALREADY_FOUND.getValue());
		verify(additionModificationJavaFxView).renderError(
				AdditionModificationView.ERRORS.DETAIL_ALREADY_FOUND.getValue());
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldShowAllDetailsCallShowAllOnDetailsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		Set<Detail> details = new HashSet<>();
		details.add(detail);
		toDoJavaFxView.showAllDetails(details);
		verify(detailsJavaFxView).showAll(details);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldDeleteDetailCallDeleteOnDetailsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoJavaFxView.deleteDetail(detail);
		verify(detailsJavaFxView).delete(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldShowAllListsCallShowAllOnListsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Set<List> lists = new HashSet<>();
		lists.add(list);
		toDoJavaFxView.showAllLists(lists);
		verify(listsJavaFxView).showAll(lists);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldDeleteListCallDeleteOnListsView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoJavaFxView.deleteList(list);
		verify(listsJavaFxView).delete(list);
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldRenderLoginErrorCallRenderErrorOnLoginView() {
		toDoJavaFxView.renderLoginError(LoginView.ERRORS.USER_NOT_FOUND.getValue());
		verify(loginJavaFxView).renderError(LoginView.ERRORS.USER_NOT_FOUND.getValue());
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
	@Test
	public void shouldRenderRegistrationErrorCallRenderErrorOnRegistrationView() {
		toDoJavaFxView.renderRegistrationError(RegistrationView.ERRORS.USER_ALREADY_FOUND.getValue());
		verify(registrationJavaFxView).renderError(RegistrationView.ERRORS.USER_ALREADY_FOUND.getValue());
		verifyNoMoreInteractions(ignoreStubs(toDoController, loginJavaFxView, registrationJavaFxView, 
				listsJavaFxView, detailsJavaFxView, additionModificationJavaFxView, userJavaFxView));
	}
	
}
