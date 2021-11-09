/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.hybris.merchandising.dao.MerchProductDirectoryConfigDao;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedTypeDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

/**
 * Default implementation of {@link MerchProductDirectoryConfigService}.
 */
public class DefaultMerchProductDirectoryConfigService implements MerchProductDirectoryConfigService
{
	private static final Logger LOG = Logger.getLogger(DefaultMerchProductDirectoryConfigService.class);
	protected static final String ID_PARAM = "id";
	private SolrIndexedTypeDao solrIndexedTypeDao;
	private MerchProductDirectoryConfigDao merchProductDirectoryConfigDao;
	private ModelService modelService;
	private BaseSiteService baseSiteService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<MerchProductDirectoryConfigModel> getAllMerchProductDirectoryConfigs()
	{
		return merchProductDirectoryConfigDao.findAllMerchProductDirectoryConfigs();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MerchProductDirectoryConfigModel> getMerchProductDirectoryConfigForIndexedType(final String indexedType)
	{
		ServicesUtil.validateParameterNotNullStandardMessage(ID_PARAM, indexedType);

		final SolrIndexedTypeModel solrIndexedType = solrIndexedTypeDao.findIndexedTypeByIdentifier(indexedType);

		return merchProductDirectoryConfigDao.findMerchProductDirectoryConfigByIndexedType(solrIndexedType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MerchProductDirectoryConfigModel> getMerchProductDirectoryConfigForCurrentBaseSite()
	{
		return getMerchProductDirectoryConfigForBaseSite(baseSiteService.getCurrentBaseSite());
	}

	/**
	 * Retrieves the {@link MerchProductDirectoryConfigModel> being used by the specified base site.
	 * @param baseSite the {@link BaseSiteModel} being used.
	 * @return an Optional containing the {@link MerchProductDirectoryConfigModel} if found.
	 */
	private Optional<MerchProductDirectoryConfigModel> getMerchProductDirectoryConfigForBaseSite(final BaseSiteModel baseSite)
	{
		return getAllMerchProductDirectoryConfigs().stream()
				.filter(dir -> dir.getBaseSites().contains(baseSite))
				.findFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMerchProductDirectory(final MerchProductDirectoryConfigModel model) {
		LOG.debug("Updating merch product directory");
		getModelService().save(model);
	}

	protected SolrIndexedTypeDao getSolrIndexedTypeDao()
	{
		return solrIndexedTypeDao;
	}

	public void setSolrIndexedTypeDao(final SolrIndexedTypeDao solrIndexedTypeDao)
	{
		this.solrIndexedTypeDao = solrIndexedTypeDao;
	}

	protected MerchProductDirectoryConfigDao getMerchProductDirectoryConfigDao()
	{
		return merchProductDirectoryConfigDao;
	}

	public void setMerchProductDirectoryConfigDao(final MerchProductDirectoryConfigDao merchProductDirectoryConfigDao)
	{
		this.merchProductDirectoryConfigDao = merchProductDirectoryConfigDao;
	}

	protected ModelService getModelService() {
		return modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
}
