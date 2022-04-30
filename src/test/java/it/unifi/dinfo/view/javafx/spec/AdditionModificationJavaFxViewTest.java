package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.view.spec.AdditionModificationView.ERRORS;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdditionModificationJavaFxViewTest extends ApplicationTest {

	@Mock
	private ToDoController toDoController;
	
	@Mock
	private DetailsJavaFxView detailsJavaFxView;
	
	@Mock
	private ListsJavaFxView listsJavaFxView;
	
	private static User user = new User("Mario", "Rossi", "email@email.com", "password");
	
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	
	@Override
	public void start(Stage stage) throws Exception {
		MockitoAnnotations.openMocks(this);
		additionModificationJavaFxView = new AdditionModificationJavaFxView(listsJavaFxView, 
				detailsJavaFxView, toDoController);
		additionModificationJavaFxView.setCurrentUser(user);
		
		FlowPane flowPane = new FlowPane();
		/* https://stackoverflow.com/questions/67893273 */
		flowPane.setStyle(FONT_FAMILY);
		Scene scene = new Scene(flowPane, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = additionModificationJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		flowPane.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@Test
	public void shouldViewContainTextAreaAndCancelAndSaveButtons() {
		Node textAreaNode = lookup("#" + TEXTAREA_ID).tryQuery().orElse(null);
		assertThat(textAreaNode).isNotNull().isOfAnyClassIn(UpperTextArea.class);
		UpperTextArea textArea = (UpperTextArea) textAreaNode;
		assertThat(textArea.isVisible()).isTrue();
		
		Node cancelButtonNode = lookup("#" + CANCEL_BUTTON_ID).tryQuery().orElse(null);
		assertThat(cancelButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button cancelButton = (Button) cancelButtonNode;
		assertThat(cancelButton.isVisible()).isTrue();
		assertThat(cancelButton.getText()).isEqualTo(CANCEL_BUTTON_TEXT);
		
		Node saveButtonNode = lookup("#" + SAVE_BUTTON_ID).tryQuery().orElse(null);
		assertThat(saveButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button saveButton = (Button) saveButtonNode;
		assertThat(saveButton.isVisible()).isTrue();
		assertThat(saveButton.getText()).isEqualTo(SAVE_BUTTON_TEXT);
	}
	
	@Test
	public void shouldTextAreaBeInitiallyEmpty() {
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEmpty();
	}
	
	@Test
	public void shouldViewContainEmptyErrorText() {
		Node errorTextNode = lookup("#" + ERROR_TEXT_ID).tryQuery().orElse(null);
		assertThat(errorTextNode).isNotNull().isOfAnyClassIn(Text.class);
		Text errorText = (Text) errorTextNode;
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
	}
	
	@Test
	public void shouldTextAreaAndButtonsBeInitiallyDisabled() {
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().isDisable()).isTrue();
		assertThat(lookup("#" + CANCEL_BUTTON_ID).queryButton().isDisable()).isTrue();
		assertThat(lookup("#" + SAVE_BUTTON_ID).queryButton().isDisable()).isTrue();
	}
	
	@Test
	public void shouldRenderErrorMakeVisibleErrorTextAndChangeItsText() {
		additionModificationJavaFxView.renderError(ERRORS.FIELD_EMPTY.getValue());
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(ERRORS.FIELD_EMPTY.getValue());
	}
	
	@Test
	public void shouldResetGUIClearTextAreaMakeInvisibleErrorTextAndDisableButtons() {
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.LIST_ALREADY_FOUND.getValue());
		
		additionModificationJavaFxView.resetGUI();
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(cancelButton.isDisable()).isTrue();
		assertThat(saveButton.isDisable()).isTrue();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
	}
	
	@Test
	public void shouldClickOnCancelButtonResetThisViewAndEnableListsViewWhenNoListIsSelected() {
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.LIST_ALREADY_FOUND.getValue());
		
		when(listsJavaFxView.getSelectedItem()).thenReturn(null);
		clickOn(cancelButton);
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(cancelButton.isDisable()).isTrue();
		assertThat(saveButton.isDisable()).isTrue();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		verify(listsJavaFxView).enableArea();
		verifyNoInteractions(detailsJavaFxView);
	}
	
	@Test
	public void shouldClickOnCancelButtonResetThisViewAndEnableListsAndDetailsViewWhenAListIsSelected() {
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.LIST_ALREADY_FOUND.getValue());
		
		List list = new List("TEST", user);
		when(listsJavaFxView.getSelectedItem()).thenReturn(list);
		clickOn(cancelButton);
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(cancelButton.isDisable()).isTrue();
		assertThat(saveButton.isDisable()).isTrue();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
		verify(listsJavaFxView).enableArea();
		verify(detailsJavaFxView).enableArea();
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldClickOnSaveButtonCallAddListOnControllerWhenViewHasBeenEnabledForCreationAndFromListsView() {
		additionModificationJavaFxView.setCalledForCreation(Boolean.TRUE);
		additionModificationJavaFxView.setCalledFromLists(Boolean.TRUE);
		
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		
		clickOn(saveButton);
		
		verify(toDoController).addList(textArea.getText(), user);
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldClickOnSaveButtonCallAddDetailOnControllerWhenViewHasBeenEnabledForCreationAndFromDetailsView() {
		additionModificationJavaFxView.setCalledForCreation(Boolean.TRUE);
		additionModificationJavaFxView.setCalledFromLists(Boolean.FALSE);
		
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		
		List list = new List("TEST", user);
		when(listsJavaFxView.getSelectedItem()).thenReturn(list);
		clickOn(saveButton);
		
		verify(toDoController).addDetail(textArea.getText(), list);
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldClickOnSaveButtonCallModifyNameListOnControllerWhenViewHasBeenEnabledForModificationAndFromListsView() {
		additionModificationJavaFxView.setCalledForCreation(Boolean.FALSE);
		additionModificationJavaFxView.setCalledFromLists(Boolean.TRUE);
		
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test_new");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		
		List list = new List("TEST", user);
		when(listsJavaFxView.getSelectedItem()).thenReturn(list);
		clickOn(saveButton);
		
		verify(toDoController).modifyNameList(textArea.getText(), list);
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldClickOnSaveButtonCallModifyTodoDetailOnControllerWhenViewHasBeenEnabledForModificationAndFromDetailsView() {
		additionModificationJavaFxView.setCalledForCreation(Boolean.FALSE);
		additionModificationJavaFxView.setCalledFromLists(Boolean.FALSE);
		
		UpperTextArea textArea = lookup("#" + TEXTAREA_ID).queryAs(UpperTextArea.class);
		textArea.setDisable(false);
		clickOn(textArea);
		write("test-d_new");
		Button cancelButton = lookup("#" + CANCEL_BUTTON_ID).queryButton();
		cancelButton.setDisable(false);
		Button saveButton = lookup("#" + SAVE_BUTTON_ID).queryButton();
		saveButton.setDisable(false);
		
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		when(detailsJavaFxView.getSelectedItem()).thenReturn(detail);
		clickOn(saveButton);
		
		verify(toDoController).modifyTodoDetail(textArea.getText(), detail);
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldEnableAreaEnableTextAreaAndButtonsAndSetTextInTextAreaWithNameOfSelectedListWhenCalledForModificationAndFromListsView() {
		List list = new List("TEST", user);
		when(listsJavaFxView.getSelectedItem()).thenReturn(list);
		
		additionModificationJavaFxView.enableArea(Boolean.FALSE, Boolean.TRUE);
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEqualTo(list.getName());
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().isDisable()).isFalse();
		assertThat(lookup("#" + CANCEL_BUTTON_ID).queryButton().isDisable()).isFalse();
		assertThat(lookup("#" + SAVE_BUTTON_ID).queryButton().isDisable()).isFalse();
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldEnableAreaEnableTextAreaAndButtonsAndSetTextInTextAreaWithTodoOfSelectedDetailWhenCalledForModificationAndFromDetailsView() {
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		when(detailsJavaFxView.getSelectedItem()).thenReturn(detail);
		
		additionModificationJavaFxView.enableArea(Boolean.FALSE, Boolean.FALSE);
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEqualTo(detail.getTodo());
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().isDisable()).isFalse();
		assertThat(lookup("#" + CANCEL_BUTTON_ID).queryButton().isDisable()).isFalse();
		assertThat(lookup("#" + SAVE_BUTTON_ID).queryButton().isDisable()).isFalse();
		verifyNoMoreInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldEnableAreaEnableTextAreaAndButtonsAndLeaveEmptyTheTextAreaWhenCalledForCreationAndFromListsView() {
		additionModificationJavaFxView.enableArea(Boolean.TRUE, Boolean.TRUE);
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().isDisable()).isFalse();
		assertThat(lookup("#" + CANCEL_BUTTON_ID).queryButton().isDisable()).isFalse();
		assertThat(lookup("#" + SAVE_BUTTON_ID).queryButton().isDisable()).isFalse();
		verifyNoInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
	@Test
	public void shouldEnableAreaEnableTextAreaAndButtonsAndLeaveEmptyTheTextAreaWhenCalledForCreationAndFromDetailsView() {
		additionModificationJavaFxView.enableArea(Boolean.TRUE, Boolean.FALSE);
		
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + TEXTAREA_ID).queryTextInputControl().isDisable()).isFalse();
		assertThat(lookup("#" + CANCEL_BUTTON_ID).queryButton().isDisable()).isFalse();
		assertThat(lookup("#" + SAVE_BUTTON_ID).queryButton().isDisable()).isFalse();
		verifyNoInteractions(ignoreStubs(toDoController, listsJavaFxView, detailsJavaFxView));
	}
	
}
