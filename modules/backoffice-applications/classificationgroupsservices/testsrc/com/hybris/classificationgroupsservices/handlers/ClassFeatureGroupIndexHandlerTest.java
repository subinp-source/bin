/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;


@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupIndexHandlerTest
{

	private final ClassFeatureGroupIndexHandler handler = new ClassFeatureGroupIndexHandler();

	@Test
	public void shouldBeHandledCaseWhenGroupIsNotAssignedToAnyCategory()
	{
		// given
		final ClassFeatureGroupModel groupModel = mock(ClassFeatureGroupModel.class);
		given(groupModel.getClassificationClass()).willReturn(null);

		// when
		final int index = handler.get(groupModel);

		// then
		assertThat(index).isEqualTo(-1);
	}

	@Test
	public void shouldCorrectIndexBeReturned()
	{
		// given
		final ClassFeatureGroupModel group02 = mock(ClassFeatureGroupModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);

		final ClassFeatureGroupModel group01 = mock(ClassFeatureGroupModel.class);
		final ClassFeatureGroupModel group03 = mock(ClassFeatureGroupModel.class);
		given(group02.getClassificationClass()).willReturn(classificationClass);
		given(classificationClass.getClassFeatureGroups()).willReturn(List.of(group01, group02, group03));

		// when
		final int index = handler.get(group02);

		// then
		assertThat(index).isEqualTo(2);
	}

	@Test
	public void shouldBeHandledCaseWhenGroupIsAssignedToCategoryButNotYetSaved()
	{
		// given
		final ClassFeatureGroupModel group02 = mock(ClassFeatureGroupModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);

		final ClassFeatureGroupModel group01 = mock(ClassFeatureGroupModel.class);
		final ClassFeatureGroupModel group03 = mock(ClassFeatureGroupModel.class);
		given(group02.getClassificationClass()).willReturn(classificationClass);
		given(classificationClass.getClassFeatureGroups()).willReturn(List.of(group01, group03));

		// when
		final int index = handler.get(group02);

		// then
		assertThat(index).isEqualTo(-1);
	}

}
