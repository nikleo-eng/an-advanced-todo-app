package it.unifi.dinfo.repository.mysql.step.spec;

import java.util.Map;

import com.google.inject.Inject;

import io.cucumber.java.en.Given;
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
	
}
