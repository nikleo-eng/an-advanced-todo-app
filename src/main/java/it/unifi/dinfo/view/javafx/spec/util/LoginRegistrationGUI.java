package it.unifi.dinfo.view.javafx.spec.util;

import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginRegistrationGUI {
	
	private LoginRegistrationGUI() {}

	public static VBox createVBox(double width, double height) {
		var vBox = new VBox();
		vBox.setPrefSize(width, height);
		vBox.setAlignment(Pos.CENTER);
		vBox.setStyle(ToDoJavaFxView.BORDER_STYLE);
		return vBox;
	}
	
	public static GridPane createGridPane(double width) {
		var gridPane = new GridPane();
		gridPane.setPrefWidth(width);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(10);
		return gridPane;
	}
	
	public static TextField createTextField(String id) {
		var textField = new TextField();
		textField.setId(id);
		textField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		return textField;
	}
	
	public static PasswordField createPasswordField(String id) {
		var passwordField = new PasswordField();
		passwordField.setId(id);
		passwordField.setPrefWidth(ToDoJavaFxView.TEXT_FIELD_WIDTH);
		return passwordField;
	}
	
	public static Label createLabel(Node labelFor, String id, String text) {
		var label = new Label(text);
		label.setId(id);
		label.setLabelFor(labelFor);
		return label;
	}
	
	public static Text createErrorText(String id) {
		var text = new Text("");
		text.setId(id);
		text.setVisible(false);
		text.setStyle(ToDoJavaFxView.BOLD_STYLE);
		text.setFill(Color.RED);
		return text;
	}
	
	public static Button createButton(String id, String text, EventHandler<ActionEvent> onAction) {
		var button = new Button(text);
		button.setId(id);
		button.setPrefWidth(ToDoJavaFxView.BUTTON_WIDTH);
		button.setOnAction(onAction);
		return button;
	}
	
	public static GridPane create2ColumnsRowGridPane(Node node1, Node node2, GridPane gridPane) {
		if (node2 != null) {
			gridPane.addColumn(0, node1);
			gridPane.addColumn(1, node2);
		} else {
			gridPane.addColumn(0, node1);
			GridPane.setColumnSpan(node1, 2);
		}
		return gridPane;
	}
	
	public static HBox createHBox(Node child) {
		var hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(child);
		return hBox;
	}
	
}
