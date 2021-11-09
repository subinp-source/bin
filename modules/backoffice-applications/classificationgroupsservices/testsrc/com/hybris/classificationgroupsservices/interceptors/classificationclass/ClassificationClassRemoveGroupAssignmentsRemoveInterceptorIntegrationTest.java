/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.impex.jalo.ImpExException;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;


@IntegrationTest
public class ClassificationClassRemoveGroupAssignmentsRemoveInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldRemoveClassFeatureGroupAssignmentsOfRemovedRemovedCategory()
	{
		//given
		final CategoryModel samsungCategory = categoryService.getCategoryForCode("Samsung");

		//when
		modelService.remove(samsungCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>("Device", List.of("Price")), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>("Phone", List.of("Price", "RAM")), groupAssignments);
		assertThat(groupAssignments).hasSize(3);
	}
}
