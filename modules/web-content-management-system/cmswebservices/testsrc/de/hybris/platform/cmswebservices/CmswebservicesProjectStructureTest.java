/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.test.structure.AbstractProjectStructureTest;

import java.util.logging.Logger;


/**
 * Test to find duplicate jars in the extension. If the jars are already defined in the platform, they should not be
 * redefined in this extension.
 */
@IntegrationTest
public class CmswebservicesProjectStructureTest extends AbstractProjectStructureTest
{
	private static final Logger log = Logger.getLogger(CmswebservicesProjectStructureTest.class.getName());

	public CmswebservicesProjectStructureTest()
	{
		super(true, CmswebservicesConstants.EXTENSIONNAME);
	}

}
