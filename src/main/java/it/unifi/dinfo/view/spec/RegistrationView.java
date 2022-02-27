package it.unifi.dinfo.view.spec;

public interface RegistrationView {

	enum ERRORS {
		FIELD_EMPTY("A field in the form is empty"),
		PASSWORDS_NOT_MACHING("The passwords entered do not match"),
		USER_ALREADY_FOUND("Already exists a user with same email");
		
		private String value;

		ERRORS(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	};
	
	void renderError(String error);
	
}
