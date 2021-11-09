/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ExportDataTest extends ServicelayerTest
{

	public static final String GOOGLE_BUSINESS_CODE = "googleBusinessTest";

	@Before
	public void setUp()
	{
		// implement here code executed before each test
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}


	@Test
	public void testExport()
	{
		//ExportDataEvent event = new ExportDataEvent(GOOGLE_BUSINESS_CODE);
		//eventService.publishEvent(event);
	}



}
