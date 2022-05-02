package it.unifi.dinfo.guice.javafx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;

public class ToDoJavaFxModule extends AbstractModule {
	
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
    protected void configure() {
		try {
			bind(LoginJavaFxView.class)
				.toConstructor(LoginJavaFxView.class.getConstructor(ToDoController.class))
				.in(Scopes.SINGLETON);
			bind(RegistrationJavaFxView.class)
				.toConstructor(RegistrationJavaFxView.class.getConstructor(ToDoController.class))
				.in(Scopes.SINGLETON);
			bind(ListsJavaFxView.class)
				.toConstructor(ListsJavaFxView.class.getConstructor(ToDoController.class))
				.in(Scopes.SINGLETON);
			bind(DetailsJavaFxView.class)
				.toConstructor(DetailsJavaFxView.class.getConstructor(ToDoController.class))
				.in(Scopes.SINGLETON);
		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.error(e);
		}
		
		bind(ToDoView.class).to(ToDoJavaFxView.class).in(Scopes.SINGLETON);
	}
	
	@Provides @Singleton
	protected AdditionModificationJavaFxView additionModificationJavaFxView(ToDoController toDoController, 
			ListsJavaFxView listsJavaFxView, DetailsJavaFxView detailsJavaFxView) {
		AdditionModificationJavaFxView view = new AdditionModificationJavaFxView(toDoController);
		view.setListsJavaFxView(listsJavaFxView);
		view.setDetailsJavaFxView(detailsJavaFxView);
		/* management of circular dependencies */
		listsJavaFxView.setDetailsJavaFxView(detailsJavaFxView);
		detailsJavaFxView.setListsJavaFxView(listsJavaFxView);
		listsJavaFxView.setAdditionModificationJavaFxView(view);
		detailsJavaFxView.setAdditionModificationJavaFxView(view);
		return view;
	}
	
	@Provides @Singleton
	protected UserJavaFxView userJavaFxView(ToDoController toDoController, 
			LoginJavaFxView loginJavaFxView, RegistrationJavaFxView registrationJavaFxView, 
			ListsJavaFxView listsJavaFxView, DetailsJavaFxView detailsJavaFxView, 
			AdditionModificationJavaFxView additionModificationJavaFxView) {
		UserJavaFxView view = new UserJavaFxView(toDoController);
		view.setAdditionModificationJavaFxView(additionModificationJavaFxView);
		view.setLoginJavaFxView(loginJavaFxView);
		view.setRegistrationJavaFxView(registrationJavaFxView);
		view.setListsJavaFxView(listsJavaFxView);
		view.setDetailsJavaFxView(detailsJavaFxView);
		return view;
	}
	
}
