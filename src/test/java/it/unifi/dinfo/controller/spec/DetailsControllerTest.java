package it.unifi.dinfo.controller.spec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import it.unifi.dinfo.view.spec.DetailsView.ERRORS;

public class DetailsControllerTest {

	@Mock
	private ToDoView toDoView;
	
	@Mock
	private ToDoRepository toDoRepository;
	
	private DetailsController detailsController;

	@Before
	public void setUp() {
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
	public void shouldDeleteCallDeleteOnRepositoryAndOnViewWhenDetailIsFoundOnRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		when(toDoRepository.findDetailById(detail.getId())).thenReturn(detail);
		detailsController.delete(detail);
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).deleteDetail(detail);
		inOrder.verify(toDoView).deleteDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldDeleteCallRenderDetailsErrorOnViewWhenDetailIsNotFoundOnRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		when(toDoRepository.findDetailById(detail.getId())).thenReturn(null);
		detailsController.delete(detail);
		verify(toDoView).renderDetailsError(ERRORS.DETAIL_NO_LONGER_EXISTS.getValue());
		verify(toDoRepository, never()).deleteDetail(detail);
		verify(toDoView, never()).deleteDetail(detail);
	}
	
	@Test
	public void shouldModifyDoneSetDoneOnDetailAndCallSaveOnRepositoryAndOnViewWhenDetailIsFoundOnRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		when(toDoRepository.findDetailById(detail.getId())).thenReturn(detail);
		when(toDoRepository.saveDetail(detail)).thenReturn(detail);
		detailsController.modifyDone(Boolean.TRUE, detail);
		assertTrue(detail.getDone());
		InOrder inOrder = inOrder(toDoRepository, toDoView);
		inOrder.verify(toDoRepository).saveDetail(detail);
		inOrder.verify(toDoView).saveDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoRepository));
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldModifyDoneCallRenderDetailsErrorOnViewAndSaveOnRepositoryWhenDetailIsNotFoundOnRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		when(toDoRepository.findDetailById(detail.getId())).thenReturn(null);
		detailsController.modifyDone(Boolean.TRUE, detail);
		assertFalse(detail.getDone());
		verify(toDoView).renderDetailsError(ERRORS.DETAIL_NO_LONGER_EXISTS.getValue());
		verify(toDoView).saveDetail(detail);
		verify(toDoRepository, never()).saveDetail(detail);
	}
	
}
