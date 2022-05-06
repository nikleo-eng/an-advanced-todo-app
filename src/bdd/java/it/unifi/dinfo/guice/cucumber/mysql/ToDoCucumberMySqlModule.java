package it.unifi.dinfo.guice.cucumber.mysql;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import it.unifi.dinfo.repository.mysql.step.ToDoMySqlRepositoryStep;
import it.unifi.dinfo.repository.mysql.step.spec.ListMySqlRepositoryStep;
import it.unifi.dinfo.repository.mysql.step.spec.LogMySqlRepositoryStep;
import it.unifi.dinfo.repository.mysql.step.spec.UserMySqlRepositoryStep;

public class ToDoCucumberMySqlModule extends AbstractModule {

	@Override
    protected void configure() {
		bind(UserMySqlRepositoryStep.class).in(Scopes.SINGLETON);
		bind(LogMySqlRepositoryStep.class).in(Scopes.SINGLETON);
		bind(ListMySqlRepositoryStep.class).in(Scopes.SINGLETON);
		bind(ToDoMySqlRepositoryStep.class).in(Scopes.SINGLETON);
	}
	
}
