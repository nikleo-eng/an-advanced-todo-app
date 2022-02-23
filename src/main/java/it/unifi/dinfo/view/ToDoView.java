package it.unifi.dinfo.view;

import java.util.Set;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;

public interface ToDoView {

	void addList(String name, User user);
	
	void addDetail(String todo, List list);
	
	void saveList(List list);
	
	void saveDetail(Detail detail);
	
	void cancelAdditionModification();
	
	void renderAdditionModificationError(String error);
	
	void showAllDetails(Long listId);
	
	void updateAllDetails(Set<Detail> details);
	
	void selectUnselectDoneDetail(Detail detail);
	
	void deleteDetail(Detail detail);
	
	void showAllLists(Long userId);
	
	void updateAllLists(Set<List> lists);
	
	void deleteList(List list);
	
	void login(String email, String password);
	
	void renderLoginError(String error);
	
	void register(String name, String surname, String email, String password, String repeatedPassword);
	
	void renderRegistrationError(String error);
	
}
