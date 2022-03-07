package it.unifi.dinfo.view.javafx.spec;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.RegistrationView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RegistrationJavaFxView extends BaseJavaFxView implements RegistrationView {

	private Text errorText;
	private TextField nameTextField;
	private TextField surnameTextField;
	private TextField emailTextField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
	
	protected static final String EMAIL_LABEL_ID = "REGISTRATION_EMAIL_LABEL_ID";
	protected static final String EMAIL_TEXTFIELD_ID = "REGISTRATION_EMAIL_TEXTFIELD_ID";
	protected static final String NAME_LABEL_ID = "REGISTRATION_NAME_LABEL_ID";
	protected static final String NAME_TEXTFIELD_ID = "REGISTRATION_NAME_TEXTFIELD_ID";
	protected static final String SURNAME_LABEL_ID = "REGISTRATION_SURNAME_LABEL_ID";
	protected static final String SURNAME_TEXTFIELD_ID = "REGISTRATION_SURNAME_TEXTFIELD_ID";
	protected static final String PASSWORD_LABEL_ID = "REGISTRATION_PASSWORD_LABEL_ID";
	protected static final String PASSWORD_FIELD_ID = "REGISTRATION_PASSWORD_FIELD_ID";
	protected static final String CONFIRM_PASSWORD_LABEL_ID = "REGISTRATION_CONFIRM_PASSWORD_LABEL_ID";
	protected static final String CONFIRM_PASSWORD_FIELD_ID = "REGISTRATION_CONFIRM_PASSWORD_FIELD_ID";
	protected static final String REGISTER_BUTTON_ID = "REGISTRATION_BUTTON_ID";
	protected static final String ERROR_TEXT_ID = "REGISTRATION_ERROR_TEXT_ID";
	
	protected static final String EMAIL_LABEL_TEXT = "Email";
	protected static final String NAME_LABEL_TEXT = "Name";
	protected static final String SURNAME_LABEL_TEXT = "Surname";
	protected static final String PASSWORD_LABEL_TEXT = "Password";
	protected static final String CONFIRM_PASSWORD_LABEL_TEXT = "Confirm Password";
	protected static final String REGISTER_BUTTON_TEXT = "Register";

	public RegistrationJavaFxView(ToDoController toDoController) {
		super(toDoController);
		errorText = null;
		nameTextField = null;
		surnameTextField = null;
		emailTextField = null;
		passwordField = null;
		confirmPasswordField = null;
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
		var vBox = new VBox();
		vBox.setPrefSize(width, height);
		vBox.setAlignment(Pos.CENTER);
		vBox.setStyle(ToDoJavaFxView.BORDER_STYLE);
		
		var gridPane = new GridPane();
		gridPane.setPrefWidth(vBox.getPrefWidth());
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(10);
		
		var nameLabel = new Label(NAME_LABEL_TEXT);
		nameLabel.setId(NAME_LABEL_ID);
		nameTextField = new TextField();
		nameTextField.setId(NAME_TEXTFIELD_ID);
		nameTextField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		nameLabel.setLabelFor(nameTextField);
		gridPane.addColumn(0, nameLabel);
		gridPane.addColumn(1, nameTextField);
		
		var surnameLabel = new Label(SURNAME_LABEL_TEXT);
		surnameLabel.setId(SURNAME_LABEL_ID);
		surnameTextField =  new TextField();
		surnameTextField.setId(SURNAME_TEXTFIELD_ID);
		surnameTextField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		surnameLabel.setLabelFor(surnameTextField);
		gridPane.addColumn(0, surnameLabel);
		gridPane.addColumn(1, surnameTextField);
		
		var emailLabel = new Label(EMAIL_LABEL_TEXT);
		emailLabel.setId(EMAIL_LABEL_ID);
		emailTextField =  new TextField();
		emailTextField.setId(EMAIL_TEXTFIELD_ID);
		emailTextField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		emailLabel.setLabelFor(emailTextField);
		gridPane.addColumn(0, emailLabel);
		gridPane.addColumn(1, emailTextField);
		
		var passwordLabel = new Label(PASSWORD_LABEL_TEXT);
		passwordLabel.setId(PASSWORD_LABEL_ID);
		passwordField = new PasswordField();
		passwordField.setId(PASSWORD_FIELD_ID);
		passwordField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		passwordLabel.setLabelFor(passwordField);
		gridPane.addColumn(0, passwordLabel);
		gridPane.addColumn(1, passwordField);
		
		var confirmPasswordLabel = new Label(CONFIRM_PASSWORD_LABEL_TEXT);
		confirmPasswordLabel.setId(CONFIRM_PASSWORD_LABEL_ID);
		confirmPasswordField = new PasswordField();
		confirmPasswordField.setId(CONFIRM_PASSWORD_FIELD_ID);
		confirmPasswordField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		confirmPasswordLabel.setLabelFor(confirmPasswordField);
		gridPane.addColumn(0, confirmPasswordLabel);
		gridPane.addColumn(1, confirmPasswordField);
		
		var registerHBox = new HBox();
		registerHBox.setAlignment(Pos.CENTER);
		var registerButton = new Button(REGISTER_BUTTON_TEXT);
		registerButton.setId(REGISTER_BUTTON_ID);
		registerButton.setPrefWidth(ToDoJavaFxView.BUTTON_WIDTH);
		registerButton.setOnAction(ev -> getToDoController().register(nameTextField.getText(), 
				surnameTextField.getText(), emailTextField.getText(), passwordField.getText(), 
				confirmPasswordField.getText()));
		registerHBox.getChildren().add(registerButton);
		gridPane.addColumn(0, registerHBox);
		GridPane.setColumnSpan(registerHBox, 2);
		
		var registerErrorHBox = new HBox();
		registerErrorHBox.setAlignment(Pos.CENTER);
		errorText = new Text("");
		errorText.setId(ERROR_TEXT_ID);
		errorText.setVisible(false);
		errorText.setStyle(ToDoJavaFxView.BOLD_STYLE);
		errorText.setFill(Color.RED);
		registerErrorHBox.getChildren().add(errorText);
		gridPane.addColumn(0, registerErrorHBox);
		GridPane.setColumnSpan(registerErrorHBox, 2);
		
		vBox.getChildren().addAll(gridPane);
		return vBox;
	}

}
