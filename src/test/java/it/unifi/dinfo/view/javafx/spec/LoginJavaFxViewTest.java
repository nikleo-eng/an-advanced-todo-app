package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.LoginJavaFxView.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

import it.unifi.dinfo.controller.ToDoController;
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

public class LoginJavaFxViewTest extends ApplicationTest {
	
	@Mock
	private ToDoController toDoController;
	
	private LoginJavaFxView loginJavaFxView;

	@Override
	public void start(Stage stage) throws Exception {
		MockitoAnnotations.openMocks(this);
		loginJavaFxView = new LoginJavaFxView(toDoController);
		
		FlowPane flowPane = new FlowPane();
		Scene scene = new Scene(flowPane, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = loginJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		flowPane.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@Test
	public void shouldViewContainEmailAndPasswordLabelsAndInputsAndLoginButton() {
		Node emailLabelNode = lookup("#" + EMAIL_LABEL_ID).tryQuery().orElse(null);
		assertThat(emailLabelNode).isNotNull();
		assertThat(emailLabelNode).isOfAnyClassIn(Label.class);
		Label emailLabel = (Label) emailLabelNode;
		assertThat(emailLabel.isVisible()).isTrue();
		assertThat(emailLabel.getText()).isEqualTo(EMAIL_LABEL_TEXT);
		
		Node emailTextFieldNode = lookup("#" + EMAIL_TEXTFIELD_ID).tryQuery().orElse(null);
		assertThat(emailTextFieldNode).isNotNull();
		assertThat(emailTextFieldNode).isOfAnyClassIn(TextField.class);
		TextField emailTextField = (TextField) emailTextFieldNode;
		assertThat(emailTextField.isVisible()).isTrue();
		
		Node passwordLabelNode = lookup("#" + PASSWORD_LABEL_ID).tryQuery().orElse(null);
		assertThat(passwordLabelNode).isNotNull();
		assertThat(passwordLabelNode).isOfAnyClassIn(Label.class);
		Label passwordLabel = (Label) passwordLabelNode;
		assertThat(passwordLabel.isVisible()).isTrue();
		assertThat(passwordLabel.getText()).isEqualTo(PASSWORD_LABEL_TEXT);
		
		Node passwordFieldNode = lookup("#" + PASSWORD_FIELD_ID).tryQuery().orElse(null);
		assertThat(passwordFieldNode).isNotNull();
		assertThat(passwordFieldNode).isOfAnyClassIn(PasswordField.class);
		PasswordField passwordField = (PasswordField) passwordFieldNode;
		assertThat(passwordField.isVisible()).isTrue();
		
		Node loginButtonNode = lookup("#" + LOGIN_BUTTON_ID).tryQuery().orElse(null);
		assertThat(loginButtonNode).isNotNull();
		assertThat(loginButtonNode).isOfAnyClassIn(Button.class);
		Button loginButton = (Button) loginButtonNode;
		assertThat(loginButton.isVisible()).isTrue();
		assertThat(loginButton.getText()).isEqualTo(LOGIN_BUTTON_TEXT);
	}
	
	@Test
	public void shouldEmailAndPasswordTextFieldsBeInitiallyEmpty() {
		assertThat(lookup("#" + EMAIL_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + PASSWORD_FIELD_ID).queryTextInputControl().getText()).isEmpty();
	}
	
	@Test
	public void shouldViewContainEmptyErrorText() {
		Node errorTextNode = lookup("#" + ERROR_TEXT_ID).tryQuery().orElse(null);
		assertThat(errorTextNode).isNotNull();
		assertThat(errorTextNode).isOfAnyClassIn(Text.class);
		Text errorText = (Text) errorTextNode;
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
	}
	
	@Test
	public void shouldClickOnLoginButtonCallLoginOnController() {
		clickOn("#" + EMAIL_TEXTFIELD_ID);
		write("email@email.com");
		clickOn("#" + PASSWORD_FIELD_ID);
		write("password");
		clickOn("#" + LOGIN_BUTTON_ID);
		verify(toDoController).login("email@email.com", "password");
		verifyNoMoreInteractions(ignoreStubs(toDoController));
	}
	
	@Test
	public void shouldRenderErrorMakeVisibleErrorTextAndChangeItsText() {
		loginJavaFxView.renderError(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(ERRORS.EMAIL_PASSWORD_EMPTY.getValue());
	}
	
	@Test
	public void shouldResetGUIClearFieldsAndMakeInvisibleErrorText() {
		clickOn("#" + EMAIL_TEXTFIELD_ID);
		write("email@email.com");
		clickOn("#" + PASSWORD_FIELD_ID);
		write("password");
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.USER_NOT_FOUND.getValue());
		
		loginJavaFxView.resetGUI();
		
		assertThat(lookup("#" + EMAIL_TEXTFIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(lookup("#" + PASSWORD_FIELD_ID).queryTextInputControl().getText()).isEmpty();
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
	}
	
}
