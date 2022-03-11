package it.unifi.dinfo.controller.spec;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
import it.unifi.dinfo.view.spec.ListsView.ERRORS;

public class ListsControllerTest {

	@Mock
	private ToDoView toDoView;
	
	@Mock
	private ToDoRepository toDoRepository;
	
	private ListsController listsController;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		listsController = new ListsController(toDoView, toDoRepository);
	}
	
	@Test
	public void shouldGetAllCallFindAllOnRepositoryAndShowAllOnView() {
		Set<List> lists = Collections.emptySet();
		when(toDoRepository.findAllListsByUserId(1L)).thenReturn(lists);
		listsController.getAll(1L);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).findAllListsByUserId(1L);
		inOrder.verify(toDoView).showAllLists(lists);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldDeleteFirstDeleteRelatedDetailsAndThenGivenListCallingDeleteMethodsOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		Detail detail = new Detail("TEST-D", list);
		Set<Detail> details = new HashSet<>();
		details.add(detail);
		when(toDoRepository.findListById(list.getId())).thenReturn(list);
		when(toDoRepository.findAllDetailsByListId(list.getId())).thenReturn(details);
		listsController.delete(list);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).findAllDetailsByListId(list.getId());
		inOrder.verify(toDoRepository).deleteDetail(detail);
		inOrder.verify(toDoRepository).deleteList(list);
		inOrder.verify(toDoView).deleteList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldDeleteCallRenderListsErrorOnViewWhenListIsNotFoundOnRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		when(toDoRepository.findListById(list.getId())).thenReturn(null);
		listsController.delete(list);
		verify(toDoView).renderListsError(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
		verify(toDoRepository, never()).deleteList(list);
		verify(toDoView, never()).deleteList(list);
	}
	
}
