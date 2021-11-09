/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.util.builder;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CommerceCartMetadata;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CommerceCartMetadataBuilderTest
{
	private CommerceCartMetadataBuilder commerceCartMetadataBuilder = new CommerceCartMetadataBuilder();

	@Test
	public void shouldBuildEmptyCommerceCartMetadata()
	{
		final CommerceCartMetadata cartMetadata = commerceCartMetadataBuilder.build();

		Assert.assertNotNull("Should return cart metadata", cartMetadata);
		Assert.assertEquals("Name should be empty optional", Optional.empty(), cartMetadata.getName());
		Assert.assertEquals("Description should be empty optional", Optional.empty(), cartMetadata.getDescription());
		Assert.assertEquals("Expiration time should be empty optional", Optional.empty(), cartMetadata.getExpirationTime());
	}
}
