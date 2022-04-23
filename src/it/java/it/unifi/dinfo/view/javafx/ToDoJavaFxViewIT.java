package it.unifi.dinfo.view.javafx;

import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import javafx.stage.Stage;

public class ToDoJavaFxViewIT extends ApplicationTest {

	private ToDoRepository toDoRepository;
	private ToDoController toDoController;
	private ToDoJavaFxView toDoJavaFxView;
	
	@Override
	public void init() throws Exception {
		toDoJavaFxView = new ToDoJavaFxView();
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app-test");
		var sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		var hibernateSession = sessionFactory.openSession();
		toDoRepository = new ToDoMySqlRepository(hibernateSession);
		toDoController = new ToDoController(toDoJavaFxView, toDoRepository);
		toDoJavaFxView.setToDoController(toDoController);
		toDoJavaFxView.setLoginJavaFxView(new LoginJavaFxView(toDoController));
		toDoJavaFxView.setRegistrationJavaFxView(new RegistrationJavaFxView(toDoController));
		toDoJavaFxView.setListsJavaFxView(new ListsJavaFxView(toDoController));
		toDoJavaFxView.setDetailsJavaFxView(new DetailsJavaFxView(toDoJavaFxView.getListsJavaFxView(), 
				toDoController));
		toDoJavaFxView.setAdditionModificationJavaFxView(new AdditionModificationJavaFxView(
				toDoJavaFxView.getListsJavaFxView(), toDoJavaFxView.getDetailsJavaFxView(), toDoController));
		toDoJavaFxView.getListsJavaFxView().setAdditionModificationJavaFxView(
				toDoJavaFxView.getAdditionModificationJavaFxView());
		toDoJavaFxView.getListsJavaFxView().setDetailsJavaFxView(toDoJavaFxView.getDetailsJavaFxView());
		toDoJavaFxView.getDetailsJavaFxView().setAdditionModificationJavaFxView(
				toDoJavaFxView.getAdditionModificationJavaFxView());
		toDoJavaFxView.setUserJavaFxView(new UserJavaFxView(toDoController, toDoJavaFxView.getListsJavaFxView(), 
				toDoJavaFxView.getDetailsJavaFxView(), toDoJavaFxView.getAdditionModificationJavaFxView(), 
				toDoJavaFxView.getLoginJavaFxView(), toDoJavaFxView.getRegistrationJavaFxView()));
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		toDoJavaFxView.start(stage);
	}
	
	@Override
	public void stop() throws Exception {
		toDoJavaFxView.stop();
	}
	
}
