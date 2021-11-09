/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.sync;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import org.mockito.Spy;


public class SyncActionTest extends AbstractActionUnitTest<SyncAction>
{

	@Spy
	@InjectMocks
	private SyncAction action;

	@Mock
	private ObjectFacade objectFacade;
	@Mock
	private SynchronizationFacade synchronizationFacade;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private BackofficeTypeUtils backofficeTypeUtils;

	@Override
	public SyncAction getActionInstance()
	{
		return action;
	}

	@Test
	public void testCannotPerformWithNull()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(null);

		assertThat(action.canPerform(ctx)).isFalse();
		verify(objectFacade, never()).isModified(null);
	}

	@Test
	public void testCannotPerformWithListOfNull()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ActionContext<Object> ctx = mock(ActionContext.class);
		final List<Object> items = new ArrayList<>();
		items.add(null);
		when(ctx.getData()).thenReturn(items);

		assertThat(action.canPerform(ctx)).isFalse();
		verify(objectFacade, never()).isModified(null);
	}

	@Test
	public void testCannotPerformWithListOfModifiedItems()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ItemModel itemModel = mock(ItemModel.class);
		doReturn(Boolean.TRUE).when(objectFacade).isModified(itemModel);
		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(Lists.newArrayList(itemModel));

		assertThat(action.canPerform(ctx)).isFalse();
	}

	@Test
	public void testCannotPerformWithOneModifiedItem()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ItemModel itemModel = mock(ItemModel.class);
		doReturn(Boolean.TRUE).when(objectFacade).isModified(itemModel);
		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(itemModel);

		assertThat(action.canPerform(ctx)).isFalse();
	}

	@Test
	public void testCanPerformWitListOfItems()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ItemModel itemModel = mock(ItemModel.class);
		doReturn(Boolean.FALSE).when(objectFacade).isModified(itemModel);
		doReturn(Boolean.TRUE).when(backofficeTypeUtils).isAssignableFrom(any(), any());
		doReturn(Boolean.TRUE).when(action).canSync(itemModel);

		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(Lists.newArrayList(itemModel));

		assertThat(action.canPerform(ctx)).isTrue();
	}

	@Test
	public void testCanPerformOneItem()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ItemModel itemModel = mock(ItemModel.class);
		doReturn(Boolean.FALSE).when(objectFacade).isModified(itemModel);
		doReturn(Boolean.TRUE).when(backofficeTypeUtils).isAssignableFrom(any(), any());
		doReturn(Boolean.TRUE).when(action).canSync(itemModel);
		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(itemModel);

		assertThat(action.canPerform(ctx)).isTrue();
	}

	@Test
	public void testCannotPerformOneItemWhenSynchronizedObjectInstanceIsNotAssignableFromItemModel()
	{
		setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects();
		final ItemModel itemModel = mock(ItemModel.class);
		doReturn(Boolean.FALSE).when(objectFacade).isModified(itemModel);
		doReturn(Boolean.FALSE).when(backofficeTypeUtils).isAssignableFrom(any(), any());
		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(itemModel);

		assertThat(action.canPerform(ctx)).isFalse();
	}

	@Test
	public void testCannotPerformOneItemWhenSynchronizedObjectInstanceIsAssignableFromItemModelButHasNotAssignedCatalogVersion()
	{
		setupSynchronizationFacadeReturnAlwaysNoCatalogVersionForAnyObjects();
		final ItemModel itemModel = mock(ItemModel.class);
		doReturn(Boolean.FALSE).when(objectFacade).isModified(itemModel);
		doReturn(Boolean.TRUE).when(backofficeTypeUtils).isAssignableFrom(any(), any());
		final ActionContext<Object> ctx = mock(ActionContext.class);
		when(ctx.getData()).thenReturn(itemModel);

		assertThat(action.canPerform(ctx)).isFalse();
	}

	public void setupSynchronizationFacadeReturnAlwaysCatalogVersionForAnyObjects()
	{
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		when(synchronizationFacade.getSyncCatalogVersion(any())).thenReturn(Optional.of(catalogVersionModel));
	}

	public void setupSynchronizationFacadeReturnAlwaysNoCatalogVersionForAnyObjects()
	{
		when(synchronizationFacade.getSyncCatalogVersion(any())).thenReturn(Optional.empty());
	}

	@Test
	public void canSyncShouldReturnFalseIfThereAreNoExecutableSyncItems()
	{
	    //given
		doReturn(Optional.empty()).when(synchronizationFacade).getSyncCatalogVersion(anyCollection());

	    //when
		final boolean result = action.canSync(mock(ItemModel.class));

		//then
		assertThat(result).isFalse();
	}

	@Test
	public void canSyncShouldReturnTrueIfExecutableOutboundSyncItemExists()
	{
		//given
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		doReturn(Optional.of(catalogVersionModel)).when(synchronizationFacade).getSyncCatalogVersion(anyCollection());
		doReturn(Collections.emptyList()).when(synchronizationFacade).getInboundSynchronizations(any());
		final SyncItemJobModel sync = mock(SyncItemJobModel.class);
		doReturn(Collections.singletonList(sync)).when(synchronizationFacade).getOutboundSynchronizations(any());
		doReturn(true).when(synchronizationFacade).canSync(sync);

		//when
		final boolean result = action.canSync(mock(ItemModel.class));

		//then
		assertThat(result).isTrue();
	}

	@Test
	public void canSyncShouldReturnTrueIfExecutableInboundSyncItemExists()
	{
		//given
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		doReturn(Optional.of(catalogVersionModel)).when(synchronizationFacade).getSyncCatalogVersion(anyCollection());
		doReturn(Collections.emptyList()).when(synchronizationFacade).getOutboundSynchronizations(any());
		final SyncItemJobModel sync = mock(SyncItemJobModel.class);
		doReturn(Collections.singletonList(sync)).when(synchronizationFacade).getInboundSynchronizations(any());
		doReturn(true).when(synchronizationFacade).canSync(sync);

		//when
		final boolean result = action.canSync(mock(ItemModel.class));

		//then
		assertThat(result).isTrue();
	}
}
