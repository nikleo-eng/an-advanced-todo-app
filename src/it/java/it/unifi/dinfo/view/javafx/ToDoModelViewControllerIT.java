package it.unifi.dinfo.view.javafx;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.STAGE_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import com.sun.javafx.application.ParametersImpl;

import it.unifi.dinfo.app.main.ToDoAppMain;
import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import javafx.stage.Stage;

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
public class ToDoModelViewControllerIT extends ApplicationTest {
	
	private ToDoRepository toDoRepository;
	private ToDoController toDoController;
	private ToDoJavaFxView toDoJavaFxView;

	@Override
	public void init() throws Exception {
		toDoJavaFxView = new ToDoJavaFxView();
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
		List<String> args = new ArrayList<>();
		args.add("--" + ToDoAppMain.MY_SQL_HOST_OPNAME + "=" + properties.getProperty("MY_SQL_HOST"));
		args.add("--" + ToDoAppMain.MY_SQL_PORT_OPNAME + "=" + System.getProperty("mysql.port", 
				properties.getProperty("MY_SQL_PORT")));
		args.add("--" + ToDoAppMain.MY_SQL_DB_NAME_OPNAME + "=" + properties.getProperty("MY_SQL_DB_NAME"));
		args.add("--" + ToDoAppMain.MY_SQL_USER_OPNAME + "=" + properties.getProperty("MY_SQL_USER"));
		args.add("--" + ToDoAppMain.MY_SQL_PASS_OPNAME + "=" + properties.getProperty("MY_SQL_PASS"));
		ParametersImpl parameters = new ParametersImpl(args);
		ParametersImpl.registerParameters(toDoJavaFxView, parameters);
		toDoJavaFxView.init();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		toDoController = toDoJavaFxView.getToDoController();
		toDoRepository = toDoController.getToDoRepository();
		toDoJavaFxView.start(stage);
	}
	
	@Override
	public void stop() throws Exception {
		toDoJavaFxView.stop();
		cleanUpDataBase();
	}
	
	@Test
	public void shouldCorrectClickOnRegisterButtonCreateANewUserWithGivenDataAndBringViewToAppRootWithUserInfo() {
		clickOn("#" + RegistrationJavaFxView.NAME_TEXTFIELD_ID);
		write("Mario");
		clickOn("#" + RegistrationJavaFxView.SURNAME_TEXTFIELD_ID);
		write("Rossi");
		clickOn("#" + RegistrationJavaFxView.EMAIL_TEXTFIELD_ID);
		write("email@email.com");
		clickOn("#" + RegistrationJavaFxView.PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + RegistrationJavaFxView.CONFIRM_PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + RegistrationJavaFxView.REGISTER_BUTTON_ID);
		User user = toDoRepository.findUserByEmail("email@email.com");
		assertThat(user).isNotNull();
		Set<Log> logs = toDoRepository.findAllLogsByUserId(user.getId());
		assertThat(logs).isNotNull().hasSize(1);
		Log log = logs.iterator().next();
		assertThat(log).isNotNull();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getAppRoot());
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentUser()).isEqualTo(user);
		assertThat(toDoJavaFxView.getAdditionModificationJavaFxView().getCurrentUser()).isEqualTo(user);
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentLog()).isEqualTo(log);
		String lastLogText = lookup("#" + UserJavaFxView.LOG_TEXT_ID).queryText().getText();
		assertThat(lastLogText).isEqualTo(UserJavaFxView.LOG_STARTING_TEXT + UserJavaFxView.LOG_NOT_AVAILABLE_TEXT);
	}
	
	private void cleanUpDataBase() {
		Set<User> users = toDoRepository.findAllUsers();
		Iterator<User> usersIt = users.iterator();
		while (usersIt.hasNext()) {
			User user = usersIt.next();
			Set<Log> logs = toDoRepository.findAllLogsByUserId(user.getId());
			Iterator<Log> logsIt = logs.iterator();
			while (logsIt.hasNext()) {
				Log log = logsIt.next();
				toDoRepository.deleteLog(log);
				logsIt.remove();
			}
			Set<it.unifi.dinfo.model.List> lists = toDoRepository.findAllListsByUserId(user.getId());
			Iterator<it.unifi.dinfo.model.List> listsIt = lists.iterator();
			while (listsIt.hasNext()) {
				it.unifi.dinfo.model.List list = listsIt.next();
				Set<Detail> details = toDoRepository.findAllDetailsByListId(list.getId());
				Iterator<Detail> detailsIt = details.iterator();
				while (detailsIt.hasNext()) {
					Detail detail = detailsIt.next();
					toDoRepository.deleteDetail(detail);
					detailsIt.remove();
				}
				toDoRepository.deleteList(list);
				listsIt.remove();
			}
			toDoRepository.deleteUser(user);
			usersIt.remove();
		}
	}
	
}
