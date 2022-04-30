package it.unifi.dinfo.repository.mysql.spec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unifi.dinfo.model.User;

public class UserMySqlRepositoryTest {

	private SessionFactory sessionFactory;
	private Session session;
	
	private UserMySqlRepository userMySqlRepository;
	
	@Before
	public void setUp() {
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app-test");
		sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		session = sessionFactory.openSession();
		userMySqlRepository = new UserMySqlRepository(session);
	}
	
	@After
	public void tearDown() {
		session.close();
		sessionFactory.close();
	}
	
	@Test
	public void shouldFindByEmailReturnNullWhenNoUserWithGivenEmailIsFound() {
		User retievedUser = userMySqlRepository.findByEmail("email@email.com");
		assertNull(retievedUser);
	}
	
	@Test
	public void shouldFindByEmailReturnAUserWithGivenEmailWhenThisIsFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		session.getTransaction().begin();
		session.persist(user);
		session.getTransaction().commit();
		
		User retrievedUser = userMySqlRepository.findByEmail("email@email.com");
		assertEquals(user, retrievedUser);
	}
	
	@Test
	public void shouldCreateCreateAUserAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = userMySqlRepository.create(user);
		
		User retrievedUser = session.createQuery("select u from User u where u.id = ?0", User.class)
				.setParameter(0, user.getId()).getSingleResult();
		assertEquals(user, retrievedUser);
	}
	
}
