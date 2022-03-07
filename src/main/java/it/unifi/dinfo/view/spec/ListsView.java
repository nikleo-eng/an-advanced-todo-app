package it.unifi.dinfo.view.spec;

import java.util.Set;

import it.unifi.dinfo.model.List;

public interface ListsView {

	void showAll(Set<List> lists);
	
	void delete(List list);
	
	void save(List list);
	
	void add(List list);
	
}
