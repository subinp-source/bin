/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationattribute;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;


@RunWith(MockitoJUnitRunner.class)
public class ClassificationAttributeRemoveClassFeatureGroupAssignmentRemoveInterceptorTest
{
	@Mock
	private ModelService modelService;
	@Mock
	private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
	@Mock
	private InterceptorContext interceptorContext;
	@InjectMocks
	private ClassificationAttributeRemoveClassFeatureGroupAssignmentRemoveInterceptor interceptor;

	@Test
	public void shouldRemoveAllFeatureGroupAssignments()
	{
		//given
		final ClassificationAttributeModel classificationAttribute = mock(ClassificationAttributeModel.class);
		given(modelService.isNew(classificationAttribute)).willReturn(false);

		//when
		interceptor.onRemove(classificationAttribute, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should().removeAllFeatureGroupAssignments(interceptorContext,
				classificationAttribute);
	}

	@Test
	public void shouldNotRemoveAllFeatureGroupAssignments()
	{
		//given
		final ClassificationAttributeModel classificationAttribute = mock(ClassificationAttributeModel.class);
		given(modelService.isNew(classificationAttribute)).willReturn(true);

		//when
		interceptor.onRemove(classificationAttribute, interceptorContext);

		//then
		then(classFeatureGroupAssignmentService).should(never()).removeAllFeatureGroupAssignments(interceptorContext,
				classificationAttribute);
	}
}
