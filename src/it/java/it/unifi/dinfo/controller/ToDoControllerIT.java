package it.unifi.dinfo.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
	
	@Before
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
		
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
		
		toDoMySqlRepository = new ToDoMySqlRepository(properties.getProperty("MY_SQL_HOST"), 
				System.getProperty("mysql.port", properties.getProperty("MY_SQL_PORT")), 
				properties.getProperty("MY_SQL_DB_NAME"), properties.getProperty("MY_SQL_USER"), 
				properties.getProperty("MY_SQL_PASS"));
		toDoController = new ToDoController(toDoView, toDoMySqlRepository);
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
		toDoMySqlRepository.deleteLog(log);
		toDoMySqlRepository.deleteUser(user);
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
		toDoMySqlRepository.deleteLog(log);
		toDoMySqlRepository.deleteUser(user);
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
		toDoMySqlRepository.deleteLog(log);
		toDoMySqlRepository.deleteUser(user);
	}
	
}
