package it.unifi.dinfo.view.javafx.step;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.STAGE_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.testfx.framework.junit.ApplicationTest;

import com.google.inject.Inject;

/* import io.cucumber.java.Before; */
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import javafx.stage.Stage;

public class ToDoJavaFxViewStep extends ApplicationTest {

	private ToDoJavaFxView toDoJavaFxView;
	
	@Inject
	public ToDoJavaFxViewStep(ToDoJavaFxView toDoJavaFxView) {
		this.toDoJavaFxView = toDoJavaFxView;
	}

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
		toDoJavaFxView.init();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		toDoJavaFxView.start(stage);
	}
	
	@Override
	public void stop() throws Exception {
		toDoJavaFxView.stop();
	}
	
	@Then("The Lists, Details, Addition-Modification and User Views are showed")
	public void theListsDetailsAdditionModificationAndUserViewsAreShowed() {
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getAppRoot());
	}
	
	@Then("The Login and Registration Views are showed")
	public void theLoginAndRegistrationViewsAreShowed() {
		assertThat(window(STAGE_TITLE).getScene().getRoot()).isEqualTo(toDoJavaFxView.getUserRoot());
	}
	
}
