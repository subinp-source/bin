/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;


@RunWith(MockitoJUnitRunner.class)
public class ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptorTest
{
	@Mock
	ModelService modelService;
	@Mock
	ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@Mock
	ClassificationClassModel classificationClass;
	@Mock
	InterceptorContext ctx;
	@InjectMocks
	ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptor interceptor;

	@Test
	public void shouldNotCreateFeatureGroupAssignmentIfThereIsNoClassAttributeAssignment()
	{
		//given
		given(classificationClass.getAllClassificationAttributeAssignments()).willReturn(List.of());
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, ctx);

		//then
		then(ctx).should(never()).registerElementFor(any(), any());
		then(classFeatureGroupAssignmentService).should(never()).createClassFeatureGroupAssignment(any(), any());
	}

	@Test
	public void shouldCreateLackingFeatureGroupAssignmentsForAllClassAttributeAssignments()
	{
		//given
		final ClassAttributeAssignmentModel attribute1 = mock(ClassAttributeAssignmentModel.class);
		final ClassAttributeAssignmentModel attribute2 = mock(ClassAttributeAssignmentModel.class);
		given(classificationClass.getAllClassificationAttributeAssignments()).willReturn(List.of(attribute1, attribute2));
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, ctx);

		//then
		then(classFeatureGroupAssignmentService).should().createLackingFeatureGroupAssignments(ctx, classificationClass,
				attribute1);
		then(classFeatureGroupAssignmentService).should().createLackingFeatureGroupAssignments(ctx, classificationClass,
				attribute2);
	}

}
