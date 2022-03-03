package it.unifi.dinfo.repository.mysql.spec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;

public class ListMySqlRepositoryTest {

	private static Session session;
	
	private ListMySqlRepository listMySqlRepository;
	
	@BeforeClass
	public static void setUpClass() {
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app-test");
		var sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		session = sessionFactory.openSession();
	}
	
	@AfterClass
	public static void tearDownClass() {
		session.close();
	}
	
	@Before
	public void setUp() {
		listMySqlRepository = new ListMySqlRepository(session);
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
		
		session.getTransaction().begin();
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
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
		
		session.getTransaction().begin();
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
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
		
		session.getTransaction().begin();
		session.delete(user);
		session.getTransaction().commit();
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
		
		session.getTransaction().begin();
		session.delete(list1);
		session.delete(list2);
		session.delete(user);
		session.getTransaction().commit();
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
		
		session.getTransaction().begin();
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
}
