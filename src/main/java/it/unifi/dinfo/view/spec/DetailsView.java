package it.unifi.dinfo.view.spec;

import java.util.Set;

import it.unifi.dinfo.model.Detail;

public interface DetailsView {

	void showAll(Set<Detail> details);
	
	void delete(Detail detail);
	
	void add(Detail detail);
	
	void save(Detail detail);
	
}
