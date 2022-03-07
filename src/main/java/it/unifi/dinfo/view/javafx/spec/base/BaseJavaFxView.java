package it.unifi.dinfo.view.javafx.spec.base;

import it.unifi.dinfo.controller.ToDoController;

public abstract class BaseJavaFxView implements BaseIntJavaFxView {

	private ToDoController toDoController;

	protected BaseJavaFxView(ToDoController toDoController) {
		this.toDoController = toDoController;
	}

	protected ToDoController getToDoController() {
		return toDoController;
	}
	
}
