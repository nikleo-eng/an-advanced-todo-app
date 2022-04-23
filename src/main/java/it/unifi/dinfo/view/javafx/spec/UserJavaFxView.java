package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;

import java.text.SimpleDateFormat;
import java.util.Locale;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.UserView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserJavaFxView extends BaseJavaFxView implements UserView {
	
	protected static final String LOGOUT_BUTTON_TEXT = "Logout";
	public static final String LOGOUT_BUTTON_ID = "USER_LOGOUT_BUTTON_ID";
	protected static final String REFRESH_BUTTON_TEXT = "Refresh";
	public static final String REFRESH_BUTTON_ID = "USER_REFRESH_BUTTON_ID";
	public static final String USER_TEXT_ID = "USER_TEXT_ID";
	public static final String LOG_TEXT_ID = "LOG_TEXT_ID";
	
	public static final String LOG_STARTING_TEXT = "Last Login: ";
	public static final String LOG_NOT_AVAILABLE_TEXT = "N.A.";
	protected static final String SDF_PATTERN = "dd/MM/yyyy HH:mm:ss z";
	
	private static final String ITALIC_STYLE = "-fx-font-style: italic;";
	
	private ListsJavaFxView listsJavaFxView;
	private DetailsJavaFxView detailsJavaFxView;
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	private LoginJavaFxView loginJavaFxView;
	private RegistrationJavaFxView registrationJavaFxView;
	
	private User currentUser;
	private Log currentLog;
	
	private Text userText;
	private Text logText;
	private Button logoutButton;
	private Button refreshButton;

	public UserJavaFxView(ToDoController toDoController, ListsJavaFxView listsJavaFxView, 
			DetailsJavaFxView detailsJavaFxView, 
			AdditionModificationJavaFxView additionModificationJavaFxView, 
			LoginJavaFxView loginJavaFxView, RegistrationJavaFxView registrationJavaFxView) {
		super(toDoController);
		this.listsJavaFxView = listsJavaFxView;
		this.detailsJavaFxView = detailsJavaFxView;
		this.additionModificationJavaFxView = additionModificationJavaFxView;
		this.loginJavaFxView = loginJavaFxView;
		this.registrationJavaFxView = registrationJavaFxView;
		
		currentUser = null;
		currentLog = null;
		
		userText = null;
		logText = null;
		logoutButton = null;
		refreshButton = null;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}
	
	private void setUserText(User user) {
		userText.setText(user.getName() + " " + user.getSurname());
	}
	
	public void setCurrentLog(Log log) {
		currentLog = log;
	}
	
	private void setLogText(Log log) {
		SimpleDateFormat sdf = new SimpleDateFormat(SDF_PATTERN, Locale.ITALIAN);
		String formattedDate = log != null ? sdf.format(log.getIn()) : LOG_NOT_AVAILABLE_TEXT;
		logText.setText(logText.getText() + formattedDate);
	}

	public User getCurrentUser() {
		return currentUser;
	}
	
	/* Only for tests */
	public Log getCurrentLog() {
		return currentLog;
	}

	@Override
	public void userLoggedIn(User user, Log log, Log lastLog) {
		loginJavaFxView.resetGUI();
		registrationJavaFxView.resetGUI();
		listsJavaFxView.enableArea();
		detailsJavaFxView.disableArea();
		enableArea();
		
		currentUser = user;
		setUserText(user);
		getToDoController().getAllLists(user.getId());
		
		currentLog = log;
		setLogText(lastLog);
	}
	
	private void enableArea() {
		userText.setVisible(true);
		logText.setVisible(true);
		logoutButton.setVisible(true);
		refreshButton.setDisable(false);
	}
	
	public void logout(boolean stop) {
		resetGUI();
		listsJavaFxView.resetGUI();
		detailsJavaFxView.resetGUI();
		additionModificationJavaFxView.resetGUI();
		getToDoController().logout(currentLog, stop);
	}
	
	@Override
	public void resetGUI() {
		userText.setText("");
		logText.setText(LOG_STARTING_TEXT);
		userText.setVisible(false);
		logText.setVisible(false);
		logoutButton.setVisible(false);
		refreshButton.setDisable(true);
	}
	
	private void refresh() {
		listsJavaFxView.clearAll();
		detailsJavaFxView.clearAll();
		listsJavaFxView.enableArea();
		detailsJavaFxView.disableArea();
		getToDoController().getAllLists(currentUser.getId());
	}
	
	@Override
	public VBox createGUI(double width, double height) {
		var vBox = new VBox();
		var hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPrefSize(width, height);
		hBox.setSpacing(10);
		hBox.setStyle("-fx-border-color: transparent; -fx-border-insets: 4; -fx-border-width: 2;");
		
		userText = new Text("");
		userText.setId(USER_TEXT_ID);
		userText.setStyle(BOLD_STYLE);
		userText.setVisible(false);
		
		logText = new Text(LOG_STARTING_TEXT);
		logText.setId(LOG_TEXT_ID);
		logText.setStyle(ITALIC_STYLE);
		logText.setVisible(false);
		
		logoutButton = new Button(LOGOUT_BUTTON_TEXT);
		logoutButton.setId(LOGOUT_BUTTON_ID);
		logoutButton.setPrefWidth(BUTTON_WIDTH);
		logoutButton.setOnAction(ev -> logout(false));
		logoutButton.setVisible(false);
		
		refreshButton = new Button(REFRESH_BUTTON_TEXT);
		refreshButton.setId(REFRESH_BUTTON_ID);
		refreshButton.setPrefWidth(BUTTON_WIDTH);
		refreshButton.setOnAction(ev -> refresh());
		refreshButton.setDisable(true);
		
		hBox.getChildren().addAll(userText, logText, logoutButton, refreshButton);
		vBox.getChildren().add(hBox);
		return vBox;
	}

}
