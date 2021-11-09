/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;


@RunWith(MockitoJUnitRunner.class)
public class ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptorTest
{
	@Mock
	ModelService modelService;
	@Mock
	ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@InjectMocks
	ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptor interceptor;

	@Test
	public void shouldNotCreateFeatureGroupAssignmentForExistingAttributeAssignment()
	{
		//given
		final ClassAttributeAssignmentModel attributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final InterceptorContext interceptorContext = mock(InterceptorContext.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		given(modelService.isNew(attributeAssignment)).willReturn(false);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);
		given(attributeAssignment.getClassificationClass()).willReturn(classificationClass);

		//when
		interceptor.onPrepare(attributeAssignment, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).createClassFeatureGroupAssignment(any(), any());
	}

	@Test
	public void shouldCreateFeatureGroupAssignmentForNewAttributeAssignment()
	{
		//given
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		final ClassAttributeAssignmentModel attributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final InterceptorContext interceptorContext = mock(InterceptorContext.class);
		given(modelService.isNew(attributeAssignment)).willReturn(true);
		given(attributeAssignment.getClassificationClass()).willReturn(classificationClass);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(attributeAssignment, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should().createGroupAssignmentsForCategory(interceptorContext, classificationClass,
				attributeAssignment);
		then(classFeatureGroupAssignmentService).should().createGroupAssignmentsForSubcategories(interceptorContext,
				attributeAssignment);
	}
}
