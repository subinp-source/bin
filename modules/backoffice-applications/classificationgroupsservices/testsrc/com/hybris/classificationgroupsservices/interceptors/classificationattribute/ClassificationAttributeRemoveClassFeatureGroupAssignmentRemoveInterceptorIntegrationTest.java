/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationattribute;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.impex.jalo.ImpExException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
public class ClassificationAttributeRemoveClassFeatureGroupAssignmentRemoveInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldRemoveAllClassFeatureGroupAssignmentRelatedWithRemovedClassificationAttribute()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = findClassAttributeAssignment("classAttributeAssignmentCatalog/Staged/Device.Price");
		final ClassificationAttributeModel classificationAttribute = classAttributeAssignment.getClassificationAttribute();

		//when
		modelService.remove(classificationAttribute);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(ImmutablePair.of(DEVICE_CATEGORY, List.of()), groupAssignments);
		assertClassFeatureGroupAssignments(ImmutablePair.of(PHONE_CATEGORY, List.of(RAM_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(ImmutablePair.of(SAMSUNG_CATEGORY, List.of(RAM_ATTRIBUTE, TOUCH_ID_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(3);
	}

}
