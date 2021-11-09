/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CmsItemExistsInCatalogVersionPredicateTest
{
	private static final String ITEM_UUID = "some Item Uuid";

	@Mock
	private CMSItemModel itemModel;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Mock
	private CatalogVersionModel otherCatalogVersionModel;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@InjectMocks
	private CmsItemExistsInCatalogVersionPredicate predicate;

	@Before
	public void setUp()
	{
		when(uniqueItemIdentifierService.getItemModel(ITEM_UUID, CMSItemModel.class)).thenReturn(Optional.of(itemModel));
		when(itemModel.getCatalogVersion()).thenReturn(catalogVersionModel);
	}

	@Test
	public void givenItemDoesNotExist_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(uniqueItemIdentifierService.getItemModel(ITEM_UUID, CMSItemModel.class)).thenReturn(Optional.empty());

		// WHEN
		final boolean result = predicate.test(ITEM_UUID, catalogVersionModel);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenUuidIsInvalid_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(uniqueItemIdentifierService.getItemModel(ITEM_UUID, CMSItemModel.class))
				.thenThrow(new UnknownIdentifierException("invalid uuid"));

		// WHEN
		final boolean result = predicate.test(ITEM_UUID, catalogVersionModel);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenItemIsNotInExpectedCatalogVersion_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(itemModel.getCatalogVersion()).thenReturn(otherCatalogVersionModel);

		// WHEN
		final boolean result = predicate.test(ITEM_UUID, catalogVersionModel);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenItemIsInExceptedCatalogVersion_WhenTestIsCalled_ThenItReturnsTrue()
	{
		// WHEN
		final boolean result = predicate.test(ITEM_UUID, catalogVersionModel);

		// THEN
		assertTrue(result);
	}

}
