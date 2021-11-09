/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.exporter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.merchandising.client.CategoryHierarchyWrapper;
import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClient;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.service.MerchCatalogService;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;
import com.hybris.platform.merchandising.yaas.CategoryHierarchy;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

/**
 * DefaultMerchProductDirectoryCategoryExporter is a default implementation of {@link MerchCategoryExporter} for the purposes of
 * exporting the categories from a configured catalog / catalog version to CDS and assigning them to a product directory.
 *
 */
public class DefaultMerchProductDirectoryCategoryExporter extends AbstractJobPerformable<CronJobModel> implements MerchCategoryExporter {
	private MerchCatalogService merchCatalogService;
	private MerchCatalogServiceProductDirectoryClient client;
	private MerchProductDirectoryConfigService merchProductDirectoryConfigService;
	private TypeService typeService;
	private ChangeDetectionService changeDetectionService;
	private BaseSiteService baseSiteService;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultMerchProductDirectoryCategoryExporter.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCategories() {
		final Collection<MerchProductDirectoryConfigModel> productDirectories = merchProductDirectoryConfigService.getAllMerchProductDirectoryConfigs();
		productDirectories.stream().filter(MerchProductDirectoryConfigModel::isEnabled).forEach(config -> {
			final Optional<BaseSiteModel> baseSite = config.getBaseSites().stream().findFirst();
			baseSite.ifPresent(site -> {
				LOG.debug("Setting base site to first base site being used by directory config: {}", baseSite.get().getName());
				baseSiteService.setCurrentBaseSite(baseSite.get(), true);
				retrieveAndSendCategories(config);
			});
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCategoriesForCurrentBaseSite() {
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		final Collection<MerchProductDirectoryConfigModel> productDirectories = merchProductDirectoryConfigService.getAllMerchProductDirectoryConfigs();
		LOG.debug("Exporting categories for current base site: {}", currentBaseSite.getName());
		productDirectories.stream().filter(MerchProductDirectoryConfigModel::isEnabled)
							.filter(config -> config.getBaseSites().stream().anyMatch(site -> site.getUid().equals(currentBaseSite.getUid())))
							.forEach(this :: retrieveAndSendCategories);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCategories(final MerchProductDirectoryConfigModel config)
	{
		config.getBaseSites().stream().findFirst().ifPresent(baseSite -> {
			LOG.debug("Setting base site to first base site being used by directory config: {}", baseSite.getName());
			baseSiteService.setCurrentBaseSite(baseSite, true);
			retrieveAndSendCategories(config);
		});
	}

	/**
	 * retrieveAndSendCategories is a method for retrieving the category hierarchy and sending it to the configured downstream service.
	 * @param config {@link MerchProductDirectoryConfigModel config}.
	 */
	private void retrieveAndSendCategories(final MerchProductDirectoryConfigModel config) {
		try
		{
			final List<CategoryHierarchy> categoryHierarchy = merchCatalogService.getCategories(config);
			client.handleCategories(config.getCdsIdentifier(), new CategoryHierarchyWrapper(categoryHierarchy));
		}
		catch(final IndexerException exception)
		{
			LOG.error("Exception thrown retrieving categories", exception);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJob) {
		final InMemoryChangesCollector collector = new InMemoryChangesCollector();
		return perform(collector);
	}

	/**
	 * perform is a method for actually performing the delta detection functionality.
	 * @param collector the {@link InMemoryChangesCollector} to hold the changed.
	 * @return an instance of {@link PerformResult} representing the result of the delta detection job.
	 */
	protected PerformResult perform(final InMemoryChangesCollector collector) {
		changeDetectionService.collectChangesForType(typeService.getComposedTypeForClass(CategoryModel.class),
				"categoryExportStream", collector);
		final List<ItemChangeDTO> changes = collector.getChanges();
		if(LOG.isDebugEnabled()) {
			changes.forEach(itemChangeDto -> LOG.debug("Changed CategoryModel found - " + itemChangeDto.getItemPK()
					+ " itemChangeDto info=" + itemChangeDto.getInfo()));
		}
		if(!changes.isEmpty()) {
			LOG.debug("Changes is not empty. Exporting category tree");
			exportCategories();
		}
		changeDetectionService.consumeChanges(changes);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Gets the configured catalog service.
	 * @return the configured {@link MerchCatalogService}.
	 */
	protected MerchCatalogService getMerchCatalogService() {
		return merchCatalogService;
	}

	/**
	 * Sets the configured catalog service.
	 * @param merchCatalogService the configured {@link MerchCatalogService}.
	 */
	public void setMerchCatalogService(final MerchCatalogService merchCatalogService) {
		this.merchCatalogService = merchCatalogService;
	}

	/**
	 * Gets the configured client for handling the sending of the categories to Merch v2.
	 * @return the configured {@link MerchCatalogServiceProductDirectoryClient}.
	 */
	protected MerchCatalogServiceProductDirectoryClient getClient() {
		return client;
	}

	/**
	 * Sets the configured client for handling the sending of categories to Merch v2.
	 * @param client the configured {@link MerchCatalogServiceProductDirectoryClient} to send categories as.
	 */
	public void setClient(final MerchCatalogServiceProductDirectoryClient client) {
		this.client = client;
	}

	/**
	 * Gets the configured {@link TypeService} instance for retrieving composed type for class.
	 * @return the configured {@link TypeService}.
	 */
	protected TypeService getTypeService() {
		return typeService;
	}

	/**
	 * Sets the configured instance of {@link TypeService} for use.
	 * @param typeService the {@link TypeService} to inject.
	 */
	public void setTypeService(final TypeService typeService) {
		this.typeService = typeService;
	}

	/**
	 * Gets the configured instance of {@link ChangeDetectionService} for use with this.
	 * @return configured {@link ChangeDetectionService}.
	 */
	protected ChangeDetectionService getChangeDetectionService() {
		return changeDetectionService;
	}

	/**
	 * Sets the configured instance of {@link ChangeDetectionService}.
	 * @param changeDetectionService - the {@link ChangeDetectionService} to inject.
	 */
	public void setChangeDetectionService(final ChangeDetectionService changeDetectionService) {
		this.changeDetectionService = changeDetectionService;
	}

	/**
	 * Gets the configured instance of {@link BaseSiteService} for use with this.
	 * @return configured {@link BaseSiteService}.
	 */
	protected BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	/**
	 * Sets the configured instance of {@link BaseSiteService}.
	 * @param changeDetectionService - the {@link BaseSiteService} to inject.
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	/**
	 * Gets the configured instance of {@link MerchProductDirectoryConfigService} for use with this.
	 * @return configured {@link MerchProductDirectoryConfigService}.
	 */
	protected MerchProductDirectoryConfigService getMerchProductDirectoryConfigService() {
		return merchProductDirectoryConfigService;
	}

	/**
	 * Sets the configured instance of {@link MerchProductDirectoryConfigService} for use with this.
	 * @param merchProductDirectoryConfigService the {@link MerchProductDirectoryConfigService} to inject.
	 */
	public void setMerchProductDirectoryConfigService(
			final MerchProductDirectoryConfigService merchProductDirectoryConfigService) {
		this.merchProductDirectoryConfigService = merchProductDirectoryConfigService;
	}
}
