package it.unifi.dinfo.view.spec;

import java.util.Set;

import it.unifi.dinfo.model.List;

public interface ListsView {
	
	enum ERRORS {
		LIST_NO_LONGER_EXISTS("The selected list no longer exists; try Refresh button!");
		
		private String value;

		ERRORS(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	void showAll(Set<List> lists);
	
	void delete(List list);
	
	void save(List list);
	
	void add(List list);
	
	void renderError(String error);
	
}
