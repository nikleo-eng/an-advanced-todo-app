package it.unifi.dinfo.view.javafx.spec;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.LoginView;
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

public class LoginJavaFxView extends BaseJavaFxView implements LoginView {

	private Text errorText;
	private TextField emailTextField;
	private PasswordField passwordField;
	
	protected static final String EMAIL_LABEL_ID = "LOGIN_EMAIL_LABEL_ID";
	protected static final String EMAIL_TEXTFIELD_ID = "LOGIN_EMAIL_TEXTFIELD_ID";
	protected static final String PASSWORD_LABEL_ID = "LOGIN_PASSWORD_LABEL_ID";
	protected static final String PASSWORD_FIELD_ID = "LOGIN_PASSWORD_FIELD_ID";
	protected static final String LOGIN_BUTTON_ID = "LOGIN_BUTTON_ID";
	protected static final String ERROR_TEXT_ID = "LOGIN_ERROR_TEXT_ID";
	
	protected static final String EMAIL_LABEL_TEXT = "Email";
	protected static final String PASSWORD_LABEL_TEXT = "Password";
	protected static final String LOGIN_BUTTON_TEXT = "Login";

	public LoginJavaFxView(ToDoController toDoController) {
		super(toDoController);
		errorText = null;
		emailTextField = null;
		passwordField = null;
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
		var vBox = new VBox();
		vBox.setStyle(ToDoJavaFxView.BORDER_STYLE);
		vBox.setPrefSize(width, height);
		vBox.setAlignment(Pos.CENTER);
		
		var gridPane = new GridPane();
		gridPane.setPrefWidth(vBox.getPrefWidth());
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(10);
		
		var emailLabel = new Label(EMAIL_LABEL_TEXT);
		emailLabel.setId(EMAIL_LABEL_ID);
		emailTextField = new TextField();
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
		
		var loginButtonHBox = new HBox();
		loginButtonHBox.setAlignment(Pos.CENTER);
		var loginButton = new Button(LOGIN_BUTTON_TEXT);
		loginButton.setId(LOGIN_BUTTON_ID);
		loginButton.setPrefWidth(ToDoJavaFxView.BUTTON_WIDTH);
		loginButtonHBox.getChildren().add(loginButton);
		loginButton.setOnAction(ev -> getToDoController().login(
				emailTextField.getText(), passwordField.getText()));
		gridPane.addColumn(0, loginButtonHBox);
		GridPane.setColumnSpan(loginButtonHBox, 2);
		
		var loginErrorHBox = new HBox();
		loginErrorHBox.setAlignment(Pos.CENTER);
		errorText = new Text("");
		errorText.setId(ERROR_TEXT_ID);
		errorText.setVisible(false);
		errorText.setStyle(ToDoJavaFxView.BOLD_STYLE);
		errorText.setFill(Color.RED);
		loginErrorHBox.getChildren().add(errorText);
		gridPane.addColumn(0, loginErrorHBox);
		GridPane.setColumnSpan(loginErrorHBox, 2);
		
		vBox.getChildren().addAll(gridPane);
		return vBox;
	}

}
