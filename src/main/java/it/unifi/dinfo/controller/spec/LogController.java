package it.unifi.dinfo.controller.spec;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

import static it.unifi.dinfo.view.spec.LoginView.*;

import java.util.Date;

public class LogController extends BaseController {

	public LogController(ToDoView toDoView, ToDoRepository toDoRepository) {
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
		
		Log newLog = new Log(new Date(), userSelected);
		newLog = getToDoRepository().createLog(newLog);
		
		Log lastLog = getToDoRepository().findLastLogBeforeIdAndByUserId(
				newLog.getId(), userSelected.getId());
		
		getToDoView().userLoggedIn(userSelected, newLog, lastLog);
	}
	
	public void logout(Log log, boolean stop) {
		log.setOut(new Date());
		log = getToDoRepository().saveLog(log);
		
		if (!stop) {
			getToDoView().userLoggedOut();
		}
	}
	
}
