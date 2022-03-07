package it.unifi.dinfo.app.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import javafx.application.Application;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "an-advanced-todo-app", mixinStandardHelpOptions = true)
public class ToDoAppMain implements Runnable {
	
	public static final String MY_SQL_HOST_OPNAME = "my-sql-host";
	public static final String MY_SQL_PORT_OPNAME = "my-sql-port";
	public static final String MY_SQL_DB_NAME_OPNAME = "my-sql-db-name";
	public static final String MY_SQL_USER_OPNAME = "my-sql-user";
	public static final String MY_SQL_PASS_OPNAME = "my-sql-pass";
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Spec
	private CommandSpec commandSpec;
	
	@Option(names = { "--" + MY_SQL_HOST_OPNAME }, description = "MySql Host Address", 
			required = true)
	private String mySqlHost;

	@Option(names = { "--" + MY_SQL_PORT_OPNAME }, description = "MySql Host Port", 
			required = true)
	private int mySqlPort;

	@Option(names = { "--" + MY_SQL_DB_NAME_OPNAME }, description = "MySql Database Name", 
			required = true)
	private String mySqlDbName;
	
	@Option(names = { "--" + MY_SQL_USER_OPNAME }, description = "MySql Database User", 
			required = true)
	private String mySqlDbUser;

	@Option(names = { "--" + MY_SQL_PASS_OPNAME }, description = "MySql Database Pass",
			required = true)
	private String mySqlDbPass;

	public static void main(String[] args) {
		new CommandLine(new ToDoAppMain()).execute(args);
	}

	@Override
	public void run() {
		String[] args = commandSpec.options().stream().map(opt -> opt.names()[0] + "=" 
				+ opt.getValue().toString()).toArray(size -> new String[size]);
		LOGGER.info("Application Runned");
		Application.launch(ToDoJavaFxView.class, args);
	}
	
}
