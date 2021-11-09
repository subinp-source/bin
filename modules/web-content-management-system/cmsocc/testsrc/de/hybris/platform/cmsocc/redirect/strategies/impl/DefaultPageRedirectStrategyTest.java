/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.strategies.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsocc.redirect.suppliers.PageRedirectSupplier;
import de.hybris.platform.core.model.ItemModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageRedirectStrategyTest
{
	@InjectMocks
	private DefaultPageRedirectStrategy strategy;

	@Mock
	private List<PageRedirectSupplier> suppliers;
	@Mock
	private PageRedirectSupplier supplier;
	@Mock
	private Predicate<ItemModel> predicate;
	@Mock
	private HttpServletRequest request;
	@Mock
	private PreviewDataModel previewData;
	@Mock
	private AbstractPageModel page;

	@Before
	public void setup()
	{
		when(supplier.getConstrainedBy()).thenReturn(predicate);
		when(supplier.shouldRedirect(request, previewData)).thenReturn(true);

		when(predicate.test(any())).thenReturn(true);
		when(previewData.getPage()).thenReturn(page);

		suppliers = Arrays.asList(supplier);
		strategy.setPageRedirectSuppliers(suppliers);
	}

	@Test
	public void shouldRedirectWhenSupplierMatchesPredicateAndShouldRedirectReturnsTrue()
	{
		// WHEN
		final boolean value = strategy.shouldRedirect(request, previewData);

		// THEN
		assertTrue(value);
		verify(supplier).getConstrainedBy();
		verify(predicate).test(previewData.getPage());
		verify(supplier).shouldRedirect(request, previewData);
	}

	@Test
	public void shouldNotRedirectWhenSupplierMatchesPredicateAndShouldRedirectReturnsFalse()
	{
		// GIVEN
		when(supplier.shouldRedirect(request, previewData)).thenReturn(false);

		// WHEN
		final boolean value = strategy.shouldRedirect(request, previewData);

		// THEN
		assertFalse(value);
		verify(supplier).getConstrainedBy();
		verify(predicate).test(previewData.getPage());
		verify(supplier).shouldRedirect(request, previewData);
	}

	@Test
	public void shouldNotRedirectWhenNoSupplierMatchesPredicate()
	{
		// GIVEN
		when(predicate.test(previewData.getPage())).thenReturn(false);

		// WHEN
		final boolean value = strategy.shouldRedirect(request, previewData);

		// THEN
		assertFalse(value);
		verify(supplier).getConstrainedBy();
		verify(predicate).test(previewData.getPage());
		verify(supplier, never()).shouldRedirect(request, previewData);
	}

	@Test
	public void shouldNotRedirectWhenNoPageInPreviewData()
	{
		// GIVEN
		when(previewData.getPage()).thenReturn(null);

		// WHEN
		final boolean value = strategy.shouldRedirect(request, previewData);

		//THEN
		assertFalse(value);
		verifyZeroInteractions(supplier);
	}

}
