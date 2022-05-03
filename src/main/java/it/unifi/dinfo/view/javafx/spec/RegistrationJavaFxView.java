package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.spec.util.LoginRegistrationGUI.*;

import com.google.inject.Inject;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.RegistrationView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RegistrationJavaFxView extends BaseJavaFxView implements RegistrationView {

	private Text errorText;
	private TextField nameTextField;
	private TextField surnameTextField;
	private TextField emailTextField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
	
	public static final String EMAIL_LABEL_ID = "REGISTRATION_EMAIL_LABEL_ID";
	public static final String EMAIL_TEXTFIELD_ID = "REGISTRATION_EMAIL_TEXTFIELD_ID";
	public static final String NAME_LABEL_ID = "REGISTRATION_NAME_LABEL_ID";
	public static final String NAME_TEXTFIELD_ID = "REGISTRATION_NAME_TEXTFIELD_ID";
	public static final String SURNAME_LABEL_ID = "REGISTRATION_SURNAME_LABEL_ID";
	public static final String SURNAME_TEXTFIELD_ID = "REGISTRATION_SURNAME_TEXTFIELD_ID";
	public static final String PASSWORD_LABEL_ID = "REGISTRATION_PASSWORD_LABEL_ID";
	public static final String PASSWORD_FIELD_ID = "REGISTRATION_PASSWORD_FIELD_ID";
	public static final String CONFIRM_PASSWORD_LABEL_ID = "REGISTRATION_CONFIRM_PASSWORD_LABEL_ID";
	public static final String CONFIRM_PASSWORD_FIELD_ID = "REGISTRATION_CONFIRM_PASSWORD_FIELD_ID";
	public static final String REGISTER_BUTTON_ID = "REGISTRATION_BUTTON_ID";
	public static final String ERROR_TEXT_ID = "REGISTRATION_ERROR_TEXT_ID";
	
	protected static final String EMAIL_LABEL_TEXT = "Email";
	protected static final String NAME_LABEL_TEXT = "Name";
	protected static final String SURNAME_LABEL_TEXT = "Surname";
	protected static final String PASSWORD_LABEL_TEXT = "Password";
	protected static final String CONFIRM_PASSWORD_LABEL_TEXT = "Confirm Password";
	protected static final String REGISTER_BUTTON_TEXT = "Register";

	@Inject
	public RegistrationJavaFxView(ToDoController toDoController) {
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
		nameTextField.clear();
		surnameTextField.clear();
		emailTextField.clear();
		passwordField.clear();
		confirmPasswordField.clear();
	}

	@Override
	public VBox createGUI(double width, double height) {
		var vBox = createVBox(width, height);
		var gridPane = createGridPane(vBox.getPrefWidth());
		
		nameTextField = createTextField(NAME_TEXTFIELD_ID);
		var nameLabel = createLabel(nameTextField, NAME_LABEL_ID, NAME_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(nameLabel, nameTextField, gridPane);
		
		surnameTextField = createTextField(SURNAME_TEXTFIELD_ID);
		var surnameLabel = createLabel(surnameTextField, SURNAME_LABEL_ID, SURNAME_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(surnameLabel, surnameTextField, gridPane);
		
		emailTextField = createTextField(EMAIL_TEXTFIELD_ID);
		var emailLabel = createLabel(emailTextField, EMAIL_LABEL_ID, EMAIL_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(emailLabel, emailTextField, gridPane);
		
		passwordField = createPasswordField(PASSWORD_FIELD_ID);
		var passwordLabel = createLabel(passwordField, PASSWORD_LABEL_ID, PASSWORD_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(passwordLabel, passwordField, gridPane);
		
		confirmPasswordField = createPasswordField(CONFIRM_PASSWORD_FIELD_ID);
		var confirmPasswordLabel = createLabel(confirmPasswordField, CONFIRM_PASSWORD_LABEL_ID, 
				CONFIRM_PASSWORD_LABEL_TEXT);
		gridPane = create2ColumnsRowGridPane(confirmPasswordLabel, confirmPasswordField, gridPane);
		
		var registerButton = createButton(REGISTER_BUTTON_ID, REGISTER_BUTTON_TEXT, 
				ev -> getToDoController().register(nameTextField.getText(), surnameTextField.getText(), 
						emailTextField.getText(), passwordField.getText(), 
						confirmPasswordField.getText()));
		var registerButtonHBox = createHBox(registerButton);
		gridPane = create2ColumnsRowGridPane(registerButtonHBox, null, gridPane);
		
		errorText = createErrorText(ERROR_TEXT_ID);
		var loginErrorHBox = createHBox(errorText);
		gridPane = create2ColumnsRowGridPane(loginErrorHBox, null, gridPane);
		
		vBox.getChildren().addAll(gridPane);
		return vBox;
	}

}
