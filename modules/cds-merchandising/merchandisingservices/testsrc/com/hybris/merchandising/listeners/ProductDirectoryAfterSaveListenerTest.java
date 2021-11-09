/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.processor.ProductDirectoryProcessor;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;

@IntegrationTest
public class ProductDirectoryAfterSaveListenerTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private MerchProductDirectoryConfigService merchProductDirectoryConfigService;

	private ProductDirectoryAfterSaveListener listener;

	private ProductDirectoryProcessor processor;

	private static final int PRODUCT_DIRECTORY_TYPE_CODE = 2506;
	private static final String INDEXED_TYPE = "solrIndexedType1";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/integration/DefaultMerchProductDirectoryConfigServiceIntegrationTest.impex", CharEncoding.UTF_8);
		processor = Mockito.mock(ProductDirectoryProcessor.class);
		listener = new ProductDirectoryAfterSaveListener();
		listener.setModelService(modelService);
		listener.setProductDirectoryProcessor(processor);
	}

	@Test
	public void afterSaveProductDirectoryHandling()
	{
		testAfterSaveEvent(AfterSaveEvent.UPDATE);
		testAfterSaveEvent(AfterSaveEvent.CREATE);
		Mockito.verify(processor, Mockito.times(2)).createUpdate(Mockito.any(MerchProductDirectoryConfigModel.class));
	}

	private void testAfterSaveEvent(final int action)
	{
		final MerchProductDirectoryConfigModel configModel = getConfigModel();
		if(configModel == null)
		{
			fail("Config model is null");
		}
		final AfterSaveEvent afterSave = Mockito.mock(AfterSaveEvent.class);
		Mockito.when(afterSave.getPk()).thenReturn(configModel.getPk());
		Mockito.when(afterSave.getType()).thenReturn(action);
		listener.afterSave(Collections.singletonList(afterSave));
	}

	private MerchProductDirectoryConfigModel getConfigModel()
	{
		final Optional<MerchProductDirectoryConfigModel> config = merchProductDirectoryConfigService.getMerchProductDirectoryConfigForIndexedType(INDEXED_TYPE);
		if(config.isPresent())
		{
			final MerchProductDirectoryConfigModel configModel = config.get();
			final PK modelPk = configModel.getPk();
			assertEquals("Expected model to have PK", PRODUCT_DIRECTORY_TYPE_CODE, modelPk.getTypeCode());
			return configModel;
		}
		return null;
	}
}
