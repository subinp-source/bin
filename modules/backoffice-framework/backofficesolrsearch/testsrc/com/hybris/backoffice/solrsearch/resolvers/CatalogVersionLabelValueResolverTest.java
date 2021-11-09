/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.resolvers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import org.junit.Test;

public class CatalogVersionLabelValueResolverTest
{
	private CatalogVersionLabelValueResolver resolver = new CatalogVersionLabelValueResolver();

	@Test
	public void resolveIndexKey()
	{
		final LanguageModel language = mock(LanguageModel.class);
		doReturn("ISOCODE").when(language).getIsocode();
		final IndexedProperty property = mock(IndexedProperty.class);
		doReturn("propertyName").when(property).getName();
		doReturn("propertyType").when(property).getType();

		final String key = resolver.resolveIndexKey(property, language);

		assertThat(key).isEqualTo("propertyName_propertyType_isocode");
	}
}
