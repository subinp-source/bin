/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.order.impl.OrderEntryModifiableChecker;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.order.EntryGroup;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Tests whether an order/cart entry can be remove or not
 */
public class BundleOrderEntryModifiableChecker extends OrderEntryModifiableChecker
{
	private BundleTemplateService bundleTemplateService;

	@Override
	public boolean canModify(@Nonnull final AbstractOrderEntryModel entryToUpdate)
	{
		validateParameterNotNullStandardMessage("entryToUpdate", entryToUpdate);
		final EntryGroup bundleGroup = bundleTemplateService.getBundleEntryGroup(entryToUpdate);
		if (bundleGroup != null)
		{
			return false;
		}
		return super.canModify(entryToUpdate);
	}

	protected BundleTemplateService getBundleTemplateService()
	{
		return bundleTemplateService;
	}

	@Required
	public void setBundleTemplateService(final BundleTemplateService bundleTemplateService)
	{
		this.bundleTemplateService = bundleTemplateService;
	}
}
