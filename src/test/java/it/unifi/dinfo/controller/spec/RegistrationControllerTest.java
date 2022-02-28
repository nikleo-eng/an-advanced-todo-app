package it.unifi.dinfo.controller.spec;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.spec.RegistrationView.ERRORS;

public class RegistrationControllerTest {
	
	@Mock
	private ToDoView toDoView;
	
	@Mock
	private ToDoRepository toDoRepository;
	
	private RegistrationController registrationController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		registrationController = new RegistrationController(toDoView, toDoRepository);
	}
	
	@Test
	public void shouldRegistrationWithNameNullRenderErrorEmptyField() {
		registrationController.register(null, "Rossi", "email@email.com", "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithSurnameNullRenderErrorEmptyField() {
		registrationController.register("Mario", null, "email@email.com", "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithEmailNullRenderErrorEmptyField() {
		registrationController.register("Mario", "Rossi", null, "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithPasswordNullRenderErrorEmptyField() {
		registrationController.register("Mario", "Rossi", "email@email.com", null, "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithRepeatedPasswordNullRenderErrorEmptyField() {
		registrationController.register("Mario", "Rossi", "email@email.com", "password", null);
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithNameBlankRenderErrorEmptyField() {
		registrationController.register(" ", "Rossi", "email@email.com", "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithSurnameBlankRenderErrorEmptyField() {
		registrationController.register("Mario", " ", "email@email.com", "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithEmailBlankRenderErrorEmptyField() {
		registrationController.register("Mario", "Rossi", " ", "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithPasswordEmptyRenderErrorEmptyField() {
		registrationController.register("Mario", "Rossi", "email@email.com", "", "password");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithRepeatedPasswordEmptyRenderErrorEmptyField() {
		registrationController.register("Mario", "Rossi", "email@email.com", "password", "");
		verify(toDoView).renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithPasswordsNotMachingRenderErrorPasswordsNotMatching() {
		registrationController.register("Mario", "Rossi", "email@email.com", "password", "password2");
		verify(toDoView).renderRegistrationError(ERRORS.PASSWORDS_NOT_MACHING.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldRegistrationWithEmailAlreadyRegisteredRenderErrorUserAlreadyFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(user);
		registrationController.register("Mario", "Rossi", "email@email.com", "password", "password");
		verify(toDoView).renderRegistrationError(ERRORS.USER_ALREADY_FOUND.getValue());
		verify(toDoRepository, never()).createUser(any(User.class));
		verify(toDoView, never()).setCurrentUser(any(User.class));
	}
	
	@Test
	public void shouldCorrectRegistrationCallCreateUserOnRepositoryAndSetCurrentUserOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		when(toDoRepository.findUserByEmail("email@email.com")).thenReturn(null);
		when(toDoRepository.createUser(any(User.class))).thenReturn(user);
		registrationController.register("Mario", "Rossi", "email@email.com", "password", "password");
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).createUser(any(User.class));
		inOrder.verify(toDoView).setCurrentUser(user);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}

}
