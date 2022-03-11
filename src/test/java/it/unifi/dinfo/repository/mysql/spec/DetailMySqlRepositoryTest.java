package it.unifi.dinfo.repository.mysql.spec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;

public class DetailMySqlRepositoryTest {

	private static Session session;
	
	private DetailMySqlRepository detailMySqlRepository;
	
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
		detailMySqlRepository = new DetailMySqlRepository(session);
	}
	
	@Test
	public void shouldCreateCreateADetailAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.getTransaction().commit();
		
		Detail detail = new Detail("TEST-D", list);
		detail = detailMySqlRepository.create(detail);
		
		Detail retrievedDetail = session.createQuery("select d from Detail d where d.id = ?0", 
				Detail.class).setParameter(0, detail.getId()).getSingleResult();
		assertEquals(detail, retrievedDetail);
		
		session.getTransaction().begin();
		session.delete(detail);
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldSaveSaveADetailAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.persist(detail);
		session.getTransaction().commit();
		
		detail.setTodo("TEST-D_NEW");
		detail.setDone(Boolean.TRUE);
		detail = detailMySqlRepository.save(detail);
		assertEquals("TEST-D_NEW", detail.getTodo());
		assertEquals(Boolean.TRUE, detail.getDone());
		
		Detail retrievedDetail = session.createQuery("select d from Detail d where d.id = ?0", 
				Detail.class).setParameter(0, detail.getId()).getSingleResult();
		assertEquals(detail, retrievedDetail);
		assertEquals("TEST-D_NEW", retrievedDetail.getTodo());
		assertEquals(Boolean.TRUE, retrievedDetail.getDone());
		
		session.getTransaction().begin();
		session.delete(detail);
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldDeleteDeleteADetail() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.persist(detail);
		session.getTransaction().commit();
		
		detailMySqlRepository.delete(detail);
		
		Long countedDetails = session.createQuery("select count(d) from Detail d where d.id = ?0", 
				Long.class).setParameter(0, list.getId()).getSingleResult();
		assertEquals(0L, (long) countedDetails);
		
		session.getTransaction().begin();
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldFindAllByListIdReturnDetailsSetLinkedToGivenListId() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail1 = new Detail("TEST-D1", list);
		Detail detail2 = new Detail("TEST-D2", list);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.persist(detail1);
		session.persist(detail2);
		session.getTransaction().commit();
		
		Set<Detail> retrievedDetails = detailMySqlRepository.findAllByListId(list.getId());
		assertThat(retrievedDetails).containsExactly(detail1, detail2);
		
		session.getTransaction().begin();
		session.delete(detail1);
		session.delete(detail2);
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldFindByTodoAndListIdReturnNullWhenNoDetailWithGivenTodoAndListIdIsFound() {
		Detail retrievedDetail = detailMySqlRepository.findByTodoAndListId("TEST-D", 1L);
		assertNull(retrievedDetail);
	}
	
	@Test
	public void shouldFindByTodoAndListIdReturnADetailWithGivenTodoAndListIdWhenThisIsFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.persist(detail);
		session.getTransaction().commit();
		
		Detail retrievedDetail = detailMySqlRepository.findByTodoAndListId(detail.getTodo(), 
				list.getId());
		assertEquals(detail, retrievedDetail);
		
		session.getTransaction().begin();
		session.delete(detail);
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldFindByIdReturnNullWhenNoDetailWithGivenIdIsFound() {
		Detail retrievedDetail = detailMySqlRepository.findById(1L);
		assertNull(retrievedDetail);
	}
	
	@Test
	public void shouldFindByIdReturnADetailWithGivenIdWhenThisIsFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(list);
		session.persist(detail);
		session.getTransaction().commit();
		
		Detail retrievedDetail = detailMySqlRepository.findById(detail.getId());
		assertEquals(detail, retrievedDetail);
		
		session.getTransaction().begin();
		session.delete(detail);
		session.delete(list);
		session.delete(user);
		session.getTransaction().commit();
	}
	
}
