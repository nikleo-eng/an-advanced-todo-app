package it.unifi.dinfo.view.spec;

public interface AdditionModificationView {
	
	enum ERRORS {
		FIELD_EMPTY("The field is empty"),
		LIST_ALREADY_FOUND("Already exists a list with same name"),
		DETAIL_ALREADY_FOUND("Already exists a detail in the selected list with same todo"),
		LIST_NO_LONGER_EXISTS("The selected list no longer exists; try Refresh button!"),
		DETAIL_NO_LONGER_EXISTS("The selected detail no longer exists; try Refresh button!");
		
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
