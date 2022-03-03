package it.unifi.dinfo.repository.mysql.spec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.dinfo.model.User;

public class UserMySqlRepositoryTest {

	private static Session session;
	
	private UserMySqlRepository userMySqlRepository;
	
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
		userMySqlRepository = new UserMySqlRepository(session);
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
		
		session.getTransaction().begin();
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldCreateCreateAUserAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		user = userMySqlRepository.create(user);
		
		User retrievedUser = session.createQuery("select u from User u where u.id = ?0", User.class)
				.setParameter(0, user.getId()).getSingleResult();
		assertEquals(user, retrievedUser);
		
		session.getTransaction().begin();
		session.delete(user);
		session.getTransaction().commit();
	}
	
}
