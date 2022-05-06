package it.unifi.dinfo.repository.mysql.step;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

import io.cucumber.java.After;
import it.unifi.dinfo.guice.mysql.ToDoMySqlModule;

public class ToDoMySqlRepositoryStep {
	
	private SessionFactory sessionFactory;
	private Session session;
	
	@Inject
	public ToDoMySqlRepositoryStep(SessionFactory sessionFactory, Session session) {
		this.sessionFactory = sessionFactory;
		this.session = session;
	}

	@After
	public void tearDown() {
		ToDoMySqlModule.closeSessionFactory(sessionFactory, session);
	}
	
}
