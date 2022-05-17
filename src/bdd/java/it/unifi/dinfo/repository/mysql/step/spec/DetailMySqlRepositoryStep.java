package it.unifi.dinfo.repository.mysql.step.spec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.google.inject.Inject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;

public class DetailMySqlRepositoryStep {

	private ToDoMySqlRepository toDoMySqlRepository;

	@Inject
	public DetailMySqlRepositoryStep(ToDoMySqlRepository toDoMySqlRepository) {
		this.toDoMySqlRepository = toDoMySqlRepository;
	}
	
	@Given("The database contains a detail with the following values")
	public void theDatabaseContainsADetailWithTheFollowingValues(Map<String, String> values) {
		String todo = values.get("todo");
		String done = values.get("done");
		String list_name = values.get("list_name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(list_name, user.getId());
		Detail detail = new Detail(todo, list);
		detail.setDone(Boolean.valueOf(done));
		toDoMySqlRepository.createDetail(detail);
	}
	
	@Given("From the database is deleted the detail with the following values")
	public void fromTheDatabaseIsDeletedTheDetailWithTheFollowingValues(Map<String, String> values) {
		String todo = values.get("todo");
		String list_name = values.get("list_name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(list_name, user.getId());
		Detail detail = toDoMySqlRepository.findDetailByTodoAndListId(todo, list.getId());
		toDoMySqlRepository.deleteDetail(detail);
	}
	
	@Then("Between the details in the datatase there is the detail with the following values")
	public void betweenTheDetailsInTheDatabaseThereIsTheDetailWithTheFollowingValues(
			Map<String, String> values) {
		String todo = values.get("todo");
		String done = values.get("done");
		String list_name = values.get("list_name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(list_name, user.getId());
		Detail detail = toDoMySqlRepository.findDetailByTodoAndListId(todo, list.getId());
		assertThat(detail).isNotNull();
		assertThat(detail.getDone()).isEqualTo(Boolean.valueOf(done));
	}
	
	@Then("Between the details in the datatase there is not the detail with the following values")
	public void betweenTheDetailsInTheDatabaseThereIsNotTheDetailWithTheFollowingValues(
			Map<String, String> values) {
		String todo = values.get("todo");
		String list_name = values.get("list_name");
		String user_email = values.get("user_email");
		User user = toDoMySqlRepository.findUserByEmail(user_email);
		List list = toDoMySqlRepository.findListByNameAndUserId(list_name, user.getId());
		Detail detail = toDoMySqlRepository.findDetailByTodoAndListId(todo, list.getId());
		assertThat(detail).isNull();
	}
	
}
