/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.setup;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.commerceservices.setup.SetupSyncJobService;
import de.hybris.platform.commerceservices.setup.data.EditSyncAttributeDescriptorData;
import de.hybris.platform.commerceservices.setup.impl.DefaultSetupSyncJobService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SetupSyncJobService}, overrides {@link DefaultSetupSyncJobService} and defines new edit sync
 * descriptors for globalMarketplaceProductCatalog
 */
public class MarketplaceSetupSyncJobService extends DefaultSetupSyncJobService
{
	private static final Logger LOG = Logger.getLogger(MarketplaceSetupSyncJobService.class);
	private Map<String, Map<Class<?>, List<EditSyncAttributeDescriptorData>>> productCatalogEditSyncDescriptorsMapping = new LinkedHashMap<>();
	private static final String OFFLINE_VERSION = "Staged";
	private static final String ONLINE_VERSION = "Online";

	@Override
	public void createProductCatalogSyncJob(final String catalogId)
	{
		final SyncItemJobModel syncItemJob = getCatalogSyncJob(catalogId);
		if (Objects.isNull(syncItemJob))
		{
			LOG.info("Creating product sync item job for [" + catalogId + "]");

			final CatalogVersionModel source = getCatalogVersion(catalogId, OFFLINE_VERSION);
			final CatalogVersionModel target = getCatalogVersion(catalogId, ONLINE_VERSION);

			final CatalogVersionSyncJobModel syncJob = getModelService().create(CatalogVersionSyncJobModel.class);
			syncJob.setCode(createJobIdentifier(catalogId));
			syncJob.setSourceVersion(source);
			syncJob.setTargetVersion(target);
			syncJob.setCreateNewItems(Boolean.TRUE);
			syncJob.setRemoveMissingItems(Boolean.TRUE);
			getModelService().save(syncJob);

			processRootTypes(syncJob, catalogId, getProductCatalogRootTypeCodes());
			processEditSyncAttributeDescriptors(syncJob, catalogId, getProductCatalogEditSyncDescriptors(catalogId));

			LOG.info("Created product sync item job [" + syncJob.getCode() + "]");
		}
		else if (getProductCatalogEditSyncDescriptorsMapping().containsKey(catalogId))
		{
			processEditSyncAttributeDescriptors(syncItemJob, catalogId, getProductCatalogEditSyncDescriptorsMapping().get(catalogId));
		}
	}

	protected Map<Class<?>, List<EditSyncAttributeDescriptorData>> getProductCatalogEditSyncDescriptors(final String catalogId)
	{
		if (getProductCatalogEditSyncDescriptorsMapping().containsKey(catalogId))
		{
			return getProductCatalogEditSyncDescriptorsMapping().get(catalogId);
		}
		return getProductCatalogEditSyncDescriptors();
	}

	protected Map<String, Map<Class<?>, List<EditSyncAttributeDescriptorData>>> getProductCatalogEditSyncDescriptorsMapping()
	{
		return productCatalogEditSyncDescriptorsMapping;
	}

	@Required
	public void setProductCatalogEditSyncDescriptorsMapping(
			final Map<String, Map<Class<?>, List<EditSyncAttributeDescriptorData>>> productCatalogEditSyncDescriptorsMapping)
	{
		this.productCatalogEditSyncDescriptorsMapping = productCatalogEditSyncDescriptorsMapping;
	}
}
