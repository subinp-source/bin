/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.platformbackoffice.services;

import static org.mockito.Mockito.verify;

import de.hybris.platform.platformbackoffice.dao.ClassificationAttributeAssignmentDAO;
import de.hybris.platform.platformbackoffice.services.impl.DefaultClassificationAttributeAssignmentService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ClassificationAttributeAssignmentServiceTest
{

	@Mock
	private ClassificationAttributeAssignmentDAO classificationAttributeAssignmentDAO;

	@InjectMocks
	private DefaultClassificationAttributeAssignmentService defaultClassificationAttributeAssignmentService;

	@Test
	public void shouldParseQualifier()
	{
		// given

		// when
		defaultClassificationAttributeAssignmentService.findClassAttributeAssignment("ElectronicsClassification/1.0/40.Weight, 94");

		// then
		verify(classificationAttributeAssignmentDAO).getClassificationAttributeAssignment("ElectronicsClassification", "1.0", "40",
				"Weight, 94");
	}
}
