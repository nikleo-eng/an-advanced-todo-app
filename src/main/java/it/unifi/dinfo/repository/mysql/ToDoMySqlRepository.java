package it.unifi.dinfo.repository.mysql;

import static org.hibernate.cfg.AvailableSettings.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.repository.mysql.spec.DetailMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.ListMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.LogMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.UserMySqlRepository;

public class ToDoMySqlRepository implements ToDoRepository {
	
	private UserMySqlRepository userMySqlRepository;
	private ListMySqlRepository listMySqlRepository;
	private DetailMySqlRepository detailMySqlRepository;
	private LogMySqlRepository logMySqlRepository;
	
	private Session hibernateSession;
	
	/* Only for tests */
	protected ToDoMySqlRepository(UserMySqlRepository userMySqlRepository, 
			ListMySqlRepository listMySqlRepository, DetailMySqlRepository detailMySqlRepository, 
			LogMySqlRepository logMySqlRepository) {
		this.userMySqlRepository = userMySqlRepository;
		this.listMySqlRepository = listMySqlRepository;
		this.detailMySqlRepository = detailMySqlRepository;
		this.logMySqlRepository = logMySqlRepository;
	}

	public ToDoMySqlRepository(String host, String port, String dbName, 
			String user, String pass) {
		hibernateSession = createHibernateSession(host, port, dbName, user, pass);
		userMySqlRepository = new UserMySqlRepository(hibernateSession);
		listMySqlRepository = new ListMySqlRepository(hibernateSession);
		detailMySqlRepository = new DetailMySqlRepository(hibernateSession);
		logMySqlRepository = new LogMySqlRepository(hibernateSession);
	}
	
	private static Session createHibernateSession(String host, String port, String dbName, 
			String user, String pass) {
		Map<String, String> properties = new HashMap<>();
		properties.put(URL, "jdbc:mysql://" + host + ":" + port + "/" + dbName);
		properties.put(USER, user);
		properties.put(PASS, pass);
		
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app", 
				properties);
		var sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		return sessionFactory.openSession();
	}

	@Override
	public User findUserByEmail(String email) {
		return userMySqlRepository.findByEmail(email);
	}

	@Override
	public User createUser(User user) {
		return userMySqlRepository.create(user);
	}

	@Override
	public Set<List> findAllListsByUserId(Long userId) {
		return listMySqlRepository.findAllByUserId(userId);
	}

	@Override
	public List findListByNameAndUserId(String name, Long userId) {
		return listMySqlRepository.findByNameAndUserId(name, userId);
	}

	@Override
	public List createList(List list) {
		return listMySqlRepository.create(list);
	}

	@Override
	public List saveList(List list) {
		return listMySqlRepository.save(list);
	}

	@Override
	public void deleteList(List list) {
		listMySqlRepository.delete(list);
	}

	@Override
	public Set<Detail> findAllDetailsByListId(Long listId) {
		return detailMySqlRepository.findAllByListId(listId);
	}

	@Override
	public Detail findDetailByTodoAndListId(String todo, Long listId) {
		return detailMySqlRepository.findByTodoAndListId(todo, listId);
	}

	@Override
	public Detail createDetail(Detail detail) {
		return detailMySqlRepository.create(detail);
	}

	@Override
	public Detail saveDetail(Detail detail) {
		return detailMySqlRepository.save(detail);
	}

	@Override
	public void deleteDetail(Detail detail) {
		detailMySqlRepository.delete(detail);
	}

	@Override
	public Log createLog(Log log) {
		return logMySqlRepository.create(log);
	}

	@Override
	public Log saveLog(Log log) {
		return logMySqlRepository.save(log);
	}

	@Override
	public Log findLastLogBeforeIdAndByUserId(Long id, Long userId) {
		return logMySqlRepository.findLastBeforeIdAndByUserId(id, userId);
	}

	@Override
	public List findListById(Long id) {
		return listMySqlRepository.findById(id);
	}

	@Override
	public Detail findDetailById(Long id) {
		return detailMySqlRepository.findById(id);
	}
	
	/* Only for tests */
	public void deleteUser(User user) {
		userMySqlRepository.delete(user);
	}
	
	/* Only for tests */
	public void deleteLog(Log log) {
		logMySqlRepository.delete(log);
	}
	
	/* Only for tests */
	public Set<Log> findAllLogsByUserId(Long userId) {
		return logMySqlRepository.findAllByUserId(userId);
	}
	
	/* Only for tests */
	protected Session getHibernateSession() {
		return hibernateSession;
	}
	
}
