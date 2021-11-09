/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.hac.controller;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.hac.facade.HacMaintenanceFacade;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import javax.annotation.Resource;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class MaintainControllerTest extends ServicelayerBaseTest
{
	@Resource
	private HacMaintenanceFacade hacMaintenanceFacade;
	private MaintainController maintainController;

	@Before
	public void setUp()
	{
		maintainController = new MaintainController(hacMaintenanceFacade);
	}

	@Test
	public void shouldNotThrowExceptionForMigration()
	{
		final String keysToMigrate = "User.encodedPassword|User.passwordAnswer|User.passwordQuestion";
		final Throwable actual = ThrowableAssert
				.catchThrowable(() -> maintainController.keyMigrationMigrate(keysToMigrate));
		assertThat(actual).isNull();
	}

	@Test
	public void shouldNotThrowExceptionForEmptyMigrationKey()
	{
		final Throwable actual = ThrowableAssert
				.catchThrowable(() ->maintainController.keyMigrationMigrate(""));
		assertThat(actual).isNull();
	}
}
