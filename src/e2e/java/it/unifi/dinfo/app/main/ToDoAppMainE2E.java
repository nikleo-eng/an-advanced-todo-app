package it.unifi.dinfo.app.main;

import java.util.Properties;

import org.junit.Before;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.view.javafx.ToDoJavaFxView;

/**
 * Communicates with a MySQL DMBS server on localhost;
 * within the project root, for Linux containers, build the image on Docker with
 * 
 * <pre>
 * docker build -f mysql.Dockerfile -t custom_mysql:latest .
 * </pre>
 * 
 * otherwise, for Windows containers, build the image on Docker with
 * 
 * <pre>
 * docker build -f mysql.win.Dockerfile -t custom_mysql:latest .
 * </pre>
 * 
 * and then run it with
 * 
 * <pre>
 * docker run -p 3306:3306 --rm custom_mysql:latest
 * </pre>
 */
public class ToDoAppMainE2E extends ApplicationTest {
	
	@Before
	public void setUp() throws Exception {
		var properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
		var appArgs = new String[] { 
				"--" + ToDoAppMain.MY_SQL_HOST_OPNAME + "=" + properties.getProperty("MY_SQL_HOST"), 
				"--" + ToDoAppMain.MY_SQL_PORT_OPNAME + "=" + System.getProperty("mysql.port", 
						properties.getProperty("MY_SQL_PORT")), 
				"--" + ToDoAppMain.MY_SQL_DB_NAME_OPNAME + "=" + properties.getProperty("MY_SQL_DB_NAME"), 
				"--" + ToDoAppMain.MY_SQL_USER_OPNAME + "=" + properties.getProperty("MY_SQL_USER"), 
				"--" + ToDoAppMain.MY_SQL_PASS_OPNAME + "=" + properties.getProperty("MY_SQL_PASS")
		};
		launch(ToDoJavaFxView.class, appArgs);
	}

}
