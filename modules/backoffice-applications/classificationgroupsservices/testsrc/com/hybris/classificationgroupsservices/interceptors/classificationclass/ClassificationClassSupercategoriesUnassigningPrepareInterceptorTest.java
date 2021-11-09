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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;


@RunWith(MockitoJUnitRunner.class)
public class ClassificationClassSupercategoriesUnassigningPrepareInterceptorTest
{
	@Mock
	private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@Mock
	private ModelService modelService;
	@Mock
	private InterceptorContext interceptorContext;
	@Mock
	private ClassificationClassModel classificationClass;
	@InjectMocks
	private ClassificationClassSupercategoriesUnassigningPrepareInterceptor interceptor;

	@Test
	public void shouldNotRemoveFeatureGroupAssignmentsIfThereIsNoUnassignedSupercategories()
	{
		//given
		given(modelService.isNew(classificationClass)).willReturn(false);
		given(classFeatureGroupAssignmentService.findUnassignedSupercategories(classificationClass)).willReturn(List.of());
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).removeFeatureGroupAssignmentsInCategory(any(), any(), any());
		then(classFeatureGroupAssignmentService).should(never()).removeFeatureGroupAssignmentsInSubCategories(any(), any(), any());
	}

	@Test
	public void shouldNotRemoveFeatureGroupAssignmentsIfClassificationClassIsNew()
	{
		//given
		given(modelService.isNew(classificationClass)).willReturn(true);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).removeFeatureGroupAssignmentsInCategory(any(), any(), any());
		then(classFeatureGroupAssignmentService).should(never()).removeFeatureGroupAssignmentsInSubCategories(any(), any(), any());
	}

	@Test
	public void shouldRemoveFeatureGroupAssignments()
	{
		//given
		given(modelService.isNew(classificationClass)).willReturn(false);
		final ClassificationClassModel unassignedCategory1 = mock(ClassificationClassModel.class);
		final ClassificationClassModel unassignedCategory2 = mock(ClassificationClassModel.class);
		given(classFeatureGroupAssignmentService.findUnassignedSupercategories(classificationClass))
				.willReturn(List.of(unassignedCategory1, unassignedCategory2));
		final List attributeAssignmentOfCategory1 = mock(List.class);
		final List attributeAssignmentOfCategory2 = mock(List.class);
		given(unassignedCategory1.getAllClassificationAttributeAssignments()).willReturn(attributeAssignmentOfCategory1);
		given(unassignedCategory2.getAllClassificationAttributeAssignments()).willReturn(attributeAssignmentOfCategory2);
		given(classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass)).willReturn(true);

		//when
		interceptor.onPrepare(classificationClass, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should().removeFeatureGroupAssignmentsInCategory(interceptorContext,
				classificationClass, attributeAssignmentOfCategory1);
		then(classFeatureGroupAssignmentService).should().removeFeatureGroupAssignmentsInSubCategories(interceptorContext,
				classificationClass, attributeAssignmentOfCategory1);
		then(classFeatureGroupAssignmentService).should().removeFeatureGroupAssignmentsInCategory(interceptorContext,
				classificationClass, attributeAssignmentOfCategory2);
		then(classFeatureGroupAssignmentService).should().removeFeatureGroupAssignmentsInSubCategories(interceptorContext,
				classificationClass, attributeAssignmentOfCategory2);
	}
}
