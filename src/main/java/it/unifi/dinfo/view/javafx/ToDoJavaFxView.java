package it.unifi.dinfo.view.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ToDoJavaFxView extends Application {
	
	public static String BUTTON_ID = "buttonClickMe";
	public static String BUTTON_PRE_CLICK_TEXT = "click me!";
	public static String BUTTON_POST_CLICK_TEXT = "clicked!";

	@Override
	public void start(Stage stage) throws Exception {
		Button button = new Button(BUTTON_PRE_CLICK_TEXT);
		button.setId(BUTTON_ID);
        button.setOnAction(actionEvent -> {
        	button.setText(BUTTON_POST_CLICK_TEXT);
        	button.setDisable(true);
        });
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button);
        Scene scene = new Scene(stackPane, 500, 500);
        stage.setScene(scene);
        stage.setTitle("An Advanced ToDo App");
        stage.show();
	}
	
}
