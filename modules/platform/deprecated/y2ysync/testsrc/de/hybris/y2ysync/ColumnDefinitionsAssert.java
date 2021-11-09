/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.y2ysync;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;


public class ColumnDefinitionsAssert extends AbstractAssert<ColumnDefinitionsAssert, Collection<Y2YColumnDefinitionModel>>
{

	protected ColumnDefinitionsAssert(final Collection<Y2YColumnDefinitionModel> actual)
	{
		super(actual, ColumnDefinitionsAssert.class);
	}

	public static ColumnDefinitionsAssert assertThat(final Collection<Y2YColumnDefinitionModel> actual)
	{
		return new ColumnDefinitionsAssert(actual);
	}

	public ColumnDefinitionsAssert hasSize(final int size)
	{
		Assertions.assertThat(actual).isNotNull().hasSize(size);

		return this;
	}

	public ColumnDefinitionAssert containsDefintionFor(final AttributeDescriptorModel descriptor)
	{
		final Optional<Y2YColumnDefinitionModel> firstTry = actual.stream()
		                                                          .filter(ad -> ad.getAttributeDescriptor().equals(descriptor))
		                                                          .findFirst();

		Assertions.assertThat(firstTry.isPresent()).isTrue();

		return new ColumnDefinitionAssert(firstTry.get());
	}
}
