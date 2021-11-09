/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.impex.jalo.ImpExException;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;


@IntegrationTest
public class ClassificationClassAttributeAssignmentRemovalPrepareInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldRemoveClassFeatureGroupAssignmentsInSubcategoriesDuringUnassigningAttributeAssignment()
	{
		//given
		final ClassificationClassModel phoneCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(PHONE_CATEGORY);

		//when
		phoneCategory.setDeclaredClassificationAttributeAssignments(List.of());
		modelService.save(phoneCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(4);
	}
}
