/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.functions;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.NavigationEntryData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultNavigationEntryCmsPageItemModelConversionFunctionTest
{

	@Mock
	private CMSAdminPageService cmsAdminPageService;

	@InjectMocks
	private DefaultNavigationEntryCmsPageItemModelConversionFunction conversionFunction;

	@Test
	public void testGetPageFromNavigationEntry()
	{
		final NavigationEntryData navigationEntry = Mockito.mock(NavigationEntryData.class);
		conversionFunction.apply(navigationEntry);
		verify(cmsAdminPageService).getPageForIdFromActiveCatalogVersion(Mockito.anyString());
	}
}
