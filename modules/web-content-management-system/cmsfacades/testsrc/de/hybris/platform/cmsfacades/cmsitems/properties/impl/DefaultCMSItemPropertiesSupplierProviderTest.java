/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.properties.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSItemPropertiesSupplierProviderTest
{
	@InjectMocks
	private DefaultCMSItemPropertiesSupplierProvider provider;

	@Mock
	private CMSItemPropertiesSupplier serviceUsed;
	@Mock
	private CMSItemPropertiesSupplier serviceNotUsed;
	@Mock
	private Predicate<CMSItemModel> predicateForUsedService;
	@Mock
	private Predicate<CMSItemModel> predicateForNotUsedService;

	@Mock
	private CMSItemModel itemModel;

	@Before
	public void setUp()
	{
		provider.setCmsItemPropertiesSuppliers(Arrays.asList(serviceUsed, serviceNotUsed));

		when(serviceUsed.getConstrainedBy()).thenReturn(predicateForUsedService);
		when(serviceNotUsed.getConstrainedBy()).thenReturn(predicateForNotUsedService);

		when(predicateForUsedService.test(itemModel)).thenReturn(Boolean.TRUE);
		when(predicateForNotUsedService.test(itemModel)).thenReturn(Boolean.FALSE);
	}

	@Test
	public void shouldReturnListOfServicesConstrainedByItemModel()
	{
		// WHEN
		final List<CMSItemPropertiesSupplier> services = provider.getSuppliers(itemModel);

		// THEN
		assertThat(services, Matchers.hasSize(1));
		assertThat(services.get(0), is(serviceUsed));
	}
}
