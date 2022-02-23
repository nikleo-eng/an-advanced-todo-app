package it.unifi.dinfo.view.spec;

import it.unifi.dinfo.view.spec.base.BaseView;

public interface LoginView extends BaseView {
	
	void login(String email, String password);
	
	void renderError(String error);
	
}
