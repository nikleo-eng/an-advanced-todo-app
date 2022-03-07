package it.unifi.dinfo.view;

import java.util.Set;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;

public interface ToDoView {

	void addList(List list);
	
	void addDetail(Detail detail);
	
	void saveList(List list);
	
	void saveDetail(Detail detail);
	
	void renderAdditionModificationError(String error);
	
	void showAllDetails(Set<Detail> details);
	
	void deleteDetail(Detail detail);
	
	void showAllLists(Set<List> lists);
	
	void deleteList(List list);
	
	void userLoggedIn(User user);
	
	void userLoggedOut();
	
	void renderLoginError(String error);
	
	void renderRegistrationError(String error);
	
}
