package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import javafx.scene.control.ListView;

public class DetailsJavaFxViewStep extends ApplicationTest {

	@Then("The list view of Details contains a detail with todo {string}")
	public void theListViewOfDetailsContainsADetailWithTodo(String todo) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId(todo)).tryQuery().isPresent());
		ListView<Detail> listView = lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView();
		assertThat(listView.getItems()).isNotEmpty().filteredOn(
				(detail) -> detail.getTodo().equals(todo)).hasSize(1);
	}
	
	@Then("The list view of Details does not contain a detail with todo {string}")
	public void theListViewOfDetailsDoesNotContainADetailWithTodo(String todo) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> !lookup("#" + DetailsJavaFxView.getRowLabelId(todo)).tryQuery().isPresent());
		ListView<Detail> listView = lookup("#" + DetailsJavaFxView.LISTVIEW_ID).queryListView();
		assertThat(listView.getItems()).filteredOn((detail) -> detail.getTodo().equals(todo)).isEmpty();
	}
	
	@When("The user clicks the Add detail button")
	public void theUserClicksTheAddDetailButton() {
		clickOn("#" + DetailsJavaFxView.ADD_BUTTON_ID);
	}
	
	@When("The user clicks the {string} label in list view of Details")
	public void theUserClicksTheLabelInListViewOfDetails(String label) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowLabelId(label)).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowLabelId(label));
	}
	
	@When("The user clicks the {string} checkbox in list view of Details")
	public void theUserClicksTheCheckboxInListViewOfDetails(String todo) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowCheckBoxId(todo)).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowCheckBoxId(todo));
	}
	
	@When("The user clicks the {string} modify icon in list view of Details")
	public void theUserClicksTheModifyIconInListViewOfDetails(String todo) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowModifyButtonId(todo)).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowModifyButtonId(todo));
	}
	
	@When("The user clicks the {string} delete icon in list view of Details")
	public void theUserClicksTheDeleteIconInListViewOfDetails(String todo) throws TimeoutException {
		waitFor(10, TimeUnit.SECONDS, 
				() -> lookup("#" + DetailsJavaFxView.getRowDeleteButtonId(todo)).tryQuery().isPresent());
		clickOn("#" + DetailsJavaFxView.getRowDeleteButtonId(todo));
	}
	
	@Then("The details error text shows {string}")
	public void theDetailsErrorTextShowsErrorMessage(String errorMessage) {
		String detailsErrorText = lookup("#" + DetailsJavaFxView.ERROR_TEXT_ID).queryText().getText();
		assertThat(detailsErrorText).isEqualTo(errorMessage);
	}
	
}
