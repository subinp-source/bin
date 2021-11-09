/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.sap.productconfig.runtime.interf.services.ConfigurationProductUtil;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;




/**
 * Default implementation of {@link ConfigurationProductUtil}
 */
public class ConfigurationProductUtilImpl implements ConfigurationProductUtil
{
	private static final Logger LOG = Logger.getLogger(ConfigurationProductUtilImpl.class);
	private CatalogVersionService catalogVersionService;
	private ProductDao productDao;

	/**
	 * @param productDao
	 *           Accessing the product persistence
	 */
	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}

	protected ProductDao getProductDao()
	{
		return this.productDao;
	}

	@Override
	public ProductModel getProductForCurrentCatalog(final String productCode)
	{
		return getProductForActiveVersions(getCurrentCatalogVersions(), productCode);
	}

	protected List<CatalogVersionModel> getCurrentCatalogVersions()
	{
		final List<CatalogVersionModel> versionList = getCatalogVersionService().getSessionCatalogVersions().stream()
				.filter(cV -> isProductCatalogVersionActive(cV)).collect(Collectors.toList());
		Preconditions.checkState(!versionList.isEmpty(), "We expect at least one active catalog version");
		return versionList;
	}

	protected ProductModel getProductForActiveVersions(final List<CatalogVersionModel> versions, final String productCode)
	{
		final List<ProductModel> productList = versions.stream()
				.flatMap(version -> getProductDao().findProductsByCode(version, productCode).stream()).collect(Collectors.toList());
		Preconditions.checkState(productList.size() == 1, "We expect to get exactly one product from the active catalog versions");

		return productList.get(0);
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected boolean isProductCatalogVersionActive(final CatalogVersionModel currentCatalogVersion)
	{
		final Boolean active = currentCatalogVersion.getActive();
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format(
					"When searching for active catalog versions, we found version %s for catalog %s in session which is active: %s",
					currentCatalogVersion.getVersion(), currentCatalogVersion.getCatalog().getId(), active.booleanValue()));
		}
		return (active.booleanValue()) && !(currentCatalogVersion.getCatalog() instanceof ContentCatalogModel)
				&& !(currentCatalogVersion.getCatalog() instanceof ClassificationSystemModel);
	}



}
