/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;


@RunWith(MockitoJUnitRunner.class)
public class ClassificationClassRemoveGroupAssignmentsRemoveInterceptorTest
{

	@Mock
	private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@InjectMocks
	private ClassificationClassRemoveGroupAssignmentsRemoveInterceptor interceptor;


	@Test
	public void shouldRemoveAllClassFeatureGroupAssignmentsFromRemovingCategory()
	{
		//given
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		final ClassAttributeAssignmentModel attributeAssignment1 = mock(ClassAttributeAssignmentModel.class);
		final ClassAttributeAssignmentModel attributeAssignment2 = mock(ClassAttributeAssignmentModel.class);
		final InterceptorContext ctx = mock(InterceptorContext.class);
		final ClassFeatureGroupAssignmentModel classFeaturegroupAssignment1 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel classFeaturegroupAssignment2 = mock(ClassFeatureGroupAssignmentModel.class);
		given(classificationClass.getAllClassificationAttributeAssignments())
				.willReturn(List.of(attributeAssignment1, attributeAssignment2));
		given(classFeatureGroupAssignmentService.findFeatureGroupAssignment(attributeAssignment1, classificationClass))
				.willReturn(Optional.of(classFeaturegroupAssignment1));
		given(classFeatureGroupAssignmentService.findFeatureGroupAssignment(attributeAssignment2, classificationClass))
				.willReturn(Optional.of(classFeaturegroupAssignment2));
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onRemove(classificationClass, ctx);

		//then
		verify(ctx).registerElementFor(classFeaturegroupAssignment1, PersistenceOperation.DELETE);
		verify(ctx).registerElementFor(classFeaturegroupAssignment2, PersistenceOperation.DELETE);
		verifyNoMoreInteractions(ctx);
	}
}
