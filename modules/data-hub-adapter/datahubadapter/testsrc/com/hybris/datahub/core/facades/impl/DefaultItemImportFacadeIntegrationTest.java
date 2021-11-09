/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.datahub.core.facades.impl;

import org.junit.Before;

public class DefaultItemImportFacadeIntegrationTest extends AbstractItemImportFacadeIntegrationTest
{
	@Before
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		enableDistributedImpexAndSld(false, false);
	}
}
