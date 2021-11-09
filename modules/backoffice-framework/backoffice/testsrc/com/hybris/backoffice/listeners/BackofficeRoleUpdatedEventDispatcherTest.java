/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.listeners;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;

import java.util.ArrayList;
import java.util.Collection;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.model.user.BackofficeRoleModel;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeRoleUpdatedEventDispatcherTest
{

	private static final int BACKOFFICE_ROLE_DEPLOYMENT_CODE = 5;

	private static final PK PK_USER_GROUP = PK.createFixedUUIDPK(BACKOFFICE_ROLE_DEPLOYMENT_CODE, 1);
	private static final BackofficeRoleModel ROLE_MODEL = mock(BackofficeRoleModel.class);

	@InjectMocks
	@Spy
	private BackofficeRoleUpdatedEventDispatcher testSubject;

	@Mock
	private ModelService modelService;
	@Mock
	private EventService eventService;


	@Test
	public void shouldSendBackofficeRoleClusterAwareEventWhenBackofficeRoleUpdateIsPerformed()
	{
		//when
		when(testSubject.shouldPerform()).thenReturn(true);

		//then
		testSubject.afterSave(getEventsWithBackofficeRoleUpdateEvents());

		//verify
		final ArgumentCaptor<BackofficeRoleUpdatedClusterAwareEvent> roleCapture = ArgumentCaptor
				.forClass(BackofficeRoleUpdatedClusterAwareEvent.class);
		verify(eventService).publishEvent(roleCapture.capture());

		final BackofficeRoleUpdatedClusterAwareEvent event = roleCapture.getValue();
		Assertions.assertThat(event.getRoles()).containsOnly(PK_USER_GROUP);
	}

	@Test
	public void shouldNotBackofficeRoleClusterAwareEventWhenBackofficeRoleUpdateIsNotPerformed()
	{
		//when
		when(testSubject.shouldPerform()).thenReturn(true);

		//then
		testSubject.afterSave(getEventsWithUserGroupUpdateEvents());

		//verify
		verify(eventService, times(0)).publishEvent(anyObject());
	}

	private Collection<AfterSaveEvent> getEventsWithBackofficeRoleUpdateEvents()
	{
		final Collection<AfterSaveEvent> events = createAfterSaveEventCollection();

		when(ROLE_MODEL.getItemtype()).thenReturn(BackofficeRoleModel._TYPECODE);
		when(modelService.get(PK_USER_GROUP)).thenReturn(ROLE_MODEL);

		return events;
	}

	private Collection<AfterSaveEvent> getEventsWithUserGroupUpdateEvents()
	{
		final Collection<AfterSaveEvent> events = createAfterSaveEventCollection();
		final UserGroupModel userGroupModel = mock(UserGroupModel.class);

		when(userGroupModel.getItemtype()).thenReturn(UserGroupModel._TYPECODE);
		when(modelService.get(PK_USER_GROUP)).thenReturn(userGroupModel);

		return events;
	}

	private Collection<AfterSaveEvent> createAfterSaveEventCollection()
	{
		final AfterSaveEvent afterSaveEvent = mock(AfterSaveEvent.class);
		when(afterSaveEvent.getPk()).thenReturn(PK_USER_GROUP);

		final Collection<AfterSaveEvent> events = new ArrayList<>();
		events.add(afterSaveEvent);

		return events;
	}


}
