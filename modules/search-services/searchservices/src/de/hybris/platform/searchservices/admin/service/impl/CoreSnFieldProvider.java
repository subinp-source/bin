/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;


import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.service.SnFieldProvider;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.indexer.service.impl.CatalogVersionSnIndexerValueProvider;
import de.hybris.platform.searchservices.indexer.service.impl.ModelAttributeSnIndexerValueProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnFieldProvider}.
 */
public class CoreSnFieldProvider implements SnFieldProvider
{
	private CatalogTypeService catalogTypeService;

	@Override
	public List<SnField> getDefaultFields(final SnIndexType indexType)
	{
		final List<SnField> fields = new ArrayList<>();

		// PK field
		final SnField pkField = new SnField();
		pkField.setId(SearchservicesConstants.PK_FIELD);
		pkField.setFieldType(SnFieldType.LONG);
		pkField.setValueProvider(ModelAttributeSnIndexerValueProvider.ID);
		pkField.setValueProviderParameters(Map.of(ModelAttributeSnIndexerValueProvider.EXPRESSION_PARAM, "pk.longValueAsString"));
		pkField.setRetrievable(Boolean.TRUE);
		pkField.setSearchable(Boolean.FALSE);
		pkField.setLocalized(Boolean.FALSE);
		pkField.setQualifierTypeId(null);
		pkField.setMultiValued(Boolean.FALSE);

		fields.add(pkField);

		// Catalog version field
		if (catalogTypeService.isCatalogVersionAwareType(indexType.getItemComposedType()))
		{
			final SnField catalogVersionField = new SnField();
			catalogVersionField.setId(SearchservicesConstants.CATALOG_VERSION_FIELD);
			catalogVersionField.setFieldType(SnFieldType.STRING);
			catalogVersionField.setValueProvider(CatalogVersionSnIndexerValueProvider.ID);
			catalogVersionField.setValueProviderParameters(null);
			catalogVersionField.setRetrievable(Boolean.TRUE);
			catalogVersionField.setSearchable(Boolean.FALSE);
			catalogVersionField.setLocalized(Boolean.FALSE);
			catalogVersionField.setQualifierTypeId(null);
			catalogVersionField.setMultiValued(Boolean.FALSE);

			fields.add(catalogVersionField);
		}

		return fields;
	}

	public CatalogTypeService getCatalogTypeService()
	{
		return catalogTypeService;
	}

	@Required
	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}
}
