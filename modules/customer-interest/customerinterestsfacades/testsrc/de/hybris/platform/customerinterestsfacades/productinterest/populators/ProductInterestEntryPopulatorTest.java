/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.productinterest.populators;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestEntryData;
import de.hybris.platform.customerinterestsservices.productinterest.ProductInterestConfigService;
import de.hybris.platform.notificationservices.enums.NotificationType;

import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit test for {@link ProductInterestEntryPopulator}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductInterestEntryPopulatorTest
{

	@Mock
	ProductInterestConfigService productInterestConfigService;

	private ProductInterestEntryPopulator populator;

	private Entry<NotificationType, Date> source;
	private ProductInterestEntryData target;
	private Date now;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		populator = new ProductInterestEntryPopulator(productInterestConfigService);

		now = new Date();
		source = Collections.singletonMap(NotificationType.NOTIFICATION, now).entrySet().iterator().next();
		target = new ProductInterestEntryData();

	}

	@Test
	public void testPopulate()
	{
		when(productInterestConfigService.getProductInterestExpiryDate(Mockito.any())).thenReturn(now);
		populator.populate(source, target);

		Assert.assertEquals(NotificationType.NOTIFICATION.name(), target.getInterestType());
		Assert.assertTrue(target.getDateAdded().getTime() == now.getTime());
		Assert.assertEquals(now, target.getExpirationDate());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithSourceNull()
	{
		populator.populate(null, target);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithTargetNull()
	{
		populator.populate(source, null);
	}
}
