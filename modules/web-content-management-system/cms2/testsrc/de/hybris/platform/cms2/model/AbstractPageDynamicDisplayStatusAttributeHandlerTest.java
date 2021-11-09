/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.SyncItemInfo;
import de.hybris.platform.catalog.synchronization.SynchronizationStatusService;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsItemDisplayStatus;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.PK;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AbstractPageDynamicDisplayStatusAttributeHandlerTest
{
	private static final String STAGED_VERSION = "Staged";
	private static final String ONLINE_VERSION = "Online";

	@Spy
	@InjectMocks
	private AbstractPageDynamicDisplayStatusAttributeHandler dynamicAttributeHandler;

	@Mock
	private CMSWorkflowService cmsWorkflowService;
	@Mock
	private SynchronizationStatusService synchronizationStatusService;

	@Mock
	private CatalogVersionModel stagedVersion;
	@Mock
	private CatalogVersionModel onlineVersion;
	@Mock
	private CatalogModel catalog;
	@Mock
	private AbstractPageModel page;
	@Mock
	private SyncItemJobModel syncJob;
	@Mock
	private SyncItemInfo syncInfo;

	@Before
	public void setUp()
	{
		when(stagedVersion.getVersion()).thenReturn(STAGED_VERSION);
		when(onlineVersion.getVersion()).thenReturn(ONLINE_VERSION);
		when(stagedVersion.getCatalog()).thenReturn(catalog);
		when(catalog.getActiveCatalogVersion()).thenReturn(onlineVersion);
		when(page.getCatalogVersion()).thenReturn(stagedVersion);
	}

	@Test
	public void testGetSynchronizationStatus()
	{
		when(syncJob.getSourceVersion()).thenReturn(stagedVersion);
		when(syncJob.getTargetVersion()).thenReturn(onlineVersion);
		when(synchronizationStatusService.getOutboundSynchronizations(page)).thenReturn(Collections.singletonList(syncJob));
		when(synchronizationStatusService.getSyncInfo(page, syncJob)).thenReturn(syncInfo);
		when(syncInfo.getSyncStatus()).thenReturn(SyncItemStatus.IN_SYNC);

		final SyncItemStatus status = dynamicAttributeHandler.getSynchronizationItemStatus(page);

		assertThat("Status should be IN_SYNC", status, equalTo(SyncItemStatus.IN_SYNC));
		verify(synchronizationStatusService).getOutboundSynchronizations(page);
		verify(synchronizationStatusService).getSyncInfo(page, syncJob);
	}

	@Test
	public void testGetSynchronizationStatusWithoutSyncJob()
	{
		when(synchronizationStatusService.getOutboundSynchronizations(page)).thenReturn(Collections.emptyList());

		final SyncItemStatus status = dynamicAttributeHandler.getSynchronizationItemStatus(page);

		assertThat("Status should be NOT_APPLICABLE when no sync job is found for newly created item", //
				status, equalTo(SyncItemStatus.NOT_APPLICABLE));
		verify(synchronizationStatusService).getOutboundSynchronizations(page);
		verify(synchronizationStatusService, never()).getSyncInfo(page, syncJob);
	}

	@Test
	public void testGetDisplayStatusForUpdatedPageNotInWorkflow()
	{
		when(page.getApprovalStatus()).thenReturn(CmsApprovalStatus.CHECK);
		when(cmsWorkflowService.isAnyItemInWorkflow(Collections.singletonList(page))).thenReturn(Boolean.FALSE);

		final CmsItemDisplayStatus status = dynamicAttributeHandler.get(page);

		assertThat("Status should be DRAFT when the page was edited and not in a workflow", status,
				equalTo(CmsItemDisplayStatus.DRAFT));
		verifyZeroInteractions(synchronizationStatusService);
	}

	@Test
	public void testGetDisplayStatusForUnsavedUpdatedPage()
	{
		when(page.getApprovalStatus()).thenReturn(CmsApprovalStatus.CHECK);
		when(page.getPk()).thenReturn(null);

		final CmsItemDisplayStatus status = dynamicAttributeHandler.get(page);

		assertThat("Status should be DRAFT when the page was edited but not saved", status, equalTo(CmsItemDisplayStatus.DRAFT));
		verifyZeroInteractions(cmsWorkflowService, synchronizationStatusService);
	}

	@Test
	public void testGetDisplayStatusForUpdatedPageInWorkflow()
	{
		when(page.getApprovalStatus()).thenReturn(CmsApprovalStatus.CHECK);
		when(page.getPk()).thenReturn(PK.BIG_PK);
		when(cmsWorkflowService.isAnyItemInWorkflow(Collections.singletonList(page))).thenReturn(Boolean.TRUE);

		final CmsItemDisplayStatus status = dynamicAttributeHandler.get(page);

		assertThat("Status should be INPROGRESS when the page was edited and in a workflow", status,
				equalTo(CmsItemDisplayStatus.IN_PROGRESS));
		verifyZeroInteractions(synchronizationStatusService);
	}

	@Test
	public void testGetDisplayStatusForUnpublishedApprovedPage()
	{
		when(page.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);
		doReturn(SyncItemStatus.NOT_SYNC).when(dynamicAttributeHandler).getSynchronizationItemStatus(page);

		final CmsItemDisplayStatus status = dynamicAttributeHandler.get(page);

		assertThat("Status should be READYTOSYNC when the page was approved but not synced", status,
				equalTo(CmsItemDisplayStatus.READY_TO_SYNC));
		verifyZeroInteractions(cmsWorkflowService);
	}

	@Test
	public void testGetDisplayStatusForPublishedApprovedPage()
	{
		when(page.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);
		when(page.getPk()).thenReturn(PK.BIG_PK);
		doReturn(SyncItemStatus.IN_SYNC).when(dynamicAttributeHandler).getSynchronizationItemStatus(page);

		final CmsItemDisplayStatus status = dynamicAttributeHandler.get(page);

		assertThat("Status should be SYNCED when the page was approved and synced", status, equalTo(CmsItemDisplayStatus.SYNCED));
		verifyZeroInteractions(cmsWorkflowService);
	}

}
