/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addonsupport.impex.AddonConfigDataImportType;
import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultAddOnConfigDataImportServiceUnitTest
{
	private static final String EXTENSION_NAME = "DUMMY_EXTENSION_NAME";

	@Mock
	private SetupImpexService setupImpexService;

	@InjectMocks
	private DefaultAddOnConfigDataImportService importService;

	@Mock
	private ImpexMacroParameterData impexMacroParameterData;

	@Test
	public void testExecuteImportForSearchServicesWithSearchServicesImpexFile()
	{
		when(setupImpexService.importImpexFile(anyString(), any(ImpexMacroParameterData.class), anyBoolean(), anyBoolean()))
				.thenReturn(true)
				.thenReturn(false);

		final boolean result = importService.executeImport(EXTENSION_NAME, AddonConfigDataImportType.SEARCH_SERVICES, impexMacroParameterData);

		verify(setupImpexService).importImpexFile("/" + EXTENSION_NAME + "/import/searchservices/template/searchservices.impex", impexMacroParameterData, false, false);
		verify(setupImpexService).importImpexFile("/" + EXTENSION_NAME + "/import/searchservices/template/searchservicestrigger.impex", impexMacroParameterData, false, false);
		assertThat(result).isTrue();
	}

	@Test
	public void testExecuteImportForSearchServicesWithSearchServicesTriggerImpexFile()
	{
		when(setupImpexService.importImpexFile(anyString(), any(ImpexMacroParameterData.class), anyBoolean(), anyBoolean()))
				.thenReturn(false)
				.thenReturn(true);

		final boolean result = importService.executeImport(EXTENSION_NAME, AddonConfigDataImportType.SEARCH_SERVICES, impexMacroParameterData);

		verify(setupImpexService).importImpexFile("/" + EXTENSION_NAME + "/import/searchservices/template/searchservices.impex", impexMacroParameterData, false, false);
		verify(setupImpexService).importImpexFile("/" + EXTENSION_NAME + "/import/searchservices/template/searchservicestrigger.impex", impexMacroParameterData, false, false);
		assertThat(result).isTrue();
	}

	@Test
	public void testExecuteImportForSearchServicesWithNoImpexFiles()
	{
		when(setupImpexService.importImpexFile(anyString(), any(ImpexMacroParameterData.class), anyBoolean(), anyBoolean()))
				.thenReturn(false)
				.thenReturn(false);

		final boolean result = importService.executeImport(EXTENSION_NAME, AddonConfigDataImportType.SEARCH_SERVICES, impexMacroParameterData);

		verify(setupImpexService).importImpexFile("/" + EXTENSION_NAME + "/import/searchservices/template/searchservices.impex", impexMacroParameterData, false, false);
		verify(setupImpexService).importImpexFile("/" + EXTENSION_NAME + "/import/searchservices/template/searchservicestrigger.impex", impexMacroParameterData, false, false);
		assertThat(result).isFalse();
	}
}
