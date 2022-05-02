package it.unifi.dinfo.repository.mysql;

import java.util.Set;

import com.google.inject.Inject;

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
	
	@Inject
	public ToDoMySqlRepository(UserMySqlRepository userMySqlRepository, 
			ListMySqlRepository listMySqlRepository, DetailMySqlRepository detailMySqlRepository, 
			LogMySqlRepository logMySqlRepository) {
		this.userMySqlRepository = userMySqlRepository;
		this.listMySqlRepository = listMySqlRepository;
		this.detailMySqlRepository = detailMySqlRepository;
		this.logMySqlRepository = logMySqlRepository;
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
	
	@Override
	public Set<Log> findAllLogsByUserId(Long userId) {
		return logMySqlRepository.findAllByUserId(userId);
	}
	
}
