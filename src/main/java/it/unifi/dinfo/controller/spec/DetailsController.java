package it.unifi.dinfo.controller.spec;

import java.util.Set;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.spec.DetailsView.ERRORS;

public class DetailsController extends BaseController {

	public DetailsController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void getAll(Long listId) {
		Set<Detail> details = getToDoRepository().findAllDetailsByListId(listId);
		getToDoView().showAllDetails(details);
	}
	
	public void delete(Detail detail) {
		if (getToDoRepository().findDetailById(detail.getId()) == null) {
			getToDoView().renderDetailsError(ERRORS.DETAIL_NO_LONGER_EXISTS.getValue());
			return;
		}
		
		getToDoRepository().deleteDetail(detail);
		getToDoView().deleteDetail(detail);
	}
	
	public void modifyDone(Boolean done, Detail detail) {
		if (getToDoRepository().findDetailById(detail.getId()) == null) {
			getToDoView().renderDetailsError(ERRORS.DETAIL_NO_LONGER_EXISTS.getValue());
			/* To bring back to the old state the detail checkbox */
			getToDoView().saveDetail(detail);
			return;
		}
		
		detail.setDone(done);
		detail = getToDoRepository().saveDetail(detail);
		getToDoView().saveDetail(detail);
	}
	
}
