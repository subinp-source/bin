/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if the given CmsItems are already part of an active (RUNNING or PAUSED) workflow instance.
 * <p>
 * Returns <tt>TRUE</tt> if the given CmsItems are already part of an active workflow instance; <tt>FALSE</tt>
 * otherwise.
 * </p>
 */
public class IsAnyItemAlreadyInWorkflowPredicate implements Predicate<List<String>>
{
	private static final Logger LOG = LoggerFactory.getLogger(IsAnyItemAlreadyInWorkflowPredicate.class);

	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private CMSWorkflowService cmsWorkflowService;

	@Override
	public boolean test(final List<String> cmsItemUuids)
	{
		boolean result;
		try
		{
			final List<CMSItemModel> itemsUid = cmsItemUuids.stream().map(this::getCmsItem).collect(Collectors.toList());

			result = getCmsWorkflowService().isAnyItemInWorkflow(itemsUid);
		}
		catch (final UnknownIdentifierException | ConversionException | NoSuchElementException e)
		{
			LOG.debug("One of the given items is invalid", e);
			result = false;
		}

		return result;
	}

	protected CMSItemModel getCmsItem(final String itemUuid)
	{
		return getUniqueItemIdentifierService().getItemModel(itemUuid, CMSItemModel.class)
				.orElseThrow(() -> new NoSuchElementException("Cannot find element"));
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected CMSWorkflowService getCmsWorkflowService()
	{
		return cmsWorkflowService;
	}

	@Required
	public void setCmsWorkflowService(final CMSWorkflowService cmsWorkflowService)
	{
		this.cmsWorkflowService = cmsWorkflowService;
	}
}
