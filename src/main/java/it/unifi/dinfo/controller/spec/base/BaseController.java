package it.unifi.dinfo.controller.spec.base;

import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

public abstract class BaseController {

	private ToDoView toDoView;
	private ToDoRepository toDoRepository;
	
	protected BaseController(ToDoView toDoView, ToDoRepository toDoRepository) {
		this.toDoView = toDoView;
		this.toDoRepository = toDoRepository;
	}

	protected ToDoView getToDoView() {
		return toDoView;
	}
	
	protected ToDoRepository getToDoRepository() {
		return toDoRepository;
	}
	
}
