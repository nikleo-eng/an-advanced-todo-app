package it.unifi.dinfo.repository.mysql.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.google.inject.Inject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;

public class UserMySqlRepositoryStep {

	private ToDoMySqlRepository toDoMySqlRepository;
	
	@Inject
	public UserMySqlRepositoryStep(ToDoMySqlRepository toDoMySqlRepository) {
		this.toDoMySqlRepository = toDoMySqlRepository;
	}
	
	@Given("The database contains a user with the following values")
	public void theDatabaseContainsTheUserWithTheFollowingValues(Map<String, String> values) {
		String name = values.get("name");
		String surname = values.get("surname");
		String email = values.get("email");
		String password = values.get("password");
		User user = new User(name, surname, email, password);
		toDoMySqlRepository.createUser(user);
	}
	
	@Then("Between the users in the database there is a user with the following values")
	public void betweenTheUsersInTheDatabaseThereIsAUserWithTheFollowingValues(Map<String, String> values) {
		String name = values.get("name");
		String surname = values.get("surname");
		String email = values.get("email");
		String password = values.get("password");
		User user = toDoMySqlRepository.findUserByEmail(email);
		assertThat(user).isNotNull();
		assertThat(user.getName()).isEqualTo(name);
		assertThat(user.getSurname()).isEqualTo(surname);
		assertThat(user.getPassword()).isEqualTo(password);
	}
	
}
