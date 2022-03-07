package it.unifi.dinfo.view.javafx.spec;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.ListsView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class ListsJavaFxView extends BaseJavaFxView implements ListsView {
	
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	private DetailsJavaFxView detailsJavaFxView;
	
	private ListView<List> listView;
	private Button addButton;
	
	protected static final String ADD_BUTTON_TEXT = "Add";
	protected static final String ADD_BUTTON_ID = "LISTS_ADD_BUTTON_ID";
	protected static final String LISTVIEW_ID = "LISTS_LISTVIEW_ID";
	
	private static final String ROW_PATTERN_ID ="LISTS_%s_ROW_ID";
	private static final String LABEL_PATTERN_ID ="LISTS_%s_LABEL_ID";
	private static final String MODIFY_BUTTON_PATTERN_ID ="LISTS_%s_MODIFY_BUTTON_ID";
	private static final String DELETE_BUTTON_PATTERN_ID ="LISTS_%s_DELETE_BUTTON_ID";
	
	protected static String getRowId(String name) {
		return replaceString(ROW_PATTERN_ID, name);
	}
	
	protected static String getRowLabelId(String name) {
		return replaceString(LABEL_PATTERN_ID, name);
	}
	
	protected static String getRowModifyButtonId(String name) {
		return replaceString(MODIFY_BUTTON_PATTERN_ID, name);
	}
	
	protected static String getRowDeleteButtonId(String name) {
		return replaceString(DELETE_BUTTON_PATTERN_ID, name);
	}
	
	private static String replaceString(String toReplace, String replacing) {
		return String.format(toReplace, replacing);
	}
	
	public ListsJavaFxView(ToDoController toDoController) {
		super(toDoController);
		additionModificationJavaFxView = null;
		detailsJavaFxView = null;
		listView = null;
		addButton = null;
	}

	public void setAdditionModificationJavaFxView(
			AdditionModificationJavaFxView additionModificationJavaFxView) {
		this.additionModificationJavaFxView = additionModificationJavaFxView;
	}

	public void setDetailsJavaFxView(DetailsJavaFxView detailsJavaFxView) {
		this.detailsJavaFxView = detailsJavaFxView;
	}
	
	@Override
	public void resetGUI() {
		listView.getItems().clear();
		disableArea();
	}
	
	public void clearAll() {
		listView.getItems().clear();
	}

	@Override
	public void showAll(Set<List> lists) {
		listView.getItems().setAll(lists);
	}

	@Override
	public void delete(List list) {
		clearSelection();
		listView.getItems().remove(list);
		detailsJavaFxView.resetGUI();
	}
	
	@Override
	public void save(List list) {
		int itemIndex = IntStream.range(0, listView.getItems().size())
				.filter(i -> listView.getItems().get(i).getId().equals(list.getId()))
				.findFirst().orElse(-1);
		listView.getItems().set(itemIndex, list);
		listView.getSelectionModel().select(list);
		additionModificationJavaFxView.resetGUI();
		enableArea();
		updateGUIItemsList();
		detailsJavaFxView.enableArea();
	}

	@Override
	public void add(List list) {
		listView.getItems().add(list);
		additionModificationJavaFxView.resetGUI();
		enableArea();
	}
	
	private void clickAddButton() {
		clearSelection();
		detailsJavaFxView.resetGUI();
		disableArea();
		additionModificationJavaFxView.enableArea(true, true);
	}
	
	public void disableArea() {
		listView.setDisable(true);
		addButton.setDisable(true);
	}
	
	public void enableArea() {
		listView.setDisable(false);
		addButton.setDisable(false);
	}
	
	private void clearSelection() {
		listView.getSelectionModel().clearSelection();
		updateGUIItemsList();
	}
	
	public List getSelectedItem() {
		return listView.getSelectionModel().getSelectedItem();
	}
	
	private void updateGUIItemsList() {
		Arrays.asList(listView.lookupAll(".cell").toArray()).forEach(c -> {
			ListFormatCell lfc = (ListFormatCell) c;
			lfc.updateItem(lfc.getItem(), lfc.isEmpty());
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
		var titleText = new Text("Lists Area");
		titleText.setStyle(ToDoJavaFxView.BOLD_STYLE);
		titleHBox.getChildren().add(titleText);

		listView = new ListView<>();
		listView.setId(LISTVIEW_ID);
		listView.setDisable(true);
		listView.setPrefSize(vBox.getPrefWidth(), vBox.getPrefHeight() 
				- (2 * ToDoJavaFxView.HEADER_FOOTER_HEIGHT));
		listView.setCellFactory(call -> new ListFormatCell());

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
	
	private class ListFormatCell extends ListCell<List> {
		
		public ListFormatCell() {
			super();
			setOnMouseClicked(ev -> clickOnItem((ListFormatCell) ev.getSource()));
		}
		
		private void clickOnItem(ListFormatCell lfc) {
			updateGUIItemsList();
			detailsJavaFxView.enableArea();
			getToDoController().getAllDetails(lfc.getItem().getId());
		}
		
		private void clickModifyButton() {
			disableArea();
			detailsJavaFxView.disableArea();
			additionModificationJavaFxView.enableArea(false, true);
		}

		@Override
		protected void updateItem(List item, boolean empty) {
			super.updateItem(item, empty);
			setPrefWidth(getListView().getWidth() - 20);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				var text = new Label(item.getName());
				text.setId(getRowLabelId(item.getName()));
				text.setWrapText(true);
				var hBox = new HBox();
				hBox.setMinHeight(50);
				hBox.getChildren().add(text);
				hBox.setAlignment(Pos.CENTER_LEFT);

				var modifyButton = new Button();
				modifyButton.setId(getRowModifyButtonId(item.getName()));
				var modifySvgPath = new SVGPath();
				modifySvgPath.setContent(ToDoJavaFxView.SVG_CONTENT_MODIFY_ICON);
				modifyButton.setGraphic(modifySvgPath);
				modifyButton.setAlignment(Pos.CENTER);
				modifyButton.setPrefWidth(ToDoJavaFxView.BUTTON_ICON_WIDTH);
				modifyButton.setOnAction(ev -> clickModifyButton());
				
				var deleteButton = new Button();
				deleteButton.setId(getRowDeleteButtonId(item.getName()));
				var deleteSvgPath = new SVGPath();
				deleteSvgPath.setContent(ToDoJavaFxView.SVG_CONTENT_DELETE_ICON);
				deleteButton.setGraphic(deleteSvgPath);
				deleteButton.setAlignment(Pos.CENTER);
				deleteButton.setPrefWidth(ToDoJavaFxView.BUTTON_ICON_WIDTH);
				deleteButton.setOnAction(ev -> getToDoController().deleteList(item));
				
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
					hBox.setPrefWidth(gridPane.getPrefWidth());
					gridPane.addColumn(0, hBox);
					GridPane.setColumnSpan(hBox, 2);
				} else {
					hBox.setPrefWidth(gridPane.getPrefWidth() 
							- buttonsHBox.getPrefWidth() - gridPane.getHgap());
					gridPane.addColumn(0, hBox);
					gridPane.addColumn(1, buttonsHBox);
				}

				setText(null);
				setGraphic(gridPane);
				
				setId(getRowId(item.getName()));
			}
		}

	}

}
