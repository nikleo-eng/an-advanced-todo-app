package it.unifi.dinfo.controller.spec;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

import static it.unifi.dinfo.view.spec.LoginView.*;

public class LoginController extends BaseController {

	public LoginController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void login(String email, String password) {
		if (email == null || email.isBlank() || password == null || password.isEmpty()) {
			getToDoView().renderLoginError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
			return;
		}
		
		User userSelected = getToDoRepository().findUserByEmail(email.trim());
		if (userSelected == null) {
			getToDoView().renderLoginError(ERRORS.USER_NOT_FOUND.getValue());
			return;
		}
		
		if (!userSelected.getPassword().equals(password)) {
			getToDoView().renderLoginError(ERRORS.INCORRECT_PASSWORD.getValue());
			return;
		}
		
		getToDoView().setCurrentUser(userSelected);
	}
	
}
