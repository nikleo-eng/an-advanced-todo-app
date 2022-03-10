package it.unifi.dinfo.repository.mysql;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.spec.DetailMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.ListMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.LogMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.UserMySqlRepository;

public class ToDoMySqlRepositoryTest {
	
	@Mock
	private UserMySqlRepository userMySqlRepository;
	
	@Mock
	private ListMySqlRepository listMySqlRepository;
	
	@Mock
	private DetailMySqlRepository detailMySqlRepository;
	
	@Mock
	private LogMySqlRepository logMySqlRepository;

	private ToDoMySqlRepository toDoMySqlRepository;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		toDoMySqlRepository = new ToDoMySqlRepository(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository);
	}
	
	@Test
	public void shouldFindUserByEmailCallFindByEmailOnUserMySqlRepository() {
		toDoMySqlRepository.findUserByEmail("email@email.com");
		verify(userMySqlRepository).findByEmail("email@email.com");
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldCreateUserCallCreateOnUserMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		toDoMySqlRepository.createUser(user);
		verify(userMySqlRepository).create(user);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldFindAllListsByUserIdCallFindAllByUserIdOnListMySqlRepository() {
		toDoMySqlRepository.findAllListsByUserId(1L);
		verify(listMySqlRepository).findAllByUserId(1L);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldFindListByNameAndUserIdCallFindByNameAndUserIdOnListMySqlRepository() {
		toDoMySqlRepository.findListByNameAndUserId("TEST", 1L);
		verify(listMySqlRepository).findByNameAndUserId("TEST", 1L);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldCreateListCallCreateOnListMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoMySqlRepository.createList(list);
		verify(listMySqlRepository).create(list);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldSaveListCallSaveOnListMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoMySqlRepository.saveList(list);
		verify(listMySqlRepository).save(list);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldDeleteListCallDeleteOnListMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		toDoMySqlRepository.deleteList(list);
		verify(listMySqlRepository).delete(list);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldFindAllDetailsByListIdCallFindAllByListIdOnDetailMySqlRepository() {
		toDoMySqlRepository.findAllDetailsByListId(1L);
		verify(detailMySqlRepository).findAllByListId(1L);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldFindDetailByTodoAndListIdCallFindByTodoAndListIdOnDetailMySqlRepository() {
		toDoMySqlRepository.findDetailByTodoAndListId("TEST-D", 1L);
		verify(detailMySqlRepository).findByTodoAndListId("TEST-D", 1L);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldCreateDetailCallCreateOnDetailMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoMySqlRepository.createDetail(detail);
		verify(detailMySqlRepository).create(detail);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldSaveDetailCallSaveOnDetailMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoMySqlRepository.saveDetail(detail);
		verify(detailMySqlRepository).save(detail);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldDeleteDetailCallDeleteOnDetailMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		toDoMySqlRepository.deleteDetail(detail);
		verify(detailMySqlRepository).delete(detail);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldCreateLogCallCreateOnLogMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		toDoMySqlRepository.createLog(log);
		verify(logMySqlRepository).create(log);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldSaveLogCallSaveOnLogMySqlRepository() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		toDoMySqlRepository.saveLog(log);
		verify(logMySqlRepository).save(log);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
	@Test
	public void shouldFindLastLogBeforeIdAndByUserIdCallFindLastBeforeIdAndByUserIdOnLogMySqlRepository() {
		toDoMySqlRepository.findLastLogBeforeIdAndByUserId(1L, 1L);
		verify(logMySqlRepository).findLastBeforeIdAndByUserId(1L, 1L);
		verifyNoMoreInteractions(ignoreStubs(userMySqlRepository, listMySqlRepository, 
				detailMySqlRepository, logMySqlRepository));
	}
	
}
