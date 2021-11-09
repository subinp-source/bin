/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;

import java.util.Set;


/**
 * Default implementation for {@link SnIndexerValueProvider} for model attributes.
 */
public class CatalogVersionSnIndexerValueProvider extends AbstractSnIndexerValueProvider<ItemModel, Void>
{
	public static final String ID = "catalogVersionSnIndexerValueProvider";

	protected static final String SEPARATOR = ":";

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of();

	private CatalogTypeService catalogTypeService;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ItemModel source, final Void data) throws SnIndexerException
	{
		if (catalogTypeService.isCatalogVersionAwareModel(source))
		{
			final CatalogVersionModel catalogVersion = catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(source);
			final CatalogModel catalog = catalogVersion.getCatalog();
			return new StringBuilder().append(catalog.getId()).append(SEPARATOR).append(catalogVersion.getVersion()).toString();
		}
		else
		{
			return null;
		}
	}

	public CatalogTypeService getCatalogTypeService()
	{
		return catalogTypeService;
	}

	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}
}
