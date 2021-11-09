/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.postaction.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.synchronization.SyncConfig;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.cmsfacades.data.SyncRequestData;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.cmsfacades.synchronization.service.ItemSynchronizationService;
import de.hybris.platform.cmsfacades.workflow.postaction.PostWorkflowAction;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Automatic action performed after the completion of the Publish action in the workflow.
 * <p>
 * Note: Purposefully not implemented as an {@link AutomatedWorkflowTemplateJob}; see CMSX-8521 for more details.
 */
public class PublishPostWorkflowAction implements PostWorkflowAction
{
	private static final String PUBLISH_DECISION = "Publish";

	private ItemSynchronizationService itemSynchronizationService;
	private SyncConfig syncConfig;
	private CMSWorkflowActionService workflowActionService;

	@Override
	public BiPredicate<WorkflowModel, CMSWorkflowOperationData> isApplicable()
	{
		return (final WorkflowModel workflow, final CMSWorkflowOperationData operationData) -> {
			if (operationData.getOperation() == CMSWorkflowOperation.MAKE_DECISION)
			{
				final WorkflowActionModel workflowAction = getWorkflowActionService().getWorkflowActionForCode(workflow,
						operationData.getActionCode());
				final WorkflowDecisionTemplateModel decisionTemplate = workflowAction.getTemplate().getDecisionTemplates().stream()
						.findFirst().orElse(null);

				if (Objects.nonNull(decisionTemplate) && decisionTemplate.getCode().contains(PUBLISH_DECISION))
				{
					return true;
				}
			}
			return false;
		};
	}

	@Override
	public void execute(final WorkflowModel workflow)
	{
		final List<CMSItemModel> items = workflow.getAttachments().stream() //
				.map(WorkflowItemAttachmentModel::getItem) //
				.filter(item -> CMSItemModel.class.isAssignableFrom(item.getClass())) //
				.map(item -> (CMSItemModel) item) //
				.collect(Collectors.toList());

		publishItems(items);
	}

	/**
	 * Synchronizes items from a non-active catalog version to the respective active catalog version.
	 *
	 * @param items
	 *           the items to be synchronized
	 */
	protected void publishItems(final List<CMSItemModel> items)
	{
		findNonActiveItemsGroupedByCatalog(items).values().forEach(itemSet -> {
			final Map<String, Set<CMSItemModel>> itemsGroupedByCatalogVersion = itemSet.stream()
					.collect(Collectors.groupingBy(key -> key.getCatalogVersion().getVersion(), Collectors.toSet()));
			itemsGroupedByCatalogVersion.entrySet().forEach(this::performItemsSync);
		});
	}

	/**
	 * Synchronizes all items defined in the same catalog version to the associated active catalog version.
	 *
	 * @param itemsByCatalogVersionEntry
	 *           the entry containing the source catalog version name and a collection of items that are defined in the
	 *           same source catalog version.
	 */
	protected void performItemsSync(final Entry<String, Set<CMSItemModel>> itemsByCatalogVersionEntry)
	{
		final SyncRequestData syncRequestData = buildSyncRequestData(itemsByCatalogVersionEntry);
		final List<ItemModel> itemsToSync = itemsByCatalogVersionEntry.getValue().stream() //
				.map(cmsItem -> (ItemModel) cmsItem) //
				.collect(Collectors.toList());
		getItemSynchronizationService().performItemSynchronization(syncRequestData, itemsToSync, getSyncConfig());
	}

	/**
	 * Creates a {@link SyncRequestData} object determining the catalog and catalog versions to be used by the
	 * synchronization job.
	 *
	 * @param itemsByCatalogVersionEntry
	 *           the entry containing the source catalog version name and a collection of items that are defined in the
	 *           same source catalog version.
	 */
	protected SyncRequestData buildSyncRequestData(final Entry<String, Set<CMSItemModel>> itemsByCatalogVersionEntry)
	{
		final String sourceVersionId = itemsByCatalogVersionEntry.getKey();
		final CMSItemModel item = itemsByCatalogVersionEntry.getValue().stream().findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Items collection cannot be empty"));
		final CatalogModel catalog = item.getCatalogVersion().getCatalog();
		final String targetVersionId = catalog.getActiveCatalogVersion().getVersion();

		final SyncRequestData syncRequestData = new SyncRequestData();
		syncRequestData.setCatalogId(catalog.getId());
		syncRequestData.setSourceVersionId(sourceVersionId);
		syncRequestData.setTargetVersionId(targetVersionId);
		return syncRequestData;
	}

	/**
	 * Find all items defined in a non-active catalog version and group the items by catalog.
	 *
	 * @return a map of {@code <catalogId, CMSItems>}
	 */
	protected Map<String, Set<CMSItemModel>> findNonActiveItemsGroupedByCatalog(final List<CMSItemModel> items)
	{
		return items.stream() //
				.filter(item -> Objects.isNull(item.getCatalogVersion().getActive()) || !item.getCatalogVersion().getActive()) //
				.collect(Collectors.groupingBy(key -> key.getCatalogVersion().getCatalog().getId(), Collectors.toSet()));
	}

	protected ItemSynchronizationService getItemSynchronizationService()
	{
		return itemSynchronizationService;
	}

	@Required
	public void setItemSynchronizationService(final ItemSynchronizationService itemSynchronizationService)
	{
		this.itemSynchronizationService = itemSynchronizationService;
	}

	protected SyncConfig getSyncConfig()
	{
		return syncConfig;
	}

	@Required
	public void setSyncConfig(final SyncConfig syncConfig)
	{
		this.syncConfig = syncConfig;
	}

	protected CMSWorkflowActionService getWorkflowActionService()
	{
		return workflowActionService;
	}

	@Required
	public void setWorkflowActionService(final CMSWorkflowActionService workflowActionService)
	{
		this.workflowActionService = workflowActionService;
	}

}
