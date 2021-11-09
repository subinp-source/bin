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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collections;

import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PropertyGeneratorUnitTest
{
	private final IntegrationObjectItemAttributeModel attributeModel = model();

	@Mock
	private DescriptorFactory descriptorFactory;
	@InjectMocks
	private PropertyGenerator propertyGenerator;


	@Before
	public void setup()
	{
		final TypeAttributeDescriptor attributeDescriptor = mock(TypeAttributeDescriptor.class);
		when(descriptorFactory.createTypeAttributeDescriptor(attributeModel)).thenReturn(attributeDescriptor);

		final var delegate = mock(SimplePropertyGenerator.class);
		when(delegate.generate(attributeDescriptor)).thenReturn(new SimpleProperty());
		propertyGenerator.setDelegateGenerator(delegate);
	}

	@Test
	public void testGenerateSimpleProperty()
	{
		final Property prop = propertyGenerator.generate(attributeModel);
		assertThat(prop).isInstanceOf(SimpleProperty.class);
	}

	@Test
	public void testGenerateNullAttributeModel()
	{
		assertThatThrownBy(() -> propertyGenerator.generate(null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testGenerateNullAttributeDescriptor()
	{
		assertThatThrownBy(() -> propertyGenerator.generate(mock(IntegrationObjectItemAttributeModel.class)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private IntegrationObjectItemAttributeModel model()
	{
		final IntegrationObjectItemAttributeModel model = new IntegrationObjectItemAttributeModel();
		model.setAttributeDescriptor(new AttributeDescriptorModel());
		return model;
	}
}