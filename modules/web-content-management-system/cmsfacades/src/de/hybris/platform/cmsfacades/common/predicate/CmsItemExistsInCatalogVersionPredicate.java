/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if the given CmsItem exists in the expected catalog version.
 * <p>
 * Returns <tt>TRUE</tt> if the given cms item exists in the expected catalog version; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class CmsItemExistsInCatalogVersionPredicate implements BiPredicate<String, CatalogVersionModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(CmsItemExistsInCatalogVersionPredicate.class);

	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public boolean test(final String itemUuid, final CatalogVersionModel catalogVersionModel)
	{
		boolean result;
		try
		{
			final Optional<CMSItemModel> item = getUniqueItemIdentifierService().getItemModel(itemUuid, CMSItemModel.class);
			result = item.isPresent() && item.get().getCatalogVersion().equals(catalogVersionModel);
		}
		catch (final UnknownIdentifierException | ConversionException | NoSuchElementException e)
		{
			LOG.debug("The item cannot be found in the given catalog version", e);
			result = false;
		}
		return result;
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
}
