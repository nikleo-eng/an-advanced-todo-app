package it.unifi.dinfo.controller.spec;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

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
	public void shouldDeleteCallDeleteOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listsController.delete(list);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).deleteList(list);
		inOrder.verify(toDoView).deleteList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldRefreshAllCallFindAllOnRepositoryAndUpdateAllOnView() {
		Set<List> lists = Collections.emptySet();
		when(toDoRepository.findAllListsByUserId(1L)).thenReturn(lists);
		listsController.refreshAll(1L);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).findAllListsByUserId(1L);
		inOrder.verify(toDoView).updateAllLists(lists);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
}
