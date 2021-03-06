package it.unifi.dinfo.guice.cucumber.javafx;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import it.unifi.dinfo.view.javafx.step.ToDoJavaFxViewStep;
import it.unifi.dinfo.view.javafx.step.spec.AdditionModificationJavaFxViewStep;
import it.unifi.dinfo.view.javafx.step.spec.DetailsJavaFxViewStep;
import it.unifi.dinfo.view.javafx.step.spec.ListsJavaFxViewStep;
import it.unifi.dinfo.view.javafx.step.spec.LoginJavaFxViewStep;
import it.unifi.dinfo.view.javafx.step.spec.RegistrationJavaFxViewStep;
import it.unifi.dinfo.view.javafx.step.spec.UserJavaFxViewStep;

public class ToDoCucumberJavaFxModule extends AbstractModule {

	@Override
    protected void configure() {
		bind(LoginJavaFxViewStep.class).in(Scopes.SINGLETON);
		bind(RegistrationJavaFxViewStep.class).in(Scopes.SINGLETON);
		bind(UserJavaFxViewStep.class).in(Scopes.SINGLETON);
		bind(ListsJavaFxViewStep.class).in(Scopes.SINGLETON);
		bind(DetailsJavaFxViewStep.class).in(Scopes.SINGLETON);
		bind(AdditionModificationJavaFxViewStep.class).in(Scopes.SINGLETON);
		bind(ToDoJavaFxViewStep.class).in(Scopes.SINGLETON);
	}
	
}
