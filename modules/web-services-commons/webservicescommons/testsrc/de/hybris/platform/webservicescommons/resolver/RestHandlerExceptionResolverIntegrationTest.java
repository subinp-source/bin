/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.resolver;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class RestHandlerExceptionResolverIntegrationTest extends ServicelayerBaseTest
{
	@Test
	public void shouldHaveDefaultOrderSetOnResolver()
	{
		final RestHandlerExceptionResolver bean = Registry.getApplicationContext()
				.getBean("dummyRestHandlerExceptionResolverDefaultOrder", RestHandlerExceptionResolver.class);
		Assert.assertEquals(0, bean.getOrder());
	}

	@Test
	public void shouldHaveOrderSetOnResolver()
	{
		final RestHandlerExceptionResolver bean = Registry.getApplicationContext()
				.getBean("dummyRestHandlerExceptionResolverOrderTen", RestHandlerExceptionResolver.class);
		Assert.assertEquals(10, bean.getOrder());
	}

}
