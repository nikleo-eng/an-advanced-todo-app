package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import javafx.scene.control.ListView;

public class ListsJavaFxViewStep extends ApplicationTest {

	@Then("The list view of Lists contains a list with name {string}")
	public void theListViewOfListsContainsAListWithName(String name) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId(name)).tryQuery().isPresent());
		ListView<List> listView = lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView();
		assertThat(listView.getItems()).isNotEmpty().filteredOn(
				(list) -> list.getName().equals(name)).hasSize(1);
	}
	
	@Then("The list view of Lists does not contain a list with name {string}")
	public void theListViewOfListsDoesNotContainAListWithName(String name) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + ListsJavaFxView.getRowLabelId(name)).tryQuery().isPresent());
		ListView<List> listView = lookup("#" + ListsJavaFxView.LISTVIEW_ID).queryListView();
		assertThat(listView.getItems()).filteredOn((list) -> list.getName().equals(name)).isEmpty();
	}
	
	@When("The user clicks the Add list button")
	public void theUserClicksTheAddListButton() {
		clickOn("#" + ListsJavaFxView.ADD_BUTTON_ID);
	}
	
	@When("The user clicks the {string} label in list view of Lists")
	public void theUserClicksTheLabelInListViewOfLists(String label) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowLabelId(label)).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowLabelId(label));
	}
	
	@When("The user clicks the {string} modify icon in list view of Lists")
	public void theUserClicksTheModifyIconInListViewOfLists(String name) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowModifyButtonId(name)).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowModifyButtonId(name));
	}
	
	@When("The user clicks the {string} delete icon in list view of Lists")
	public void theUserClicksTheDeleteIconInListViewOfLists(String name) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + ListsJavaFxView.getRowDeleteButtonId(name)).tryQuery().isPresent());
		clickOn("#" + ListsJavaFxView.getRowDeleteButtonId(name));
	}
	
	@Then("The lists error text shows {string}")
	public void theListsErrorTextShowsErrorMessage(String errorMessage) {
		String listsErrorText = lookup("#" + ListsJavaFxView.ERROR_TEXT_ID).queryText().getText();
		assertThat(listsErrorText).isEqualTo(errorMessage);
	}
	
}
