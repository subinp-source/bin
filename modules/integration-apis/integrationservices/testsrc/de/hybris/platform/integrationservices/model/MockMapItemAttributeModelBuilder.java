/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.integrationservices.model;

import static de.hybris.platform.integrationservices.model.MockMapAttributeDescriptorModelBuilder.mapAttributeDescriptor;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class MockMapItemAttributeModelBuilder extends BaseMockItemAttributeModelBuilder<MockMapItemAttributeModelBuilder>
{
	MockMapItemAttributeModelBuilder()
	{
	}

	public MockMapItemAttributeModelBuilder withSource(final String source)
	{
		return withIntegrationObjectItemCode(source);
	}

	public MockMapItemAttributeModelBuilder withTarget(final String target)
	{
		attributeDescriptorBuilderOrDefault(mapAttributeDescriptor()).withReturnType(target);
		return withReturnIntegrationObject(target);
	}

	@Override
	protected MockMapItemAttributeModelBuilder myself()
	{
		return this;
	}
}
