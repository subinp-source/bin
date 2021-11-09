/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationintegration.segment.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


public class UserTypeSegmentsProviderIntegrationTest extends ServicelayerTest
{
	@Resource
	private UserTypeSegmentsProvider defaultUserSegmentsProvider;


	@Resource
	UserService userService;

	@Test
	public void testRegistered()
	{
		//given
		final UserModel user = userService.getAdminUser();

		//when
		final List<SegmentMappingData> userSegments = defaultUserSegmentsProvider.getUserSegments(user);

		//then
		Assert.assertNotNull("Returned list should not be null", userSegments);
		Assert.assertEquals("Returned list should have only one element", 1, userSegments.size());
		Assert.assertEquals(UserTypeSegmentsProvider.REGISTERED_USER, userSegments.get(0).getCode());
	}

	@Test
	public void testAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		//when
		final List<SegmentMappingData> userSegments = defaultUserSegmentsProvider.getUserSegments(user);

		//then
		Assert.assertNotNull("Returned list should not be null", userSegments);
		Assert.assertEquals("Returned list should have only one element", 1, userSegments.size());
		Assert.assertEquals(UserTypeSegmentsProvider.ANONYMOUS_USER, userSegments.get(0).getCode());
	}
}
