/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.NullImpexRowFilter;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;


/**
 * Test for {@link NullImpexRowFilterTest}
 */
@UnitTest
public class NullImpexRowFilterTest
{
	@Test
	public void testNullFilter()
	{
		final ImpexRowFilter filter = new NullImpexRowFilter();
		Assert.assertTrue(filter.filter(null));
		Assert.assertTrue(filter.filter(new HashMap<Integer, String>()));
	}

}
