/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultB2BUserGroupsLookUpStrategyIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private DefaultB2BUserGroupsLookUpStrategy b2bUserGroupsLookUpStrategy;

	@Test
	public void shouldGetUserGroups() throws Exception
	{
		final List<String> userGroups = b2bUserGroupsLookUpStrategy.getUserGroups();
		Assert.assertNotNull(userGroups);
		Assert.assertEquals(4, userGroups.size());
		Assert.assertEquals("b2badmingroup", userGroups.get(0));
		Assert.assertEquals("b2bcustomergroup", userGroups.get(1));
		Assert.assertEquals("b2bmanagergroup", userGroups.get(2));
		Assert.assertEquals("b2bapprovergroup", userGroups.get(3));
	}
}
