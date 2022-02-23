package it.unifi.dinfo.view.spec;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.view.spec.base.BaseView;

public interface AdditionModificationView extends BaseView {

	void addList(String name, User user);
	
	void addDetail(String todo, List list);
	
	void saveList(List list);
	
	void saveDetail(Detail detail);
	
	void cancelAdditionModification();
	
	void renderError(String error);
	
}
