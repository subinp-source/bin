/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.impex.jalo.ImpExException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
public class ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldRemoveGroupAssignmentsInSubcategoriesDuringRemovingAttributeAssignment()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = findClassAttributeAssignment("classAttributeAssignmentCatalog/Staged/Phone.RAM");

		//when
		modelService.remove(classAttributeAssignment);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(ImmutablePair.of(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(ImmutablePair.of(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(ImmutablePair.of(SAMSUNG_CATEGORY, List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(4);
	}

}
