package it.unifi.dinfo.controller.spec;

import java.util.Set;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

public class ListsController extends BaseController {

	public ListsController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void getAll(Long userId) {
		Set<List> lists = getToDoRepository().findAllListsByUserId(userId);
		getToDoView().showAllLists(lists);
	}
	
	public void delete(List list) {
		Set<Detail> details = getToDoRepository().findAllDetailsByListId(list.getId());
		details.stream().forEach(detail -> getToDoRepository().deleteDetail(detail));
		getToDoRepository().deleteList(list);
		getToDoView().deleteList(list);
	}
	
}
