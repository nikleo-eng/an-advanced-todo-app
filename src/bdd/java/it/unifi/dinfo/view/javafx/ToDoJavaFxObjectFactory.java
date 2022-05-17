package it.unifi.dinfo.view.javafx;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.guice.CucumberModules;
import io.cucumber.guice.ScenarioScope;
import it.unifi.dinfo.guice.controller.ToDoControllerModule;
import it.unifi.dinfo.guice.cucumber.javafx.ToDoCucumberJavaFxModule;
import it.unifi.dinfo.guice.cucumber.mysql.ToDoCucumberMySqlModule;
import it.unifi.dinfo.guice.javafx.ToDoJavaFxModule;
import it.unifi.dinfo.guice.mysql.ToDoMySqlModule;

public final class ToDoJavaFxObjectFactory implements ObjectFactory {
	
	private static final Logger LOGGER = LogManager.getLogger();

	private Injector injector;

	public ToDoJavaFxObjectFactory() {}

	@Override
	public boolean addClass(Class<?> clazz) {
		return true;
	}

	@Override
	public void start() {
		injector = getInjector();
		injector.getInstance(ScenarioScope.class).enterScope();
	}

	@Override
	public void stop() {
		injector.getInstance(ScenarioScope.class).exitScope();
	}

	@Override
	public <T> T getInstance(Class<T> clazz) {
		return injector.getInstance(clazz);
	}
	
	private Injector getInjector() {
		try {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
			return Guice.createInjector(
					Modules.combine(
							CucumberModules.createScenarioModule(), 
							new ToDoMySqlModule(
									properties.getProperty("MY_SQL_HOST"), 
									Integer.valueOf(System.getProperty("mysql.port", 
											properties.getProperty("MY_SQL_PORT"))), 
									properties.getProperty("MY_SQL_DB_NAME"), 
									properties.getProperty("MY_SQL_USER"), 
									properties.getProperty("MY_SQL_PASS")), 
							new ToDoControllerModule(), 
							new ToDoJavaFxModule(), 
							new ToDoCucumberMySqlModule(), 
							new ToDoCucumberJavaFxModule()));
		} catch (Exception e) {
			LOGGER.error(e);
			return null;
		}
	}

}
