/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */

package com.hybris.classificationgroupsservices.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import org.junit.Test;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;


public class ClassFeatureGroupAssignmentFullQualifierHandlerTest
{
	ClassFeatureGroupAssignmentFullQualifierHandler classAttributeAssignmentFullQualifierAttributeHandler = new ClassFeatureGroupAssignmentFullQualifierHandler();

	@Test
	public void shouldGetFullQualifier()
	{
		// given
		final ClassificationSystemModel catalog = mock(ClassificationSystemModel.class);
		given(catalog.getId()).willReturn("Catalog $Id");

		final ClassificationSystemVersionModel version = mock(ClassificationSystemVersionModel.class);
		given(version.getVersion()).willReturn("Catalog Version");
		given(version.getCatalog()).willReturn(catalog);

		final ClassificationAttributeModel attribute = mock(ClassificationAttributeModel.class);
		given(attribute.getCode()).willReturn("Attribute Code");
		given(attribute.getSystemVersion()).willReturn(version);

		final ClassificationClassModel category = mock(ClassificationClassModel.class);
		given(category.getCode()).willReturn("Category Code");

		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		given(classAttributeAssignment.getSystemVersion()).willReturn(version);
		given(classAttributeAssignment.getClassificationClass()).willReturn(category);
		given(classAttributeAssignment.getClassificationAttribute()).willReturn(attribute);

		final ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = mock(ClassFeatureGroupAssignmentModel.class);
		given(classFeatureGroupAssignment.getClassAttributeAssignment()).willReturn(classAttributeAssignment);

		// when
		final String fullQualifier = classAttributeAssignmentFullQualifierAttributeHandler.get(classFeatureGroupAssignment);

		// then
		assertThat(fullQualifier).as("the field should contain full attribute qualifier separated by '_'")
				.isEqualTo("CatalogId_CatalogVersion_CategoryCode_AttributeCode");
	}
}
