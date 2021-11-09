/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

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
public class ClassificationClassAttributeAssignmentRemovalPrepareInterceptorTest
{
	@Mock
	private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@Mock
	private ModelService modelService;
	@Mock
	private InterceptorContext interceptorContext;
	@InjectMocks
	private ClassificationClassAttributeAssignmentRemovalPrepareInterceptor interceptor;

	@Test
	public void shouldNotRemoveFeatureGroupAssignmentOfNewClassAttributeAssignment()
	{
		//given
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		given(modelService.isNew(classificationClass)).willReturn(true);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).removeFeatureGroupAssignments(any(), any());
	}

	@Test
	public void shouldInvokeRemovingFeatureGroupAssignmentIfClassAttributeAssignmentIsNotNew()
	{
		//given
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		given(modelService.isNew(classificationClass)).willReturn(false);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should().removeFeatureGroupAssignments(interceptorContext, classificationClass);
	}
}
