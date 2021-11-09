/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class TransactionRollbackTest extends ServicelayerTransactionalTest
{
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;

	private final String userId = "TransactionRollbackTestUser" + new Date().getTime();

	@Test(expected = UnknownIdentifierException.class)
	public void runFirsthouldBeNotFoundBeforeTheTest()
	{
		userService.getUserForUID(userId);
	}

	@Test
	public void runSecondShouldInsertUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(userId);
		modelService.save(user);
		Assert.assertNotNull(userService.getUserForUID(userId));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void runLasThouldBeNotFoundAfterTheTest()
	{
		userService.getUserForUID(userId);
	}
}
