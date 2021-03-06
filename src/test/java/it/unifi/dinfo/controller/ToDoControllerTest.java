package it.unifi.dinfo.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.controller.spec.AdditionModificationController;
import it.unifi.dinfo.controller.spec.DetailsController;
import it.unifi.dinfo.controller.spec.ListsController;
import it.unifi.dinfo.controller.spec.LogController;
import it.unifi.dinfo.controller.spec.RegistrationController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;

public class ToDoControllerTest {

	@Mock
	private RegistrationController registrationController;
	
	@Mock
	private LogController logController;
	
	@Mock
	private ListsController listsController;
	
	@Mock
	private DetailsController detailsController;
	
	@Mock
	private AdditionModificationController additionModificationController;
	
	private ToDoController toDoController;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		toDoController = new ToDoController(logController, registrationController, listsController, 
				detailsController, additionModificationController);
	}
	
	@Test
	public void shouldLoginCallLoginOnLogController() {
		toDoController.login("email@email.com", "password");
		verify(logController).login("email@email.com", "password");
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldLogoutCallLogoutOnLogController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		toDoController.logout(log, false);
		verify(logController).logout(log, false);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldRegisterCallRegisterOnRegistrationController() {
		toDoController.register("Mario", "Rossi", "email@email.com", "password", "password");
		verify(registrationController).register("Mario", "Rossi", "email@email.com", "password", 
				"password");
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldAddListCallAddListOnAdditionModificationController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		toDoController.addList("TEST", user);
		verify(additionModificationController).addList("TEST", user);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldAddDetailCallAddDetailOnAdditionModificationController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoController.addDetail("TEST-D", list);
		verify(additionModificationController).addDetail("TEST-D", list);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldModifyNameListCallModifyNameListOnAdditionModificationController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoController.modifyNameList("TEST_NEW", list);
		verify(additionModificationController).modifyNameList("TEST_NEW", list);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldModifyTodoDetailCallModifyTodoDetailOnAdditionModificationController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoController.modifyTodoDetail("TEST-D_NEW", detail);
		verify(additionModificationController).modifyTodoDetail("TEST-D_NEW", detail);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldModifyDoneDetailCallModifyDoneOnDetailsController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoController.modifyDoneDetail(Boolean.TRUE, detail);
		verify(detailsController).modifyDone(Boolean.TRUE, detail);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldDeleteListCallDeleteOnListsController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoController.deleteList(list);
		verify(listsController).delete(list);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldDeleteDetailCallDeleteOnDetailsController() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoController.deleteDetail(detail);
		verify(detailsController).delete(detail);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldGetAllListsCallGetAllOnListsController() {
		toDoController.getAllLists(1L);
		verify(listsController).getAll(1L);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
	@Test
	public void shouldGetAllDetailsCallGetAllOnDetailsController() {
		toDoController.getAllDetails(1L);
		verify(detailsController).getAll(1L);
		verifyNoMoreInteractions(ignoreStubs(logController, registrationController, listsController, 
				detailsController, additionModificationController));
	}
	
}
