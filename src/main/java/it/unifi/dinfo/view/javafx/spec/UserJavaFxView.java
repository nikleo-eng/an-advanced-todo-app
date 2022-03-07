package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;

import it.unifi.dinfo.controller.ToDoController;
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
	protected static final String LOGOUT_BUTTON_ID = "USER_LOGOUT_BUTTON_ID";
	protected static final String REFRESH_BUTTON_TEXT = "Refresh";
	protected static final String REFRESH_BUTTON_ID = "USER_REFRESH_BUTTON_ID";
	protected static final String USER_TEXT_ID = "USER_TEXT_ID";
	
	private ListsJavaFxView listsJavaFxView;
	private DetailsJavaFxView detailsJavaFxView;
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	private LoginJavaFxView loginJavaFxView;
	private RegistrationJavaFxView registrationJavaFxView;
	
	private User currentUser;
	private Text currUserText;

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
		currUserText = null;
	}
	
	/* Only for tests */
	protected void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public void userLoggedIn(User currentUser) {
		this.currentUser = currentUser;
		currUserText.setText(currentUser.getName() + " " + currentUser.getSurname());
		loginJavaFxView.resetGUI();
		registrationJavaFxView.resetGUI();
		additionModificationJavaFxView.setCurrentUser(currentUser);
		listsJavaFxView.enableArea();
		detailsJavaFxView.disableArea();
		getToDoController().getAllLists(currentUser.getId());
	}
	
	private void logout() {
		resetGUI();
		listsJavaFxView.resetGUI();
		detailsJavaFxView.resetGUI();
		additionModificationJavaFxView.resetGUI();
		additionModificationJavaFxView.setCurrentUser(null);
		getToDoController().logout();
	}
	
	@Override
	public void resetGUI() {
		currUserText.setText("");
		currentUser = null;	
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
		
		currUserText = new Text("");
		currUserText.setId(USER_TEXT_ID);
		currUserText.setStyle(BOLD_STYLE);
		
		var logoutButton = new Button(LOGOUT_BUTTON_TEXT);
		logoutButton.setId(LOGOUT_BUTTON_ID);
		logoutButton.setPrefWidth(BUTTON_WIDTH);
		logoutButton.setOnAction(ev -> logout());
		
		var refreshButton = new Button(REFRESH_BUTTON_TEXT);
		refreshButton.setId(REFRESH_BUTTON_ID);
		refreshButton.setPrefWidth(BUTTON_WIDTH);
		refreshButton.setOnAction(ev -> refresh());
		
		hBox.getChildren().addAll(currUserText, logoutButton, refreshButton);
		vBox.getChildren().add(hBox);
		return vBox;
	}

}
