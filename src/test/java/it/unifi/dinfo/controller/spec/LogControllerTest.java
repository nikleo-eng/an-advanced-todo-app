package it.unifi.dinfo.controller.spec;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.spec.LoginView.ERRORS;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
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
	@Parameters(
			method = 
				"parametersForshouldLoginWithEmailNullOrBlankOrPasswordNullOrEmptyRenderErrorEmptyField")
	public void shouldLoginWithEmailNullOrBlankOrPasswordNullOrEmptyRenderErrorEmptyField(
			String email, String password) {
		logController.login(email, password);
		verify(toDoView).renderLoginError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class), any(Log.class), any(Log.class));
	}
	
	@SuppressWarnings("unused")
	private Object[] parametersForshouldLoginWithEmailNullOrBlankOrPasswordNullOrEmptyRenderErrorEmptyField() {
	    return new Object[] { 
	        new String[] { null, "password" }, 
	        new String[] { " ", "password" }, 
	        new String[] { "email@email.com", null }, 
	        new String[] { "email@email.com", "" }, 
	        new String[] { null, null }, 
	        new String[] { " ", "" } 
	    };
	}
	
	@Test
	public void shouldLoginWithEmailOfNoUserRenderErrorUserNotFound() {
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(null);
		logController.login("email@email.com", "password");
		verify(toDoView).renderLoginError(ERRORS.USER_NOT_FOUND.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class), any(Log.class), any(Log.class));
	}
	
	@Test
	public void shouldLoginWithWrongPasswordPerUserRenderErrorIncorrectPassword() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(user);
		logController.login("email@email.com", "passwordWrong");
		verify(toDoView).renderLoginError(ERRORS.INCORRECT_PASSWORD.getValue());
		verify(toDoView, never()).userLoggedIn(any(User.class), any(Log.class), any(Log.class));
	}
	
	@Test
	public void shouldCorrectLoginCallUserLoggedInSetCurrentUserAndSetCurrentLogOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		Log log = new Log(new Date(), user);
		log.setId(1L);
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(user);
		when(toDoRepository.createLog(any(Log.class))).thenReturn(log);
		when(toDoRepository.findLastLogBeforeIdAndByUserId(log.getId(), user.getId())).thenReturn(null);
		logController.login("email@email.com", "password");
		verify(toDoView).userLoggedIn(user, log, null);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository, toDoView));
	}
	
	@Test
	public void shouldLogoutSetOutOnGivenLogCallSaveLogOnRepositoryAndIfAppIsNotStoppedCallUserLoggedOutOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		when(toDoRepository.saveLog(log)).thenReturn(log);
		logController.logout(log, false);
		assertNotNull(log.getOut());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveLog(log);
		inOrder.verify(toDoView).userLoggedOut();
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldLogoutSetOutOnGivenLogCallSaveLogOnRepositoryAndIfAppIsStoppedNotCallUserLoggedOutOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		when(toDoRepository.saveLog(log)).thenReturn(log);
		logController.logout(log, true);
		assertNotNull(log.getOut());
		verify(toDoRepository).saveLog(log);
		verify(toDoView, never()).userLoggedOut();
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
}
