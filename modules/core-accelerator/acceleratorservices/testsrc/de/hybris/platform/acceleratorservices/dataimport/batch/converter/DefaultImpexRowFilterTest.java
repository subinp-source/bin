/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.DefaultImpexRowFilter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test for {@link DefaultImpexRowFilterTest}
 */
@UnitTest
@Ignore
public class DefaultImpexRowFilterTest
{

	private static final String TEST_VALUE = "test";
	private DefaultImpexRowFilter filter;
	private Map<Integer, String> row;



	@Before
	public void setUp()
	{
		filter = new DefaultImpexRowFilter();
		row = new HashMap<Integer, String>();
	}

	@Test
	public void testExpression()
	{
		filter.setExpression("row[1]");
		Assert.assertFalse(filter.filter(row));
		row.put(Integer.valueOf(0), TEST_VALUE);
		Assert.assertFalse(filter.filter(row));
		row.put(Integer.valueOf(1), null);
		Assert.assertFalse(filter.filter(row));
		row.put(Integer.valueOf(1), "");
		Assert.assertFalse(filter.filter(row));
		row.put(Integer.valueOf(1), TEST_VALUE);
		Assert.assertTrue(filter.filter(row));
	}

	@Test
	public void testComplexExpression()
	{
		filter.setExpression("row[1] && row[0] == '" + TEST_VALUE + "'");
		Assert.assertFalse(filter.filter(row));
		row.put(Integer.valueOf(0), TEST_VALUE);
		Assert.assertFalse(filter.filter(row));
		row.put(Integer.valueOf(1), TEST_VALUE);
		Assert.assertTrue(filter.filter(row));
	}
}
