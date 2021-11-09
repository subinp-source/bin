/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;


@RunWith(MockitoJUnitRunner.class)
public class ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptorTest
{
	@Mock
	private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@Mock
	private ModelService modelService;
	@Mock
	private InterceptorContext interceptorContext;
	@InjectMocks
	private ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptor interceptor;

	@Test
	public void shouldNotRemoveFeatureGroupAssignmentIfModelIsNew()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		given(modelService.isNew(classAttributeAssignment)).willReturn(true);

		//when
		interceptor.onRemove(classAttributeAssignment, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).removeAllFeatureGroupAssignments(any(InterceptorContext.class),
				any(List.class));
	}

	@Test
	public void shouldNotRemoveFeatureGroupAssignmentIfCategoryIsNotInstanceOfClassificationClass()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel classificationClass = mock(MockFlexibleTypeClass.class);

		given(modelService.isNew(classAttributeAssignment)).willReturn(false);
		given(classAttributeAssignment.getClassificationClass()).willReturn(classificationClass);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(false);

		//when
		interceptor.onRemove(classAttributeAssignment, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).removeAllFeatureGroupAssignments(any(InterceptorContext.class),
				any(List.class));
	}

	@Test
	public void shouldRemoveFeatureGroupAssignment()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);

		given(modelService.isNew(classAttributeAssignment)).willReturn(false);
		given(classAttributeAssignment.getClassificationClass()).willReturn(classificationClass);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onRemove(classAttributeAssignment, interceptorContext);

		//then
		final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
		then(classFeatureGroupAssignmentService).should().removeAllFeatureGroupAssignments(eq(interceptorContext),
				captor.capture());
		assertThat(captor.getValue()).containsExactly(classAttributeAssignment);
	}

	public class MockFlexibleTypeClass extends ClassificationClassModel
	{
		// This extension cannot impact on other extensions
	}
}
