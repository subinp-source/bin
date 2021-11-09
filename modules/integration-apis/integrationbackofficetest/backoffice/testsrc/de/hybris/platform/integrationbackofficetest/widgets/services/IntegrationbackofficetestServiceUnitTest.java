/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.integrationbackofficetest.services.IntegrationbackofficetestService;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@UnitTest
public class IntegrationbackofficetestServiceUnitTest
{
	@Test
	public void testIntegrationbackofficetestServiceConstructor()
	{
		IntegrationbackofficetestService service = new IntegrationbackofficetestService();
		assertNotNull(service);
	}
}
