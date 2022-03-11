package it.unifi.dinfo.view.javafx;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unifi.dinfo.app.main.ToDoAppMain;
import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.Log;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.repository.mysql.ToDoMySqlRepository;
import it.unifi.dinfo.view.ToDoView;
import it.unifi.dinfo.view.javafx.spec.AdditionModificationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.ListsJavaFxView;
import it.unifi.dinfo.view.javafx.spec.LoginJavaFxView;
import it.unifi.dinfo.view.javafx.spec.RegistrationJavaFxView;
import it.unifi.dinfo.view.javafx.spec.UserJavaFxView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToDoJavaFxView extends Application implements ToDoView {
	
	public static final String STAGE_TITLE = "An Advanced ToDo App";
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static final double SCENE_WIDTH = 1200;
	public static final double SCENE_HEIGHT = 800;
	public static final String BORDER_STYLE = "-fx-border-color: black; "
			+ "-fx-border-insets: 4; -fx-border-width: 2;";
	public static final String BOLD_STYLE = "-fx-font-weight: bold;";
	public static final double BUTTON_WIDTH = 100;
	public static final double HEADER_FOOTER_HEIGHT = 50;
	public static final double TEXT_FIELD_WIDTH = 300;
	public static final double BUTTON_ICON_WIDTH = 48;
	public static final String SVG_CONTENT_MODIFY_ICON = 
			"M 19.171875 2 C 18.448125 2 17.724375 2.275625 17.171875 2.828125 L 16 4 L 20 8 L "
			+ "21.171875 6.828125 C 22.275875 5.724125 22.275875 3.933125 21.171875 2.828125 C "
			+ "20.619375 2.275625 19.895625 2 19.171875 2 z M 14.5 5.5 L 3 17 L 3 21 L 7 21 L 18.5 "
			+ "9.5 L 14.5 5.5 z";
	public static final String SVG_CONTENT_DELETE_ICON =
			"M 10.806641 2 C 10.289641 2 9.7956875 2.2043125 9.4296875 2.5703125 L 9 3 L 4 3 "
			+ "A 1.0001 1.0001 0 1 0 4 5 L 20 5 A 1.0001 1.0001 0 1 0 20 3 L 15 3 L 14.570312 "
			+ "2.5703125 C 14.205312 2.2043125 13.710359 2 13.193359 2 L 10.806641 2 z M 4.3652344 "
			+ "7 L 5.8925781 20.263672 C 6.0245781 21.253672 6.877 22 7.875 22 L 16.123047 22 C "
			+ "17.121047 22 17.974422 21.254859 18.107422 20.255859 L 19.634766 7 L 4.3652344 7 z";
	
	private ToDoController toDoController;
	private LoginJavaFxView loginJavaFxView;
	private RegistrationJavaFxView registrationJavaFxView;
	private ListsJavaFxView listsJavaFxView;
	private DetailsJavaFxView detailsJavaFxView;
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	private UserJavaFxView userJavaFxView;
	
	private Stage stage;
	private FlowPane appRoot;
	private FlowPane userRoot;
	
	public ToDoJavaFxView() {
		toDoController = null;
		loginJavaFxView = null;
		registrationJavaFxView = null;
		listsJavaFxView = null;
		detailsJavaFxView = null;
		additionModificationJavaFxView = null;
		userJavaFxView = null;
		stage = null;
		appRoot = null;
		userRoot = null;
	}
	
	@Override
	public void init() throws Exception {
		Map<String, String> mainArgs = getParameters().getNamed();
		String host = mainArgs.get(ToDoAppMain.MY_SQL_HOST_OPNAME);
		String port = mainArgs.get(ToDoAppMain.MY_SQL_PORT_OPNAME);
		String dbName = mainArgs.get(ToDoAppMain.MY_SQL_DB_NAME_OPNAME);
		String user = mainArgs.get(ToDoAppMain.MY_SQL_USER_OPNAME);
		String pass = mainArgs.get(ToDoAppMain.MY_SQL_PASS_OPNAME);
		
		var toDoMySqlRepository = new ToDoMySqlRepository(host, port, dbName, user, pass);
		toDoController = new ToDoController(this, toDoMySqlRepository);
		
		loginJavaFxView = new LoginJavaFxView(toDoController);
		registrationJavaFxView = new RegistrationJavaFxView(toDoController);
		
		listsJavaFxView = new ListsJavaFxView(toDoController);
		detailsJavaFxView = new DetailsJavaFxView(listsJavaFxView, toDoController);
		additionModificationJavaFxView = new AdditionModificationJavaFxView(listsJavaFxView, 
				detailsJavaFxView, toDoController);
		listsJavaFxView.setAdditionModificationJavaFxView(additionModificationJavaFxView);
		listsJavaFxView.setDetailsJavaFxView(detailsJavaFxView);
		detailsJavaFxView.setAdditionModificationJavaFxView(additionModificationJavaFxView);
		
		userJavaFxView = new UserJavaFxView(toDoController, listsJavaFxView, detailsJavaFxView, 
				additionModificationJavaFxView, loginJavaFxView, registrationJavaFxView);
		
		LOGGER.info("Application Initialized");
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		userRoot = new FlowPane();
		appRoot = new FlowPane();
		
		var scene = new Scene(userRoot, SCENE_WIDTH, SCENE_HEIGHT);
		
		var loginVBox = loginJavaFxView.createGUI(scene.getWidth() / 2, scene.getHeight());
		var registrationVBox = registrationJavaFxView.createGUI(scene.getWidth() / 2, 
				scene.getHeight());
		userRoot.getChildren().addAll(loginVBox, registrationVBox);
		
		var appVBox1 = new VBox();
		appVBox1.setPrefSize(scene.getWidth() / 2, scene.getHeight());
		var appVBox2 = new VBox();
		appVBox2.setPrefSize(scene.getWidth() / 2, scene.getHeight());
		
		var listVBox = listsJavaFxView.createGUI(appVBox1.getPrefWidth(), 
				appVBox1.getPrefHeight() - 300);
		var additionModificationVBox = additionModificationJavaFxView.createGUI(
				appVBox1.getPrefWidth(), 300);
		appVBox1.getChildren().addAll(listVBox, additionModificationVBox);
		
		var userVBox = userJavaFxView.createGUI(appVBox2.getPrefWidth(), HEADER_FOOTER_HEIGHT);
		var detailVBox = detailsJavaFxView.createGUI(appVBox2.getPrefWidth(), 
				appVBox2.getPrefHeight() - HEADER_FOOTER_HEIGHT);
		appVBox2.getChildren().addAll(userVBox, detailVBox);
		appRoot.getChildren().addAll(appVBox1, appVBox2);
		
		stage.setScene(scene);
		stage.setTitle(STAGE_TITLE);
		stage.show();
		
		LOGGER.info("Application Started");
	}
	
	@Override
	public void stop() throws Exception {
		if (userJavaFxView.getCurrentUser() != null) {
			userJavaFxView.logout(true);
		}
		
		LOGGER.info("Application Stopped");
    }

	@Override
	public void addList(List list) {
		listsJavaFxView.add(list);
	}

	@Override
	public void addDetail(Detail detail) {
		detailsJavaFxView.add(detail);
	}

	@Override
	public void saveList(List list) {
		listsJavaFxView.save(list);
	}

	@Override
	public void saveDetail(Detail detail) {
		detailsJavaFxView.save(detail);
	}

	@Override
	public void renderAdditionModificationError(String error) {
		additionModificationJavaFxView.renderError(error);
	}

	@Override
	public void showAllDetails(Set<Detail> details) {
		detailsJavaFxView.showAll(details);
	}

	@Override
	public void deleteDetail(Detail detail) {
		detailsJavaFxView.delete(detail);
	}

	@Override
	public void showAllLists(Set<List> lists) {
		listsJavaFxView.showAll(lists);
	}

	@Override
	public void deleteList(List list) {
		listsJavaFxView.delete(list);
	}
	
	@Override
	public void renderLoginError(String error) {
		loginJavaFxView.renderError(error);
	}

	@Override
	public void renderRegistrationError(String error) {
		registrationJavaFxView.renderError(error);
	}

	@Override
	public void userLoggedIn(User user, Log log, Log lastLog) {
		userJavaFxView.userLoggedIn(user, log, lastLog);
		additionModificationJavaFxView.setCurrentUser(user);
		stage.getScene().setRoot(appRoot);
	}
	
	@Override
	public void userLoggedOut() {
		userJavaFxView.setCurrentLog(null);
		userJavaFxView.setCurrentUser(null);
		additionModificationJavaFxView.setCurrentUser(null);
		stage.getScene().setRoot(userRoot);
	}
	
	@Override
	public void renderListsError(String error) {
		listsJavaFxView.renderError(error);
	}

	@Override
	public void renderDetailsError(String error) {
		detailsJavaFxView.renderError(error);
	}

	/* Only for tests */
	protected void setToDoController(ToDoController toDoController) {
		this.toDoController = toDoController;
	}

	/* Only for tests */
	protected void setLoginJavaFxView(LoginJavaFxView loginJavaFxView) {
		this.loginJavaFxView = loginJavaFxView;
	}

	/* Only for tests */
	protected void setRegistrationJavaFxView(RegistrationJavaFxView registrationJavaFxView) {
		this.registrationJavaFxView = registrationJavaFxView;
	}

	/* Only for tests */
	protected void setListsJavaFxView(ListsJavaFxView listsJavaFxView) {
		this.listsJavaFxView = listsJavaFxView;
	}

	/* Only for tests */
	protected void setDetailsJavaFxView(DetailsJavaFxView detailsJavaFxView) {
		this.detailsJavaFxView = detailsJavaFxView;
	}

	/* Only for tests */
	protected void setAdditionModificationJavaFxView(AdditionModificationJavaFxView additionModificationJavaFxView) {
		this.additionModificationJavaFxView = additionModificationJavaFxView;
	}

	/* Only for tests */
	protected void setUserJavaFxView(UserJavaFxView userJavaFxView) {
		this.userJavaFxView = userJavaFxView;
	}

	/* Only for tests */
	protected FlowPane getAppRoot() {
		return appRoot;
	}

	/* Only for tests */
	protected FlowPane getUserRoot() {
		return userRoot;
	}
	
}
