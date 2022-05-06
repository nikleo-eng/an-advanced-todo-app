package it.unifi.dinfo.repository.mysql.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;

public class LogMySqlRepositoryStep {

	private ToDoMySqlRepository toDoMySqlRepository;
	
	@Inject
	public LogMySqlRepositoryStep(ToDoMySqlRepository toDoMySqlRepository) {
		this.toDoMySqlRepository = toDoMySqlRepository;
	}

	@Given("The datatase contains a log with the following values")
	public void theDatataseContainsALogWithTheFollowingValues(Map<String, String> values) 
			throws ParseException {
		String in = values.get("in");
		String out = values.get("out");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		SimpleDateFormat sdf = new SimpleDateFormat(UserJavaFxView.SDF_PATTERN, Locale.ITALIAN);
		sdf.setLenient(true);
		Date dateIn = sdf.parse(in);
		Date dateOut = sdf.parse(out);
		Log log = new Log(dateIn, user);
		log.setOut(dateOut);
		toDoMySqlRepository.createLog(log);
	}
	
	@Then("Between the logs in the datatase there is a log for the user with the email {string}")
	public void betweenTheLogsInTheDatataseThereIsALogForTheUserWithTheEmail(String email) {
		User user = toDoMySqlRepository.findUserByEmail(email);
		Set<Log> logs = toDoMySqlRepository.findAllLogsByUserId(user.getId());
		assertThat(logs).isNotNull().isNotEmpty();
	}
	
}
