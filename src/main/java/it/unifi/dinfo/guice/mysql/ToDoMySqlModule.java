package it.unifi.dinfo.guice.mysql;

import static org.hibernate.cfg.AvailableSettings.PASS;
import static org.hibernate.cfg.AvailableSettings.URL;
import static org.hibernate.cfg.AvailableSettings.USER;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.DetailMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.ListMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.LogMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.UserMySqlRepository;

public class ToDoMySqlModule extends AbstractModule {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private String mySqlHost = "localhost";
	private int mySqlPort = 3306;
	private String mySqlDbName = "an_advanced_todo_app_db";
	private String mySqlDbUser = "user";
	private String mySqlDbPass = "password";
	
	public ToDoMySqlModule(String mySqlHost, int mySqlPort, String mySqlDbName, 
			String mySqlDbUser, String mySqlDbPass) {
		super();
		this.mySqlHost = mySqlHost;
		this.mySqlPort = mySqlPort;
		this.mySqlDbName = mySqlDbName;
		this.mySqlDbUser = mySqlDbUser;
		this.mySqlDbPass = mySqlDbPass;
	}

	@Override
	protected void configure() {
		var hibernateSessionFactory = createSessionFactory(mySqlHost, mySqlPort, mySqlDbName, 
				mySqlDbUser, mySqlDbPass);
		var hibernateSession = hibernateSessionFactory.openSession();
		
		bind(SessionFactory.class).toInstance(hibernateSessionFactory);
		bind(Session.class).toInstance(hibernateSession);
		
		try {
			bind(UserMySqlRepository.class)
				.toConstructor(UserMySqlRepository.class.getConstructor(Session.class))
				.in(Scopes.SINGLETON);
			bind(LogMySqlRepository.class)
				.toConstructor(LogMySqlRepository.class.getConstructor(Session.class))
				.in(Scopes.SINGLETON);
			bind(ListMySqlRepository.class)
				.toConstructor(ListMySqlRepository.class.getConstructor(Session.class))
				.in(Scopes.SINGLETON);
			bind(DetailMySqlRepository.class)
				.toConstructor(DetailMySqlRepository.class.getConstructor(Session.class))
				.in(Scopes.SINGLETON);
		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.error(e);
		}
		
		bind(ToDoRepository.class).to(ToDoMySqlRepository.class).in(Scopes.SINGLETON);
	}
	
	public static SessionFactory createSessionFactory(String host, int port, String dbName, 
			String user, String pass) {
		Map<String, String> properties = new HashMap<>();
		properties.put(URL, "jdbc:mysql://" + host + ":" + port + "/" + dbName);
		properties.put(USER, user);
		properties.put(PASS, pass);
		
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app", 
				properties);
		return entityManagerFactory.unwrap(SessionFactory.class);
	}
	
	public static void closeSessionFactory(SessionFactory hibernateSessionFactory, 
			Session hibernateSession) {
		hibernateSession.close();
		hibernateSessionFactory.close();
	}

}
