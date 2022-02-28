package it.unifi.dinfo.controller.spec;

import java.util.Set;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

public class DetailsController extends BaseController {

	public DetailsController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void getAll(Long listId) {
		Set<Detail> details = getToDoRepository().findAllDetailsByListId(listId);
		getToDoView().showAllDetails(details);
	}
	
	public void delete(Detail detail) {
		getToDoRepository().deleteDetail(detail);
		getToDoView().deleteDetail(detail);
	}
	
	public void modifyDone(Boolean done, Detail detail) {
		detail.setDone(done);
		detail = getToDoRepository().saveDetail(detail);
		getToDoView().saveDetail(detail);
	}
	
}
