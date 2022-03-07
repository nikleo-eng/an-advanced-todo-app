package it.unifi.dinfo.view.javafx.spec;

import static it.unifi.dinfo.view.javafx.ToDoJavaFxView.*;
import static it.unifi.dinfo.view.javafx.spec.DetailsJavaFxView.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.testfx.util.WaitForAsyncUtils.checkAllExceptions;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

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
import it.unifi.dinfo.model.Detail;
import it.unifi.dinfo.model.List;
import it.unifi.dinfo.model.User;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailsJavaFxViewTest extends ApplicationTest {
	
	/* https://github.com/TestFX/TestFX/issues/367#issuecomment-347077166 */
	@Rule
    public TestFXRule testFXRule = new TestFXRule(3);

	@Mock
	private ToDoController toDoController;
	
	@Mock
	private ListsJavaFxView listsJavaFxView;
	
	@Mock
	private AdditionModificationJavaFxView additionModificationJavaFxView;
	
	private DetailsJavaFxView detailsJavaFxView;
	
	@Override
	public void start(Stage stage) throws Exception {
		MockitoAnnotations.openMocks(this);
		detailsJavaFxView = new DetailsJavaFxView(listsJavaFxView, toDoController);
		detailsJavaFxView.setAdditionModificationJavaFxView(additionModificationJavaFxView);
		
		FlowPane flowPane = new FlowPane();
		Scene scene = new Scene(flowPane, SCENE_WIDTH, SCENE_HEIGHT);
		VBox vBox = detailsJavaFxView.createGUI(scene.getWidth(), scene.getHeight());
		flowPane.getChildren().add(vBox);
		stage.setScene(scene);
		
		stage.show();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldViewContainListViewAndAddButton() {
		Node listViewNode = lookup("#" + LISTVIEW_ID).tryQuery().orElse(null);
		assertThat(listViewNode).isNotNull().isOfAnyClassIn(ListView.class);
		ListView<Detail> listView = (ListView<Detail>) listViewNode;
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
	public void shouldUnselectedDetailHaveCheckBoxAndLabelButNotModifyAndDeleteButtons() 
			throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		/* https://github.com/TestFX/TestFX/issues/392 */
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		assertThat(lookup("#" + getRowCheckBoxId(detail.getTodo())).tryQueryAs(CheckBox.class)).isNotEmpty();
		assertThat(lookup("#" + getRowLabelId(detail.getTodo())).tryQueryAs(Label.class)).isNotEmpty();
		assertThat(lookup("#" + getRowModifyButtonId(detail.getTodo())).tryQueryAs(Button.class)).isEmpty();
		assertThat(lookup("#" + getRowDeleteButtonId(detail.getTodo())).tryQueryAs(Button.class)).isEmpty();
	}
	
	@Test
	public void shouldSelectedDetailHaveCheckBoxLabelAndModifyAndDeleteButtons() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail.getTodo()));
		
		assertThat(lookup("#" + getRowCheckBoxId(detail.getTodo())).tryQueryAs(CheckBox.class)).isNotEmpty();
		assertThat(lookup("#" + getRowLabelId(detail.getTodo())).tryQueryAs(Label.class)).isNotEmpty();
		assertThat(lookup("#" + getRowModifyButtonId(detail.getTodo())).tryQueryAs(Button.class)).isNotEmpty();
		assertThat(lookup("#" + getRowDeleteButtonId(detail.getTodo())).tryQueryAs(Button.class)).isNotEmpty();
	}
	
	@Test
	public void shouldClickOnAddButtonDisableAllOnThisAndListsViewAndEnableAllOnAdditionModificationView() 
			throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail.getTodo()));
		clickOn(addButton);
		
		verify(listsJavaFxView).disableArea();
		verify(additionModificationJavaFxView).enableArea(true, false);
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
		assertThat(listView.getSelectionModel().isEmpty()).isTrue();
	}
	
	@Test
	public void shouldResetGUIClearListAndDisableAll() {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		detailsJavaFxView.resetGUI();
		
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
		assertThat(listView.getItems()).isEmpty();
	}
	
	@Test
	public void shouldShowAllResetListWithGivenItems() {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List listOld = new List("TEST_OLD", user);
		Detail detailOld = new Detail("TEST-D", listOld);
		listView.getItems().add(detailOld);
		
		List listNew = new List("TEST_NEW", user);
		Detail detailNew = new Detail("TEST-D_NEW", listNew);
		Set<Detail> details = new HashSet<Detail>();
		details.add(detailNew);
		
		/* https://github.com/TestFX/TestFX/issues/713 */
		checkAllExceptions = false;
		
		detailsJavaFxView.showAll(details);
		
		assertThat(listView.getItems()).containsExactly(detailNew);
		assertThat(listView.getItems()).doesNotContain(detailOld);
	}
	
	@Test
	public void shouldEnableAreaEnableListViewAndButton() {
		detailsJavaFxView.enableArea();
		
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().isDisable()).isFalse();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isFalse();
	}
	
	@Test
	public void shouldDisableAreaDisableListViewAndButton() {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		detailsJavaFxView.disableArea();
		
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
	}
	
	@Test
	public void shouldGetSelectedItemReturnTheSelectedItemInList() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail1 = new Detail("TEST-D_1", list);
		Detail detail2 = new Detail("TEST-D_2", list);
		listView.getItems().addAll(detail1, detail2);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail2.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail2.getTodo()));
		
		Detail selectedDetail = detailsJavaFxView.getSelectedItem();
		assertThat(selectedDetail).isEqualTo(detail2);
	}
	
	@Test
	public void shouldClickOnModifyButtonDisableAllOnThisAndListsViewAndEnableAllOnAdditionModificationView() 
			throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		Button addButton = lookup("#" + ADD_BUTTON_ID).queryButton();
		addButton.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail.getTodo()));
		clickOn("#" + getRowModifyButtonId(detail.getTodo()));
		
		verify(listsJavaFxView).disableArea();
		verify(additionModificationJavaFxView).enableArea(false, false);
		assertThat(listView.isDisable()).isTrue();
		assertThat(addButton.isDisable()).isTrue();
	}
	
	@Test
	public void shouldClickOnDeleteButtonCallDeleteDetailOnController() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail.getTodo()));
		clickOn("#" + getRowDeleteButtonId(detail.getTodo()));
		
		verify(toDoController).deleteDetail(detail);
	}
	
	@Test
	public void shouldAddAddGivenDetailToListEnableAllOnThisAndListsViewAndResetAdditionModificationView() {
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		
		detailsJavaFxView.add(detail);
		
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getItems()).containsExactly(detail);
		verify(additionModificationJavaFxView).resetGUI();
		verify(listsJavaFxView).enableArea();
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().isDisable()).isFalse();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isFalse();
	}
	
	@Test
	public void shouldDeleteClearSelectionAndRemoveGivenDetailOnListView() 
			throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().addAll(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail.getTodo()));
		
		Detail deletetDetail = new Detail("TEST-D", list);
		
		checkAllExceptions = false;
		
		detailsJavaFxView.delete(deletetDetail);
		
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getItems()).isEmpty();
		assertThat(lookup("#" + LISTVIEW_ID).queryListView().getSelectionModel().isEmpty()).isTrue();
	}
	
	@Test
	public void shouldSaveEnableAllOnThisAndListsViewResetAdditionModificationViewAndUpdateGivenDetailOnList() {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setId(1L);
		listView.getItems().addAll(detail);
		
		Detail savedDetail = new Detail("TEST-D_NEW", list);
		savedDetail.setId(1L);
		
		checkAllExceptions = false;
		
		detailsJavaFxView.save(savedDetail);
		
		verify(listsJavaFxView).enableArea();
		assertThat(listView.isDisable()).isFalse();
		assertThat(lookup("#" + ADD_BUTTON_ID).queryButton().isDisable()).isFalse();
		verify(additionModificationJavaFxView).resetGUI();
		assertThat(listView.getItems().get(0).getTodo()).isEqualTo(savedDetail.getTodo());
		assertThat(listView.getSelectionModel().getSelectedItem()).isEqualTo(savedDetail);
	}
	
	@Test
	public void shouldItemCheckBoxStateBeSelectedWhenLinkedDetailIsDone() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setDone(Boolean.TRUE);
		listView.getItems().addAll(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		CheckBox checkBox = lookup("#" + getRowCheckBoxId(detail.getTodo())).queryAs(CheckBox.class);
		assertThat(checkBox.isSelected()).isEqualTo(detail.getDone());
	}
	
	@Test
	public void shouldItemCheckBoxStateBeInitiallyUnselected() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().addAll(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		CheckBox checkBox = lookup("#" + getRowCheckBoxId(detail.getTodo())).queryAs(CheckBox.class);
		assertThat(checkBox.isSelected()).isFalse();
	}
	
	@Test
	public void shouldClickOnItemCheckBoxCallModifyDoneDetailOnControllerWithCheckBoxState() 
			throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		clickOn("#" + getRowId(detail.getTodo()));
		clickOn("#" + getRowCheckBoxId(detail.getTodo()));
		
		CheckBox checkBox = lookup("#" + getRowCheckBoxId(detail.getTodo())).queryAs(CheckBox.class);
		verify(toDoController).modifyDoneDetail(checkBox.isSelected(), detail);
	}
	
	@Test
	public void shouldItemLabelBeDisabledWhenTheLinkedItemIsDone() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		detail.setDone(Boolean.TRUE);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		assertThat(lookup("#" + getRowLabelId(detail.getTodo())).queryLabeled().isDisable()).isTrue();
	}
	
	@Test
	public void shouldItemLabelBeInitallyEnabled() throws TimeoutException {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return lookup("#" + getRowLabelId(detail.getTodo())).tryQuery().isPresent();
			}
		});
		
		assertThat(lookup("#" + getRowLabelId(detail.getTodo())).queryLabeled().isDisable()).isFalse();
	}
	
	@Test
	public void shouldClearAllClearListView() {
		ListView<Detail> listView = lookup("#" + LISTVIEW_ID).queryListView();
		listView.setDisable(false);
		
		User user = new User("Mario", "Rossi", "email@email.com", "password");
		List list = new List("TEST", user);
		Detail detail = new Detail("TEST-D", list);
		listView.getItems().add(detail);
		
		detailsJavaFxView.clearAll();
		
		assertThat(listView.getItems()).isEmpty();
		verifyNoInteractions(ignoreStubs(toDoController, listsJavaFxView, additionModificationJavaFxView));
	}
	
}
