package it.unifi.dinfo.guice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.controller.spec.AdditionModificationController;
import it.unifi.dinfo.controller.spec.DetailsController;
import it.unifi.dinfo.controller.spec.ListsController;
import it.unifi.dinfo.controller.spec.LogController;
import it.unifi.dinfo.controller.spec.RegistrationController;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.ToDoView;

public class ToDoControllerModule extends AbstractModule {
	
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
    protected void configure() {
		try {
			bind(LogController.class)
				.toConstructor(LogController.class.getConstructor(
						ToDoView.class, ToDoRepository.class))
				.in(Scopes.SINGLETON);
			bind(RegistrationController.class)
				.toConstructor(RegistrationController.class.getConstructor(
						ToDoView.class, ToDoRepository.class))
				.in(Scopes.SINGLETON);
			bind(ListsController.class)
				.toConstructor(ListsController.class.getConstructor(
						ToDoView.class, ToDoRepository.class))
				.in(Scopes.SINGLETON);
			bind(DetailsController.class)
				.toConstructor(DetailsController.class.getConstructor(
						ToDoView.class, ToDoRepository.class))
				.in(Scopes.SINGLETON);
			bind(AdditionModificationController.class)
				.toConstructor(AdditionModificationController.class.getConstructor(
						ToDoView.class, ToDoRepository.class))
				.in(Scopes.SINGLETON);
		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.error(e);
		}
		
		bind(ToDoController.class).in(Scopes.SINGLETON);
	}
	
}
