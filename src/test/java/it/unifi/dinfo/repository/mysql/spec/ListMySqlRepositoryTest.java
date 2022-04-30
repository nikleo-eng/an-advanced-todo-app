package it.unifi.dinfo.repository.mysql.spec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;

public class ListMySqlRepositoryTest {

	private SessionFactory sessionFactory;
	private Session session;
	
	private ListMySqlRepository listMySqlRepository;
	
	@Before
	public void setUp() {
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app-test");
		sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		session = sessionFactory.openSession();
		listMySqlRepository = new ListMySqlRepository(session);
	}
	
	@After
	public void tearDown() {
		session.close();
		sessionFactory.close();
	}
	
	@Test
	public void shouldCreateCreateAListAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		session.getTransaction().begin();
		session.persist(user);
		session.getTransaction().commit();
		
		List list = new List("TEST", user);
		list = listMySqlRepository.create(list);
		
		List retrievedList = session.createQuery("select l from List l where l.id = ?0", List.class)
				.setParameter(0, list.getId()).getSingleResult();
		assertEquals(list, retrievedList);
	}
	
	@Test
	public void shouldSaveSaveAListAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.getTransaction().commit();
		
		list.setName("TEST_NEW");
		list = listMySqlRepository.save(list);
		assertEquals("TEST_NEW", list.getName());
		
		List retrievedList = session.createQuery("select l from List l where l.id = ?0", List.class)
				.setParameter(0, list.getId()).getSingleResult();
		assertEquals(list, retrievedList);
		assertEquals("TEST_NEW", retrievedList.getName());
	}
	
	@Test
	public void shouldDeleteDeleteAList() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.getTransaction().commit();
		
		listMySqlRepository.delete(list);
		
		Long countedLists = session.createQuery("select count(l) from List l where l.id = ?0", 
				Long.class).setParameter(0, list.getId()).getSingleResult();
		assertEquals(0L, (long) countedLists);
	}
	
	@Test
	public void shouldFindAllByUserIdReturnListsSetLinkedToGivenUserId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list1 = new List("TEST1", user);
		List list2 = new List("TEST2", user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list1);
		session.persist(list2);
		session.getTransaction().commit();
		
		Set<List> retrievedLists = listMySqlRepository.findAllByUserId(user.getId());
		assertThat(retrievedLists).containsExactly(list1, list2);
	}
	
	@Test
	public void shouldFindByNameAndUserIdReturnNullWhenNoListWithGivenNameAndUserIdIsFound() {
		List retrievedList = listMySqlRepository.findByNameAndUserId("TEST", 1L);
		assertNull(retrievedList);
	}
	
	@Test
	public void shouldFindByNameAndUserIdReturnAListWithGivenNameAndUserIdWhenThisIsFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.getTransaction().commit();
		
		List retrievedList = listMySqlRepository.findByNameAndUserId(list.getName(), user.getId());
		assertEquals(list, retrievedList);
	}
	
	@Test
	public void shouldFindByIdReturnNullWhenNoListWithGivenIdIsFound() {
		List retrievedList = listMySqlRepository.findById(1L);
		assertNull(retrievedList);
	}
	
	@Test
	public void shouldFindByIdReturnAListWithGivenIdWhenThisIsFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.getTransaction().commit();
		
		List retrievedList = listMySqlRepository.findById(list.getId());
		assertEquals(list, retrievedList);
	}
	
}
