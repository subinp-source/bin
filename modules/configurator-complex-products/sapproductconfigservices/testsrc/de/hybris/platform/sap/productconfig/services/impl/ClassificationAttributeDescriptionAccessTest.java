/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.sap.productconfig.services.intf.ClassificationAttributeDescriptionAccess;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class ClassificationAttributeDescriptionAccessTest
{

	private ClassificationAttributeDescriptionAccess classUnderTest;
	private ClassificationAttributeModel classificationAttribute;

	@Before
	public void setUp()
	{
		classUnderTest = new SimpleClassificationAttributeDescriptionAccessImpl();
		classificationAttribute = new ClassificationAttributeModel();
	}

	@Test
	public void testGetDescription()
	{
		assertNull(classUnderTest.getDescription(classificationAttribute));
	}

	@Test
	public void testGetDescriptionWithNull()
	{
		assertNull(classUnderTest.getDescription(null));
	}
}
