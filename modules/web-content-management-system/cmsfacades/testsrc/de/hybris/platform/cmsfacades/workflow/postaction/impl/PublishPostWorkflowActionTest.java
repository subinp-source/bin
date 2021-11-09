/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.postaction.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.synchronization.SyncConfig;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.cmsfacades.data.SyncRequestData;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.cmsfacades.synchronization.service.ItemSynchronizationService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PublishPostWorkflowActionTest
{
	private static final String WORKFLOW_NAME = "test-workflow-name";
	private static final String CATALOG_ID = "test-catalog";
	private static final String ACTIVE_CATALOG_VERSION_NAME = "test-catalog-version-active";
	private static final String CATALOG_VERSION_NAME = "test-catalog-version";
	private static final String ACTION_CODE = "test-action";

	@Spy
	@InjectMocks
	private PublishPostWorkflowAction publishAction;

	@Mock
	private ItemSynchronizationService itemSynchronizationService;
	@Mock
	private SyncConfig syncConfig;
	@Mock
	private CMSWorkflowActionService workflowActionService;

	@Mock
	private CMSItemModel cmsItem;
	@Mock
	private CMSItemModel cmsItem2;
	@Mock
	private CMSItemModel cmsItem3;
	@Mock
	private CatalogModel catalog;
	@Mock
	private CatalogVersionModel activeCatalogVersion;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private Entry<String, Set<CMSItemModel>> itemsByCatalogVersionEntry;
	@Mock
	private WorkflowActionModel workflowAction;
	@Mock
	private WorkflowModel workflow;
	@Mock
	private WorkflowItemAttachmentModel itemAttachment;
	@Mock
	private WorkflowItemAttachmentModel itemAttachment2;
	@Mock
	private WorkflowDecisionModel decision;
	@Mock
	private CMSWorkflowOperationData operationData;
	@Mock
	private WorkflowDecisionTemplateModel decisionTemplate;
	@Mock
	private WorkflowActionTemplateModel actionTemplate;

	@Before
	public void setUp()
	{
		when(catalog.getId()).thenReturn(CATALOG_ID);
		when(catalog.getActiveCatalogVersion()).thenReturn(activeCatalogVersion);
		when(catalogVersion.getCatalog()).thenReturn(catalog);
		when(catalogVersion.getVersion()).thenReturn(CATALOG_VERSION_NAME);
		when(catalogVersion.getActive()).thenReturn(Boolean.FALSE);
		when(activeCatalogVersion.getVersion()).thenReturn(ACTIVE_CATALOG_VERSION_NAME);
		when(activeCatalogVersion.getActive()).thenReturn(Boolean.TRUE);
		when(cmsItem.getCatalogVersion()).thenReturn(catalogVersion);

		when(operationData.getOperation()).thenReturn(CMSWorkflowOperation.MAKE_DECISION);
		when(operationData.getActionCode()).thenReturn(ACTION_CODE);
		when(workflowAction.getTemplate()).thenReturn(actionTemplate);
		when(actionTemplate.getDecisionTemplates()).thenReturn(Collections.singletonList(decisionTemplate));
		when(workflowActionService.getWorkflowActionForCode(workflow, ACTION_CODE)).thenReturn(workflowAction);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailBuildSyncRequestDataWhenEntryHasNoValue()
	{
		when(itemsByCatalogVersionEntry.getValue()).thenReturn(Collections.emptySet());

		publishAction.buildSyncRequestData(itemsByCatalogVersionEntry);
	}

	@Test
	public void testBuildSyncRequestData()
	{
		when(itemsByCatalogVersionEntry.getValue()).thenReturn(Collections.singleton(cmsItem));
		when(itemsByCatalogVersionEntry.getKey()).thenReturn(CATALOG_VERSION_NAME);

		final SyncRequestData buildSyncRequestData = publishAction.buildSyncRequestData(itemsByCatalogVersionEntry);

		assertThat(buildSyncRequestData.getCatalogId(), equalTo(CATALOG_ID));
		assertThat(buildSyncRequestData.getSourceVersionId(), equalTo(CATALOG_VERSION_NAME));
		assertThat(buildSyncRequestData.getTargetVersionId(), equalTo(ACTIVE_CATALOG_VERSION_NAME));
	}

	@Test
	public void testFindNonActiveItemsGroupedByCatalog()
	{
		when(cmsItem2.getCatalogVersion()).thenReturn(activeCatalogVersion);
		when(cmsItem3.getCatalogVersion()).thenReturn(catalogVersion);
		when(catalogVersion.getActive()).thenReturn(null).thenReturn(Boolean.FALSE);

		final Map<String, Set<CMSItemModel>> result = publishAction
				.findNonActiveItemsGroupedByCatalog(Arrays.asList(cmsItem, cmsItem2, cmsItem3));

		assertThat(result.values().iterator().next(), containsInAnyOrder(cmsItem, cmsItem3));
	}

	@Test
	public void testPublishItems()
	{
		final Map<String, Set<CMSItemModel>> itemsByCatalog = new HashMap<>();
		itemsByCatalog.put(CATALOG_ID, Sets.newHashSet(cmsItem, cmsItem2));
		doReturn(itemsByCatalog).when(publishAction).findNonActiveItemsGroupedByCatalog(any());
		when(cmsItem2.getCatalogVersion()).thenReturn(catalogVersion);

		publishAction.publishItems(Arrays.asList(cmsItem, cmsItem2, cmsItem3));

		verify(publishAction).performItemsSync(any());
	}

	@Test
	public void testExecuteAction()
	{
		when(workflowAction.getWorkflow()).thenReturn(workflow);
		when(workflowAction.getDecisions()).thenReturn(Collections.singleton(decision));
		when(workflow.getAttachments()).thenReturn(Arrays.asList(itemAttachment, itemAttachment2));
		when(workflow.getName()).thenReturn(WORKFLOW_NAME);
		when(itemAttachment.getItem()).thenReturn(cmsItem);
		when(itemAttachment2.getItem()).thenReturn(cmsItem2);
		doNothing().when(publishAction).publishItems(any());

		publishAction.execute(workflow);

		verify(publishAction).publishItems(Arrays.asList(cmsItem, cmsItem2));
	}

	@Test
	public void testIsApplicableToPublishDecision()
	{
		when(decisionTemplate.getCode()).thenReturn("PagePublished");

		assertTrue(publishAction.isApplicable().test(workflow, operationData));
	}

	@Test
	public void testIsNotApplicableToApproveDecision()
	{
		when(decisionTemplate.getCode()).thenReturn("PageApproved");

		assertFalse(publishAction.isApplicable().test(workflow, operationData));
	}

	@Test
	public void testIsNotApplicableToCancelOperation()
	{
		when(operationData.getOperation()).thenReturn(CMSWorkflowOperation.CANCEL_WORKFLOW);

		assertFalse(publishAction.isApplicable().test(workflow, operationData));
	}

}
