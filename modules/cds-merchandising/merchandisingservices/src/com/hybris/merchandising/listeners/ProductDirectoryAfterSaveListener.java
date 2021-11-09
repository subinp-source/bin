/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.listeners;

import java.util.Collection;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.processor.ProductDirectoryProcessor;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;

/**
 * ProductDirectoryAfterSaveListener is an implementation of {@link AfterSaveListener} for use with
 * capturing changes to {@link MerchProductDirectoryConfigModel} models.
 *
 */
public class ProductDirectoryAfterSaveListener implements AfterSaveListener {
	private ModelService modelService;
	private ProductDirectoryProcessor productDirectoryProcessor;

	private static final int PRODUCT_DIRECTORY_TYPE_CODE = 2506;

	@Override
	public void afterSave(final Collection<AfterSaveEvent> events)
	{
		for (final AfterSaveEvent event : events) {
			final int type = event.getType();
			if (AfterSaveEvent.UPDATE == type || AfterSaveEvent.CREATE == type) {
				final PK pk = event.getPk();
				if (PRODUCT_DIRECTORY_TYPE_CODE == pk.getTypeCode()) {
					final MerchProductDirectoryConfigModel productDirectory = modelService.get(pk);
					productDirectoryProcessor.createUpdate(productDirectory);
				}
			}
		}
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setProductDirectoryProcessor(final ProductDirectoryProcessor productDirectoryProcessor)
	{
		this.productDirectoryProcessor = productDirectoryProcessor;
	}
}
