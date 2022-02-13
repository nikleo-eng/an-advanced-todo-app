package it.unifi.dinfo.view.javafx;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static org.testfx.api.FxAssert.*;
import static org.testfx.matcher.base.NodeMatchers.*;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import javafx.stage.Stage;

public class ToDoJavaFxViewTest extends ApplicationTest {

	@Override
	public void start(Stage stage) throws Exception {
		new ToDoJavaFxView().start(stage);
	}
	
	@Test
	public void shouldContainButtonClickMe() {
		verifyThat("#" + BUTTON_ID, isNotNull());
		verifyThat("#" + BUTTON_ID, isVisible());
		verifyThat("#" + BUTTON_ID, hasText(BUTTON_PRE_CLICK_TEXT));
	}
	
	@Test
	public void shouldButtonChangeStateAndTextWhenClicked() {
		clickOn("#" + BUTTON_ID);
		verifyThat("#" + BUTTON_ID, hasText(BUTTON_POST_CLICK_TEXT));
		verifyThat("#" + BUTTON_ID, isDisabled());
	}

}
