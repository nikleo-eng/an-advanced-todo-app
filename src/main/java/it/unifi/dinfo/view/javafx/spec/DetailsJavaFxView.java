package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.spec.util.ListsDetailsGUI.*;

import java.util.Set;
import java.util.stream.IntStream;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.DetailsView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsJavaFxView extends BaseJavaFxView implements DetailsView {

	private AdditionModificationJavaFxView additionModificationJavaFxView;
	private ListsJavaFxView listsJavaFxView;
	
	private ListView<Detail> listView;
	private Button addButton;
	private Text errorText;
	
	protected static final String ADD_BUTTON_TEXT = "Add";
	public static final String ADD_BUTTON_ID = "DETAILS_ADD_BUTTON_ID";
	public static final String LISTVIEW_ID = "DETAILS_LISTVIEW_ID";
	public static final String ERROR_TEXT_ID = "DETAILS_ERROR_TEXT_ID";
	
	private static final String ROW_PATTERN_ID ="DETAILS_%s_ROW_ID";
	private static final String CHECKBOX_PATTERN_ID ="DETAILS_%s_CHECKBOX_ID";
	private static final String LABEL_PATTERN_ID ="DETAILS_%s_LABEL_ID";
	private static final String MODIFY_BUTTON_PATTERN_ID ="DETAILS_%s_MODIFY_BUTTON_ID";
	private static final String DELETE_BUTTON_PATTERN_ID ="DETAILS_%s_DELETE_BUTTON_ID";
	
	public static String getRowId(String todo) {
		return replaceString(ROW_PATTERN_ID, todo);
	}
	
	public static String getRowCheckBoxId(String todo) {
		return replaceString(CHECKBOX_PATTERN_ID, todo);
	}
	
	public static String getRowLabelId(String todo) {
		return replaceString(LABEL_PATTERN_ID, todo);
	}
	
	public static String getRowModifyButtonId(String todo) {
		return replaceString(MODIFY_BUTTON_PATTERN_ID, todo);
	}
	
	public static String getRowDeleteButtonId(String todo) {
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
		errorText = null;
	}

	public void setAdditionModificationJavaFxView(
			AdditionModificationJavaFxView additionModificationJavaFxView) {
		this.additionModificationJavaFxView = additionModificationJavaFxView;
	}
	
	public void disableArea() {
		listView.setDisable(true);
		addButton.setDisable(true);
		resetListsAndDetailsErrors();
	}
	
	private void resetListsAndDetailsErrors() {
		resetError();
		listsJavaFxView.resetError();
	}
	
	public void resetError() {
		errorText.setText("");
		errorText.setVisible(false);
	}
	
	@Override
	public void resetGUI() {
		listView.getItems().clear();
		disableArea();
	}
	
	@Override
	public void renderError(String error) {
		errorText.setText(error);
		errorText.setVisible(true);
	}
	
	public void enableArea() {
		listView.setDisable(false);
		addButton.setDisable(false);
	}
	
	private void clearSelection() {
		listView.getSelectionModel().clearSelection();
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
		listsJavaFxView.enableArea();
	}
	
	private void clickAddButton() {
		clearSelection();
		listsJavaFxView.disableArea();
		disableArea();
		additionModificationJavaFxView.enableArea(true, false);
	}

	@Override
	public VBox createGUI(double width, double height) {
		var vBox = createVBox(width, height);
		
		var titleText = createText("Details Area");
		var titleHBox = createHBox(titleText, vBox.getPrefWidth());
		
		listView = createListView(LISTVIEW_ID, vBox.getPrefWidth(), vBox.getPrefHeight(), 
				call -> new DetailFormatCell());

		addButton = createButton(ADD_BUTTON_ID, ADD_BUTTON_TEXT, ev -> clickAddButton());
		var buttonHBox = createHBox(addButton, vBox.getPrefWidth());
		
		errorText = createErrorText(ERROR_TEXT_ID);
		var textHBox = createHBox(errorText, vBox.getPrefWidth());
		
		vBox.getChildren().addAll(titleHBox, listView, buttonHBox, textHBox);
		return vBox;
	}
	
	private class DetailFormatCell extends ListCell<Detail> {
		
		public DetailFormatCell() {
			super();
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

			if (item == null) {
				setText(null);
				setGraphic(null);
				setId(null);
			} else {
				var checkBoxLabelHBox = createCellCheckBoxLabelHBox(getRowCheckBoxId(item.getTodo()), 
						item.getDone(), ev -> { ev.consume(); resetListsAndDetailsErrors(); 
							getToDoController().modifyDoneDetail(
								((CheckBox) ev.getSource()).isSelected(), item); }, 
						getRowLabelId(item.getTodo()), item.getTodo(), item.getDone());

				var modifyButton = createCellSvgButton(getRowModifyButtonId(item.getTodo()), 
						ev ->  { ev.consume(); clickModifyButton(); }, 
						ToDoJavaFxView.SVG_CONTENT_MODIFY_ICON);
				
				var deleteButton = createCellSvgButton(getRowDeleteButtonId(item.getTodo()), 
						ev -> { ev.consume(); resetListsAndDetailsErrors(); 
						getToDoController().deleteDetail(item); }, 
						ToDoJavaFxView.SVG_CONTENT_DELETE_ICON);
				
				var buttonsHBox = createCellButtonsHBox(modifyButton, deleteButton);

				var gridPane = createCellGridPane(isSelected(), checkBoxLabelHBox, buttonsHBox, 
						getPrefWidth());

				setText(null);
				setGraphic(gridPane);
				setId(getRowId(item.getTodo()));
			}
		}

	}

}
