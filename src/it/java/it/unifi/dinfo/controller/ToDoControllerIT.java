package it.unifi.dinfo.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import it.unifi.dinfo.guice.controller.ToDoControllerModule;
import it.unifi.dinfo.guice.mysql.ToDoMySqlModule;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;
import it.unifi.dinfo.view.ToDoView;

/**
 * Communicates with a MySQL DMBS server on localhost;
 * within the project root, for Linux containers, build the image on Docker with
 * 
 * <pre>
 * docker build -f mysql.Dockerfile -t custom_mysql:latest .
 * </pre>
 * 
 * otherwise, for Windows containers, build the image on Docker with
 * 
 * <pre>
 * docker build -f mysql.win.Dockerfile -t custom_mysql:latest .
 * </pre>
 * 
 * and then run it with
 * 
 * <pre>
 * docker run -p 3306:3306 --rm custom_mysql:latest
 * </pre>
 */
public class ToDoControllerIT {

	@Mock
	private ToDoView toDoView;
	
	private ToDoMySqlRepository toDoMySqlRepository;
	
	private ToDoController toDoController;
	
	private SessionFactory hibernateSessionFactory;
	private Session hibernateSession;
	
	@Before
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
		Injector injector = Guice.createInjector(
				Modules.combine(
						new ToDoMySqlModule(
								properties.getProperty("MY_SQL_HOST"), 
								Integer.valueOf(System.getProperty("mysql.port", 
										properties.getProperty("MY_SQL_PORT"))), 
								properties.getProperty("MY_SQL_DB_NAME"), 
								properties.getProperty("MY_SQL_USER"), 
								properties.getProperty("MY_SQL_PASS")), 
						new ToDoControllerModule(), 
						new AbstractModule() {
							@Override
						    protected void configure() {
								bind(ToDoView.class).toInstance(toDoView);
							}
						}));
		toDoMySqlRepository = injector.getInstance(ToDoMySqlRepository.class);
		toDoController = injector.getInstance(ToDoController.class);
		hibernateSessionFactory = injector.getInstance(SessionFactory.class);
		hibernateSession = injector.getInstance(Session.class);
	}
	
	@After
	public void tearDown() {
		ToDoMySqlModule.closeSessionFactory(hibernateSessionFactory, hibernateSession);
	}
	
	@Test
	public void shouldCorrectLoginCallUserLoggedInOnViewAndCreateFirstLogInfo() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		toDoController.login(user.getEmail(), user.getPassword());
		Set<Log> logs = toDoMySqlRepository.findAllLogsByUserId(user.getId());
		assertThat(logs).isNotNull().hasSize(1);
		Log log = logs.iterator().next();
		assertThat(log).isNotNull();
		verify(toDoView).userLoggedIn(user, log, null);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldLogoutWhenAppIsNotStoppedCallUserLoggedOutOnViewAndEndLogInfo() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		Log log = new Log(new Date(), user);
		log = toDoMySqlRepository.createLog(log);
		toDoController.logout(log, false);
		assertThat(log.getOut()).isNotNull().isAfter(log.getIn());
		verify(toDoView).userLoggedOut();
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectRegisterCallUserLoggedInOnViewWithCreatedUserAndLogInfo() {
		toDoController.register("Mario", "Rossi", "email@email.com", "password", "password");
		User user = toDoMySqlRepository.findUserByEmail("email@email.com");
		assertThat(user).isNotNull();
		Set<Log> logs = toDoMySqlRepository.findAllLogsByUserId(user.getId());
		assertThat(logs).isNotNull().hasSize(1);
		Log log = logs.iterator().next();
		assertThat(log).isNotNull();
		verify(toDoView).userLoggedIn(user, log, null);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectAddListCallAddListOnViewWithCreatedListLinkedToGivenUser() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		toDoController.addList("TEST", user);
		Set<List> lists = toDoMySqlRepository.findAllListsByUserId(user.getId());
		assertThat(lists).isNotNull().hasSize(1);
		List list = lists.iterator().next();
		assertThat(list).isNotNull();
		verify(toDoView).addList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectAddDetailCallAddDetailOnViewWithCreatedDetailLinkedToGivenList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		toDoController.addDetail("TEST-D", list);
		Set<Detail> details = toDoMySqlRepository.findAllDetailsByListId(list.getId());
		assertThat(details).isNotNull().hasSize(1);
		Detail detail = details.iterator().next();
		assertThat(detail).isNotNull();
		verify(toDoView).addDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectModifyNameListCallSaveListOnViewWithModifiedList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		toDoController.modifyNameList("TEST_NEW", list);
		list = toDoMySqlRepository.findListById(list.getId());
		assertThat(list).isNotNull();
		assertThat(list.getName()).isEqualTo("TEST_NEW");
		verify(toDoView).saveList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectModifyTodoDetailCallSaveDetailOnViewWithModifiedDetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		Detail detail = new Detail("TEST-D", list);
		detail = toDoMySqlRepository.createDetail(detail);
		toDoController.modifyTodoDetail("TEST-D_NEW", detail);
		detail = toDoMySqlRepository.findDetailById(detail.getId());
		assertThat(detail).isNotNull();
		assertThat(detail.getTodo()).isEqualTo("TEST-D_NEW");
		verify(toDoView).saveDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectModifyDoneDetailCallSaveDetailOnViewWithModifiedDetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		Detail detail = new Detail("TEST-D", list);
		detail = toDoMySqlRepository.createDetail(detail);
		toDoController.modifyDoneDetail(Boolean.TRUE, detail);
		detail = toDoMySqlRepository.findDetailById(detail.getId());
		assertThat(detail).isNotNull();
		assertThat(detail.getDone()).isTrue();
		verify(toDoView).saveDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectDeleteListCallDeleteListOnViewWithDeletedList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		Detail detail = new Detail("TEST-D", list);
		detail = toDoMySqlRepository.createDetail(detail);
		toDoController.deleteList(list);
		Set<List> lists = toDoMySqlRepository.findAllListsByUserId(user.getId());
		assertThat(lists).isNotNull().isEmpty();
		verify(toDoView).deleteList(list);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldCorrectDeleteDetailCallDeleteDetailOnViewWithDeletedDetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		Detail detail = new Detail("TEST-D", list);
		detail = toDoMySqlRepository.createDetail(detail);
		toDoController.deleteDetail(detail);
		Set<Detail> details = toDoMySqlRepository.findAllDetailsByListId(list.getId());
		assertThat(details).isNotNull().isEmpty();
		verify(toDoView).deleteDetail(detail);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldGetAllListsCallShowAllListsOnViewWithRetrievedListsLinkedToGivenUserId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list1 = new List("TEST_1", user);
		list1 = toDoMySqlRepository.createList(list1);
		List list2 = new List("TEST_2", user);
		list2 = toDoMySqlRepository.createList(list2);
		toDoController.getAllLists(user.getId());
		Set<List> lists = toDoMySqlRepository.findAllListsByUserId(user.getId());
		assertThat(lists).isNotNull().containsExactlyInAnyOrder(list1, list2);
		verify(toDoView).showAllLists(lists);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
	@Test
	public void shouldGetAllDetailsCallShowAllDetailsOnViewWithRetrievedDetailsLinkedToGivenListId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = toDoMySqlRepository.createUser(user);
		List list = new List("TEST", user);
		list = toDoMySqlRepository.createList(list);
		Detail detail1 = new Detail("TEST-D_1", list);
		detail1 = toDoMySqlRepository.createDetail(detail1);
		Detail detail2 = new Detail("TEST-D_2", list);
		detail2 = toDoMySqlRepository.createDetail(detail2);
		toDoController.getAllDetails(list.getId());
		Set<Detail> details = toDoMySqlRepository.findAllDetailsByListId(list.getId());
		assertThat(details).isNotNull().containsExactlyInAnyOrder(detail1, detail2);
		verify(toDoView).showAllDetails(details);
		verifyNoMoreInteractions(ignoreStubs(toDoView));
	}
	
}
