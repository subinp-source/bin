/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


public class BaseDaoTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private BaseDao baseDao;
	@Resource
	private ModelService modelService;

	@Test
	public void testFindFirstByAttribute() throws Exception
	{

		final Class<? extends ItemModel> modelClass = UserModel.class;
		Assert.assertEquals("User", modelService.getModelType(modelClass));
		Assert.assertNotNull(baseDao.findFirstByAttribute(UserModel.UID, "admin", modelClass));

		de.hybris.platform.core.Registry.getApplicationContext().getBean("itemModelCloneCreator");
	}
}
