package it.unifi.dinfo.controller;

import it.unifi.dinfo.controller.spec.AdditionModificationController;
import it.unifi.dinfo.controller.spec.DetailsController;
import it.unifi.dinfo.controller.spec.ListsController;
import it.unifi.dinfo.controller.spec.LogController;
import it.unifi.dinfo.controller.spec.RegistrationController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

public class ToDoController {

	private LogController logController;
	private RegistrationController registrationController;
	private ListsController listsController;
	private DetailsController detailsController;
	private AdditionModificationController additionModificationController;
	
	public ToDoController(ToDoView toDoView, ToDoRepository toDoRepository) {
		logController = new LogController(toDoView, toDoRepository);
		registrationController = new RegistrationController(toDoView, toDoRepository);
		listsController = new ListsController(toDoView, toDoRepository);
		detailsController = new DetailsController(toDoView, toDoRepository);
		additionModificationController = new AdditionModificationController(toDoView, toDoRepository);
	}
	
	/* Only for tests */
	protected ToDoController(LogController logController, 
			RegistrationController registrationController, ListsController listsController, 
			DetailsController detailsController, 
			AdditionModificationController additionModificationController) {
		this.logController = logController;
		this.registrationController = registrationController;
		this.listsController = listsController;
		this.detailsController = detailsController;
		this.additionModificationController = additionModificationController;
	}

	public void login(String email, String password) {
		logController.login(email, password);
	}
	
	public void logout() {
		logController.logout();
	}
	
	public void register(String name, String surname, String email, String password, 
			String repeatedPassword) {
		registrationController.register(name, surname, email, password, repeatedPassword);
	}
	
	public void addList(String name, User user) {
		additionModificationController.addList(name, user);
	}
	
	public void addDetail(String todo, List list) {
		additionModificationController.addDetail(todo, list);
	}
	
	public void modifyNameList(String name, List list) {
		additionModificationController.modifyNameList(name, list);
	}
	
	public void modifyTodoDetail(String todo, Detail detail) {
		additionModificationController.modifyTodoDetail(todo, detail);
	}
	
	public void modifyDoneDetail(Boolean done, Detail detail) {
		detailsController.modifyDone(done, detail);
	}
	
	public void deleteList(List list) {
		listsController.delete(list);
	}
	
	public void deleteDetail(Detail detail) {
		detailsController.delete(detail);
	}
	
	public void getAllLists(Long userId) {
		listsController.getAll(userId);
	}
	
	public void getAllDetails(Long listId) {
		detailsController.getAll(listId);
	}
	
}
