/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.schema.property;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;
import de.hybris.platform.odata2services.odata.schema.attribute.AttributeAnnotationListGenerator;

import org.apache.olingo.odata2.api.edm.provider.Property;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * @deprecated use {@link SimplePropertyGenerator} instead. Current implementation simply delegates to it.
 */
@Deprecated(since = "1905.09-CEP", forRemoval = true)
public class PropertyGenerator implements SchemaElementGenerator<Property, IntegrationObjectItemAttributeModel>
{
	private DescriptorFactory descriptorFactory;
	private SimplePropertyGenerator delegate;

	@Override
	public Property generate(final IntegrationObjectItemAttributeModel attributeModel)
	{
		Preconditions.checkArgument(attributeModel != null,
				"A Property cannot be generated from a null IntegrationObjectItemAttributeModel");
		final AttributeDescriptorModel attributeDescriptor = attributeModel.getAttributeDescriptor();
		Preconditions.checkArgument(attributeDescriptor != null,
				"A Property cannot be generated from a null AttributeDescriptorModel");

		final TypeAttributeDescriptor attribute = asDescriptor(attributeModel);
		return delegate.generate(attribute);
	}

	protected TypeAttributeDescriptor asDescriptor(final IntegrationObjectItemAttributeModel attributeModel)
	{
		return descriptorFactory.createTypeAttributeDescriptor(attributeModel);
	}

	public void setAttributeListGenerator(final AttributeAnnotationListGenerator generator)
	{
		//attributes list generator is not used anymore but this method has to stay here for backwards compatibility
	}

	@Required
	public void setDescriptorFactory(final DescriptorFactory factory)
	{
		descriptorFactory = factory;
	}

	@Required
	public void setDelegateGenerator(final SimplePropertyGenerator generator)
	{
		delegate = generator;
	}
}
