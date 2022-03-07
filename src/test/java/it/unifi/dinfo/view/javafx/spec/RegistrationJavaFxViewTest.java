package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.view.spec.RegistrationView.ERRORS;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegistrationJavaFxViewTest extends ApplicationTest {

	@Mock
	private ToDoController toDoController;
	
	private RegistrationJavaFxView registrationJavaFxView;

	@Override
	public void start(Stage stage) throws Exception {
		MockitoAnnotations.openMocks(this);
		registrationJavaFxView = new RegistrationJavaFxView(toDoController);
		
		FlowPane flowPane = new FlowPane();
		Scene scene = new Scene(flowPane, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = registrationJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		flowPane.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@Test
	public void shouldViewContainNameSurnameEmailPasswordAndConfirmPasswordLabelsAndInputsAndRegisterButton() {
		Node nameLabelNode = lookup("#" + NAME_LABEL_ID).tryQuery().orElse(null);
		assertThat(nameLabelNode).isNotNull().isOfAnyClassIn(Label.class);
		Label nameLabel = (Label) nameLabelNode;
		assertThat(nameLabel.isVisible()).isTrue();
		assertThat(nameLabel.getText()).isEqualTo(NAME_LABEL_TEXT);
		
		Node nameTextFieldNode = lookup("#" + NAME_TEXTFIELD_ID).tryQuery().orElse(null);
		assertThat(nameTextFieldNode).isNotNull().isOfAnyClassIn(TextField.class);
		TextField nameTextField = (TextField) nameTextFieldNode;
		assertThat(nameTextField.isVisible()).isTrue();
		
		Node surnameLabelNode = lookup("#" + SURNAME_LABEL_ID).tryQuery().orElse(null);
		assertThat(surnameLabelNode).isNotNull().isOfAnyClassIn(Label.class);
		Label surnameLabel = (Label) surnameLabelNode;
		assertThat(surnameLabel.isVisible()).isTrue();
		assertThat(surnameLabel.getText()).isEqualTo(SURNAME_LABEL_TEXT);
		
		Node surnameTextFieldNode = lookup("#" + SURNAME_TEXTFIELD_ID).tryQuery().orElse(null);
		assertThat(surnameTextFieldNode).isNotNull().isOfAnyClassIn(TextField.class);
		TextField surnameTextField = (TextField) surnameTextFieldNode;
		assertThat(surnameTextField.isVisible()).isTrue();
		
		Node emailLabelNode = lookup("#" + EMAIL_LABEL_ID).tryQuery().orElse(null);
		assertThat(emailLabelNode).isNotNull().isOfAnyClassIn(Label.class);
		Label emailLabel = (Label) emailLabelNode;
		assertThat(emailLabel.isVisible()).isTrue();
		assertThat(emailLabel.getText()).isEqualTo(EMAIL_LABEL_TEXT);
		
		Node emailTextFieldNode = lookup("#" + EMAIL_TEXTFIELD_ID).tryQuery().orElse(null);
		assertThat(emailTextFieldNode).isNotNull().isOfAnyClassIn(TextField.class);
		TextField emailTextField = (TextField) emailTextFieldNode;
		assertThat(emailTextField.isVisible()).isTrue();
		
		Node passwordLabelNode = lookup("#" + PASSWORD_LABEL_ID).tryQuery().orElse(null);
		assertThat(passwordLabelNode).isNotNull().isOfAnyClassIn(Label.class);
		Label passwordLabel = (Label) passwordLabelNode;
		assertThat(passwordLabel.isVisible()).isTrue();
		assertThat(passwordLabel.getText()).isEqualTo(PASSWORD_LABEL_TEXT);
		
		Node passwordFieldNode = lookup("#" + PASSWORD_FIELD_ID).tryQuery().orElse(null);
		assertThat(passwordFieldNode).isNotNull().isOfAnyClassIn(PasswordField.class);
		PasswordField passwordField = (PasswordField) passwordFieldNode;
		assertThat(passwordField.isVisible()).isTrue();
		
		Node confirmPasswordLabelNode = lookup("#" + CONFIRM_PASSWORD_LABEL_ID).tryQuery().orElse(null);
		assertThat(confirmPasswordLabelNode).isNotNull().isOfAnyClassIn(Label.class);
		Label confirmPasswordLabel = (Label) confirmPasswordLabelNode;
		assertThat(confirmPasswordLabel.isVisible()).isTrue();
		assertThat(confirmPasswordLabel.getText()).isEqualTo(CONFIRM_PASSWORD_LABEL_TEXT);
		
		Node confirmPasswordFieldNode = lookup("#" + CONFIRM_PASSWORD_FIELD_ID).tryQuery().orElse(null);
		assertThat(confirmPasswordFieldNode).isNotNull().isOfAnyClassIn(PasswordField.class);
		PasswordField confirmPasswordField = (PasswordField) confirmPasswordFieldNode;
		assertThat(confirmPasswordField.isVisible()).isTrue();
		
		Node registerButtonNode = lookup("#" + REGISTER_BUTTON_ID).tryQuery().orElse(null);
		assertThat(registerButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button registerButton = (Button) registerButtonNode;
		assertThat(registerButton.isVisible()).isTrue();
		assertThat(registerButton.getText()).isEqualTo(REGISTER_BUTTON_TEXT);
	}
	
	@Test
	public void shouldNameSurnameEmailPasswordAndConfirmPasswordTextFieldsBeInitiallyEmpty() {
		assertThat(lookup("#" + NAME_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + SURNAME_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + EMAIL_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + PASSWORD_FIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + CONFIRM_PASSWORD_FIELD_ID).queryTextInputControl().getText()).isEmpty();
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
	public void shouldClickOnRegisterButtonCallRegisterOnController() {
		clickOn("#" + NAME_TEXTFIELD_ID);
		write("Mario");
		clickOn("#" + SURNAME_TEXTFIELD_ID);
		write("Rossi");
		clickOn("#" + EMAIL_TEXTFIELD_ID);
		write("email@email.com");
		clickOn("#" + PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + CONFIRM_PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + REGISTER_BUTTON_ID);
		verify(toDoController).register("Mario", "Rossi", "email@email.com", "password", "password");
		verifyNoMoreInteractions(ignoreStubs(toDoController));
	}
	
	@Test
	public void shouldRenderErrorMakeVisibleErrorTextAndChangeItsText() {
		registrationJavaFxView.renderError(ERRORS.FIELD_EMPTY.getValue());
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(ERRORS.FIELD_EMPTY.getValue());
	}
	
	@Test
	public void shouldResetGUIClearFieldsAndMakeInvisibleErrorText() {
		clickOn("#" + NAME_TEXTFIELD_ID);
		write("Mario");
		clickOn("#" + SURNAME_TEXTFIELD_ID);
		write("Rossi");
		clickOn("#" + EMAIL_TEXTFIELD_ID);
		write("email@email.com");
		clickOn("#" + PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + CONFIRM_PASSWORD_FIELD_ID);
		write("password");
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.USER_ALREADY_FOUND.getValue());
		
		registrationJavaFxView.resetGUI();
		
		assertThat(lookup("#" + NAME_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + SURNAME_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + EMAIL_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + PASSWORD_FIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + CONFIRM_PASSWORD_FIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
	}
	
}
