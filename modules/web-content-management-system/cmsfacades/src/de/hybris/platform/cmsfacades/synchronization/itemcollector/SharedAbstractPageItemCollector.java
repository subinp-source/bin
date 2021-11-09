/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemcollector;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cmsfacades.common.itemcollector.ItemCollector;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Collects the shared content slots of a given {@link AbstractPageModel}.
 * Returns only those content slots that are from catalog version in the active session.
 */
public class SharedAbstractPageItemCollector implements ItemCollector<AbstractPageModel>
{

	private CMSAdminContentSlotService contentSlotService;
	private Predicate<String> contentSlotExistsPredicate;

	@Override
	public List<ItemModel> collect(final AbstractPageModel item)
	{
		return getContentSlotService().getContentSlotsForPage(item) //
				.stream() //
				.filter(ContentSlotData::isFromMaster) //
				.filter(contentSlotData -> getContentSlotExistsPredicate().test(contentSlotData.getUid())) //
				.map(contentSlotData -> getContentSlotService().getContentSlotForId(contentSlotData.getUid())) //
				.collect(Collectors.toList());
	}

	protected CMSAdminContentSlotService getContentSlotService()
	{
		return contentSlotService;
	}

	@Required
	public void setContentSlotService(final CMSAdminContentSlotService contentSlotService)
	{
		this.contentSlotService = contentSlotService;
	}

	protected Predicate<String> getContentSlotExistsPredicate()
	{
		return contentSlotExistsPredicate;
	}

	@Required
	public void setContentSlotExistsPredicate(Predicate<String> contentSlotExistsPredicate)
	{
		this.contentSlotExistsPredicate = contentSlotExistsPredicate;
	}
}
