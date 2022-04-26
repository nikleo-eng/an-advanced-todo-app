package it.unifi.dinfo.view.javafx;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.STAGE_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import it.unifi.dinfo.view.spec.RegistrationView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ToDoJavaFxViewIT extends ApplicationTest {

	private ToDoRepository toDoRepository;
	private ToDoController toDoController;
	private ToDoJavaFxView toDoJavaFxView;
	
	@Override
	public void init() throws Exception {
		toDoJavaFxView = new ToDoJavaFxView();
		var entityManagerFactory = Persistence.createEntityManagerFactory("an-advanced-todo-app-test");
		var hibenateSessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		var hibernateSession = hibenateSessionFactory.openSession();
		toDoJavaFxView.setHibernateSessionFactory(hibenateSessionFactory);
		toDoJavaFxView.setHibernateSession(hibernateSession);
		toDoRepository = new ToDoMySqlRepository(hibernateSession);
		toDoJavaFxView.setToDoRepository(toDoRepository);
		toDoController = new ToDoController(toDoJavaFxView, toDoRepository);
		toDoJavaFxView.setToDoController(toDoController);
		toDoJavaFxView.setLoginJavaFxView(new LoginJavaFxView(toDoController));
		toDoJavaFxView.setRegistrationJavaFxView(new RegistrationJavaFxView(toDoController));
		toDoJavaFxView.setListsJavaFxView(new ListsJavaFxView(toDoController));
		toDoJavaFxView.setDetailsJavaFxView(new DetailsJavaFxView(toDoJavaFxView.getListsJavaFxView(), 
				toDoController));
		toDoJavaFxView.setAdditionModificationJavaFxView(new AdditionModificationJavaFxView(
				toDoJavaFxView.getListsJavaFxView(), toDoJavaFxView.getDetailsJavaFxView(), toDoController));
		toDoJavaFxView.getListsJavaFxView().setAdditionModificationJavaFxView(
				toDoJavaFxView.getAdditionModificationJavaFxView());
		toDoJavaFxView.getListsJavaFxView().setDetailsJavaFxView(toDoJavaFxView.getDetailsJavaFxView());
		toDoJavaFxView.getDetailsJavaFxView().setAdditionModificationJavaFxView(
				toDoJavaFxView.getAdditionModificationJavaFxView());
		toDoJavaFxView.setUserJavaFxView(new UserJavaFxView(toDoController, toDoJavaFxView.getListsJavaFxView(), 
				toDoJavaFxView.getDetailsJavaFxView(), toDoJavaFxView.getAdditionModificationJavaFxView(), 
				toDoJavaFxView.getLoginJavaFxView(), toDoJavaFxView.getRegistrationJavaFxView()));
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		toDoJavaFxView.start(stage);
	}
	
	@Override
	public void stop() throws Exception {
		toDoJavaFxView.stop();
	}
	
	@Test
	public void shouldCorrectlyLoginFromControllerAddLogInfoToLoggedUserAndBringViewToAppRootWithLoggedUserInfo() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		toDoController.login(user.getEmail(), user.getPassword());
		Set<Log> logs = toDoRepository.findAllLogsByUserId(user.getId());
		assertThat(logs).isNotNull().hasSize(2);
		Log lastLog = logs.iterator().next();
		assertThat(lastLog).isNotNull().isNotEqualTo(log);
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getAppRoot());
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentUser()).isEqualTo(user);
		assertThat(toDoJavaFxView.getAdditionModificationJavaFxView().getCurrentUser()).isEqualTo(user);
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentLog()).isEqualTo(lastLog);
		String lastLogText = lookup("#" + UserJavaFxView.LOG_TEXT_ID).queryText().getText();
		assertThat(lastLogText).isEqualTo(UserJavaFxView.LOG_STARTING_TEXT + 
				(new SimpleDateFormat(UserJavaFxView.SDF_PATTERN, Locale.ITALIAN)).format(log.getIn()));
		String userText = lookup("#" + UserJavaFxView.USER_TEXT_ID).queryText().getText();
		assertThat(userText).isEqualTo(user.getName() + " " + user.getSurname());
	}
	
	@Test
	public void shouldWrongRegisterFromControllerWithNotMatchedPasswordsDoNotChangeViewAndShowRegistrationErrorMessage() {
		Text errorText = lookup("#" + RegistrationJavaFxView.ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		toDoController.register("Mario", "Rossi", "email@email.com", "password", "password1");
		assertThat(toDoRepository.findUserByEmail("email@email.com")).isNull();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentUser()).isNull();
		assertThat(toDoJavaFxView.getAdditionModificationJavaFxView().getCurrentUser()).isNull();
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentLog()).isNull();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(RegistrationView.ERRORS.PASSWORDS_NOT_MACHING.getValue());
	}
	
	@Test
	public void shouldAddListFromControllerAddCorrectlyAListInDatabaseAndListViewOfListsArea() throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		toDoController.login(user.getEmail(), user.getPassword());
		assertThat(toDoRepository.findAllListsByUserId(user.getId())).isNullOrEmpty();
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		toDoController.addList("TEST", user);
		/* https://github.com/TestFX/TestFX/issues/392 */
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		Set<List> lists = toDoRepository.findAllListsByUserId(user.getId());
		assertThat(lists).isNotNull().hasSize(1);
		List list = lists.iterator().next();
		assertThat(list).isNotNull();
		assertThat(list.getName()).isEqualTo("TEST");
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView()
				.getItems()).hasSize(1).containsExactly(list);
	}
	
	@Test
	public void shouldAddDetailFromControllerAddCorrectlyADetailInDatabaseAndListViewOfDetailsArea() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		List list = new List("TEST", user);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		toDoController.login(user.getEmail(), user.getPassword());
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		assertThat(toDoRepository.findAllDetailsByListId(list.getId())).isNullOrEmpty();
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		toDoController.addDetail("TEST-D", list);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D")).tryQuery().isPresent());
		Set<Detail> details = toDoRepository.findAllDetailsByListId(list.getId());
		assertThat(details).isNotNull().hasSize(1);
		Detail detail = details.iterator().next();
		assertThat(detail).isNotNull();
		assertThat(detail.getTodo()).isEqualTo("TEST-D");
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView()
				.getItems()).hasSize(1).containsExactly(detail);
	}
	
}
