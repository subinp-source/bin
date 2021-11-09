/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.structure;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.test.structure.AbstractProjectStructureTest;
import de.hybris.platform.webservicescommons.constants.WebservicescommonsConstants;


@IntegrationTest
@SuppressWarnings("squid:S2187")
public class ProjectStructureTest extends AbstractProjectStructureTest
{
	public ProjectStructureTest()
	{
		super(WebservicescommonsConstants.EXTENSIONNAME);
	}
}
