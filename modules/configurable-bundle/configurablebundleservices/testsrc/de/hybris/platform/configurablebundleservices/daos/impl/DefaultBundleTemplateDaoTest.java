/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * JUnit test suite for {@link DefaultBundleTemplateDao}
 */
@UnitTest
public class DefaultBundleTemplateDaoTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private DefaultBundleTemplateDao bundleTemplateDao;

	@Before
	public void setUp() throws Exception
	{
		bundleTemplateDao = new DefaultBundleTemplateDao();
	}

	@Test
	public void testFindBundleTemplatesByProductWhenNoProduct()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("productModel can not be null");

		bundleTemplateDao.findBundleTemplatesByProduct(null);
	}

}
