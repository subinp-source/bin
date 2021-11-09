/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.itemcomments;

import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.COMPONENT_ADD_COMMENT_BUTTON;
import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.MODEL_COMMENTS;
import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.SETTING_COMMENTS_LIST_RENDERER;
import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.SETTING_DATE_FORMAT;
import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.SETTING_DEFAULT_COMMENTS_COMMENT_TYPE;
import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.SETTING_POPUP_POSITION;
import static com.hybris.backoffice.widgets.itemcomments.ItemCommentsController.SOCKET_IN_INPUT_ITEM;
import static com.hybris.cockpitng.testing.util.CockpitTestUtil.simulateEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.zkoss.zk.ui.event.Events.ON_CHANGING;
import static org.zkoss.zk.ui.event.Events.ON_CLICK;

import com.hybris.cockpitng.core.events.CockpitEvent;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.admin.CockpitMainWindowComposer;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.DeclaredViewEvent;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;


@DeclaredInput(value = SOCKET_IN_INPUT_ITEM, socketType = ItemModel.class)
@DeclaredGlobalCockpitEvent(eventName = CockpitMainWindowComposer.HEARTBEAT_EVENT, scope = CockpitEvent.SESSION)
@DeclaredViewEvent(componentID = COMPONENT_ADD_COMMENT_BUTTON, eventName = ON_CLICK)
@NullSafeWidget
public class ItemCommentsControllerTest extends AbstractWidgetUnitTest<ItemCommentsController>
{
	private static final int NUMBER_OF_COMMENTS_IN_STUB = 4;
	private static final String BACKOFFICE_COMMENT = "backofficeComment";
	private static final String INPUT_ITEM_MODEL = "inputItemModel";
	private static final String SCLASS_COMMENTS_LIST_EMPTY = "yw-commentslist-comment-empty";
	private static final String SCLASS_ADD_COMMENT_SECTION_BUTTON_OK = "yw-add-comment-section-button-ok";
	private static final String SCLASS_ADD_COMMENT_SECTION_BUTTON_CANCEL = "yw-add-comment-section-button-cancel";

	@Spy
	@InjectMocks
	private ItemCommentsController itemCommentsController;

	@Mock
	private ObjectFacade objectFacade;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private CommentService commentService;
	@Mock
	private UserService userService;
	@Mock
	private PermissionFacade permissionFacade;
	@Mock
	private NotificationService notificationService;
	@Mock
	private WidgetComponentRenderer<Div, Object, CommentModel> listRenderer;
	@Spy
	private Button showCommentsButton;
	@Spy
	private Label commentsCount;
	@Spy
	private Popup commentsPopup;
	@Spy
	private Button addCommentButton;
	@Spy
	private Div commentsListContainer;

	@Mock
	private ItemModel inputItem;

	@Before
	public void setUp() throws Exception
	{
		CockpitTestUtil.mockZkEnvironment();
		final List<CommentModel> comments = IntStream.range(0, NUMBER_OF_COMMENTS_IN_STUB).mapToObj(value -> new CommentModel())
				.collect(Collectors.toList());
		when(inputItem.getComments()).thenReturn(comments);
	}

	private static ItemModel createItemModelWithoutCommentsStub()
	{
		final ItemModel itemModel = new ItemModel();
		itemModel.setComments(new ArrayList<>());
		return itemModel;
	}

	@Override
	protected ItemCommentsController getWidgetController()
	{
		return itemCommentsController;
	}

	@Test
	public void shouldNotRenderControllerWhenComponentIsNull()
	{
		// given
		final Component component = null;

		// when
		itemCommentsController.initialize(component);

		// then
		verify(itemCommentsController, times(0)).render();
	}

	@Test
	public void shouldRenderDisabledShowPopupButton()
	{
		// given
		final ItemModel itemModel = null;

		// when
		itemCommentsController.onInputItemReceive(itemModel);

		// then
		verify(itemCommentsController, times(1)).render();
		assertThat(showCommentsButton.isDisabled()).isTrue();
		assertThat(showCommentsButton.getEventListeners(Events.ON_CLICK)).isEmpty();
	}

	@Test
	public void shouldRenderHiddenCommentsCountLabel()
	{
		// given
		final ItemModel itemModel = null;

		// when
		itemCommentsController.onInputItemReceive(itemModel);

		// then
		verify(itemCommentsController, times(1)).render();
		assertThat(commentsCount.isVisible()).isFalse();
		assertThat(commentsCount.getEventListeners(Events.ON_CLICK)).isEmpty();
	}

	@Test
	public void shouldInitializeControllerWithCorrectParameters()
	{
		// given
		final String dateFormat = "yyyy";
		widgetSettings.put(SETTING_POPUP_POSITION, "at_pointer", String.class);
		widgetSettings.put(SETTING_DATE_FORMAT, dateFormat, String.class);
		widgetSettings.put(SETTING_COMMENTS_LIST_RENDERER, "defaultCommentsListRenderer", String.class);

		// when
		itemCommentsController.initialize(new Div());

		// then
		assertThat(itemCommentsController.getPopupPosition()).isEqualTo(PopupPosition.AT_POINTER);
	}

	@Test
	public void shouldReceiveInput()
	{
		// given
		when(permissionFacade.canReadInstance(any())).thenReturn(true);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		assertThat(itemCommentsController.getCommentsFromModel().size()).isEqualTo(inputItem.getComments().size());
		verifyListIsRendered(NUMBER_OF_COMMENTS_IN_STUB);
	}

	@Test
	public void shouldOpenPopupAndScrollToLastCommentWhenShowPopupButtonWasClickedAndItemIsSelected()
	{
		// given
		itemCommentsController.setValue(INPUT_ITEM_MODEL, inputItem);
		itemCommentsController.initialize(new Div());

		// when
		simulateEvent(showCommentsButton, ON_CLICK, null);

		// then
		verify(commentsPopup).open(any(Component.class), anyString());
		verify(itemCommentsController).scrollToLastComment();
	}

	@Test
	public void shouldNotOpenPopupWhenShowPopupButtonWasClickedAndNoItemIsSelected()
	{
		// given
		itemCommentsController.setValue(INPUT_ITEM_MODEL, null);
		itemCommentsController.initialize(new Div());

		// when
		simulateEvent(showCommentsButton, ON_CLICK, null);

		// then
		verify(commentsPopup, never()).open(any(Component.class), anyString());
	}

	@Test
	public void shouldNotOpenPopupWhenCommentsCountLabelWasClickedAndNoItemIsSelected()
	{
		// given
		itemCommentsController.setValue(INPUT_ITEM_MODEL, null);
		itemCommentsController.initialize(new Div());

		// when
		simulateEvent(commentsCount, ON_CLICK, null);

		// then
		verify(commentsPopup, never()).open(any(Component.class), anyString());
	}

	@Test
	public void shouldReactOnHeartbeat() throws ObjectNotFoundException
	{
		// given
		when(objectFacade.reload(any(ItemModel.class))).thenReturn(inputItem);
		when(permissionFacade.canReadInstance(any())).thenReturn(true);
		itemCommentsController.onInputItemReceive(inputItem);

		// when
		itemCommentsController.onHeartbeat(new DefaultCockpitEvent(CockpitMainWindowComposer.HEARTBEAT_EVENT, null, null));

		// then
		verify(objectFacade, times(2)).reload(any(Object.class));
		verifyListIsRendered(NUMBER_OF_COMMENTS_IN_STUB);
	}

	@Test
	public void shouldOpenTheAddNewCommentContainerWhenAddNewCommentButtonIsClicked()
	{
		// given
		final Div addCommentContainer = Mockito.mock(Div.class);
		doReturn(addCommentContainer).when(itemCommentsController).createAddCommentContainer(any(), any(), any());
		when(itemCommentsController.getAddCommentContainer()).thenReturn(addCommentContainer);

		// when
		itemCommentsController.showNewCommentSection();

		// then
		assertThat(addCommentContainer).isNotNull();
		verify(itemCommentsController.getAddCommentContainer()).detach();
		verify(itemCommentsController.getCommentsPopup()).appendChild(addCommentContainer);
	}

	@Test
	public void shouldAddNewCommentWhenOkButtonIsClicked() throws Exception
	{
		// given
		final String commentContents = "Sample comment";
		final List comments = new ArrayList();
		final int sizeBefore = comments.size();

		final CommentModel commentModel = mock(CommentModel.class);
		widgetModel.setValue(MODEL_COMMENTS, comments);
		when(objectFacade.create(CommentModel._TYPECODE)).thenReturn(commentModel);
		when(objectFacade.save(any(CommentModel.class))).thenReturn(commentModel);
		when(inputItem.getComments()).thenReturn(comments);
		final DomainModel domainModel = mock(DomainModel.class);
		when(commentService.getDomainForCode(anyString())).thenReturn(domainModel);
		final ComponentModel componentModel = mock(ComponentModel.class);
		when(commentService.getComponentForCode(eq(domainModel), anyString())).thenReturn(componentModel);
		widgetSettings.put(SETTING_DEFAULT_COMMENTS_COMMENT_TYPE, BACKOFFICE_COMMENT);
		final CommentTypeModel commentTypeModel = mock(CommentTypeModel.class);
		when(commentService.getCommentTypeForCode(eq(componentModel), eq(BACKOFFICE_COMMENT))).thenReturn(commentTypeModel);
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeInstance(inputItem)).thenReturn(true);
		itemCommentsController.setValue(INPUT_ITEM_MODEL, inputItem);

		// when
		itemCommentsController.showNewCommentSection();
		CockpitTestUtil.findChild(commentsPopup, Textbox.class).ifPresent(textbox -> textbox.setText(commentContents));
		getOkButton().ifPresent(this::clickButton);

		// then
		verify(objectFacade).save(commentModel);

		final ArgumentCaptor<List> commentsToSave = ArgumentCaptor.forClass(List.class);
		verify(inputItem).setComments(commentsToSave.capture());
		assertThat(commentsToSave.getValue().size()).isEqualTo(sizeBefore + 1);
		verify((CommentModel) commentsToSave.getValue().get(0)).setText(commentContents);
	}

	@Test
	public void shouldNotAddNewCommentWhenCancelButtonIsClicked() throws Exception
	{
		// when
		itemCommentsController.showNewCommentSection();
		getCancelButton().ifPresent(this::clickButton);

		// then
		verify(objectFacade, never()).save(any(CommentModel.class));
		verify(addCommentButton).setVisible(true);
		assertThat(itemCommentsController.getAddCommentContainer().isVisible()).isFalse();
	}

	@Test
	public void shouldCloseAndOpenPopupOnPopupReload()
	{
		// given
		final Executable emptyExecutable = () -> {
			// do nothing
		};

		// when
		itemCommentsController.openPopup();
		itemCommentsController.runWithReopeningPopup(emptyExecutable);

		// then
		verify(itemCommentsController).closePopup();
		verify(itemCommentsController, times(2)).openPopup();
	}

	@Test
	public void shouldScrollToLastCommentAfterRenderingAllComments()
	{
		// given
		when(itemCommentsController.getInputItemModel()).thenReturn(inputItem);

		// when
		itemCommentsController.render();

		// then
		verify(itemCommentsController).scrollToLastComment();
	}

	@Test
	public void shouldHideAddNewCommentContainerAndShowButton()
	{
		// when
		itemCommentsController.showNewCommentSection();
		getCancelButton().ifPresent(this::clickButton);

		// then
		assertThat(itemCommentsController.getAddCommentContainer().isVisible()).isFalse();
		assertThat(itemCommentsController.getAddCommentButton().isVisible()).isTrue();
	}

	@Test
	public void shouldRenderCommentsIfNewCommentsAppeared() throws ObjectNotFoundException
	{
		// given
		when(itemCommentsController.getCommentsFromModel()).thenReturn(new ArrayList<>());
		when(itemCommentsController.getObjectFacade().reload(any(Object.class))).thenReturn(inputItem);
		doNothing().when(itemCommentsController).setValue(any(), any());
		when(permissionFacade.canReadInstance(any())).thenReturn(true);

		// when
		itemCommentsController.loadNewCommentsIfPossible();

		// then
		verify(itemCommentsController).render();
	}

	@Test
	public void shouldNotRenderCommentsIfNewCommentsDidNotAppear() throws ObjectNotFoundException
	{
		// given
		when(itemCommentsController.getCommentsFromModel()).thenReturn(new ArrayList<>());
		when(itemCommentsController.getObjectFacade().reload(any(Object.class))).thenReturn(createItemModelWithoutCommentsStub());

		// when
		itemCommentsController.loadNewCommentsIfPossible();

		// then
		verify(itemCommentsController, times(0)).render();
		verify(itemCommentsController, times(0)).scrollToLastComment();
	}

	@Test
	public void shouldCreateAddCommentContainer()
	{
		// when
		itemCommentsController.showNewCommentSection();

		// then
		final Optional<Textbox> textbox = CockpitTestUtil.findChild(commentsPopup, Textbox.class);
		assertThat(textbox.isPresent()).isTrue();
		final List<Button> buttons = CockpitTestUtil.findAllChildren(commentsPopup, Button.class).collect(Collectors.toList());
		assertThat(buttons).hasSize(2);
		verify(itemCommentsController).createOkButtonEventListener(any());
		verify(itemCommentsController).createCancelButtonListener();
	}

	@Test
	public void shouldSaveItemItemComment() throws ObjectSavingException
	{
		// given
		final CommentModel commentModel = Mockito.mock(CommentModel.class);
		doNothing().when(itemCommentsController).addCommentToItem(commentModel);
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeInstance(itemCommentsController.getInputItemModel())).thenReturn(true);

		// when
		itemCommentsController.tryToSaveItemComment(commentModel);

		// then
		verify(itemCommentsController.getObjectFacade()).save(commentModel);
		verify(itemCommentsController).addCommentToItem(commentModel);
	}

	@Test
	public void showNewCommentsSectionShouldRunWithPopupReload()
	{
		// when
		itemCommentsController.showNewCommentSection();

		// then
		verify(itemCommentsController).runWithReopeningPopup(any());
	}

	@Test
	public void cancelButtonEventShouldRunWithPopupReload()
	{
		// given

		// when
		itemCommentsController.showNewCommentSection();
		getCancelButton().ifPresent(this::clickButton);

		// then
		verify(itemCommentsController, times(2)).runWithReopeningPopup(any());
	}

	@Test
	public void okButtonEventShouldRunWithPopupReload()
	{
		// when
		itemCommentsController.showNewCommentSection();
		getOkButton().ifPresent(this::clickButton);

		// then
		verify(itemCommentsController, times(2)).runWithReopeningPopup(any());
	}

	@Test
	public void shouldReturnTrueWhenThereArePermissionsToCreateComment()
	{
		// given
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeInstance(itemCommentsController.getInputItemModel())).thenReturn(true);

		// when
		final boolean canCreateComment = itemCommentsController.canCreateComment();

		// then
		assertThat(canCreateComment).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenThereIsNoPermissionToChangeWorkflowAttachment()
	{
		// given
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeInstance(itemCommentsController.getInputItemModel())).thenReturn(false);

		// when
		final boolean canCreateComment = itemCommentsController.canCreateComment();

		// then
		assertThat(canCreateComment).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenThereIsNoPermissionToCreateCommentType()
	{
		// given
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(false);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeInstance(itemCommentsController.getInputItemModel())).thenReturn(true);

		// when
		final boolean canCreateComment = itemCommentsController.canCreateComment();

		// then
		assertThat(canCreateComment).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenThereIsNoPermissionToChangeCommentType()
	{
		// given
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(false);
		when(permissionFacade.canChangeInstance(itemCommentsController.getInputItemModel())).thenReturn(true);

		// when
		final boolean canCreateComment = itemCommentsController.canCreateComment();

		// then
		assertThat(canCreateComment).isFalse();
	}

	@Test
	public void shouldRefreshModelBeforeProcessReceivedInput() throws ObjectNotFoundException
	{
		// given
		final ItemModel refreshedModel = new ItemModel();
		final List<CommentModel> comments = new ArrayList<>();
		IntStream.range(0, NUMBER_OF_COMMENTS_IN_STUB + 2).forEach(value -> comments.add(new CommentModel()));
		refreshedModel.setComments(comments);

		when(permissionFacade.canReadInstance(any())).thenReturn(true);
		when(objectFacade.reload(inputItem)).thenReturn(refreshedModel);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		verify(itemCommentsController).initDataType(refreshedModel);
		verify(itemCommentsController).setValue(ItemCommentsController.MODEL_INPUT_ITEM, refreshedModel);
		assertThat(itemCommentsController.getCommentsFromModel().size()).isEqualTo(refreshedModel.getComments().size());
		verifyListIsRendered(NUMBER_OF_COMMENTS_IN_STUB + 2);
	}

	@Test
	public void shouldShow0CommentsNoForNotAllowedUser()
	{
		// given
		when(permissionFacade.canReadInstance(any(CommentModel.class))).thenReturn(false);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		assertThat(commentsCount.getValue()).isEqualTo("0");
	}

	@Test
	public void shouldShowCommentsCountForUserWithReadOnlyPermission()
	{
		// given
		when(permissionFacade.canReadInstance(any())).thenReturn(true);
		when(permissionFacade.canReadType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(false);
		when(permissionFacade.canChangeInstance(any(CommentModel.class))).thenReturn(false);
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(false);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		assertThat(commentsCount.getValue()).isEqualTo(String.valueOf(NUMBER_OF_COMMENTS_IN_STUB));
		assertThat(commentsCount.isVisible()).isTrue();
	}

	@Test
	public void shouldOpenCommentsPopupForUserWithReadOnlyPermissionWhenButtonIsClicked()
	{
		// given
		when(permissionFacade.canReadInstance(any())).thenReturn(true);
		when(permissionFacade.canReadType(CommentModel._TYPECODE)).thenReturn(true);
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(false);
		when(permissionFacade.canChangeInstance(any(CommentModel.class))).thenReturn(false);
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(false);

		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// when
		simulateEvent(showCommentsButton, ON_CLICK, null);

		// then
		verify(itemCommentsController).openPopup();
	}

	@Test
	public void shouldDisableAddNewCommentButtonForUsersWithoutChangePrivilegeForCommentType()
	{
		// given
		when(permissionFacade.canChangeType(CommentModel._TYPECODE)).thenReturn(false);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		verify(addCommentButton).setDisabled(true);
	}

	@Test
	public void shouldDisableAddNewCommentButtonForUsersWithoutCreateInstancePrivilegeForCommentType()
	{
		// given
		when(permissionFacade.canCreateTypeInstance(CommentModel._TYPECODE)).thenReturn(false);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		verify(addCommentButton).setDisabled(true);
	}

	@Test
	public void shouldDisableAddNewCommentButtonForUsersWithoutChangeInstancePrivilegeForProvidedItem()
	{
		// given
		when(permissionFacade.canChangeInstance(inputItem)).thenReturn(false);

		// when
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// then
		verify(addCommentButton).setDisabled(true);
	}

	@Test
	public void shouldNotShowCommentsNoForNotAllowedUser()
	{
		// given
		when(permissionFacade.canReadType(CommentModel._TYPECODE)).thenReturn(false);
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);

		// when
		itemCommentsController.openPopup();

		// then
		verify(commentsListContainer).setSclass(argThat(new BaseMatcher<>()
		{
			@Override
			public boolean matches(final Object o)
			{
				return Arrays.asList(CockpitTestUtil.getSClasses(Objects.toString(o))).contains(SCLASS_COMMENTS_LIST_EMPTY);
			}

			@Override
			public void describeTo(final Description description)
			{
				description.appendText("Should set CSS class on comments list: " + SCLASS_COMMENTS_LIST_EMPTY);
			}
		}));
		verify(commentsListContainer).appendChild(any(HtmlBasedComponent.class));
		verify(listRenderer, never()).render(any(), any(), any(), any(), any());
	}

	@Test
	public void shouldDisableOkButtonOnShow()
	{
		// given
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);
		final Optional<Button> okButton = getOkButton();

		// when
		itemCommentsController.showNewCommentSection();

		// then
		okButton.ifPresent(button -> assertThat(button.isDisabled()).isTrue());
	}

	@Test
	public void shouldDisableOkButtonOnContentClear()
	{
		// given
		executeInputSocketEvent(SOCKET_IN_INPUT_ITEM, inputItem);
		itemCommentsController.showNewCommentSection();
		final Optional<Textbox> commentContents = CockpitTestUtil.findChild(commentsPopup, Textbox.class);
		final Optional<Button> okButton = getOkButton();

		// when
		commentContents
				.ifPresent(textbox -> simulateEvent(textbox, new InputEvent(ON_CHANGING, textbox, "Some text", StringUtils.EMPTY)));

		// then
		okButton.ifPresent(button -> assertThat(button.isDisabled()).isFalse());

		// and when
		commentContents
				.ifPresent(textbox -> simulateEvent(textbox, new InputEvent(ON_CHANGING, textbox, StringUtils.EMPTY, "Some text")));

		// then
		okButton.ifPresent(button -> assertThat(button.isDisabled()).isTrue());
	}

	private void verifyListIsRendered(final int times)
	{
		verify(listRenderer, times(times)).render(any(), any(), any(), any(), any());
	}

	private Optional<Button> getOkButton()
	{
		return CockpitTestUtil.findChild(commentsPopup, cmp -> (cmp instanceof Button)
				&& CockpitTestUtil.isSClassSet((HtmlBasedComponent) cmp, SCLASS_ADD_COMMENT_SECTION_BUTTON_OK));
	}

	private Optional<Button> getCancelButton()
	{
		return CockpitTestUtil.findChild(commentsPopup, cmp -> (cmp instanceof Button)
				&& CockpitTestUtil.isSClassSet((HtmlBasedComponent) cmp, SCLASS_ADD_COMMENT_SECTION_BUTTON_CANCEL));
	}

	private void clickButton(final Button button)
	{
		simulateEvent(button, ON_CLICK, null);
	}
}
