package it.unifi.dinfo.controller.spec;

import static org.junit.Assert.assertTrue;
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

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

public class DetailsControllerTest {

	@Mock
	private ToDoView toDoView;
	
	@Mock
	private ToDoRepository toDoRepository;
	
	private DetailsController detailsController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		detailsController = new DetailsController(toDoView, toDoRepository);
	}
	
	@Test
	public void shouldGetAllCallFindAllOnRepositoryAndShowAllOnView() {
		Set<Detail> details = Collections.emptySet();
		when(toDoRepository.findAllDetailsByListId(1L)).thenReturn(details);
		detailsController.getAll(1L);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).findAllDetailsByListId(1L);
		inOrder.verify(toDoView).showAllDetails(details);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldDeleteCallDeleteOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detailsController.delete(detail);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).deleteDetail(detail);
		inOrder.verify(toDoView).deleteDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldModifyDoneSetDoneOnDetailAndCallSaveOnRepositoryAndOnView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		when(toDoRepository.saveDetail(detail)).thenReturn(detail);
		detailsController.modifyDone(Boolean.TRUE, detail);
		assertTrue(detail.getDone());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveDetail(detail);
		inOrder.verify(toDoView).saveDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
}
