/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.resolvers;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ItemModelLabelValueResolverTest
{

	@Spy
	private ItemModelLabelValueResolver resolver;

	@Test
	public void resolveIndexKeyShouldCreateFieldWithLowercaseIsoCode()
	{
		//given
		final IndexedProperty indexProperty = new IndexedProperty();
		indexProperty.setName("catalogVersion");
		indexProperty.setType("string");
		final LanguageModel language = new LanguageModel();
		language.setIsocode("es_CO");

		//when
		final String result = resolver.resolveIndexKey(indexProperty, language);

		//then
		assertThat(result).isEqualTo("catalogVersion_es_co_string");
	}

}
