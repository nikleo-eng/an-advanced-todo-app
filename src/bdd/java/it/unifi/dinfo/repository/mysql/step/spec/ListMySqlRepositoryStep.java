package it.unifi.dinfo.repository.mysql.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.google.inject.Inject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;

public class ListMySqlRepositoryStep {

	private ToDoMySqlRepository toDoMySqlRepository;

	@Inject
	public ListMySqlRepositoryStep(ToDoMySqlRepository toDoMySqlRepository) {
		this.toDoMySqlRepository = toDoMySqlRepository;
	}
	
	@Given("The database contains a list with the following values")
	public void theDatabaseContainsAListWithTheFollowingValues(Map<String, String> values) {
		String name = values.get("name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = new List(name, user);
		toDoMySqlRepository.createList(list);
	}
	
	@Given("From the database is deleted the list with the following values")
	public void fromTheDatabaseIsDeletedTheListWithTheFollowingValues(Map<String, String> values) {
		String name = values.get("name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(name, user.getId());
		toDoMySqlRepository.deleteList(list);
	}
	
	@Then("Between the lists in the datatase there is the list with the following values")
	public void betweenTheListsInTheDatabaseThereIsTheListWithTheFollowingValues(
			Map<String, String> values) {
		String name = values.get("name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(name, user.getId());
		assertThat(list).isNotNull();
	}
	
	@Then("Between the lists in the datatase there is not the list with the following values")
	public void betweenTheListsInTheDatabaseThereIsNotTheListWithTheFollowingValues(
			Map<String, String> values) {
		String name = values.get("name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(name, user.getId());
		assertThat(list).isNull();
	}
	
}
