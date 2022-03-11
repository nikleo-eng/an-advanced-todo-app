package it.unifi.dinfo.view.spec;

import java.util.Set;

import it.unifi.dinfo.model.Detail;

public interface DetailsView {
	
	enum ERRORS {
		DETAIL_NO_LONGER_EXISTS("The selected detail no longer exists; try Refresh button!");
		
		private String value;

		ERRORS(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	void showAll(Set<Detail> details);
	
	void delete(Detail detail);
	
	void add(Detail detail);
	
	void save(Detail detail);
	
	void renderError(String error);
	
}
