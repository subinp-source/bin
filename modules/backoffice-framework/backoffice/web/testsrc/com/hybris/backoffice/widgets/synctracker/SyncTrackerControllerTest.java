/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.synctracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.hybris.backoffice.BackofficeTestUtil;
import com.hybris.backoffice.events.processes.ProcessFinishedEvent;
import com.hybris.backoffice.sync.SyncTask;
import com.hybris.backoffice.sync.SyncTaskExecutionInfo;
import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;


@DeclaredGlobalCockpitEvent(eventName = ProcessFinishedEvent.EVENT_NAME, scope = CockpitEvent.APPLICATION)
@DeclaredGlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.APPLICATION)
@DeclaredInput(value = SyncTrackerController.SOCKET_IN_SYNC_TASK, socketType = SyncTaskExecutionInfo.class)
@ExtensibleWidget(level = ExtensibleWidget.ALL)
@NullSafeWidget
public class SyncTrackerControllerTest extends AbstractWidgetUnitTest<SyncTrackerController>
{

	private static final String CRON_JOB_CODE = "cronJobCode";
	private static final long PK1 = 1L;
	private static final long PK2 = 2L;
	private static final long PK3 = 3L;

	@InjectMocks
	private SyncTrackerController controller;
	@Mock
	private transient ObjectFacade objectFacade;
	@Mock
	private transient CockpitEventQueue cockpitEventQueue;
	@Mock
	private transient SynchronizationFacade synchronizationFacade;
	@Mock
	private transient AfterSyncItemsHandler afterSyncItemsHandler;



	@Override
	protected SyncTrackerController getWidgetController()
	{
		return controller;
	}

	@Test
	public void shouldStartTrackingNewSynchronization()
	{
		//given
		final SyncTaskExecutionInfo executionInfo = mockExecutionInfo(CRON_JOB_CODE, PK1, PK2);

		//when
		executeInputSocketEvent(SyncTrackerController.SOCKET_IN_SYNC_TASK, executionInfo);

		//then
		assertThat(getTrackingMap().get(CRON_JOB_CODE)).containsOnly(String.valueOf(PK1), String.valueOf(PK2));
	}

	@Test
	public void shouldSendUpdateSockedAndCallAfterSyncItemsHandler()
	{
		//given
		widgetSettings.put(SyncTrackerController.SETTING_SEND_GLOBAL_EVENT, Boolean.TRUE, Boolean.class);
		final SyncTaskExecutionInfo executionInfo = mockExecutionInfo(CRON_JOB_CODE, PK1, PK2);
		executeInputSocketEvent(SyncTrackerController.SOCKET_IN_SYNC_TASK, executionInfo);

		//when
		final ProcessFinishedEvent processFinishedEvent = mockProcessFinishedEvent(CRON_JOB_CODE);
		final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(ProcessFinishedEvent.EVENT_NAME, processFinishedEvent,
				null);
		executeGlobalEvent(ProcessFinishedEvent.EVENT_NAME, CockpitEvent.APPLICATION, cockpitEvent);

		//then
		assertSocketOutput(SyncTrackerController.SOCKET_OUT_SYNCED_ITEMS, (Predicate<List<ItemModel>>) itemModels -> CollectionUtils
				.isEqualCollection(itemModels, executionInfo.getSyncTask().getItems()));
		assertThat(getTrackingMap().get(CRON_JOB_CODE)).isNull();

		verify(afterSyncItemsHandler).handleDeletedItems(eq(new HashSet<>()), eq(true));
		verify(afterSyncItemsHandler).handleUpdatedItems(eq(new HashSet<>(executionInfo.getSyncTask().getItems())), eq(true));
	}

	@Test
	public void shouldTrackWithCounterpartItems()
	{
		//given
		widgetSettings.put(SyncTrackerController.SETTING_FIND_SYNC_COUNTERPARTS, Boolean.TRUE, Boolean.class);
		final SyncTaskExecutionInfo executionInfo = mockExecutionInfo(CRON_JOB_CODE, PK1, PK2);
		final SyncItemJobModel syncItemJob = executionInfo.getSyncTask().getSyncItemJob();
		final ItemModel item1 = executionInfo.getSyncTask().getItems().get(0);
		final ItemModel item2 = executionInfo.getSyncTask().getItems().get(1);
		final ItemModel counterPartItem = mockItemWithPK(PK3);
		when(synchronizationFacade.findSyncCounterpart(item1, syncItemJob)).thenReturn(Optional.of(counterPartItem));
		when(synchronizationFacade.findSyncCounterpart(item2, syncItemJob)).thenReturn(Optional.empty());

		//when
		executeInputSocketEvent(SyncTrackerController.SOCKET_IN_SYNC_TASK, executionInfo);

		//then
		assertThat(getTrackingMap().get(CRON_JOB_CODE)).containsOnly(String.valueOf(PK1), String.valueOf(PK2), String.valueOf(PK3));
	}

	@Test
	public void shouldGetCatalogVersionCounterPart()
	{
		shouldSendCounterPartCatalogVersion(false);
		shouldSendCounterPartCatalogVersion(false);
	}

	public void shouldSendCounterPartCatalogVersion(final boolean fromSource)
	{
		//given
		widgetSettings.put(SyncTrackerController.SETTING_FIND_SYNC_COUNTERPARTS, Boolean.TRUE, Boolean.class);
		final CatalogVersionModel srcCV = new CatalogVersionModel();
		BackofficeTestUtil.setPk(srcCV, PK1);
		final CatalogVersionModel targetCV = new CatalogVersionModel();
		BackofficeTestUtil.setPk(targetCV, PK2);
		final SyncItemJobModel syncItemJob = mock(SyncItemJobModel.class);
		when(syncItemJob.getSourceVersion()).thenReturn(srcCV);
		when(syncItemJob.getTargetVersion()).thenReturn(targetCV);

		final SyncTask syncTask = new SyncTask(Lists.newArrayList(fromSource ? srcCV : targetCV), syncItemJob);
		final SyncTaskExecutionInfo executionInfo = new SyncTaskExecutionInfo(syncTask, CRON_JOB_CODE);

		//when
		executeInputSocketEvent(SyncTrackerController.SOCKET_IN_SYNC_TASK, executionInfo);

		//then
		assertThat(getTrackingMap().get(CRON_JOB_CODE)).containsOnly(String.valueOf(PK1), String.valueOf(PK2));
	}

	protected ProcessFinishedEvent mockProcessFinishedEvent(final String cronJobCode)
	{
		final AfterCronJobFinishedEvent cronJobEvent = mock(AfterCronJobFinishedEvent.class);
		when(cronJobEvent.getCronJob()).thenReturn(cronJobCode);
		return new ProcessFinishedEvent(cronJobEvent);
	}

	protected Map<String, Set<String>> getTrackingMap()
	{
		return (Map<String, Set<String>>) widgetModel.getValue(SyncTrackerController.MODEL_TRACKED_SYNCHRONIZATIONS, Map.class);
	}

	protected SyncTaskExecutionInfo mockExecutionInfo(final String cronJobCode, final long... pks)
	{
		final CatalogVersionModel srcCV = mock(CatalogVersionModel.class);
		final CatalogVersionModel targetCV = mock(CatalogVersionModel.class);
		final SyncItemJobModel syncItemJob = mock(SyncItemJobModel.class);
		when(syncItemJob.getSourceVersion()).thenReturn(srcCV);
		when(syncItemJob.getTargetVersion()).thenReturn(targetCV);

		final List<ItemModel> items = new ArrayList<>();
		for (final long pk : pks)
		{
			final ItemModel item = mockItemWithPK(pk);
			items.add(item);

		}

		final SyncTask syncTask = new SyncTask(items, syncItemJob);
		return new SyncTaskExecutionInfo(syncTask, cronJobCode);
	}

	private ItemModel mockItemWithPK(final long pk)
	{
		final ItemModel item = new ProductModel();
		BackofficeTestUtil.setPk(item, pk);
		try
		{
			when(objectFacade.load(String.valueOf(pk))).thenReturn(item);
		}
		catch (final ObjectNotFoundException e)
		{
			e.printStackTrace();
		}
		return item;
	}

	@Test
	public void shouldUpdateTrackingMapWhenUpdatedProductIsInTrackedCatalogVersionSynchronization()
	{
		// given
		// catalog version is being tracked
		final PK trackedCatalogVersionPk = PK.fromLong(101L);
		final CatalogVersionModel trackedCatalogVersion = mock(CatalogVersionModel.class);
		when(trackedCatalogVersion.getPk()).thenReturn(trackedCatalogVersionPk);
		final Set<String> trackedCatalogVersionsPks = new HashSet<>();
		trackedCatalogVersionsPks.add(String.valueOf(trackedCatalogVersionPk));
		controller.getTrackingMap().put(CRON_JOB_CODE, trackedCatalogVersionsPks);

		final PK updatedProductPk = PK.fromLong(201L);
		final ProductModel updatedProduct = mock(ProductModel.class);
		when(updatedProduct.getPk()).thenReturn(updatedProductPk);

		final CockpitEvent cockpitEventWithUpdatedProduct = mock(CockpitEvent.class);
		when(cockpitEventWithUpdatedProduct.getDataAsCollection()).thenReturn(Collections.singleton(updatedProduct));

		// updated product is in tracked catalog version
		when(synchronizationFacade.getSyncCatalogVersion(Collections.singletonList(updatedProduct)))
				.thenReturn(Optional.of(trackedCatalogVersion));

		// when
		executeGlobalEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, CockpitEvent.APPLICATION, cockpitEventWithUpdatedProduct);

		// then
		// catalog version is being tracked
		assertThat(controller.getTrackingMap().get(CRON_JOB_CODE)).contains(String.valueOf(trackedCatalogVersionPk));

		// updated product is also being tracked
		assertThat(controller.getTrackingMap().get(CRON_JOB_CODE)).contains(String.valueOf(updatedProductPk));
	}

	@Test
	public void shouldNotUpdateTrackingMapWhenUpdatedProductIsInCatalogVersionThatIsNotBeingSynchronized()
	{
		// given
		final PK updatedProductPk = PK.fromLong(201L);
		final ProductModel updatedProduct = mock(ProductModel.class);
		when(updatedProduct.getPk()).thenReturn(updatedProductPk);

		final CockpitEvent cockpitEventWithUpdatedProduct = mock(CockpitEvent.class);
		when(cockpitEventWithUpdatedProduct.getDataAsCollection()).thenReturn(Collections.singleton(updatedProduct));

		// updated product is in catalog version that is not being tracked
		when(synchronizationFacade.getSyncCatalogVersion(Collections.singletonList(updatedProduct))).thenReturn(Optional.empty());

		// when
		executeGlobalEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, CockpitEvent.APPLICATION, cockpitEventWithUpdatedProduct);

		// then: product is not being tracked
		assertThat(controller.getTrackingMap()).isEmpty();
	}

	@Test
	public void shouldAddItemToDeletedIfItemCannotBeLoaded() throws ObjectNotFoundException
	{
		//given
		widgetSettings.put(SyncTrackerController.SETTING_SEND_GLOBAL_EVENT, Boolean.TRUE, Boolean.class);

		final SyncTaskExecutionInfo executionInfo = mockExecutionInfo(CRON_JOB_CODE, PK1, PK2);

		final ItemModel itemModel1 = executionInfo.getSyncTask().getItems().stream()
				.filter(item -> item.getPk().toString().equals(String.valueOf(PK1))).findFirst().get();
		final ItemModel itemModel2 = executionInfo.getSyncTask().getItems().stream()
				.filter(item -> item.getPk().toString().equals(String.valueOf(PK2))).findFirst().get();

		when(objectFacade.load(eq(String.valueOf(PK1)))).thenThrow(ObjectNotFoundException.class);

		final ProcessFinishedEvent processFinishedEvent = mockProcessFinishedEvent(CRON_JOB_CODE);
		final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(ProcessFinishedEvent.EVENT_NAME, processFinishedEvent,
				null);

		//when
		executeInputSocketEvent(SyncTrackerController.SOCKET_IN_SYNC_TASK, executionInfo);
		executeGlobalEvent(ProcessFinishedEvent.EVENT_NAME, CockpitEvent.APPLICATION, cockpitEvent);

		//then
		verify(afterSyncItemsHandler).handleDeletedItems(eq(Collections.singleton(itemModel1)), eq(true));
		verify(afterSyncItemsHandler).handleUpdatedItems(eq(new HashSet<>(Collections.singleton(itemModel2))), eq(true));
	}

}
