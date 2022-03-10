package it.unifi.dinfo.repository.mysql.spec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;

public class LogMySqlRepositoryTest {

	private static Session session;
	
	private LogMySqlRepository logMySqlRepository;
	
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
		logMySqlRepository = new LogMySqlRepository(session);
	}
	
	@Test
	public void shouldCreateCreateALogAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		session.getTransaction().begin();
		session.persist(user);
		session.getTransaction().commit();
		
		Log log = new Log(new Date(), user);
		log = logMySqlRepository.create(log);
		
		Log retrievedLog = session.createQuery("select l from Log l where l.id = ?0", Log.class)
				.setParameter(0, log.getId()).getSingleResult();
		assertEquals(log, retrievedLog);
		
		session.getTransaction().begin();
		session.delete(log);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldSaveSaveALogAndReturnIt() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Log log = new Log(new Date(), user);
		session.getTransaction().begin();
		session.persist(user);
		session.persist(log);
		session.getTransaction().commit();
		
		log.setOut(new Date());
		log = logMySqlRepository.save(log);
		assertNotNull(log.getOut());
		
		Log retrievedLog = session.createQuery("select l from Log l where l.id = ?0", Log.class)
				.setParameter(0, log.getId()).getSingleResult();
		assertEquals(log, retrievedLog);
		assertNotNull(retrievedLog.getOut());
		
		session.getTransaction().begin();
		session.delete(log);
		session.delete(user);
		session.getTransaction().commit();
	}
	
	@Test
	public void shouldFindLastBeforeIdAndByUserIdReturnNullWhenNoLogWithGivenIdAndUserIdIsFound() {
		Log retrievedLog = logMySqlRepository.findLastBeforeIdAndByUserId(1L, 1L);
		assertNull(retrievedLog);
	}
	
	@Test
	public void shouldFindLastBeforeIdAndByUserIdReturnALogWithGivenIdAndUserIdWhenThisIsFound() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		
		Calendar calMin2H = Calendar.getInstance();
		calMin2H.add(Calendar.HOUR, -2);
		Log log1 = new Log(calMin2H.getTime(), user);
		calMin2H.add(Calendar.MINUTE, +1);
		log1.setOut(calMin2H.getTime());
		
		Calendar calMin1H = Calendar.getInstance();
		calMin1H.add(Calendar.HOUR, -1);
		Log log2 = new Log(calMin1H.getTime(), user);
		calMin1H.add(Calendar.MINUTE, +1);
		log2.setOut(calMin1H.getTime());
		
		Log log3 = new Log(new Date(), user);
		
		session.getTransaction().begin();
		session.persist(user);
		session.persist(log1);
		session.persist(log2);
		session.persist(log3);
		session.getTransaction().commit();
		
		Log retrievedLog = logMySqlRepository.findLastBeforeIdAndByUserId(log3.getId(), user.getId());
		assertEquals(log2, retrievedLog);
		
		retrievedLog = logMySqlRepository.findLastBeforeIdAndByUserId(log2.getId(), user.getId());
		assertEquals(log1, retrievedLog);
		
		session.getTransaction().begin();
		session.delete(log3);
		session.delete(log2);
		session.delete(log1);
		session.delete(user);
		session.getTransaction().commit();
	}
	
}
