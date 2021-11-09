/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.exception.NonEditableItemInWorkflowException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowItemValidatorTest
{
	@Mock
	private CMSWorkflowService cmsWorkflowService;

	@Mock
	private CMSItemModel cmsItemModel;

	@InjectMocks
	@Spy
	private DefaultWorkflowItemValidator defaultWorkflowItemValidator;

	@Before
	public void setUp()
	{
		doThrow(new NonEditableItemInWorkflowException("invalid")).when(defaultWorkflowItemValidator).throwNotEditableInCurrentWorkflowContextException();
	}

	@Test(expected = NonEditableItemInWorkflowException.class)
	public void shouldThrowExceptionIfItemIsNotEditableByCurrentUser()
	{
		// GIVEN
		when(cmsWorkflowService.isItemEditableBySessionUser(cmsItemModel)).thenReturn(false);

		// WHEN
		defaultWorkflowItemValidator.validate(cmsItemModel);
	}

	@Test
	public void shouldPassIfUserIsParticipantOfWorkflowThatContainsItem()
	{
		// GIVEN
		when(cmsWorkflowService.isItemEditableBySessionUser(cmsItemModel)).thenReturn(true);

		// WHEN
		defaultWorkflowItemValidator.validate(cmsItemModel);
	}
}
