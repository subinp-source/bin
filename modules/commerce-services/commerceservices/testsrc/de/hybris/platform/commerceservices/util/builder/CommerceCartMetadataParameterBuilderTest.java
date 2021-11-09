/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util.builder;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.service.data.CommerceCartMetadataParameter;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CommerceCartMetadataParameterBuilderTest
{
	private CommerceCartMetadataParameterBuilder commerceCartMetadataParameterBuilder = new CommerceCartMetadataParameterBuilder();

	@Test
	public void shouldBuildEmptyCommerceCartMetadataParameter()
	{
		final CommerceCartMetadataParameter cartMetadataParameter = commerceCartMetadataParameterBuilder.build();

		Assert.assertNotNull("Should return cart metadata parameter", cartMetadataParameter);
		Assert.assertEquals("Name should be empty optional", Optional.empty(), cartMetadataParameter.getName());
		Assert.assertEquals("Description should be empty optional", Optional.empty(), cartMetadataParameter.getDescription());
		Assert.assertEquals("Expiration time should be empty optional", Optional.empty(), cartMetadataParameter.getExpirationTime());
	}
}
