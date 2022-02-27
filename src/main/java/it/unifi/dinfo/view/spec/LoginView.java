package it.unifi.dinfo.view.spec;

public interface LoginView {
	
	enum ERRORS {
		EMAIL_PASSWORD_EMPTY("The email or password field is empty"),
		USER_NOT_FOUND("User with email entered not found"),
		INCORRECT_PASSWORD("Has been entered an incorrect password");
		
		private String value;

		ERRORS(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	void renderError(String error);
	
}
