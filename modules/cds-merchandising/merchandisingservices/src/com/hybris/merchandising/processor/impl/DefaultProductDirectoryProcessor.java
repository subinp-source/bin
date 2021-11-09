/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.processor.impl;

import java.net.URL;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.charon.RawResponse;
import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClient;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.model.ProductDirectory;
import com.hybris.merchandising.processor.ProductDirectoryProcessor;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;

import de.hybris.platform.site.BaseSiteService;


/**
 * Default implementation of {@link ProductDirectoryProcessor}
 */
public class DefaultProductDirectoryProcessor implements ProductDirectoryProcessor
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultProductDirectoryProcessor.class);
	private MerchProductDirectoryConfigService merchProductDirectoryConfigService;
	private MerchCatalogServiceProductDirectoryClient catalogServiceClient;
	private BaseSiteService baseSiteService;

	@Override
	public void createUpdate(final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel) {
		LOG.info("createUpdate invoked for product directory with PK: {}", merchProductDirectoryConfigModel.getPk());
		if(merchProductDirectoryConfigModel.isEnabled())
		{
			final ProductDirectory productDirectory = getProductDirectory(merchProductDirectoryConfigModel);
			setBaseSite(merchProductDirectoryConfigModel);
	
			if (StringUtils.isBlank(productDirectory.getId()))
			{
				LOG.info("createUpdate invoked for product directory - new product directory found");
				final String identifier = getProductDirectoryId(catalogServiceClient.createProductDirectory(productDirectory));
				merchProductDirectoryConfigModel.setCdsIdentifier(identifier);
				merchProductDirectoryConfigService.updateMerchProductDirectory(merchProductDirectoryConfigModel);
			}
			else
			{
				LOG.info("Updating product directory with ID:{}", productDirectory.getId());
				catalogServiceClient.updateProductDirectory(productDirectory.getId(), productDirectory);
			}
		}
	}

	@Override
	public void delete(final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel)
	{
		if(merchProductDirectoryConfigModel.getCdsIdentifier() != null)
		{
			setBaseSite(merchProductDirectoryConfigModel);
			LOG.info("Deleting product directory with ID:{}", merchProductDirectoryConfigModel.getCdsIdentifier());
			catalogServiceClient.deleteProductDirectory(merchProductDirectoryConfigModel.getCdsIdentifier());
		}
	}

	/**
	 * Sets the current base site to one which the {@link MerchProductDirectoryConfigModel} is using.
	 * @param merchProductDirectoryConfigModel the config model we wish to set the base site for.
	 */
	private void setBaseSite(final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel)
	{
		merchProductDirectoryConfigModel.getBaseSites().stream().findAny().ifPresent(baseSite -> 
			baseSiteService.setCurrentBaseSite(baseSite, true)
		);
	}

	/**
	 * Converts an {@link MerchProductDirectoryConfigModel} object to a {@link ProductDirectory} object.
	 * @param model the inbound model to convert.
	 * @return a ProductDirectory represneting this.
	 */
	protected ProductDirectory getProductDirectory(final MerchProductDirectoryConfigModel model)
	{
		return ProductDirectory.fromMerchProductDirectoryConfigModel(model);
	}

	/**
	 * Returns the productDirectoryId from the location header of the directory response.
	 * @param directoryResponse the {@link RawResponse} returned by the API call.
	 * @return a String representation of the ID.
	 */
	protected String getProductDirectoryId(final RawResponse directoryResponse)
	{
		final Optional<URL> location = (Optional<URL>) directoryResponse.location();
		return location.map(this :: getIdentifierFromPath).orElse(null);
	}

	/**
	 * Returns the identifier from the location URL.
	 * @param location the location URL to retrieve it from.
	 * @return the identifier extracted from path.
	 */
	private String getIdentifierFromPath(final URL location)
	{
		final String path = location.getPath();
		return path.substring(path.lastIndexOf('/') + 1);
	}

	public void setCatalogServiceClient(final MerchCatalogServiceProductDirectoryClient catalogServiceClient)
	{
		this.catalogServiceClient = catalogServiceClient;
	}

	public void setMerchProductDirectoryConfigService(
			final MerchProductDirectoryConfigService merchProductDirectoryConfigService)
	{
		this.merchProductDirectoryConfigService = merchProductDirectoryConfigService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
}
