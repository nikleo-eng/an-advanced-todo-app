package it.unifi.dinfo.view.javafx.spec.util;

import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ListsDetailsGUI {
	
	private ListsDetailsGUI() {}

	public static VBox createVBox(double width, double height) {
		var vBox = new VBox();
		vBox.setPrefSize(width, height);
		vBox.setStyle(ToDoJavaFxView.BORDER_STYLE);
		return vBox;
	}
	
	public static Text createText(String text) {
		var textObj = new Text(text);
		textObj.setStyle(ToDoJavaFxView.BOLD_STYLE);
		return textObj;
	}
	
	public static Text createErrorText(String id) {
		Text text = new Text("");
		text.setId(id);
		text.setStyle(ToDoJavaFxView.BOLD_STYLE);
		text.setFill(Color.RED);
		text.setVisible(false);
		return text;
	}
	
	public static Button createButton(String id, String text, EventHandler<ActionEvent> onAction) {
		var button = new Button(text);
		button.setId(id);
		button.setDisable(true);
		button.setPrefWidth(ToDoJavaFxView.BUTTON_WIDTH);
		button.setOnAction(onAction);
		return button;
	}
	
	public static HBox createHBox(Node child, double width) {
		var hBox = new HBox();
		hBox.setPrefSize(width, ToDoJavaFxView.HEADER_FOOTER_HEIGHT);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(child);
		return hBox;
	}
	
	public static <T> ListView<T> createListView(String id, double width, double height, 
			Callback<ListView<T>, ListCell<T>> cellFactory) {
		ListView<T> listView = new ListView<>();
		listView.setId(id);
		listView.setDisable(true);
		listView.setPrefSize(width, height - (3 * ToDoJavaFxView.HEADER_FOOTER_HEIGHT));
		listView.setCellFactory(cellFactory);
		return listView;
	}
	
	public static Button createCellSvgButton(String id, EventHandler<ActionEvent> onAction, 
			String content) {
		var button = new Button();
		button.setId(id);
		var svgPath = new SVGPath();
		svgPath.setContent(content);
		button.setGraphic(svgPath);
		button.setAlignment(Pos.CENTER);
		button.setPrefWidth(ToDoJavaFxView.BUTTON_ICON_WIDTH);
		button.setOnAction(onAction);
		return button;
	}
	
	public static HBox createCellLabelHBox(String text, String id) {
		var label = new Label(text);
		label.setId(id);
		label.setWrapText(true);
		var hBox = new HBox();
		hBox.setMinHeight(50);
		hBox.getChildren().add(label);
		hBox.setAlignment(Pos.CENTER_LEFT);
		return hBox;
	}
	
	public static HBox createCellCheckBoxLabelHBox(String checkBoxId, boolean checkBoxSelected, 
			EventHandler<ActionEvent> checkBoxOnAction, String labelId, String labelText, 
			boolean labelDisable) {
		var checkBox = new CheckBox();
		checkBox.setId(checkBoxId);
		checkBox.setSelected(checkBoxSelected);
		checkBox.setOnAction(checkBoxOnAction);
		var label = new Label(labelText);
		label.setId(labelId);
		label.setDisable(labelDisable);
		label.setWrapText(true);
		var hBox = new HBox();
		hBox.setMinHeight(50);
		hBox.getChildren().addAll(checkBox, label);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(10);
		return hBox;
	}
	
	public static HBox createCellButtonsHBox(Node... children) {
		var hBox = new HBox();
		hBox.setMinHeight(50);
		hBox.getChildren().addAll(children);
		hBox.setSpacing(10);
		hBox.setPrefWidth((ToDoJavaFxView.BUTTON_ICON_WIDTH * 2) + 10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		return hBox;
	}
	
	public static GridPane createCellGridPane(boolean isSelected, HBox labelHBox, HBox buttonsHBox, 
			double width) {
		var gridPane = new GridPane();
		gridPane.setPrefWidth(width);
		gridPane.setMinHeight(50);
		gridPane.setHgap(20);
		if (!isSelected) {
			labelHBox.setPrefWidth(gridPane.getPrefWidth());
			gridPane.addColumn(0, labelHBox);
			GridPane.setColumnSpan(labelHBox, 2);
		} else {
			labelHBox.setPrefWidth(gridPane.getPrefWidth() 
					- buttonsHBox.getPrefWidth() - gridPane.getHgap());
			gridPane.addColumn(0, labelHBox);
			gridPane.addColumn(1, buttonsHBox);
		}
		return gridPane;
	}
	
}
