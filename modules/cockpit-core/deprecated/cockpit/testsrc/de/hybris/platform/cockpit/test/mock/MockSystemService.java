/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.test.mock;

import de.hybris.platform.cockpit.services.impl.SystemServiceImpl;
import de.hybris.platform.core.model.user.UserModel;


public class MockSystemService extends SystemServiceImpl
{

	@Override
	public boolean checkAttributePermissionOn(final String typeCode, final String attributeQualifier, final String permissionCode)
	{
		return true;
	}

	@Override
	public boolean checkPermissionOn(final UserModel user, final String typeCode, final String permissionCode)
	{
		return true;
	}

}
