/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.mock;

import de.hybris.platform.servicelayer.MockTest;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;


@Ignore
@ContextConfiguration(locations =
{ "classpath:/servicelayer/test/servicelayer-mock-base-test.xml",
		"classpath:/servicelayer/test/servicelayer-mock-i18n-test.xml" })
public class HybrisMokitoTest extends MockTest
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(HybrisMokitoTest.class);

	public HybrisMokitoTest()
	{
		// enable @Mock annotation
		super();
		MockitoAnnotations.initMocks(this);
	}

}
