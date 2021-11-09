/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.processor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.hybris.charon.RawResponse;
import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClient;
import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClientAdapter;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.model.ProductDirectory;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;
import com.hybris.merchandising.service.impl.DefaultMerchProductDirectoryConfigService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.site.BaseSiteService;


/**
 * {@link DefaultProductDirectoryProcessorTest} test class for {@link DefaultProductDirectoryProcessor}
 */
@UnitTest
public class DefaultProductDirectoryProcessorTest
{
	public static final String CDS_IDENTIFIER = UUID.randomUUID().toString();
	public static final String APPAREL_UK = "apparel-uk";

	private DefaultProductDirectoryProcessor productDirectoryProcessor;
	private MerchProductDirectoryConfigModel configModel;
	private BaseSiteModel baseSiteModel;
	private BaseSiteService baseSiteService;
	private LanguageModel languageModel;
	private MerchCatalogServiceProductDirectoryClient catalogServiceClient;
	private MerchProductDirectoryConfigService productDirectoryConfigService;

	@Before
	public void setUp() {
		productDirectoryProcessor = new DefaultProductDirectoryProcessor();
		configModel = Mockito.mock(MerchProductDirectoryConfigModel.class);
		baseSiteModel = Mockito.mock(BaseSiteModel.class);
		languageModel = Mockito.mock(LanguageModel.class);
		baseSiteService = Mockito.mock(BaseSiteService.class);

		Mockito.when(baseSiteModel.getUid()).thenReturn(APPAREL_UK);
		Mockito.when(baseSiteModel.getName()).thenReturn(APPAREL_UK);

		Mockito.when(configModel.isEnabled()).thenReturn(Boolean.TRUE);
		Mockito.when(configModel.getDefaultLanguage()).thenReturn(languageModel);
		Mockito.when(configModel.getBaseSites()).thenReturn(Arrays.asList(baseSiteModel));
		Mockito.when(configModel.getDisplayName()).thenReturn("Apparel-UK PD");
		Mockito.when(configModel.getRollUpStrategy()).thenReturn("Master Product");

		catalogServiceClient = Mockito.mock(MerchCatalogServiceProductDirectoryClientAdapter.class);
		productDirectoryConfigService = Mockito.mock(DefaultMerchProductDirectoryConfigService.class);

		final RawResponse response = Mockito.mock(RawResponse.class);
		try
		{
			final URL location = new URL("http://localhost:80/mytenant/" + CDS_IDENTIFIER);
			Mockito.when(response.location()).thenReturn(Optional.of(location));
			Mockito.when(catalogServiceClient.createProductDirectory(Mockito.any(ProductDirectory.class))).thenReturn(response);
		}
		catch(final MalformedURLException e)
		{
			fail("Exception creating mocked URL");
		}

		productDirectoryProcessor.setBaseSiteService(baseSiteService);
		productDirectoryProcessor.setCatalogServiceClient(catalogServiceClient);
		productDirectoryProcessor.setMerchProductDirectoryConfigService(productDirectoryConfigService);
	}

	@Test
	public void testCreateProductDirectory()
	{
		Mockito.when(configModel.getCdsIdentifier()).thenReturn(null);
		productDirectoryProcessor.createUpdate(configModel);
		Mockito.verify(catalogServiceClient).createProductDirectory(Mockito.any());
		Mockito.verify(productDirectoryConfigService).updateMerchProductDirectory(Mockito.any());
	}

	@Test
	public void testUpdateProductDirectory()
	{
		Mockito.when(configModel.getCdsIdentifier()).thenReturn(CDS_IDENTIFIER);
		productDirectoryProcessor.createUpdate(configModel);
		Mockito.verify(catalogServiceClient).updateProductDirectory(Mockito.anyString(), Mockito.any());
		Mockito.verify(productDirectoryConfigService, Mockito.never()).updateMerchProductDirectory(Mockito.any());
	}

	@Test
	public void testUpdateProductDirectoryNotEnabled()
	{
		Mockito.when(configModel.isEnabled()).thenReturn(Boolean.FALSE);
		productDirectoryProcessor.createUpdate(configModel);
		Mockito.verify(catalogServiceClient, Mockito.never()).updateProductDirectory(Mockito.anyString(), Mockito.any());

	}

	@Test
	public void testDelete()
	{
		Mockito.when(configModel.getCdsIdentifier()).thenReturn(CDS_IDENTIFIER);
		productDirectoryProcessor.delete(configModel);
		Mockito.verify(catalogServiceClient).deleteProductDirectory(configModel.getCdsIdentifier());
	}

	@Test
	public void testGetProductDirectoryId()
	{
		try
		{
			final RawResponse<String> response = Mockito.mock(RawResponse.class);
			final URL url = new URL("https://localhost:9002/test/12345");
			Mockito.when(response.location()).thenReturn(Optional.of(url));
	
			final String identifier = productDirectoryProcessor.getProductDirectoryId(response);
			assertEquals("Expected identifier to be set", "12345", identifier);
		}
		catch(final MalformedURLException e)
		{
			fail("Exception thrown setting up test");
		}
	}
}
