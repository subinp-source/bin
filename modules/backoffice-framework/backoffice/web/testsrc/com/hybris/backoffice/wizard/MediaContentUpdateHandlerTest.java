/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.wizard;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


@RunWith(MockitoJUnitRunner.class)
public class MediaContentUpdateHandlerTest
{
	public static final String NEW_MEDIA = "newMedia";
	public static final String MEDIA_CONTENT = "mediaContent";

	@InjectMocks
	private MediaContentUpdateHandler handler;
	@Mock
	private MediaService mediaService;
	@Mock
	private ObjectFacade objectFacade;
	@Mock
	private FlowActionHandlerAdapter adapter;
	@Mock
	private CustomType customType;
	@Mock
	private MediaModel newMedia;
	@Mock
	private ModelService modelService;
	@Mock
	private FileUploadResult mediaContent;
	@Mock
	private WidgetInstanceManager widgetInstanceManager;
	@Mock
	private NotificationService notificationService;

	private HashMap<String, String> params;

	@Before
	public void setUp()
	{
		widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();

		params = new HashMap<>();
		params.put(MediaContentUpdateHandler.MEDIA_CONTENT_PROPERTY, MEDIA_CONTENT);
		params.put(MediaContentUpdateHandler.MEDIA_PROPERTY, NEW_MEDIA);

		when(adapter.getWidgetInstanceManager()).thenReturn(widgetInstanceManager);
		widgetInstanceManager.getModel().setValue(NEW_MEDIA, newMedia);
		widgetInstanceManager.getModel().setValue(MEDIA_CONTENT, mediaContent);
	}

	@Test
	public void shouldSetMetaData()
	{
		when(objectFacade.isModified(newMedia)).thenReturn(false);
		when(mediaContent.getName()).thenReturn("product-image.gif");
		when(mediaContent.getContentType()).thenReturn("image/gif");
		when(mediaContent.getData()).thenReturn(new byte[1]);

		handler.perform(customType, adapter, params);

		verify(newMedia).setRealFileName(mediaContent.getName());
		verify(newMedia).setMime(mediaContent.getContentType());
		verify(mediaService).setDataForMedia(newMedia, mediaContent.getData());
	}

	@Test
	public void shouldCreatedMediaBeRollbackedWhenSettingContentFails() throws ObjectSavingException, ObjectDeletionException
	{
		// given
		given(modelService.clone(newMedia)).willReturn(newMedia);
		willThrow(ObjectSavingException.class).given(mediaService).setDataForMedia(any(), any());

		// expect
		assertThatThrownBy(() -> handler.perform(customType, adapter, params)).isInstanceOf(ModelSavingException.class);
		then(objectFacade).should().delete(newMedia);
	}

	@Test
	public void shouldContentNotBeSetWhenItIsEmpty()
	{
		// given
		widgetInstanceManager.getModel().setValue(MEDIA_CONTENT, null);

		// when
		handler.perform(customType, adapter, params);

		// then
		then(mediaService).should(never()).setDataForMedia(any(), any());
	}

	@Test
	public void shouldSuccessNotificationBeShownWhenMediaIsSavedCorrectly()
	{
		// when
		handler.perform(customType, adapter, params);

		// then
		then(notificationService).should().notifyUser(anyString(), anyString(), eq(NotificationEvent.Level.SUCCESS), any());
	}

	@Test
	public void performShouldSaveNewMediaWithNoContent() throws ObjectSavingException {
	    //given
		doReturn(true).when(modelService).isNew(modelService);
		widgetInstanceManager.getModel().setValue(MEDIA_CONTENT, null);

	    //when
		handler.perform(customType, adapter, params);

	    //then
		verify(objectFacade).save(newMedia);
		verifyNoMoreInteractions(newMedia);
	}

	@Test
	public void performShouldEmptyUpdateMediaContentOnNUllContentPassed() throws ObjectSavingException {
	    //given
		doReturn(false).when(modelService).isNew(modelService);
		widgetInstanceManager.getModel().setValue(MEDIA_CONTENT, null);

	    //when
		handler.perform(customType, adapter, params);

	    //then
		verify(objectFacade).save(newMedia);
		verify(mediaService).removeDataFromMedia(newMedia);
		verifyNoMoreInteractions(newMedia);
	}

}
