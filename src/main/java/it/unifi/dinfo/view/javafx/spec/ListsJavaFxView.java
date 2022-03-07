package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.spec.util.ListsDetailsGUI.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.ListsView;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

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
		var vBox = createVBox(width, height);
		
		var titleText = createText("Lists Area");
		var titleHBox = createHBox(titleText, vBox.getPrefWidth());

		listView = createListView(LISTVIEW_ID, vBox.getPrefWidth(), vBox.getPrefHeight(), 
				call -> new ListFormatCell());

		addButton = createButton(ADD_BUTTON_ID, ADD_BUTTON_TEXT, ev -> clickAddButton());
		var buttonHBox = createHBox(addButton, vBox.getPrefWidth());
		
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
				var labelHBox = createCellLabelHBox(item.getName(), getRowLabelId(item.getName()));

				var modifyButton = createCellSvgButton(getRowModifyButtonId(item.getName()), 
						ev -> clickModifyButton(), ToDoJavaFxView.SVG_CONTENT_MODIFY_ICON);
				
				var deleteButton = createCellSvgButton(getRowDeleteButtonId(item.getName()), 
						ev -> getToDoController().deleteList(item), 
						ToDoJavaFxView.SVG_CONTENT_DELETE_ICON);
				
				var buttonsHBox = createCellButtonsHBox(modifyButton, deleteButton);

				var gridPane = createCellGridPane(isSelected(), labelHBox, buttonsHBox, getPrefWidth());

				setText(null);
				setGraphic(gridPane);
				
				setId(getRowId(item.getName()));
			}
		}

	}

}
