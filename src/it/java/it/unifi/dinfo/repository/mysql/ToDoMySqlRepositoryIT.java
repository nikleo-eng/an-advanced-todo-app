package it.unifi.dinfo.repository.mysql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;

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
public class ToDoMySqlRepositoryIT {

	private Session hibernateSession;
	private ToDoMySqlRepository toDoMySqlRepository;
	
	@Before
	public void setUp() throws Exception {
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
		
		toDoMySqlRepository = new ToDoMySqlRepository(properties.getProperty("MY_SQL_HOST"), 
				System.getProperty("mysql.port", properties.getProperty("MY_SQL_PORT")), 
				properties.getProperty("MY_SQL_DB_NAME"), properties.getProperty("MY_SQL_USER"), 
				properties.getProperty("MY_SQL_PASS"));
		hibernateSession = toDoMySqlRepository.getHibernateSession();
	}
	
	@Test
	public void shouldFindUserByEmailReturnAUserWhenExistsAUserWithGivenEmail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.getTransaction().commit();
		User returnedUser = toDoMySqlRepository.findUserByEmail(user.getEmail());
		assertThat(returnedUser).isNotNull().isEqualTo(user);
		assertThat(returnedUser.getId()).isEqualTo(user.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldCreateUserReturnTheCreatedUser() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		User createdUser = toDoMySqlRepository.createUser(user);
		assertThat(createdUser).isNotNull().isEqualTo(user);
		User retrievedUser = hibernateSession.createQuery("select u from User u where u.email = ?0", 
				User.class).setParameter(0, user.getEmail()).getSingleResult();
		assertThat(retrievedUser).isNotNull().isEqualTo(createdUser);
		assertThat(retrievedUser.getId()).isEqualTo(createdUser.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(createdUser);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindAllListsByUserIdReturnAListWhenExistsAListLinkedToGivenUserId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.getTransaction().commit();
		Set<List> returnedLists = toDoMySqlRepository.findAllListsByUserId(user.getId());
		assertThat(returnedLists).isNotNull().hasSize(1);
		List returnedList = returnedLists.iterator().next();
		assertThat(returnedList).isNotNull().isEqualTo(list);
		assertThat(returnedList.getId()).isEqualTo(list.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindListByNameAndUserIdReturnAListWhenExistsAListLinkedToGivenUserIdAndWithGivenName() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.getTransaction().commit();
		List returnedList = toDoMySqlRepository.findListByNameAndUserId(list.getName(), user.getId());
		assertThat(returnedList).isNotNull().isEqualTo(list);
		assertThat(returnedList.getId()).isEqualTo(list.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldCreateListReturnTheCreatedList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.getTransaction().commit();
		List createdList = toDoMySqlRepository.createList(list);
		assertThat(createdList).isNotNull().isEqualTo(list);
		List retrievedList = hibernateSession.createQuery("select l from List l where l.user.id = ?0 and l.name = ?1", 
				List.class).setParameter(0, user.getId()).setParameter(1, list.getName()).getSingleResult();
		assertThat(retrievedList).isNotNull().isEqualTo(createdList);
		assertThat(retrievedList.getId()).isEqualTo(createdList.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(createdList);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldSaveListReturnTheSavedAndModifiedList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.getTransaction().commit();
		list.setName("TEST_NEW");
		List savedList = toDoMySqlRepository.saveList(list);
		assertThat(savedList).isNotNull();
		assertThat(savedList.getId()).isEqualTo(list.getId());
		assertThat(savedList.getName()).isEqualTo("TEST_NEW");
		List retrievedList = hibernateSession.createQuery("select l from List l where l.user.id = ?0 and l.name = ?1", 
				List.class).setParameter(0, user.getId()).setParameter(1, savedList.getName()).getSingleResult();
		assertThat(retrievedList).isNotNull().isEqualTo(savedList);
		assertThat(retrievedList.getId()).isEqualTo(savedList.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(savedList);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shoulDeleteListReallyDeleteTheGivenList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.getTransaction().commit();
		toDoMySqlRepository.deleteList(list);
		List retrievedList = hibernateSession.createQuery("select l from List l where l.user.id = ?0 and l.name = ?1", 
				List.class).setParameter(0, user.getId()).setParameter(1, list.getName())
				.uniqueResultOptional().orElse(null);
		assertThat(retrievedList).isNull();
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindAllDetailsByListIdReturnADetailWhenExistsADetailLinkedToGivenListId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.persist(detail);
		hibernateSession.getTransaction().commit();
		Set<Detail> returnedDetails = toDoMySqlRepository.findAllDetailsByListId(list.getId());
		assertThat(returnedDetails).isNotNull().hasSize(1);
		Detail returnedDetail = returnedDetails.iterator().next();
		assertThat(returnedDetail).isNotNull().isEqualTo(detail);
		assertThat(returnedDetail.getId()).isEqualTo(detail.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(detail);
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindDetailByTodoAndListIdReturnADetailWhenExistsADetailLinkedToGivenListIdAndWithGivenTodo() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.persist(detail);
		hibernateSession.getTransaction().commit();
		Detail returnedDetail = toDoMySqlRepository.findDetailByTodoAndListId(detail.getTodo(), list.getId());
		assertThat(returnedDetail).isNotNull().isEqualTo(detail);
		assertThat(returnedDetail.getId()).isEqualTo(detail.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(detail);
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldCreateDetailReturnTheCreatedDetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.getTransaction().commit();
		Detail createdDetail = toDoMySqlRepository.createDetail(detail);
		assertThat(createdDetail).isNotNull().isEqualTo(detail);
		Detail retrievedDetail = hibernateSession.createQuery("select d from Detail d where d.list.id = ?0 "
				+ "and d.todo = ?1", Detail.class).setParameter(0, list.getId()).setParameter(1, detail.getTodo())
				.getSingleResult();
		assertThat(retrievedDetail).isNotNull().isEqualTo(createdDetail);
		assertThat(retrievedDetail.getId()).isEqualTo(createdDetail.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(createdDetail);
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldSaveDetailReturnTheSavedAndModifiedDetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.persist(detail);
		hibernateSession.getTransaction().commit();
		detail.setTodo("TEST-D_NEW");
		detail.setDone(Boolean.TRUE);
		Detail savedDetail = toDoMySqlRepository.saveDetail(detail);
		assertThat(savedDetail).isNotNull();
		assertThat(savedDetail.getId()).isEqualTo(detail.getId());
		assertThat(savedDetail.getTodo()).isEqualTo("TEST-D_NEW");
		assertThat(savedDetail.getDone()).isTrue();
		Detail retrievedDetail = hibernateSession.createQuery("select d from Detail d where d.list.id = ?0 "
				+ "and d.todo = ?1", Detail.class).setParameter(0, list.getId()).setParameter(1, savedDetail.getTodo())
				.getSingleResult();
		assertThat(retrievedDetail).isNotNull().isEqualTo(savedDetail);
		assertThat(retrievedDetail.getId()).isEqualTo(savedDetail.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(savedDetail);
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shoulDeleteDetailReallyDeleteTheGivenDetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.persist(detail);
		hibernateSession.getTransaction().commit();
		toDoMySqlRepository.deleteDetail(detail);
		Detail retrievedDetail = hibernateSession.createQuery("select d from Detail d where d.list.id = ?0 "
				+ "and d.todo = ?1", Detail.class).setParameter(0, list.getId()).setParameter(1, detail.getTodo())
				.uniqueResultOptional().orElse(null);
		assertThat(retrievedDetail).isNull();
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldCreateLogReturnTheCreatedLog() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.getTransaction().commit();
		Log createdLog = toDoMySqlRepository.createLog(log);
		assertThat(createdLog).isNotNull().isEqualTo(log);
		Log retrievedLog = hibernateSession.createQuery("select l from Log l where l.user.id = ?0 "
				+ "and l.in = ?1", Log.class).setParameter(0, user.getId()).setParameter(1, createdLog.getIn())
				.getSingleResult();
		assertThat(retrievedLog).isNotNull().isEqualTo(createdLog);
		assertThat(retrievedLog.getId()).isEqualTo(createdLog.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(createdLog);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldSaveLogReturnTheSavedAndModifiedLog() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(log);
		hibernateSession.getTransaction().commit();
		log.setOut(new Date());
		Log savedLog = toDoMySqlRepository.saveLog(log);
		assertThat(savedLog).isNotNull();
		assertThat(savedLog.getId()).isEqualTo(log.getId());
		assertThat(savedLog.getOut()).isNotNull();
		Log retrievedLog = hibernateSession.createQuery("select l from Log l where l.user.id = ?0 "
				+ "and l.in = ?1", Log.class).setParameter(0, user.getId()).setParameter(1, savedLog.getIn())
				.getSingleResult();
		assertThat(retrievedLog).isNotNull().isEqualTo(savedLog);
		assertThat(retrievedLog.getId()).isEqualTo(savedLog.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(savedLog);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindLastLogBeforeIdAndByUserIdReturnTheFirstLogWhenTheGivenUserIdIsLinkedToTwoLogs() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, -1);
		Log log1 = new Log(cal.getTime(), user);
		cal.set(Calendar.MINUTE, 1);
		Log log2 = new Log(cal.getTime(), user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(log1);
		hibernateSession.persist(log2);
		hibernateSession.getTransaction().commit();
		Log returnedLog = toDoMySqlRepository.findLastLogBeforeIdAndByUserId(log2.getId(), user.getId());
		assertThat(returnedLog).isNotNull().isEqualTo(log1);
		assertThat(returnedLog.getId()).isEqualTo(log1.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(log2);
		hibernateSession.delete(log1);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindListByIdReturnAListWhenExistsAListLinkedToGivenListId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.getTransaction().commit();
		List returnedList = toDoMySqlRepository.findListById(list.getId());
		assertThat(returnedList).isNotNull().isEqualTo(list);
		assertThat(returnedList.getId()).isEqualTo(list.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
	@Test
	public void shouldFindDetailByIdReturnADetailWhenExistsADetailLinkedToGivenDetailId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		hibernateSession.getTransaction().begin();
		hibernateSession.persist(user);
		hibernateSession.persist(list);
		hibernateSession.persist(detail);
		hibernateSession.getTransaction().commit();
		Detail returnedDetail = toDoMySqlRepository.findDetailById(detail.getId());
		assertThat(returnedDetail).isNotNull().isEqualTo(detail);
		assertThat(returnedDetail.getId()).isEqualTo(detail.getId());
		hibernateSession.getTransaction().begin();
		hibernateSession.delete(detail);
		hibernateSession.delete(list);
		hibernateSession.delete(user);
		hibernateSession.getTransaction().commit();
	}
	
}
