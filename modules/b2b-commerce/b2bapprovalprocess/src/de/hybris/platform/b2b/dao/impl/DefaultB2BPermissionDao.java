/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao.impl;

import de.hybris.platform.b2b.dao.B2BPermissionDao;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.List;


public class DefaultB2BPermissionDao extends DefaultGenericDao<B2BPermissionModel> implements B2BPermissionDao
{
	public DefaultB2BPermissionDao()
	{
		super(B2BPermissionModel._TYPECODE);
	}

	@Override
	public B2BPermissionModel findPermissionByCode(final String code)
	{
		final List<B2BPermissionModel> permissions = this.find(Collections.singletonMap(B2BPermissionModel.CODE, code));
		return (permissions.iterator().hasNext() ? permissions.iterator().next() : null);
	}
}
