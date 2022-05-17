package it.unifi.dinfo.view.javafx;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

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
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources/features", 
	glue = { "it.unifi.dinfo.repository.mysql", "it.unifi.dinfo.view.javafx" }, 
	objectFactory = ToDoJavaFxObjectFactory.class)
public class ToDoJavaFxViewBDD {
	
}
