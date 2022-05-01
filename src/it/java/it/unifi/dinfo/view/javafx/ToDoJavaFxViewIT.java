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
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.framework.junit.TestFXRule;

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
import it.unifi.dinfo.view.spec.DetailsView;
import it.unifi.dinfo.view.spec.RegistrationView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ToDoJavaFxViewIT extends ApplicationTest {
	
	/* https://github.com/TestFX/TestFX/issues/367#issuecomment-347077166 */
	@Rule
	public TestFXRule testFXRule = new TestFXRule(3);

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
	public void shouldCorrectlyLoginAddLogInfoToLoggedUserAndBringViewToAppRootWithLoggedUserInfo() {
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
	public void shouldWrongRegisterWithNotMatchedPasswordsDoNotChangeViewAndShowRegistrationErrorMessage() {
		Text errorText = lookup("#" + RegistrationJavaFxView.ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		clickOn("#" + RegistrationJavaFxView.NAME_TEXTFIELD_ID);
		write("Mario");
		clickOn("#" + RegistrationJavaFxView.SURNAME_TEXTFIELD_ID);
		write("Rossi");
		clickOn("#" + RegistrationJavaFxView.EMAIL_TEXTFIELD_ID);
		write("email@email.com");
		clickOn("#" + RegistrationJavaFxView.PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + RegistrationJavaFxView.CONFIRM_PASSWORD_FIELD_ID);
		write("password1");
		clickOn("#" + RegistrationJavaFxView.REGISTER_BUTTON_ID);
		assertThat(toDoRepository.findUserByEmail("email@email.com")).isNull();
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentUser()).isNull();
		assertThat(toDoJavaFxView.getAdditionModificationJavaFxView().getCurrentUser()).isNull();
		assertThat(toDoJavaFxView.getUserJavaFxView().getCurrentLog()).isNull();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(RegistrationView.ERRORS.PASSWORDS_NOT_MACHING.getValue());
	}
	
	@Test
	public void shouldAddListAddCorrectlyAListInDatabaseAndListViewOfListsArea() throws TimeoutException {
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
		assertThat(toDoRepository.findAllListsByUserId(user.getId())).isNullOrEmpty();
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		clickOn("#" + ListsJavaFxView.ADD_BUTTON_ID);
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("TEST");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
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
	public void shouldAddDetailAddCorrectlyADetailInDatabaseAndListViewOfDetailsArea() throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		List list = new List("TEST", user);
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
		assertThat(toDoRepository.findAllDetailsByListId(list.getId())).isNullOrEmpty();
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		clickOn("#" + DetailsJavaFxView.ADD_BUTTON_ID);
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("TEST-D");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
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
	
	@Test
	public void shouldLogoutEndLogInfoOfLoggedUserAndBringViewToUserRoot() {
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
		Log log = toDoJavaFxView.getUserJavaFxView().getCurrentLog();
		clickOn("#" + UserJavaFxView.LOGOUT_BUTTON_ID);
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
		Log modifiedLog = toDoRepository.findAllLogsByUserId(log.getUser().getId())
				.stream().filter(innerLog -> innerLog.equals(log)).findAny().orElse(null);
		assertThat(modifiedLog).isNotNull();
		assertThat(modifiedLog.getOut()).isNotNull();
	}
	
	@Test
	public void shouldWrongModifyNameListWithNoNameDoNotDisableAdditionModificationAreaAndShowErrorMessage() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		List list = new List("TEST", user);
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
		clickOn("#" + ListsJavaFxView.getRowModifyButtonId("TEST"));
		Text errorText = lookup("#" + AdditionModificationJavaFxView.ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		eraseText("TEST".length());
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID).queryAs(TextArea.class)
				.isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.CANCEL_BUTTON_ID).queryButton()
				.isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID).queryButton()
				.isDisabled()).isFalse();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(AdditionModificationJavaFxView.ERRORS.FIELD_EMPTY.getValue());
	}
	
	@Test
	public void shouldWrongModifyTodoDetailWithTodoAlreadyCreatedDoNotDisableAdditionModificationAreaAndShowErrorMessage() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		List list = new List("TEST", user);
		Detail detail1 = new Detail("TEST-D", list);
		Detail detail2 = new Detail("TEST-D_OTHER", list);
		user = toDoRepository.createUser(user);
		log = toDoRepository.createLog(log);
		list = toDoRepository.createList(list);
		detail1 = toDoRepository.createDetail(detail1);
		detail2 = toDoRepository.createDetail(detail2);
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
		clickOn("#" + DetailsJavaFxView.getRowModifyButtonId("TEST-D"));
		Text errorText = lookup("#" + AdditionModificationJavaFxView.ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		clickOn("#" + AdditionModificationJavaFxView.TEXTAREA_ID);
		write("_OTHER");
		clickOn("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID);
		assertThat(lookup("#" + AdditionModificationJavaFxView.TEXTAREA_ID).queryAs(TextArea.class)
				.isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.CANCEL_BUTTON_ID).queryButton()
				.isDisabled()).isFalse();
		assertThat(lookup("#" + AdditionModificationJavaFxView.SAVE_BUTTON_ID).queryButton()
				.isDisabled()).isFalse();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(AdditionModificationJavaFxView
				.ERRORS.DETAIL_ALREADY_FOUND.getValue());
	}
	
	@Test
	public void shouldWrongModifyDoneDetailWithDetailAlreadyDeletedDoNotModifySelectedDetailAndShowErrorMessageInDetailsArea() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		List list = new List("TEST", user);
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
		Text errorText = lookup("#" + DetailsJavaFxView.ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		toDoRepository.deleteDetail(detail);
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D")).tryQuery().isPresent());
		assertThat(lookup("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D"))
				.queryAs(CheckBox.class).isSelected()).isFalse();
		clickOn("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D"));
		assertThat(lookup("#" + DetailsJavaFxView.getRowCheckBoxId("TEST-D"))
				.queryAs(CheckBox.class).isSelected()).isFalse();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(DetailsView.ERRORS.DETAIL_NO_LONGER_EXISTS.getValue());
	}
	
	@Test
	public void shouldDeleteListDeleteCorrectlyTheSelectedListInDatabaseAndListViewOfListsArea() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		List list = new List("TEST", user);
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
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView().getItems())
				.isNotEmpty().hasSize(1).containsExactly(list);
		clickOn("#" + ListsJavaFxView.getRowLabelId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowDeleteButtonId("TEST")).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowDeleteButtonId("TEST"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + ListsJavaFxView.getRowDeleteButtonId("TEST")).tryQuery().isPresent());
		assertThat(lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		assertThat(toDoRepository.findListByNameAndUserId("TEST", user.getId())).isNull();
	}
	
	@Test
	public void shouldDeleteDetailDeleteCorrectlyTheSelectedDetailInDatabaseAndListViewOfDetailsArea() 
			throws TimeoutException {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Log log = new Log(calendar.getTime(), user);
		log.setOut(Calendar.getInstance().getTime());
		List list = new List("TEST", user);
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
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView().getItems())
				.isNotEmpty().hasSize(1).containsExactly(detail);
		clickOn("#" + DetailsJavaFxView.getRowLabelId("TEST-D"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowDeleteButtonId("TEST-D")).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowDeleteButtonId("TEST-D"));
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + DetailsJavaFxView.getRowDeleteButtonId("TEST-D")).tryQuery().isPresent());
		assertThat(lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView().getItems()).isEmpty();
		assertThat(toDoRepository.findDetailByTodoAndListId("TEST-D", list.getId())).isNull();
	}
	
}
