package it.unifi.dinfo.controller.spec;

import java.util.Set;

import com.google.inject.Inject;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.spec.ListsView.ERRORS;

public class ListsController extends BaseController {

	@Inject
	public ListsController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void getAll(Long userId) {
		Set<List> lists = getToDoRepository().findAllListsByUserId(userId);
		getToDoView().showAllLists(lists);
	}
	
	public void delete(List list) {
		if (getToDoRepository().findListById(list.getId()) == null) {
			getToDoView().renderListsError(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
			return;
		}
		
		Set<Detail> details = getToDoRepository().findAllDetailsByListId(list.getId());
		details.stream().forEach(detail -> getToDoRepository().deleteDetail(detail));
		getToDoRepository().deleteList(list);
		getToDoView().deleteList(list);
	}
	
}
