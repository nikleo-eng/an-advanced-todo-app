package it.unifi.dinfo.repository;

import java.util.Set;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;

public interface ToDoRepository {
	
	User findUserByEmail(String email);
	
	User createUser(User user);
	
	Set<List> findAllListsByUserId(Long userId);
	
	List findListByNameAndUserId(String name, Long userId);
	
	List createList(List list);
	
	List saveList(List list);
	
	void deleteList(List list);
	
	Set<Detail> findAllDetailsByListId(Long listId);
	
	Detail findDetailByTodoAndListId(String todo, Long listId);
	
	Detail createDetail(Detail detail);
	
	Detail saveDetail(Detail detail);
	
	void deleteDetail(Detail detail);
	
	Log createLog(Log log);
	
	Log saveLog(Log log);
	
	Log findLastLogBeforeIdAndByUserId(Long id, Long userId);
	
}
