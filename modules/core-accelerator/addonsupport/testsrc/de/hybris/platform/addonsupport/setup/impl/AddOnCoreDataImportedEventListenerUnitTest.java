/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addonsupport.impex.AddonConfigDataImportType;
import de.hybris.platform.addonsupport.setup.AddOnConfigDataImportService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.converters.PopulatorList;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AddOnCoreDataImportedEventListenerUnitTest
{
	private static final String STORE_ID = "DUMMY_STORE";
	private static final String CONFIG_EXTENSION_NAME = "DUMMY_EXTENSION_NAME";

	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private AddOnConfigDataImportService dataImportService;

	@InjectMocks
	private AddOnCoreDataImportedEventListener eventListener;


	@Mock
	private BaseSiteModel baseSiteModel;
	@Mock
	private AddOnDataImportEventContext eventContext;
	@Mock
	private ImportData importData;
	@Mock
	private ImpexMacroParameterData impexMacroParameterData;
	@Mock
	private PopulatorList<AddOnDataImportEventContext, ImpexMacroParameterData> populatorList;

	@Before
	public void setUp()
	{
		when(baseSiteService.getBaseSiteForUID(anyString())).thenReturn(baseSiteModel);
		when(impexMacroParameterData.getConfigExtensionName()).thenReturn(CONFIG_EXTENSION_NAME);
		when(populatorList.getPopulators()).thenReturn(Collections.emptyList());
	}

	@Test
	public void testSearchServicesImpexFilesAreNotImportedWhenChannelIsNotSupported()
	{
		eventListener.setSupportedChannels(Lists.newArrayList(SiteChannel.B2C));
		when(baseSiteModel.getChannel()).thenReturn(SiteChannel.B2B);

		eventListener.processStoreNames(eventContext, importData, impexMacroParameterData, false, STORE_ID);

		verify(dataImportService, never()).executeImport(anyString(), any(), any());
	}

	@Test
	public void testSearchServicesImpexFilesAreImportedWhenChannelIsSupported()
	{
		eventListener.setSupportedChannels(Lists.newArrayList(SiteChannel.B2C));
		when(baseSiteModel.getChannel()).thenReturn(SiteChannel.B2C);

		eventListener.processStoreNames(eventContext, importData, impexMacroParameterData, false, STORE_ID);

		verify(dataImportService).executeImport(CONFIG_EXTENSION_NAME, AddonConfigDataImportType.SEARCH_SERVICES, impexMacroParameterData);
	}
}
