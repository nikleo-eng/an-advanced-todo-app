package it.unifi.dinfo.app.main;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import it.unifi.dinfo.guice.controller.ToDoControllerModule;
import it.unifi.dinfo.guice.javafx.ToDoJavaFxModule;
import it.unifi.dinfo.guice.mysql.ToDoMySqlModule;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import javafx.application.Platform;
import javafx.stage.Stage;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

@Command(name = "an-advanced-todo-app", mixinStandardHelpOptions = true)
public class ToDoAppMain implements Runnable {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Option(names = { "--my-sql-host" }, description = "MySql Host Address", required = true)
	private String mySqlHost;

	@Option(names = { "--my-sql-port" }, description = "MySql Host Port", required = true)
	private int mySqlPort;

	@Option(names = { "--my-sql-db-name" }, description = "MySql Database Name", required = true)
	private String mySqlDbName;
	
	@Option(names = { "--my-sql-user" }, description = "MySql Database User", required = true)
	private String mySqlDbUser;

	@Option(names = { "--my-sql-pass" }, description = "MySql Database Pass", required = true)
	private String mySqlDbPass;
	
	@Option(names = { "--log4j-root-level" }, description = "Log4j Root Level", required = false, 
			defaultValue = "INFO", showDefaultValue = Visibility.ALWAYS)
	private String log4jRootLevel;
	
	@Option(names = { "--log4j-hibernate-level" }, description = "Log4j Hibernate Level", required = false, 
			defaultValue = "INFO", showDefaultValue = Visibility.ALWAYS)
	private String log4jHibernateLevel;

	public static void main(String[] args) {
		new CommandLine(new ToDoAppMain()).execute(args);
	}

	@Override
	public void run() {
		Configurator.setLevel(LogManager.getRootLogger().getName(), 
				Level.getLevel(log4jRootLevel) != null ? Level.getLevel(log4jRootLevel) : Level.OFF);
		Configurator.setLevel(LogManager.getLogger("org.hibernate.type").getName(), 
				Level.getLevel(log4jHibernateLevel) != null ? Level.getLevel(log4jHibernateLevel): Level.OFF);
		
		LOGGER.info("Application Runned");
		Injector injector = Guice.createInjector(
				Modules.combine(
						new ToDoMySqlModule(mySqlHost, mySqlPort, mySqlDbName, mySqlDbUser, mySqlDbPass), 
						new ToDoControllerModule(), 
						new ToDoJavaFxModule()));
		ToDoJavaFxView view = injector.getInstance(ToDoJavaFxView.class);
		SessionFactory hibernateSessionFactory = injector.getInstance(SessionFactory.class);
		Session hibernateSession = injector.getInstance(Session.class);
		
		Platform.startup(() -> {
			try {
				Stage stage = new Stage();
				view.init();
				view.start(stage);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				view.stop();
				ToDoMySqlModule.closeSessionFactory(hibernateSessionFactory, hibernateSession);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}));
	}
	
}
