package it.unifi.dinfo.view.spec;

import it.unifi.dinfo.view.spec.base.BaseView;

public interface RegistrationView extends BaseView {

	void register(String name, String surname, String email, String password, 
			String repeatedPassword);
	
	void renderError(String error);
	
}
