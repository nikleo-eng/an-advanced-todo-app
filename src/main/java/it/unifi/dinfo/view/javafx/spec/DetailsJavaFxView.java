package it.unifi.dinfo.view.javafx.spec;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.DetailsView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class DetailsJavaFxView extends BaseJavaFxView implements DetailsView {

	private AdditionModificationJavaFxView additionModificationJavaFxView;
	private ListsJavaFxView listsJavaFxView;
	
	private ListView<Detail> listView;
	private Button addButton;
	
	protected static final String ADD_BUTTON_TEXT = "Add";
	protected static final String ADD_BUTTON_ID = "DETAILS_ADD_BUTTON_ID";
	protected static final String LISTVIEW_ID = "DETAILS_LISTVIEW_ID";
	
	private static final String ROW_PATTERN_ID ="DETAILS_%s_ROW_ID";
	private static final String CHECKBOX_PATTERN_ID ="DETAILS_%s_CHECKBOX_ID";
	private static final String LABEL_PATTERN_ID ="DETAILS_%s_LABEL_ID";
	private static final String MODIFY_BUTTON_PATTERN_ID ="DETAILS_%s_MODIFY_BUTTON_ID";
	private static final String DELETE_BUTTON_PATTERN_ID ="DETAILS_%s_DELETE_BUTTON_ID";
	
	protected static String getRowId(String todo) {
		return replaceString(ROW_PATTERN_ID, todo);
	}
	
	protected static String getRowCheckBoxId(String todo) {
		return replaceString(CHECKBOX_PATTERN_ID, todo);
	}
	
	protected static String getRowLabelId(String todo) {
		return replaceString(LABEL_PATTERN_ID, todo);
	}
	
	protected static String getRowModifyButtonId(String todo) {
		return replaceString(MODIFY_BUTTON_PATTERN_ID, todo);
	}
	
	protected static String getRowDeleteButtonId(String todo) {
		return replaceString(DELETE_BUTTON_PATTERN_ID, todo);
	}
	
	private static String replaceString(String toReplace, String replacing) {
		return String.format(toReplace, replacing);
	}

	public DetailsJavaFxView(ListsJavaFxView listsJavaFxView, ToDoController toDoController) {
		super(toDoController);
		this.listsJavaFxView = listsJavaFxView;
		additionModificationJavaFxView = null;
		listView = null;
		addButton = null;
	}

	public void setAdditionModificationJavaFxView(
			AdditionModificationJavaFxView additionModificationJavaFxView) {
		this.additionModificationJavaFxView = additionModificationJavaFxView;
	}
	
	public void disableArea() {
		listView.setDisable(true);
		addButton.setDisable(true);
	}
	
	@Override
	public void resetGUI() {
		listView.getItems().clear();
		disableArea();
	}
	
	public void enableArea() {
		listView.setDisable(false);
		addButton.setDisable(false);
	}
	
	private void clearSelection() {
		listView.getSelectionModel().clearSelection();
		updateGUIItemsList();
	}
	
	public Detail getSelectedItem() {
		return listView.getSelectionModel().getSelectedItem();
	}
	
	public void clearAll() {
		listView.getItems().clear();
	}
	
	@Override
	public void showAll(Set<Detail> details) {
		listView.getItems().setAll(details);
	}

	@Override
	public void delete(Detail detail) {
		clearSelection();
		listView.getItems().remove(detail);
	}
	
	@Override
	public void add(Detail detail) {
		listView.getItems().add(detail);
		additionModificationJavaFxView.resetGUI();
		enableArea();
		listsJavaFxView.enableArea();
	}

	@Override
	public void save(Detail detail) {
		int itemIndex = IntStream.range(0, listView.getItems().size())
				.filter(i -> listView.getItems().get(i).getId().equals(detail.getId()))
				.findFirst().orElse(-1);
		listView.getItems().set(itemIndex, detail);
		listView.getSelectionModel().select(detail);
		additionModificationJavaFxView.resetGUI();
		enableArea();
		updateGUIItemsList();
		listsJavaFxView.enableArea();
	}
	
	private void clickAddButton() {
		clearSelection();
		listsJavaFxView.disableArea();
		disableArea();
		additionModificationJavaFxView.enableArea(true, false);
	}
	
	private void updateGUIItemsList() {
		Arrays.asList(listView.lookupAll(".cell").toArray()).forEach(c -> {
			DetailFormatCell dfc = (DetailFormatCell) c;
			dfc.updateItem(dfc.getItem(), dfc.isEmpty());
		});
	}

	@Override
	public VBox createGUI(double width, double height) {
		var vBox = new VBox();
		vBox.setPrefSize(width, height);
		vBox.setStyle(ToDoJavaFxView.BORDER_STYLE);
		
		var titleHBox = new HBox();
		titleHBox.setPrefSize(vBox.getPrefWidth(), ToDoJavaFxView.HEADER_FOOTER_HEIGHT);
		titleHBox.setAlignment(Pos.CENTER);
		var titleText = new Text("Details Area");
		titleText.setStyle(ToDoJavaFxView.BOLD_STYLE);
		titleHBox.getChildren().add(titleText);
		
		listView = new ListView<>();
		listView.setDisable(true);
		listView.setId(LISTVIEW_ID);
		listView.setPrefSize(vBox.getPrefWidth(), vBox.getPrefHeight() 
				- (2 * ToDoJavaFxView.HEADER_FOOTER_HEIGHT));
		listView.setCellFactory(call -> new DetailFormatCell());

		var buttonHBox = new HBox();
		buttonHBox.setPrefSize(vBox.getPrefWidth(), ToDoJavaFxView.HEADER_FOOTER_HEIGHT);
		buttonHBox.setAlignment(Pos.CENTER);
		addButton = new Button(ADD_BUTTON_TEXT);
		addButton.setId(ADD_BUTTON_ID);
		addButton.setDisable(true);
		addButton.setPrefWidth(ToDoJavaFxView.BUTTON_WIDTH);
		addButton.setOnAction(ev -> clickAddButton());
		buttonHBox.getChildren().add(addButton);
		
		vBox.getChildren().addAll(titleHBox, listView, buttonHBox);
		return vBox;
	}
	
	private class DetailFormatCell extends ListCell<Detail> {
		
		public DetailFormatCell() {
			super();
			setOnMouseClicked(ev -> updateGUIItemsList());
		}
		
		private void clickModifyButton() {
			disableArea();
			listsJavaFxView.disableArea();
			additionModificationJavaFxView.enableArea(false, false);
		}

		@Override
		protected void updateItem(Detail item, boolean empty) {
			super.updateItem(item, empty);
			setPrefWidth(getListView().getWidth() - 20);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				var checkBox = new CheckBox();
				checkBox.setId(getRowCheckBoxId(item.getTodo()));
				checkBox.setSelected(item.getDone());
				checkBox.setOnAction(ev -> getToDoController().modifyDoneDetail(
						((CheckBox) ev.getSource()).isSelected(), item));
				
				var text = new Label(item.getTodo());
				text.setId(getRowLabelId(item.getTodo()));
				text.setDisable(item.getDone());
				text.setWrapText(true);
				var textHBox = new HBox();
				textHBox.setMinHeight(50);
				textHBox.getChildren().addAll(checkBox, text);
				textHBox.setAlignment(Pos.CENTER_LEFT);
				textHBox.setSpacing(10);

				var modifyButton = new Button();
				modifyButton.setId(getRowModifyButtonId(item.getTodo()));
				var modifySvgPath = new SVGPath();
				modifySvgPath.setContent(ToDoJavaFxView.SVG_CONTENT_MODIFY_ICON);
				modifyButton.setGraphic(modifySvgPath);
				modifyButton.setAlignment(Pos.CENTER);
				modifyButton.setPrefWidth(ToDoJavaFxView.BUTTON_ICON_WIDTH);
				modifyButton.setOnAction(ev -> clickModifyButton());
				
				var deleteButton = new Button();
				deleteButton.setId(getRowDeleteButtonId(item.getTodo()));
				var deleteSvgPath = new SVGPath();
				deleteSvgPath.setContent(ToDoJavaFxView.SVG_CONTENT_DELETE_ICON);
				deleteButton.setGraphic(deleteSvgPath);
				deleteButton.setAlignment(Pos.CENTER);
				deleteButton.setPrefWidth(ToDoJavaFxView.BUTTON_ICON_WIDTH);
				deleteButton.setOnAction(ev -> getToDoController().deleteDetail(item));
				
				var buttonsHBox = new HBox();
				buttonsHBox.setMinHeight(50);
				buttonsHBox.getChildren().addAll(modifyButton, deleteButton);
				buttonsHBox.setSpacing(10);
				buttonsHBox.setPrefWidth(modifyButton.getPrefWidth() 
						+ deleteButton.getPrefWidth() + buttonsHBox.getSpacing());
				buttonsHBox.setAlignment(Pos.CENTER_LEFT);

				var gridPane = new GridPane();
				gridPane.setPrefWidth(getPrefWidth());
				gridPane.setMinHeight(50);
				gridPane.setHgap(20);
				
				if (!isSelected()) {
					textHBox.setPrefWidth(gridPane.getPrefWidth());
					gridPane.addColumn(0, textHBox);
					GridPane.setColumnSpan(textHBox, 2);
				} else {	
					textHBox.setPrefWidth(gridPane.getPrefWidth() 
							- buttonsHBox.getPrefWidth() - gridPane.getHgap());
					gridPane.addColumn(0, textHBox);
					gridPane.addColumn(1, buttonsHBox);
				}

				setText(null);
				setGraphic(gridPane);
				
				setId(getRowId(item.getTodo()));
			}
		}

	}

}
