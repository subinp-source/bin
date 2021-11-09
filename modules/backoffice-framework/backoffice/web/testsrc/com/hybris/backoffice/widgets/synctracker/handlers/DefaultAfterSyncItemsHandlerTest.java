/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.synctracker.handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import de.hybris.platform.core.model.ItemModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.events.impl.DefaultCockpitEventQueue;


@RunWith(MockitoJUnitRunner.class)
public class DefaultAfterSyncItemsHandlerTest
{

	@Spy
	@InjectMocks
	private DefaultAfterSyncItemsHandler defaultAfterSyncItemsHandler;

	@Mock
	private DefaultCockpitEventQueue cockpitEventQueue;

	@Mock
	private ItemModel item1;

	@Mock
	private ItemModel item2;


	@Test
	public void shouldSendItemsUpdatedEventWhenSendGlobalEventsIsTrueAndItemsAreNotEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();
		items.add(item1);
		items.add(item2);

		//when
		defaultAfterSyncItemsHandler.handleUpdatedItems(items, true);

		//then
		verify(cockpitEventQueue).publishEvent(argThat(new ArgumentMatcher<>() {
			@Override
			public boolean matches(final Object o) {
				return ObjectCRUDHandler.OBJECTS_UPDATED_EVENT.equals(((CockpitEvent) o).getName())
						&& CollectionUtils.isEqualCollection(((CockpitEvent) o).getDataAsCollection(), items);
			}
		}));
	}

	@Test
	public void shouldNotSendItemsUpdatedEventWhenSendGlobalEventsIsFalseAndItemsAreNotEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();
		items.add(item1);
		items.add(item2);

		//when
		defaultAfterSyncItemsHandler.handleUpdatedItems(items, false);

		//then
		verify(cockpitEventQueue, never()).publishEvent(any());
	}

	@Test
	public void shouldNotSendItemsUpdatedEventWhenSendGlobalEventsIsTrueAndItemsAreEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();

		//when
		defaultAfterSyncItemsHandler.handleUpdatedItems(items, true);

		//then
		verify(cockpitEventQueue, never()).publishEvent(any());
	}

	@Test
	public void shouldNotSendItemsUpdatedEventWhenSendGlobalEventsIsFalseAndItemsAreEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();

		//when
		defaultAfterSyncItemsHandler.handleUpdatedItems(items, false);

		//then
		verify(cockpitEventQueue, never()).publishEvent(any());
	}

	@Test
	public void shouldSendItemsDeletedEventWhenSendGlobalEventsIsTrueAndItemsAreNotEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();
		items.add(item1);
		items.add(item2);

		//when
		defaultAfterSyncItemsHandler.handleDeletedItems(items, true);

		//then
		verify(cockpitEventQueue).publishEvent(argThat(new ArgumentMatcher<CockpitEvent>()
		{
			@Override
			public boolean matches(final Object o)
			{
				return ObjectCRUDHandler.OBJECTS_DELETED_EVENT.equals(((CockpitEvent) o).getName())
						&& CollectionUtils.isEqualCollection(((CockpitEvent) o).getDataAsCollection(), items);
			}
		}));
	}

	@Test
	public void shouldNotSendItemsDeletedEventWhenSendGlobalEventsIsFalseAndItemsAreNotEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();
		items.add(item1);
		items.add(item2);

		//when
		defaultAfterSyncItemsHandler.handleDeletedItems(items, false);

		//then
		verify(cockpitEventQueue, never()).publishEvent(any());
	}

	@Test
	public void shouldNotSendItemsDeletedEventWhenSendGlobalEventsIsTrueAndItemsAreEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();

		//when
		defaultAfterSyncItemsHandler.handleDeletedItems(items, true);

		//then
		verify(cockpitEventQueue, never()).publishEvent(any());
	}

	@Test
	public void shouldNotSendItemsDeletedEventWhenSendGlobalEventsIsFalseAndItemsAreEmpty()
	{
		//given
		final Set<ItemModel> items = new HashSet<>();

		//when
		defaultAfterSyncItemsHandler.handleDeletedItems(items, false);

		//then
		verify(cockpitEventQueue, never()).publishEvent(any());
	}

}
