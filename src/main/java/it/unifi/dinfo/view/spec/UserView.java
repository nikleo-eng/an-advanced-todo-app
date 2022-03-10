package it.unifi.dinfo.view.spec;

import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;

public interface UserView {

	void userLoggedIn(User user, Log log, Log lastLog);
	
}
