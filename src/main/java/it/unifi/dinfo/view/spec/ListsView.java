package it.unifi.dinfo.view.spec;

import java.util.Set;

import it.unifi.dinfo.model.List;
import it.unifi.dinfo.view.spec.base.BaseView;

public interface ListsView extends BaseView {

	void showAllLists(Long userId);
	
	void updateAllLists(Set<List> lists);
	
	void deleteList(List list);
	
}
