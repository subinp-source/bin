/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;


@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupAssignmentIndexHandlerTest
{

	private final ClassFeatureGroupAssignmentIndexHandler handler = new ClassFeatureGroupAssignmentIndexHandler();

	@Test
	public void shouldBeHandledCaseWhenFeatureIsNotAssignedToAnyGroup()
	{
		// given
		final ClassFeatureGroupAssignmentModel groupAssignmentModel = mock(ClassFeatureGroupAssignmentModel.class);
		given(groupAssignmentModel.getClassFeatureGroup()).willReturn(null);

		// when
		final int index = handler.get(groupAssignmentModel);

		// then
		assertThat(index).isEqualTo(-1);
	}

	@Test
	public void shouldCorrectIndexBeReturned()
	{
		// given
		final ClassFeatureGroupAssignmentModel groupAssignment02 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupModel groupModel = mock(ClassFeatureGroupModel.class);

		final ClassFeatureGroupAssignmentModel groupAssignment01 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel groupAssignment03 = mock(ClassFeatureGroupAssignmentModel.class);
		given(groupModel.getClassFeatureGroupAssignments())
				.willReturn(List.of(groupAssignment01, groupAssignment02, groupAssignment03));

		given(groupAssignment02.getClassFeatureGroup()).willReturn(groupModel);

		// when
		final int index = handler.get(groupAssignment02);

		// then
		assertThat(index).isEqualTo(2);
	}

	@Test
	public void shouldBeHandledCaseWhenFeatureIsAssignedToGroupButNotYetSaved()
	{
		// given
		final ClassFeatureGroupAssignmentModel groupAssignment02 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupModel groupModel = mock(ClassFeatureGroupModel.class);

		final ClassFeatureGroupAssignmentModel groupAssignment01 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel groupAssignment03 = mock(ClassFeatureGroupAssignmentModel.class);
		given(groupModel.getClassFeatureGroupAssignments()).willReturn(List.of(groupAssignment01, groupAssignment03));

		given(groupAssignment02.getClassFeatureGroup()).willReturn(groupModel);

		// when
		final int index = handler.get(groupAssignment02);

		// then
		assertThat(index).isEqualTo(-1);
	}

}
