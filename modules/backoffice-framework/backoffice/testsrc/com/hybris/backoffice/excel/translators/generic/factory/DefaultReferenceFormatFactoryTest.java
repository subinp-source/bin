/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.translators.generic.factory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultReferenceFormatFactoryTest
{

	private final ReferenceFormatFactory referenceFormatFactory = new DefaultReferenceFormatFactory();

	@Test
	public void shouldCalculateReferenceFormatForCatalogVersion()
	{
		// given

		// when
		final String referenceFormat = referenceFormatFactory
				.create(RequiredAttributeTestFactory.prepareStructureForCatalogVersion());

		// then
		assertThat(referenceFormat).isEqualTo("CatalogVersion.version:Catalog.id");
	}

	@Test
	public void shouldCalculateReferenceFormatForSupercategories()
	{
		// given

		// when
		final String referenceFormat = referenceFormatFactory
				.create(RequiredAttributeTestFactory.prepareStructureForSupercategories());

		// then
		assertThat(referenceFormat).isEqualTo("Category.code:CatalogVersion.version:Catalog.id");
	}

	@Test
	public void shouldCalculateReferenceFormatForPrices()
	{
		// given

		// when
		final String referenceFormat = referenceFormatFactory.create(RequiredAttributeTestFactory.prepareStructureForPrices());

		// then
		assertThat(referenceFormat).isEqualTo("PriceRow.price:Unit.code:Currency.isocode");
	}
}
