package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.ListsJavaFxView.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.testfx.util.WaitForAsyncUtils.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.framework.junit.TestFXRule;

import it.unifi.dinfo.controller.ToDoController;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import it.unifi.dinfo.view.spec.ListsView.ERRORS;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ListsJavaFxViewTest extends ApplicationTest {
	
	/* https://github.com/TestFX/TestFX/issues/367#issuecomment-347077166 */
	@Rule
    public TestFXRule testFXRule = new TestFXRule(3);

	@Mock
	private ToDoController toDoController;
	
	@Mock
	private DetailsJavaFxView detailsJavaFxView;
	
	@Mock
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	
	private ListsJavaFxView listsJavaFxView;
	
	@Override
	public void start(Stage stage) throws Exception {
		MockitoAnnotations.openMocks(this);
		listsJavaFxView = new ListsJavaFxView(toDoController);
		listsJavaFxView.setDetailsJavaFxView(detailsJavaFxView);
		listsJavaFxView.setAdditionModificationJavaFxView(additionModificationJavaFxView);
		
		FlowPane flowPane = new FlowPane();
		Scene scene = new Scene(flowPane, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = listsJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		flowPane.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldViewContainListViewAndAddButton() {
		Node listViewNode = lookup("#" + LISTVIEW_ID).tryQuery().orElse(null);
		assertThat(listViewNode).isNotNull().isOfAnyClassIn(ListView.class);
		ListView<List> listView = (ListView<List>) listViewNode;
		assertThat(listView.isVisible()).isTrue();
		
		Node addButtonNode = lookup("#" + ADD_BUTTON_ID).tryQuery().orElse(null);
		assertThat(addButtonNode).isNotNull().isOfAnyClassIn(Button.class);
		Button addButton = (Button) addButtonNode;
		assertThat(addButton.isVisible()).isTrue();
		assertThat(addButton.getText()).isEqualTo(ADD_BUTTON_TEXT);
	}
	
	@Test
	public void shouldListViewBeInitiallyEmpty() {
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getItems()).isEmpty();
	}
	
	@Test
	public void shouldListViewAndAddButtonBeInitiallyDisabled() {
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().isDisable()).isTrue();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isTrue();
	}
	
	@Test
	public void shouldUnselectedListHaveLabelButNotModifyAndDeleteButtons() throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		/* https://github.com/TestFX/TestFX/issues/392 */
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(list.getName())).tryQuery().isPresent();
			}
		});
		
		assertThat(lookup("#" + getRowLabelId(list.getName())).tryQueryAs(Label.class)).isNotEmpty();
		assertThat(lookup("#" + getRowModifyButtonId(list.getName())).tryQueryAs(Button.class)).isEmpty();
		assertThat(lookup("#" + getRowDeleteButtonId(list.getName())).tryQueryAs(Button.class)).isEmpty();
	}
	
	@Test
	public void shouldSelectedListHaveLabelAndModifyAndDeleteButtons() throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(list.getName())).tryQuery().isPresent();
			}
		});
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowModifyButtonId(list.getName())).tryQuery().isPresent();
			}
		});
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowDeleteButtonId(list.getName())).tryQuery().isPresent();
			}
		});
		
		assertThat(lookup("#" + getRowLabelId(list.getName())).tryQueryAs(Label.class)).isNotEmpty();
		assertThat(lookup("#" + getRowModifyButtonId(list.getName())).tryQueryAs(Button.class)).isNotEmpty();
		assertThat(lookup("#" + getRowDeleteButtonId(list.getName())).tryQueryAs(Button.class)).isNotEmpty();
	}
	
	@Test
	public void shouldClickOnItemEnableDetailsViewAndCallGetAllDetailsOnController() 
			throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		listView.getItems().add(list);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		
		verify(detailsJavaFxView).enableArea();
		verify(toDoController).getAllDetails(list.getId());
		verifyNoMoreInteractions(ignoreStubs(toDoController, detailsJavaFxView, 
				additionModificationJavaFxView));
	}
	
	@Test
	public void shouldClickOnAddButtonDisableAllOnThisAndDetailsViewAndEnableAllOnAdditionModificationView() 
			throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		clickOn(addButton);
		
		verify(detailsJavaFxView).resetGUI();
		verify(additionModificationJavaFxView).enableArea(true, true);
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
		assertThat(listView.getSelectionModel().isEmpty()).isTrue();
	}
	
	@Test
	public void shouldResetGUIClearListAndDisableAll() {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		listsJavaFxView.resetGUI();
		
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
		assertThat(listView.getItems()).isEmpty();
	}
	
	@Test
	public void shouldShowAllResetListWithGivenItems() {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List listOld = new List("TEST_OLD", user);
		listView.getItems().add(listOld);
		
		List listNew = new List("TEST_NEW", user);
		Set<List> lists = new HashSet<List>();
		lists.add(listNew);
		
		/* https://github.com/TestFX/TestFX/issues/713 */
		checkAllExceptions = false;
		
		listsJavaFxView.showAll(lists);
		
		assertThat(listView.getItems()).containsExactly(listNew);
		assertThat(listView.getItems()).doesNotContain(listOld);
	}
	
	@Test
	public void shouldEnableAreaEnableListViewAndButton() {
		listsJavaFxView.enableArea();
		
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().isDisable()).isFalse();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isFalse();
	}
	
	@Test
	public void shouldDisableAreaDisableListViewAndButton() {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		listsJavaFxView.disableArea();
		
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
	}
	
	@Test
	public void shouldDisableAreaResetErrorTextAndCallResetErrorOnDetailsView() {
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
		
		listsJavaFxView.disableArea();
		
		verify(detailsJavaFxView).resetError();
		assertThat(errorText.getText()).isEmpty();
		assertThat(errorText.isVisible()).isFalse();
	}
	
	@Test
	public void shouldResetErrorReallyResetErrorText() {
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
		listsJavaFxView.resetError();
		assertThat(errorText.getText()).isEmpty();
		assertThat(errorText.isVisible()).isFalse();
	}
	
	@Test
	public void shouldGetSelectedItemReturnTheSelectedItemInList() throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list1 = new List("TEST_1", user);
		List list2 = new List("TEST_2", user);
		listView.getItems().addAll(list1, list2);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list2.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list2.getName()));
		
		List selectedList = listsJavaFxView.getSelectedItem();
		assertThat(selectedList).isEqualTo(list2);
	}
	
	@Test
	public void shouldClickOnModifyButtonDisableAllOnThisAndDetailsViewAndEnableAllOnAdditionModificationView() 
			throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().addAll(list);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowModifyButtonId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowModifyButtonId(list.getName()));
		
		verify(detailsJavaFxView).disableArea();
		verify(additionModificationJavaFxView).enableArea(false, true);
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
	}
	
	@Test
	public void shouldClickOnDeleteButtonCallDeleteListOnController() throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowDeleteButtonId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowDeleteButtonId(list.getName()));
		
		sleep(20000);
		
		verify(toDoController).deleteList(list);
	}
	
	@Test
	public void shouldClickOnDeleteButtonResetErrorTextAndCallResetErrorOnDetailsView() 
			throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		errorText.setVisible(true);
		errorText.setText(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowDeleteButtonId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowDeleteButtonId(list.getName()));
		
		verify(detailsJavaFxView).resetError();
		assertThat(errorText.getText()).isEmpty();
		assertThat(errorText.isVisible()).isFalse();
	}
	
	@Test
	public void shouldAddAddGivenListToListEnableAllOnThisViewAndResetAdditionModificationView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		
		listsJavaFxView.add(list);
		
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getItems()).containsExactly(list);
		verify(additionModificationJavaFxView).resetGUI();
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().isDisable()).isFalse();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isFalse();
	}
	
	@Test
	public void shouldDeleteClearSelectionAndRemoveGivenListOnListViewAndResetDetailsView() 
			throws TimeoutException {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().addAll(list);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowId(list.getName())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(list.getName()));
		
		List deletetList = new List("TEST", user);
		
		checkAllExceptions = false;
		
		listsJavaFxView.delete(deletetList);
		
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getItems()).isEmpty();
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getSelectionModel().isEmpty()).isTrue();
		verify(detailsJavaFxView).resetGUI();
	}
	
	@Test
	public void shouldSaveEnableAllOnThisAndDetailsViewResetAdditionModificationViewAndUpdateGivenListOnList() {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		list.setId(1L);
		listView.getItems().add(list);
		
		List savedList = new List("TEST_NEW", user);
		savedList.setId(1L);
		
		checkAllExceptions = false;
		
		listsJavaFxView.save(savedList);
		
		verify(detailsJavaFxView).enableArea();
		assertThat(listView.isDisable()).isFalse();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isFalse();
		verify(additionModificationJavaFxView).resetGUI();
		assertThat(listView.getItems().get(0).getName()).isEqualTo(savedList.getName());
		assertThat(listView.getSelectionModel().getSelectedItem()).isEqualTo(savedList);
	}
	
	@Test
	public void shouldClearAllClearListView() {
		ListView<List> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		listView.getItems().add(list);
		
		listsJavaFxView.clearAll();
		
		assertThat(listView.getItems()).isEmpty();
		verifyNoInteractions(ignoreStubs(toDoController, detailsJavaFxView, additionModificationJavaFxView));
	}
	
	@Test
	public void shouldRenderErrorMakeVisibleErrorTextAndChangeItsText() {
		listsJavaFxView.renderError(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
		Text errorText = lookup("#" + ERROR_TEXT_ID).queryText();
		assertThat(errorText.isVisible()).isTrue();
		assertThat(errorText.getText()).isEqualTo(ERRORS.LIST_NO_LONGER_EXISTS.getValue());
	}
	
	@Test
	public void shouldViewContainEmptyErrorText() {
		Node errorTextNode = lookup("#" + ERROR_TEXT_ID).tryQuery().orElse(null);
		assertThat(errorTextNode).isNotNull().isOfAnyClassIn(Text.class);
		Text errorText = (Text) errorTextNode;
		assertThat(errorText.isVisible()).isFalse();
		assertThat(errorText.getText()).isEmpty();
	}
	
}
