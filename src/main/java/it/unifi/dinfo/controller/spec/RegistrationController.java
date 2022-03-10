package it.unifi.dinfo.controller.spec;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

import static it.unifi.dinfo.view.spec.RegistrationView.*;

import java.util.Date;

public class RegistrationController extends BaseController {

	public RegistrationController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void register(String name, String surname, String email, String password, 
			String repeatedPassword) {
		if (name == null || name.isBlank() || surname == null || surname.isBlank()
				|| email == null || email.isBlank() || password == null || password.isEmpty()
				|| repeatedPassword == null || repeatedPassword.isEmpty()) {
			getToDoView().renderRegistrationError(ERRORS.FIELD_EMPTY.getValue());
			return;
		}
		
		if (!password.equals(repeatedPassword)) {
			getToDoView().renderRegistrationError(ERRORS.PASSWORDS_NOT_MACHING.getValue());
			return;
		}
		
		if (getToDoRepository().findUserByEmail(email.trim()) != null) {
			getToDoView().renderRegistrationError(ERRORS.USER_ALREADY_FOUND.getValue());
			return;
		}
		
		User newUser = new User(name.trim(), surname.trim(), email.trim(), password);
		newUser = getToDoRepository().createUser(newUser);
		
		Log newLog = new Log(new Date(), newUser);
		newLog = getToDoRepository().createLog(newLog);
		
		Log lastLog = getToDoRepository().findLastLogBeforeIdAndByUserId(
				newLog.getId(), newUser.getId());
		
		getToDoView().userLoggedIn(newUser, newLog, lastLog);
	}
	
}
