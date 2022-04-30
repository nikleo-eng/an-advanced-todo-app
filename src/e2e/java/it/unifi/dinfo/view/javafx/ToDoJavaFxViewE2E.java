package it.unifi.dinfo.view.javafx;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.STAGE_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.framework.junit.TestFXRule;

import com.sun.javafx.application.ParametersImpl;

import it.unifi.dinfo.app.main.ToDoAppMain;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
public class ToDoJavaFxViewE2E extends ApplicationTest {
	
	/* https://github.com/TestFX/TestFX/issues/367#issuecomment-347077166 */
	@Rule
    public TestFXRule testFXRule = new TestFXRule(3);
	
	private ToDoRepository toDoRepository;
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
		toDoRepository = toDoJavaFxView.getToDoRepository();
		toDoJavaFxView.start(stage);
	}
	
	@Override
	public void stop() throws Exception {
		toDoJavaFxView.stop();
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
		String userText = lookup("#" + UserJavaFxView.USER_TEXT_ID).queryText().getText();
		assertThat(userText).isEqualTo(user.getName() + " " + user.getSurname());
	}
	
	@Test
	public void shouldCorrectClickOnLoginButtonBringViewToAppRootWithLoggedUserInfo() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
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
	public void shouldClickOnLogoutButtonBringViewToUserRootAndClearCurrentUserInfo() {
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
		clickOn("#" + UserJavaFxView.LOGOUT_BUTTON_ID);
		User user = toDoRepository.findUserByEmail("email@email.com");
		Set<Log> logs = toDoRepository.findAllLogsByUserId(user.getId());
		assertThat(logs).isNotNull().hasSize(1);
		Log log = logs.iterator().next();
		assertThat(log).isNotNull();
		assertThat(log.getOut()).isNotNull();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentUser()).isNull();
		assertThat(toDoJavaFxView.getAdditionModificationJavaFxView().getCurrentUser()).isNull();
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentLog()).isNull();
	}
	
	@Test
	public void shouldListsAndDetailsAreasBeInitiallyEmpty() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
	}
	
	@Test
	public void shouldClickOnRefreshButtonUpdateListsAreaWithCreatedList() throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		list = toDoRepository.createList(list);
		clickOn("#" + UserJavaFxView.REFRESH_BUTTON_ID);
		/* https://github.com/TestFX/TestFX/issues/392 */
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		ListView<it.unifi.dinfo.model.List> listView = lookup("#" + ListsJavaFxView.LISTVIEW_ID)
				.queryListView();
		assertThat(listView.getItems()).isNotEmpty().hasSize(1).containsExactly(list);
	}
	
	@Test
	public void shouldClickOnAddButtonInListsAreaEnableAdditionModificationAreaForAddingCorrectlyANewList() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isTrue();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isTrue();
		clickOn("#" + ListsJavaFxView.ADD_BUTTON_ID);
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isFalse();
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("TEST");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		ListView<it.unifi.dinfo.model.List> listView = lookup("#" + ListsJavaFxView.LISTVIEW_ID)
				.queryListView();
		assertThat(listView.getItems()).isNotEmpty().hasSize(1);
		it.unifi.dinfo.model.List list = toDoRepository.findListByNameAndUserId("TEST", user.getId());
		assertThat(list).isNotNull();
	}
	
	@Test
	public void shouldClickOnAddButtonInDetailsAreaEnableAdditionModificationAreaForAddingCorrectlyANewDetail() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isTrue();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isTrue();
		clickOn("#" + DetailsJavaFxView.ADD_BUTTON_ID);
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isFalse();
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("TEST-D");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D")).tryQuery().isPresent());
		ListView<Detail> listView = lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView();
		assertThat(listView.getItems()).isNotEmpty().hasSize(1);
		Detail detail = toDoRepository.findDetailByTodoAndListId("TEST-D", list.getId());
		assertThat(detail).isNotNull();
	}
	
	@Test
	public void shouldClickOnDeleteIconOnOneItemInListsAreaDeleteCorrectlyTheSelectedList() throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowDeleteButtonId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowDeleteButtonId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		Set<it.unifi.dinfo.model.List> lists = toDoRepository.findAllListsByUserId(user.getId());
		assertThat(lists).isNullOrEmpty();
	}
	
	@Test
	public void shouldClickOnDeleteIconOnOneItemInDetailsAreaDeleteCorrectlyTheSelectedDetail() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		detail = toDoRepository.createDetail(detail);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D")).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowLabelId("TEST-D"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowDeleteButtonId("TEST-D")).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowDeleteButtonId("TEST-D"));
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		Set<Detail> details = toDoRepository.findAllDetailsByListId(list.getId());
		assertThat(details).isNullOrEmpty();
	}
	
	@Test
	public void shouldClickOnCheckboxOnOneItemInDetailsAreaUpdateCorrectlyDoneStateInfoOfTheSelectedDetail() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		detail = toDoRepository.createDetail(detail);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D")).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowLabelId("TEST-D"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D")).tryQuery().isPresent());
		assertThat(lookup("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D"))
				.queryAs(CheckBox.class).isSelected()).isFalse();
		assertThat(detail.getDone()).isFalse();
		clickOn("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D"));
		assertThat(lookup("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D"))
				.queryAs(CheckBox.class).isSelected()).isTrue();
		detail = toDoRepository.findDetailByTodoAndListId("TEST-D", list.getId());
		assertThat(detail.getDone()).isTrue();
	}
	
	@Test
	public void shouldClickOnModifyIconOnOneItemInListsAreaEnableAdditionModificationAreaForUpdatingCorrectlyTheNameOfTheSelectedList() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowModifyButtonId("TEST")).tryQuery().isPresent());
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isTrue();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isTrue();
		clickOn("#" + ListsJavaFxView.getRowModifyButtonId("TEST"));
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isFalse();
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("_NEW");
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).getText()).isEqualTo("TEST_NEW");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + ListsJavaFxView.getRowModifyButtonId("TEST")).tryQuery().isPresent());
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowModifyButtonId("TEST_NEW")).tryQuery().isPresent());
		assertThat(toDoRepository.findListByNameAndUserId("TEST", user.getId())).isNull();
		assertThat(toDoRepository.findListByNameAndUserId("TEST_NEW", user.getId())).isNotNull();
	}
	
	@Test
	public void shouldClickOnModifyIconOnOneItemInDetailsAreaEnableAdditionModificationAreaForUpdatingCorrectlyTheTodoOfTheSelectedDetail() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		it.unifi.dinfo.model.List list = new it.unifi.dinfo.model.List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		detail = toDoRepository.createDetail(detail);
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(user.getEmail());
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(user.getPassword());
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D")).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowLabelId("TEST-D"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowModifyButtonId("TEST-D")).tryQuery().isPresent());
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isTrue();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isTrue();
		clickOn("#" + DetailsJavaFxView.getRowModifyButtonId("TEST-D"));
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID)
				.queryButton().isDisabled()).isFalse();
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("_NEW");
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID)
				.queryAs(TextArea.class).getText()).isEqualTo("TEST-D_NEW");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D")).tryQuery().isPresent());
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId("TEST-D_NEW")).tryQuery().isPresent());
		assertThat(toDoRepository.findDetailByTodoAndListId("TEST-D", list.getId())).isNull();
		assertThat(toDoRepository.findDetailByTodoAndListId("TEST-D_NEW", list.getId())).isNotNull();
	}
	
}
