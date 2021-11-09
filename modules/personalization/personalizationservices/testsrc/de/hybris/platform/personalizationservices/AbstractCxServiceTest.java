/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices;

import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;

import org.junit.Before;


public abstract class AbstractCxServiceTest extends ServicelayerTransactionalTest
{

	protected static final String SEGMENT_CODE = "segment1";
	protected static final String CUSTOMIZATION_CODE = "customization1";
	protected static final String VARIATION_CODE = "variation1";
	protected static final String CUSTOMIZATION_CODE2 = "customization2";
	protected static final String VARIATION_CODE2 = "variation2";
	protected static final String VARIATION_CODE3 = "variation3";

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importData(new ClasspathImpExResource("/personalizationservices/test/testdata_personalizationservices.impex", "UTF-8"));
	}
}
