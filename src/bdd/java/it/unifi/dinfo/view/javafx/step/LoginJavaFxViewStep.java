package it.unifi.dinfo.view.javafx.step;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.STAGE_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testfx.framework.junit.ApplicationTest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import io.cucumber.java.After;
/* import io.cucumber.java.Before; */
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.guice.controller.ToDoControllerModule;
import it.unifi.dinfo.guice.javafx.ToDoJavaFxModule;
import it.unifi.dinfo.guice.mysql.ToDoMySqlModule;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.ToDoRepository;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import javafx.stage.Stage;

public class LoginJavaFxViewStep extends ApplicationTest {
	
	private ToDoJavaFxView toDoJavaFxView;
	private ToDoRepository toDoRepository;
	
	private SessionFactory hibernateSessionFactory;
	private Session hibernateSession;
	
	/* @Before */
	@Given("The user runs An Advanced Todo App")
	public void setUp() throws Exception {
		super.internalBefore();
	}
	
	@After
	public void tearDown() throws Exception {
		super.internalAfter();
	}

	@Override
	public void init() throws Exception {
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
		Injector injector = Guice.createInjector(
				Modules.combine(
						new ToDoMySqlModule(
								properties.getProperty("MY_SQL_HOST"), 
								Integer.valueOf(System.getProperty("mysql.port", 
										properties.getProperty("MY_SQL_PORT"))), 
								properties.getProperty("MY_SQL_DB_NAME"), 
								properties.getProperty("MY_SQL_USER"), 
								properties.getProperty("MY_SQL_PASS")), 
						new ToDoControllerModule(), 
						new ToDoJavaFxModule()));
		toDoRepository = injector.getInstance(ToDoRepository.class);
		toDoJavaFxView = injector.getInstance(ToDoJavaFxView.class);
		hibernateSessionFactory = injector.getInstance(SessionFactory.class);
		hibernateSession = injector.getInstance(Session.class);
		toDoJavaFxView.init();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		toDoJavaFxView.start(stage);
	}
	
	@Override
	public void stop() throws Exception {
		toDoJavaFxView.stop();
		ToDoMySqlModule.closeSessionFactory(hibernateSessionFactory, hibernateSession);
	}
	
	@Given("The database contains a user with the following values")
	public void the_database_contains_the_user_with_the_following_values(Map<String, String> values) {
		String name = values.get("name");
		String surname = values.get("surname");
		String email = values.get("email");
		String password = values.get("password");
		User user = new User(name, surname, email, password);
		toDoRepository.createUser(user);
	}
	
	@Given("The datatase contains a log with the following values")
	public void the_datatase_contains_a_log_with_the_following_values(Map<String, String> values) 
			throws ParseException {
		String in = values.get("in");
		String out = values.get("out");
		String user_email = values.get("user_email");
		User user = toDoRepository.findUserByEmail(user_email);
		SimpleDateFormat sdf = new SimpleDateFormat(UserJavaFxView.SDF_PATTERN, Locale.ITALIAN);
		sdf.setLenient(true);
		Date dateIn = sdf.parse(in);
		Date dateOut = sdf.parse(out);
		Log log = new Log(dateIn, user);
		log.setOut(dateOut);
		toDoRepository.createLog(log);
	}
	
	@When("The user enters {string} in the Email login input field")
	public void the_user_enters_email_in_the_email_login_input_field(String email) {
		clickOn("#" + LoginJavaFxView.EMAIL_TEXTFIELD_ID);
		write(email);
	}
	
	@When("The user enters {string} in the Password login input field")
	public void the_user_enters_password_in_the_password_login_input_field(String password) {
		clickOn("#" + LoginJavaFxView.PASSWORD_FIELD_ID);
		write(password);
	}
	
	@When("The user clicks the Login button")
	public void the_user_clicks_the_login_button() {
		clickOn("#" + LoginJavaFxView.LOGIN_BUTTON_ID);
	}
	
	@Then("The Lists, Details, Addition-Modification and User Views are showed")
	public void the_lists_details_addition_modification_and_user_views_are_showed() {
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getAppRoot());
	}
	
	@Then("The Login and Registration Views are showed")
	public void the_login_and_registration_views_are_showed() {
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
	}
	
	@Then("The user text shows {string}")
	public void the_user_text_shows_name_surname(String nameAndSurname) {
		String userText = lookup("#" + UserJavaFxView.USER_TEXT_ID).queryText().getText();
		assertThat(userText).isEqualTo(nameAndSurname);
	}
	
	@Then("The log text shows that the last login is happened on {string}")
	public void the_log_text_shows_that_the_last_login_is_happened_on_in(String in) {
		String lastLogText = lookup("#" + UserJavaFxView.LOG_TEXT_ID).queryText().getText();
		assertThat(lastLogText).isEqualTo(UserJavaFxView.LOG_STARTING_TEXT + in);
	}
	
	@Then("The login error text shows {string}")
	public void the_login_error_text_shows_error_message(String errorMessage) {
		String loginErrorText = lookup("#" + LoginJavaFxView.ERROR_TEXT_ID).queryText().getText();
		assertThat(loginErrorText).isEqualTo(errorMessage);
	}
	
}
