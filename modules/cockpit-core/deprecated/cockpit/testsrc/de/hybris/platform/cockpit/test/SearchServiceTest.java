/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.SearchService;

import java.util.Set;

import org.junit.Test;


@IntegrationTest
public class SearchServiceTest extends UIConfigurationTestBase
{

	private SearchService searchService;

	@Test
	public void testGetBaseConfigurationForProductManager()
	{
		final SearchType searchType = searchService.getSearchType("Product.test");
		assertEquals("Count of property descriptors", 2, searchType.getPropertyDescriptors().size());
		assertContainsPropertyDescriptor("Product.code", searchType.getDeclaredPropertyDescriptors());
		assertContainsPropertyDescriptor("Product.name", searchType.getDeclaredPropertyDescriptors());
		assertEquals("Count of sort properties", 1, searchType.getSortProperties().size());
		final PropertyDescriptor sortProperty = searchType.getSortProperties().iterator().next();
		assertEquals("Qualifier of sort property", "Product.name", sortProperty.getQualifier());
	}


	private void assertContainsPropertyDescriptor(final String code, final Set<PropertyDescriptor> declaredPropertyDescriptors)
	{
		for (final PropertyDescriptor propertyDescriptor : declaredPropertyDescriptors)
		{
			if (code.equalsIgnoreCase(propertyDescriptor.getQualifier()))
			{
				return;
			}
		}
		fail("No property descriptor found for " + code);
	}

	public void setSearchService(final SearchService searchService)
	{
		this.searchService = searchService;
	}
}
