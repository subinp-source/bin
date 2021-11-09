/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.services;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.util.AppendSpringConfiguration;

import org.junit.Test;

import javax.annotation.Resource;

import static junit.framework.TestCase.assertTrue;

@IntegrationTest
@AppendSpringConfiguration("classpath:/test/integrationbackoffice-test-spring.xml")
public class ReadServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ReadService readService;

	@Test
	public void testFlexibleTypes()
	{
		assertTrue(readService.isCollectionType("CollectionType"));
		assertTrue(readService.isComposedType("ComposedType"));
		assertTrue(readService.isEnumerationMetaType("EnumerationMetaType"));
		assertTrue(readService.isAtomicType("AtomicType"));
		assertTrue(readService.isMapType("MapType"));
		assertTrue(readService.isProductType("Product"));
	}
}