package it.unifi.dinfo.controller.spec;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.spec.AdditionModificationView.ERRORS;

public class AdditionModificationControllerTest {

	@Mock
	private ToDoView toDoView;
	
	@Mock
	private ToDoRepository toDoRepository;
	
	private AdditionModificationController additionModificationController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		additionModificationController = new AdditionModificationController(toDoView, toDoRepository);
	}
	
	@Test
	public void shouldAddListWithNameNullRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		additionModificationController.addList(null, user);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createList(any(List.class));
		verify(toDoView, never()).addList(any(List.class));
	}
	
	@Test
	public void shouldAddListWithNameBlankRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		additionModificationController.addList(" ", user);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createList(any(List.class));
		verify(toDoView, never()).addList(any(List.class));
	}
	
	@Test
	public void shouldAddListWithListAlreadyCreatedRenderErrorListAlreadyFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		List list = new List("TEST", user);
		when(toDoRepository.findListByNameAndUserId("TEST", 1L)).thenReturn(list);
		additionModificationController.addList("TEST", user);
		verify(toDoView).renderAdditionModificationError(ERRORS.LIST_ALREADY_FOUND.getValue());
		verify(toDoRepository, never()).createList(any(List.class));
		verify(toDoView, never()).addList(any(List.class));
	}
	
	@Test
	public void shouldCorrectAddListCallCreateOnRepositoryAndAddOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		List list = new List("TEST", user);
		when(toDoRepository.findListByNameAndUserId("TEST", 1L)).thenReturn(null);
		when(toDoRepository.createList(any(List.class))).thenReturn(list);
		additionModificationController.addList("TEST", user);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).createList(any(List.class));
		inOrder.verify(toDoView).addList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldAddDetailWithTodoNullRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		additionModificationController.addDetail(null, list);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createDetail(any(Detail.class));
		verify(toDoView, never()).addDetail(any(Detail.class));
	}
	
	@Test
	public void shouldAddDetailWithTodoBlankRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		additionModificationController.addDetail(" ", list);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).createDetail(any(Detail.class));
		verify(toDoView, never()).addDetail(any(Detail.class));
	}
	
	@Test
	public void shouldAddDetailWithDetailAlreadyCreatedRenderErrorDetailAlreadyFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		Detail detail = new Detail("TEST-D", list);
		when(toDoRepository.findDetailByTodoAndListId("TEST-D", 1L)).thenReturn(detail);
		additionModificationController.addDetail("TEST-D", list);
		verify(toDoView).renderAdditionModificationError(ERRORS.DETAIL_ALREADY_FOUND.getValue());
		verify(toDoRepository, never()).createDetail(any(Detail.class));
		verify(toDoView, never()).addDetail(any(Detail.class));
	}
	
	@Test
	public void shouldCorrectAddDetailCallCreateOnRepositoryAndAddOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		Detail detail = new Detail("TEST-D", list);
		when(toDoRepository.findDetailByTodoAndListId("TEST-D", 1L)).thenReturn(null);
		when(toDoRepository.createDetail(any(Detail.class))).thenReturn(detail);
		additionModificationController.addDetail("TEST-D", list);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).createDetail(any(Detail.class));
		inOrder.verify(toDoView).addDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldModifyTodoDetailWithTodoNullRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		additionModificationController.modifyTodoDetail(null, detail);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).saveDetail(detail);
		verify(toDoView, never()).saveDetail(detail);
	}
	
	@Test
	public void shouldModifyTodoDetailWithTodoBlankRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		additionModificationController.modifyTodoDetail(" ", detail);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).saveDetail(detail);
		verify(toDoView, never()).saveDetail(detail);
	}
	
	@Test
	public void shouldModifyTodoDetailWhenAnotherDetailWithSameTodoIsFoundRenderErrorDetailAlreadyFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		Detail retrievedDetail = new Detail("TEST-D_NEW", list);
		retrievedDetail.setId(2L);
		when(toDoRepository.findDetailByTodoAndListId("TEST-D_NEW", 1L)).thenReturn(retrievedDetail);
		additionModificationController.modifyTodoDetail("TEST-D_NEW", detail);
		verify(toDoView).renderAdditionModificationError(ERRORS.DETAIL_ALREADY_FOUND.getValue());
		verify(toDoRepository, never()).saveDetail(detail);
		verify(toDoView, never()).saveDetail(detail);
	}
	
	@Test
	public void shouldCorrectModifyTodoDetailWhenSameDetailWithSameTodoIsFoundCallSaveOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		Detail retrievedDetail = new Detail("TEST-D_NEW", list);
		retrievedDetail.setId(1L);
		when(toDoRepository.findDetailByTodoAndListId("TEST-D_NEW", 1L)).thenReturn(retrievedDetail);
		when(toDoRepository.saveDetail(detail)).thenReturn(detail);
		additionModificationController.modifyTodoDetail("TEST-D_NEW", detail);
		assertEquals("TEST-D_NEW", detail.getTodo());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveDetail(detail);
		inOrder.verify(toDoView).saveDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectModifyTodoDetailWhenNoDetailWithSameTodoIsFoundCallSaveOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		Detail detail = new Detail("TEST-D", list);
		when(toDoRepository.findDetailByTodoAndListId("TEST-D_NEW", 1L)).thenReturn(null);
		when(toDoRepository.saveDetail(detail)).thenReturn(detail);
		additionModificationController.modifyTodoDetail("TEST-D_NEW", detail);
		assertEquals("TEST-D_NEW", detail.getTodo());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveDetail(detail);
		inOrder.verify(toDoView).saveDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldModifyNameListWithNameNullRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		additionModificationController.modifyNameList(null, list);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).saveList(list);
		verify(toDoView, never()).saveList(list);
	}
	
	@Test
	public void shouldModifyNameListWithNameBlankRenderErrorEmptyField() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		additionModificationController.modifyNameList(" ", list);
		verify(toDoView).renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
		verify(toDoRepository, never()).saveList(list);
		verify(toDoView, never()).saveList(list);
	}
	
	@Test
	public void shouldModifyNameListWhenAnotherListWithSameNameIsFoundRenderErrorListAlreadyFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		List list = new List("TEST", user);
		list.setId(1L);
		List retrievedList = new List("TEST_NEW", user);
		retrievedList.setId(2L);
		when(toDoRepository.findListByNameAndUserId("TEST_NEW", 1L)).thenReturn(retrievedList);
		additionModificationController.modifyNameList("TEST_NEW", list);
		verify(toDoView).renderAdditionModificationError(ERRORS.LIST_ALREADY_FOUND.getValue());
		verify(toDoRepository, never()).saveList(list);
		verify(toDoView, never()).saveList(list);
	}
	
	@Test
	public void shouldCorrectModifyNameListWhenSameListWithSameNameIsFoundCallSaveOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		List list = new List("TEST", user);
		list.setId(1L);
		List retrievedList = new List("TEST_NEW", user);
		retrievedList.setId(1L);
		when(toDoRepository.findListByNameAndUserId("TEST_NEW", 1L)).thenReturn(retrievedList);
		when(toDoRepository.saveList(list)).thenReturn(list);
		additionModificationController.modifyNameList("TEST_NEW", list);
		assertEquals("TEST_NEW", list.getName());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveList(list);
		inOrder.verify(toDoView).saveList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectModifyNameListWhenNoListWithSameNameIsFoundCallSaveOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user.setId(1L);
		List list = new List("TEST", user);
		when(toDoRepository.findListByNameAndUserId("TEST_NEW", 1L)).thenReturn(null);
		when(toDoRepository.saveList(list)).thenReturn(list);
		additionModificationController.modifyNameList("TEST_NEW", list);
		assertEquals("TEST_NEW", list.getName());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveList(list);
		inOrder.verify(toDoView).saveList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
}
