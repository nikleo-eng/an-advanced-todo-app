package it.unifi.dinfo.view.spec;

import java.util.Set;

import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.view.spec.base.BaseView;

public interface DetailsView extends BaseView {

	void showAllDetails(Long listId);
	
	void updateAllDetails(Set<Detail> details);
	
	void selectUnselectDoneDetail(Detail detail);
	
	void deleteDetail(Detail detail);
	
}
