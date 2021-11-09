/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract class contains default implementation for {@link CMSItemPropertiesSupplier}.
 * It provides the default group name for all SmartEdit suppliers and default implementation
 * for {@link CMSItemPropertiesSupplier#isEnabled(CMSItemModel)}.
 */
public abstract class AbstractSmarteditItemPropertiesSupplier implements CMSItemPropertiesSupplier
{
	public static final String GROUP_NAME = "smartedit";
	public static final String CLASSES = "classes";

	private Predicate<CMSItemModel> itemModelPredicate;
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private SessionService sessionService;

	@Override
	public boolean isEnabled(final CMSItemModel itemModel)
	{
		return getSessionService().getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM) != null;
	}

	@Override
	public String groupName()
	{
		return GROUP_NAME;
	}

	@Override
	public Predicate<CMSItemModel> getConstrainedBy()
	{
		return getItemModelPredicate();
	}

	protected Predicate<CMSItemModel> getItemModelPredicate()
	{
		return itemModelPredicate;
	}

	@Required
	public void setItemModelPredicate(
			final Predicate<CMSItemModel> itemModelPredicate)
	{
		this.itemModelPredicate = itemModelPredicate;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(
			final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
