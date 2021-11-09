/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class IsAnyItemAlreadyInWorkflowPredicateTest
{
	private final String ITEM_UUID_1 = "some item uuid 1";
	private final String ITEM_UUID_2 = "some item uuid 2";

	private List<String> items;

	@Mock
	private CMSItemModel cmsItemModel1;

	@Mock
	private CMSItemModel cmsItemModel2;

	@Mock
	private CMSWorkflowService cmsWorkflowService;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@InjectMocks
	private IsAnyItemAlreadyInWorkflowPredicate isAnyItemAlreadyInWorkflowPredicate;

	@Before
	public void setUp()
	{
		items = Arrays.asList(ITEM_UUID_1, ITEM_UUID_2);

		when(uniqueItemIdentifierService.getItemModel(ITEM_UUID_1, CMSItemModel.class)).thenReturn(Optional.of(cmsItemModel1));
		when(uniqueItemIdentifierService.getItemModel(ITEM_UUID_2, CMSItemModel.class)).thenReturn(Optional.of(cmsItemModel2));

		when(cmsWorkflowService.isAnyItemInWorkflow(Arrays.asList(cmsItemModel1, cmsItemModel2))).thenReturn(true);
	}

	@Test
	public void givenItemDoesNotExist_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(uniqueItemIdentifierService.getItemModel(ITEM_UUID_2, CMSItemModel.class)).thenReturn(Optional.empty());

		// WHEN
		final boolean result = isAnyItemAlreadyInWorkflowPredicate.test(items);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenItemsDoNotBelongToAWorkflow_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(cmsWorkflowService.isAnyItemInWorkflow(Arrays.asList(cmsItemModel1, cmsItemModel2))).thenReturn(false);

		// WHEN
		final boolean result = isAnyItemAlreadyInWorkflowPredicate.test(items);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenItemsAlreadyBelongToAWorkflow_WhenTestIsCalled_ThenItReturnsTrue()
	{
		// WHEN
		final boolean result = isAnyItemAlreadyInWorkflowPredicate.test(items);

		// THEN
		assertTrue(result);
	}
}
