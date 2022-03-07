package it.unifi.dinfo.controller.spec;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.spec.LoginView.ERRORS;

public class LogControllerTest {

	@Mock
	private ToDoView toDoView;
	
	@Mock
	private ToDoRepository toDoRepository;
	
	private LogController logController;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		logController = new LogController(toDoView, toDoRepository);
	}
	
	@Test
	public void shouldLoginWithEmailNullRenderErrorEmptyField() {
		logController.login(null, "password");
		verify(toDoView).renderLoginError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class));
	}
	
	@Test
	public void shouldLoginWithEmailBlankRenderErrorEmptyField() {
		logController.login(" ", "password");
		verify(toDoView).renderLoginError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class));
	}
	
	@Test
	public void shouldLoginWithPasswordNullRenderErrorEmptyField() {
		logController.login("email@email.com", null);
		verify(toDoView).renderLoginError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class));
	}
	
	@Test
	public void shouldLoginWithPasswordEmptyRenderErrorEmptyField() {
		logController.login("email@email.com", "");
		verify(toDoView).renderLoginError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class));
	}
	
	@Test
	public void shouldLoginWithEmailOfNoUserRenderErrorUserNotFound() {
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(null);
		logController.login("email@email.com", "password");
		verify(toDoView).renderLoginError(ERRORS.USER_NOT_FOUND.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class));
	}
	
	@Test
	public void shouldLoginWithWrongPasswordPerUserRenderErrorIncorrectPassword() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(user);
		logController.login("email@email.com", "passwordWrong");
		verify(toDoView).renderLoginError(ERRORS.INCORRECT_PASSWORD.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class));
	}
	
	@Test
	public void shouldCorrectLoginCallUserLoggedInOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(user);
		logController.login("email@email.com", "password");
		verify(toDoView).userLoggedIn(user);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldLogoutCallOnlyUserLoggedOutOnView() {
		logController.logout();
		verify(toDoView).userLoggedOut();
		verifyNoMoreInteractions(ignoreStubs(toDoRepository, toDoView));
	}
	
}
