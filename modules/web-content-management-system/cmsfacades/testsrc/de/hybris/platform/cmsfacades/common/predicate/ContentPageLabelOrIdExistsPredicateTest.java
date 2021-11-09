/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentPageLabelOrIdExistsPredicateTest
{
	@InjectMocks
	private ContentPageLabelOrIdExistsPredicate predicate;

	@Mock
	private CMSContentPageService cmsContentPageService;

	@Mock
	private ContentPageModel contentPageModel;

	private final String VALID_LABEL_OR_ID = "validLabelOrId";
	private final String INVALID_LABEL_OR_ID = "invalidLabelOrId";


	@Test
	public void shouldReturnTrueIfLabelOrIdExists() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsContentPageService.getPageForLabelOrIdAndMatchType(VALID_LABEL_OR_ID, false)).thenReturn(contentPageModel);

		// WHEN
		final boolean result = predicate.test(VALID_LABEL_OR_ID);

		// THEN
		assertTrue(result);
	}

	@Test
	public void shouldReturnFalseIfLabelOrIdNotExists() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsContentPageService.getPageForLabelOrIdAndMatchType(INVALID_LABEL_OR_ID, false))
				.thenThrow(new CMSItemNotFoundException(""));

		// WHEN
		final boolean result = predicate.test(INVALID_LABEL_OR_ID);

		// THEN
		assertFalse(result);
	}
}

