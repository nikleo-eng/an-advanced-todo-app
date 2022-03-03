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
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.repository.mysql.spec.DetailMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.ListMySqlRepository;
import it.unifi.dinfo.repository.mysql.spec.UserMySqlRepository;

public class ToDoMySqlRepository implements ToDoRepository {
	
	private UserMySqlRepository userMySqlRepository;
	private ListMySqlRepository listMySqlRepository;
	private DetailMySqlRepository detailMySqlRepository;
	
	/* Only for tests */
	protected ToDoMySqlRepository(UserMySqlRepository userMySqlRepository, 
			ListMySqlRepository listMySqlRepository, DetailMySqlRepository detailMySqlRepository) {
		this.userMySqlRepository = userMySqlRepository;
		this.listMySqlRepository = listMySqlRepository;
		this.detailMySqlRepository = detailMySqlRepository;
	}

	public ToDoMySqlRepository(String host, String port, String dbName, 
			String user, String pass) {
		var hibernateSession = createHibernateSession(host, port, dbName, user, pass);
		userMySqlRepository = new UserMySqlRepository(hibernateSession);
		listMySqlRepository = new ListMySqlRepository(hibernateSession);
		detailMySqlRepository = new DetailMySqlRepository(hibernateSession);
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
	
}
