package it.unifi.dinfo.view.javafx.spec;

import com.google.inject.Inject;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.view.javafx.ToDoJavaFxView;
import it.unifi.dinfo.view.javafx.spec.base.BaseJavaFxView;
import it.unifi.dinfo.view.spec.AdditionModificationView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AdditionModificationJavaFxView extends BaseJavaFxView 
	implements AdditionModificationView {
	
	private ListsJavaFxView listsJavaFxView;
	private DetailsJavaFxView detailsJavaFxView;
	private User currentUser;
	private boolean calledForCreation;
	private boolean calledFromLists;

	private TextArea textArea;
	private Button cancelButton;
	private Button saveButton;
	private Text errorText;
	
	public static final String TEXTAREA_ID = "ADDITION_MODIFICATION_TEXTAREA_ID";
	public static final String CANCEL_BUTTON_ID = "ADDITION_MODIFICATION_CANCEL_BUTTON_ID";
	public static final String SAVE_BUTTON_ID = "ADDITION_MODIFICATION_SAVE_BUTTON_ID";
	public static final String ERROR_TEXT_ID = "ADDITION_MODIFICATION_ERROR_TEXT_ID";
	
	protected static final String CANCEL_BUTTON_TEXT = "Cancel";
	protected static final String SAVE_BUTTON_TEXT = "Save";
	
	@Inject
	public AdditionModificationJavaFxView(ToDoController toDoController) {
		super(toDoController);
	}
	
	public void setListsJavaFxView(ListsJavaFxView listsJavaFxView) {
		this.listsJavaFxView = listsJavaFxView;
	}

	public void setDetailsJavaFxView(DetailsJavaFxView detailsJavaFxView) {
		this.detailsJavaFxView = detailsJavaFxView;
	}

	/* Only for tests */
	protected void setCalledForCreation(boolean calledForCreation) {
		this.calledForCreation = calledForCreation;
	}

	/* Only for tests */
	protected void setCalledFromLists(boolean calledFromLists) {
		this.calledFromLists = calledFromLists;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	/* Only for tests */
	public User getCurrentUser() {
		return currentUser;
	}

	private void cancel() {
		resetGUI();
		listsJavaFxView.enableArea();
		if (listsJavaFxView.getSelectedItem() != null) {
			detailsJavaFxView.enableArea();
		}
	}
	
	public void enableArea(boolean calledForCreation, boolean calledFromLists) {
		this.calledForCreation = calledForCreation;
		this.calledFromLists = calledFromLists;
		textArea.setDisable(false);
		cancelButton.setDisable(false);
		saveButton.setDisable(false);
		
		if (!this.calledForCreation) {
			String textToModify = null;
			if (this.calledFromLists) {
				textToModify = listsJavaFxView.getSelectedItem().getName();
			} else {
				textToModify = detailsJavaFxView.getSelectedItem().getTodo();
			}
			textArea.setText(textToModify);
		}
	}
	
	@Override
	public void resetGUI() {
		textArea.setDisable(true);
		textArea.clear();
		cancelButton.setDisable(true);
		saveButton.setDisable(true);
		errorText.setText("");
		errorText.setVisible(false);
	}

	@Override
	public void renderError(String error) {
		errorText.setText(error);
		errorText.setVisible(true);
	}
	
	private void save() {
		String text = textArea.getText();
		if (calledForCreation) {
			if (calledFromLists) {
				getToDoController().addList(text, currentUser);
			} else {
				getToDoController().addDetail(text, listsJavaFxView.getSelectedItem());
			}
		} else {
			if (calledFromLists) {
				getToDoController().modifyNameList(text, listsJavaFxView.getSelectedItem());
			} else {
				getToDoController().modifyTodoDetail(text, detailsJavaFxView.getSelectedItem());
			}
		}
	}

	@Override
	public VBox createGUI(double width, double height) {
		var vBox = new VBox();
		vBox.setPrefSize(width, height);
		vBox.setStyle("-fx-border-color: black; -fx-border-insets: 10; -fx-border-width: 2;");
		
		var titleHBox = new HBox();
		titleHBox.setPrefSize(vBox.getPrefWidth(), ToDoJavaFxView.HEADER_FOOTER_HEIGHT);
		titleHBox.setAlignment(Pos.CENTER);
		var titleText = new Text("Addition/Modification Area");
		titleText.setStyle(ToDoJavaFxView.BOLD_STYLE);
		titleHBox.getChildren().add(titleText);
		
		var gridPane = new GridPane();
		gridPane.setPrefSize(vBox.getPrefWidth(), vBox.getPrefHeight() 
				- (2 * ToDoJavaFxView.HEADER_FOOTER_HEIGHT));
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		
		textArea = new UpperTextArea();
		textArea.setId(TEXTAREA_ID);
		textArea.setPrefRowCount(4);
		textArea.setMaxWidth(450);
		textArea.setDisable(true);
		
		var buttonsHBox = new HBox();
		buttonsHBox.setAlignment(Pos.CENTER);
		buttonsHBox.setSpacing(20);
		cancelButton = new Button(CANCEL_BUTTON_TEXT);
		cancelButton.setId(CANCEL_BUTTON_ID);
		cancelButton.setPrefWidth(ToDoJavaFxView.BUTTON_WIDTH);
		cancelButton.setOnAction(ev -> cancel());
		saveButton = new Button(SAVE_BUTTON_TEXT);
		saveButton.setId(SAVE_BUTTON_ID);
		saveButton.setPrefWidth(100);
		saveButton.setOnAction(ev -> save());
		cancelButton.setDisable(true);
		saveButton.setDisable(true);
		buttonsHBox.getChildren().addAll(cancelButton, saveButton);
		
		var errorTextHBox = new HBox();
		errorTextHBox.setAlignment(Pos.CENTER);
		errorText = new Text("");
		errorText.setId(ERROR_TEXT_ID);
		errorText.setStyle(ToDoJavaFxView.BOLD_STYLE);
		errorText.setFill(Color.RED);
		errorText.setVisible(false);
		errorTextHBox.getChildren().add(errorText);
		
		gridPane.addRow(0, textArea);
		gridPane.addRow(1, buttonsHBox);
		gridPane.addRow(2, errorTextHBox);
		
		vBox.getChildren().addAll(titleHBox, gridPane);
		return vBox;
	}
	
	protected static class UpperTextArea extends TextArea {
		
		public UpperTextArea() {
			super();
		}
		
		@Override
		public void replaceText(int start, int end, String text) {
	        super.replaceText(start, end, text.toUpperCase());
	    }
		
	}

}
