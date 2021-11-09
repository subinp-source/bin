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
package de.hybris.platform.integrationservices.model;

import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.simpleAttributeBuilder;
import static de.hybris.platform.integrationservices.model.MockAttributeDescriptorModelBuilder.attributeDescriptorModelBuilder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;

@UnitTest
public class IntegrationObjectItemAttributeModelUtilsUnitTest
{
	@Test
	public void testIsUniqueWhenTrueDefinitionModelTrueDescriptor()
	{
		testIsUniqueSetsToTrue(simpleAttributeBuilder()
				.withUnique(true)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(true))
				.build());
	}

	@Test
	public void testIsUniqueWhenFalseDefinitionModelTrueDescriptor()
	{
		testIsUniqueSetsToTrue(simpleAttributeBuilder()
				.withUnique(false)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(true))
				.build());
	}

	@Test
	public void testIsUniqueWhenNullDefinitionModelTrueDescriptor()
	{
		testIsUniqueSetsToTrue(simpleAttributeBuilder()
				.withUnique(null)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(true))
				.build());
	}

	@Test
	public void testIsUniqueWhenTrueDefinitionModelFalseDescriptor()
	{
		testIsUniqueSetsToTrue(simpleAttributeBuilder()
				.withUnique(true)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(false))
				.build());
	}

	@Test
	public void testIsUniqueWhenTrueDefinitionModelNullDescriptor()
	{
		testIsUniqueSetsToTrue(simpleAttributeBuilder()
				.withUnique(true)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(null))
				.build());
	}

	@Test
	public void testIsUniqueWhenFalseDefinitionModelFalseDescriptor()
	{
		testIsUniqueSetsToFalse(simpleAttributeBuilder()
				.withUnique(false)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(false))
				.build());
	}

	@Test
	public void testIsUniqueWhenNullDefinitionModelNullDescriptor()
	{
		testIsUniqueSetsToFalse(simpleAttributeBuilder()
				.withUnique(null)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(null))
				.build());

	}

	@Test
	public void testIsUniqueWhenNullDefinitionModelFalseDescriptor()
	{
		testIsUniqueSetsToFalse(simpleAttributeBuilder()
				.withUnique(null)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(false))
				.build());
	}

	@Test
	public void testIsUniqueWhenFalseDefinitionModelNullDescriptor()
	{
		testIsUniqueSetsToFalse(simpleAttributeBuilder()
				.withUnique(false)
				.withAttributeDescriptor(attributeDescriptorModelBuilder().withUnique(null))
				.build());
	}

	@Test
	public void testFalseIfNullWhenNull()
	{
		assertFalse(IntegrationObjectItemAttributeModelUtils.falseIfNull(null));
	}

	@Test
	public void testFalseIfNullWhenFalse()
	{
		assertFalse(IntegrationObjectItemAttributeModelUtils.falseIfNull(false));
	}
	
	@Test
	public void testFalseIfNullWhenTrue()
	{
		assertTrue(IntegrationObjectItemAttributeModelUtils.falseIfNull(true));
	}

	private void testIsUniqueSetsToFalse(final IntegrationObjectItemAttributeModel model)
	{
		assertFalse(IntegrationObjectItemAttributeModelUtils.isUnique(model));
	}

	private void testIsUniqueSetsToTrue(final IntegrationObjectItemAttributeModel model)
	{
		assertTrue(IntegrationObjectItemAttributeModelUtils.isUnique(model));
	}
}