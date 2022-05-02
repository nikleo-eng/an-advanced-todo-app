package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.spec.util.LoginRegistrationGUI.*;

import com.google.inject.Inject;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.LoginView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginJavaFxView extends BaseJavaFxView implements LoginView {

	private Text errorText;
	private TextField emailTextField;
	private PasswordField passwordField;
	
	public static final String EMAIL_LABEL_ID = "LOGIN_EMAIL_LABEL_ID";
	public static final String EMAIL_TEXTFIELD_ID = "LOGIN_EMAIL_TEXTFIELD_ID";
	public static final String PASSWORD_LABEL_ID = "LOGIN_PASSWORD_LABEL_ID";
	public static final String PASSWORD_FIELD_ID = "LOGIN_PASSWORD_FIELD_ID";
	public static final String LOGIN_BUTTON_ID = "LOGIN_BUTTON_ID";
	public static final String ERROR_TEXT_ID = "LOGIN_ERROR_TEXT_ID";
	
	protected static final String EMAIL_LABEL_TEXT = "Email";
	protected static final String PASSWORD_LABEL_TEXT = "Password";
	protected static final String LOGIN_BUTTON_TEXT = "Login";

	@Inject
	public LoginJavaFxView(ToDoController toDoController) {
		super(toDoController);
	}

	@Override
	public void renderError(String error) {
		errorText.setText(error);
		errorText.setVisible(true);
	}
	
	@Override
	public void resetGUI() {
		errorText.setText("");
		errorText.setVisible(false);
		emailTextField.clear();
		passwordField.clear();
	}

	@Override
	public VBox createGUI(double width, double height) {
		var vBox = createVBox(width, height);
		var gridPane = createGridPane(vBox.getPrefWidth());
		
		emailTextField = createTextField(EMAIL_TEXTFIELD_ID);
		var emailLabel = createLabel(emailTextField, EMAIL_LABEL_ID, EMAIL_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(emailLabel, emailTextField, gridPane);
		
		passwordField = createPasswordField(PASSWORD_FIELD_ID);
		var passwordLabel = createLabel(passwordField, PASSWORD_LABEL_ID, PASSWORD_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(passwordLabel, passwordField, gridPane);
		
		var loginButton = createButton(LOGIN_BUTTON_ID, LOGIN_BUTTON_TEXT, 
				ev -> getToDoController().login(emailTextField.getText(), passwordField.getText()));
		var loginButtonHBox = createHBox(loginButton);
		gridPane = create2ColumnsRowGridPane(loginButtonHBox, null, gridPane);
		
		errorText = createErrorText(ERROR_TEXT_ID);
		var loginErrorHBox = createHBox(errorText);
		gridPane = create2ColumnsRowGridPane(loginErrorHBox, null, gridPane);
		
		vBox.getChildren().addAll(gridPane);
		return vBox;
	}

}
