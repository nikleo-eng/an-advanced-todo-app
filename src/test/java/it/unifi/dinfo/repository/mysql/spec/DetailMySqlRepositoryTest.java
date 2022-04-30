package it.unifi.dinfo.repository.mysql.spec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;

public class DetailMySqlRepositoryTest {

	private SessionFactory sessionFactory;
	private Session session;
	
	private DetailMySqlRepository detailMySqlRepository;
	
	@Before
	public void setUp() {
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app-test");
		sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		session = sessionFactory.openSession();
		detailMySqlRepository = new DetailMySqlRepository(session);
	}
	
	@After
	public void tearDown() {
		session.close();
		sessionFactory.close();
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
	}
	
}
