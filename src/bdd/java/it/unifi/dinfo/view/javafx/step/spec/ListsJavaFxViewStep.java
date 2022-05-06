package it.unifi.dinfo.view.javafx.step.spec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.framework.junit.ApplicationTest;

import io.cucumber.java.en.Then;
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
	
}
