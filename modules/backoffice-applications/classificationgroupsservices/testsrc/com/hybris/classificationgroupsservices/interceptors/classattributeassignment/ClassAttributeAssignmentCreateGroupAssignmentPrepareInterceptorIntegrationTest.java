/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;


@IntegrationTest
public class ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldCreateGroupAssignmentForCategoryAndSubcategories()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("classAttributeAssignmentCatalog",
				"Staged");
		final ClassificationClassModel phone = (ClassificationClassModel) categoryService.getCategoryForCode("Phone");
		final ClassificationAttributeModel classificationAttribute = modelService.create(ClassificationAttributeModel.class);
		classificationAttribute.setCode("weight");
		classificationAttribute.setSystemVersion((ClassificationSystemVersionModel) catalogVersion);
		modelService.save(classificationAttribute);

		final ClassAttributeAssignmentModel classAttributeAssignment = modelService.create(ClassAttributeAssignmentModel.class);
		classAttributeAssignment.setClassificationAttribute(classificationAttribute);
		classAttributeAssignment.setSystemVersion((ClassificationSystemVersionModel) catalogVersion);
		classAttributeAssignment.setClassificationClass(phone);
		classAttributeAssignment.setAttributeType(ClassificationAttributeTypeEnum.STRING);

		//when
		modelService.save(classAttributeAssignment);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>("Device", List.of("Price")), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>("Phone", List.of("Price", "RAM", "weight")), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>("Samsung", List.of("Price", "RAM", "TouchId", "weight")),
				groupAssignments);
		assertThat(groupAssignments).hasSize(8);
	}
}
