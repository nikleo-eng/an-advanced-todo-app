package it.unifi.dinfo.controller.spec;

import it.unifi.dinfo.controller.spec.base.BaseController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

import static it.unifi.dinfo.view.spec.AdditionModificationView.*;

import com.google.inject.Inject;

public class AdditionModificationController extends BaseController {

	@Inject
	public AdditionModificationController(ToDoView toDoView, ToDoRepository toDoRepository) {
		super(toDoView, toDoRepository);
	}

	public void addList(String name, User user) {
		if (name == null || name.isBlank()) {
			getToDoView().renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
			return;
		}
		
		if (getToDoRepository().findListByNameAndUserId(name.trim(), user.getId()) != null) {
			getToDoView().renderAdditionModificationError(ERRORS.LIST_ALREADY_FOUND.getValue());
			return;
		}
		
		List newList = new List(name.trim(), user);
		newList = getToDoRepository().createList(newList);
		getToDoView().addList(newList);
	}
	
	public void modifyNameList(String name, List list) {
		if (name == null || name.isBlank()) {
			getToDoView().renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
			return;
		}
		
		List selectedList = getToDoRepository().findListByNameAndUserId(name.trim(), 
				list.getUser().getId());
		if (selectedList != null && !selectedList.getId().equals(list.getId())) {
			getToDoView().renderAdditionModificationError(ERRORS.LIST_ALREADY_FOUND.getValue());
			return;
		}
		
		if (getToDoRepository().findListById(list.getId()) == null) {
			getToDoView().renderAdditionModificationError(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
			return;
		}
		
		list.setName(name.trim());
		list = getToDoRepository().saveList(list);
		getToDoView().saveList(list);
	}
	
	public void addDetail(String todo, List list) {
		if (todo == null || todo.isBlank()) {
			getToDoView().renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
			return;
		}
		
		if (getToDoRepository().findDetailByTodoAndListId(todo.trim(), list.getId()) != null) {
			getToDoView().renderAdditionModificationError(ERRORS.DETAIL_ALREADY_FOUND.getValue());
			return;
		}
		
		if (getToDoRepository().findListById(list.getId()) == null) {
			getToDoView().renderAdditionModificationError(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
			return;
		}
		
		Detail newDetail = new Detail(todo.trim(), list);
		newDetail = getToDoRepository().createDetail(newDetail);
		getToDoView().addDetail(newDetail);
	}
	
	public void modifyTodoDetail(String todo, Detail detail) {
		if (todo == null || todo.isBlank()) {
			getToDoView().renderAdditionModificationError(ERRORS.FIELD_EMPTY.getValue());
			return;
		}
		
		Detail selectedDetail = getToDoRepository().findDetailByTodoAndListId(todo.trim(), 
				detail.getList().getId());
		if (selectedDetail != null && !selectedDetail.getId().equals(detail.getId())) {
			getToDoView().renderAdditionModificationError(ERRORS.DETAIL_ALREADY_FOUND.getValue());
			return;
		}
		
		if (getToDoRepository().findDetailById(detail.getId()) == null) {
			getToDoView().renderAdditionModificationError(ERRORS.DETAIL_NO_LONGER_EXISTS.getValue());
			return;
		}
		
		detail.setTodo(todo.trim());
		detail = getToDoRepository().saveDetail(detail);
		getToDoView().saveDetail(detail);
	}
	
}
