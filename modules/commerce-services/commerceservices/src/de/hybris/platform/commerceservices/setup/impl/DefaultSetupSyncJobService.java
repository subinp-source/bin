/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.commerceservices.setup.SetupSyncJobService;
import de.hybris.platform.commerceservices.setup.data.EditSyncAttributeDescriptorData;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SetupSyncJobService}
 */
public class DefaultSetupSyncJobService implements SetupSyncJobService
{
	private static final Logger LOG = Logger.getLogger(DefaultSetupSyncJobService.class);

	private static final String OFFLINE_VERSION = "Staged";
	private static final String ONLINE_VERSION = "Online";
	private static final String CMS_ITEM = "CMSItem";

	private ModelService modelService;
	private List<String> productCatalogRootTypeCodes = new LinkedList<>();
	private List<String> contentCatalogRootTypeCodes = new LinkedList<>();
	private Map<Class<?>, List<EditSyncAttributeDescriptorData>> contentCatalogEditSyncDescriptors = new LinkedHashMap<>();
	private Map<Class<?>, List<EditSyncAttributeDescriptorData>> productCatalogEditSyncDescriptors = new LinkedHashMap<>();
	private CatalogVersionService catalogVersionService;
	private CatalogSynchronizationService catalogSynchronizationService;
	private CatalogService catalogService;
	private CronJobService cronJobService;
	private TypeService typeService;
	private FlexibleSearchService flexibleSearchService;

	@Override
	public void createProductCatalogSyncJob(final String catalogId)
	{
		// Check if the sync job already exists
		if (getCatalogSyncJob(catalogId) == null)
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
			processEditSyncAttributeDescriptors(syncJob, catalogId, getProductCatalogEditSyncDescriptors());

			LOG.info("Created product sync item job [" + syncJob.getCode() + "]");
		}
	}

	protected CatalogVersionModel getCatalogVersion(final String catalogId, final String version)
	{
		return getCatalogVersionService().getCatalogVersion(catalogId, version);
	}

	@Override
	public void assignDependentSyncJobs(final String catalogId, final Set<String> dependentCatalogIds)
	{
		final SyncItemJobModel masterSyncJob = getSyncJobForCatalog(catalogId);
		if (masterSyncJob instanceof CatalogVersionSyncJobModel)
		{
			final CatalogVersionSyncJobModel masterCatSyncJob = (CatalogVersionSyncJobModel) masterSyncJob;

			final Set<CatalogVersionSyncJobModel> dependentSyncJobs = new LinkedHashSet();

			// Get the existing dependent sync jobs
			final Set<CatalogVersionSyncJobModel> existingDependentSyncJobs = masterCatSyncJob.getDependentSyncJobs();
			if (existingDependentSyncJobs != null)
			{
				dependentSyncJobs.addAll(existingDependentSyncJobs);
			}

			// Add the new dependent sync jobs for the catalog Ids
			for (final String dependantCatalogId : dependentCatalogIds)
			{
				final SyncItemJobModel dependantSyncJob = getSyncJobForCatalog(dependantCatalogId);
				if (dependantSyncJob instanceof CatalogVersionSyncJobModel)
				{
					dependentSyncJobs.add((CatalogVersionSyncJobModel) dependantSyncJob);
				}
			}

			// Set the new dependent sync jobs set
			masterCatSyncJob.setDependentSyncJobs(dependentSyncJobs);
			getModelService().save(masterCatSyncJob);

			if (LOG.isInfoEnabled())
			{
				LOG.info("Set DependentSyncJobs on CatalogVersionSyncJob [" + masterCatSyncJob.getCode() + "] to ["
						+ catalogVersionSyncJobsToString(dependentSyncJobs) + "]");
			}
		}
	}

	protected String catalogVersionSyncJobsToString(final Collection<CatalogVersionSyncJobModel> catalogVersionSyncJobs)
	{
		final StringBuilder buf = new StringBuilder();
		for (final CatalogVersionSyncJobModel catalogVersionSyncJob : catalogVersionSyncJobs)
		{
			if (buf.length() > 0)
			{
				buf.append(", ");
			}

			buf.append(catalogVersionSyncJob.getCode());
		}
		return buf.toString();
	}


	@Override
	public void createContentCatalogSyncJob(final String catalogId)
	{
		// Check if the sync job already exists
		SyncItemJobModel syncItemJob = getCatalogSyncJob(catalogId);
		if (syncItemJob == null)
		{
			LOG.info("Creating content sync item job for [" + catalogId + "]");

			final CatalogVersionModel source = getCatalogVersion(catalogId, OFFLINE_VERSION);
			final CatalogVersionModel target = getCatalogVersion(catalogId, ONLINE_VERSION);

			syncItemJob = getModelService().create(CatalogVersionSyncJobModel.class);
			syncItemJob.setCode(createJobIdentifier(catalogId));
			syncItemJob.setSourceVersion(source);
			syncItemJob.setTargetVersion(target);
			syncItemJob.setCreateNewItems(Boolean.TRUE);
			syncItemJob.setRemoveMissingItems(Boolean.TRUE);
			getModelService().save(syncItemJob);

			processRootTypes(syncItemJob, catalogId, getContentCatalogRootTypeCodes());

			final ComposedTypeModel cmsItemType = tryGetComposedType(CMS_ITEM);
			final Collection<SyncAttributeDescriptorConfigModel> cfgs = syncItemJob.getSyncAttributeConfigurations();
			for (final SyncAttributeDescriptorConfigModel syncAttributeDescriptorConfig : cfgs)
			{
				final TypeModel attributeType = syncAttributeDescriptorConfig.getAttributeDescriptor().getAttributeType();
				if (CMS_ITEM.equals(syncAttributeDescriptorConfig.getAttributeDescriptor().getEnclosingType().getCode())
						&& (CMS_ITEM.equals(attributeType.getCode()) || cmsItemType.getAllSubTypes().contains(attributeType))
						|| (attributeType instanceof CollectionTypeModel
								&& (CMS_ITEM.equals(((CollectionTypeModel) attributeType).getElementType().getCode())
										|| cmsItemType.getAllSubTypes().contains(((CollectionTypeModel) attributeType).getElementType()))))
				{
					syncAttributeDescriptorConfig.setCopyByValue(true);
					getModelService().save(syncAttributeDescriptorConfig);
				}
			}

			processEditSyncAttributeDescriptors(syncItemJob, catalogId, getContentCatalogEditSyncDescriptors());

			LOG.info("Created content sync item job [" + syncItemJob.getCode() + "]");
		}
	}


	protected String createJobIdentifier(final String catalogId)
	{
		return "sync " + catalogId + ":" + OFFLINE_VERSION + "->" + ONLINE_VERSION;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.setup.SetupSyncJobService#executeCatalogSyncJob(de.hybris.platform.core
	 * .initialization.SystemSetupContext, java.lang.String)
	 */
	@Override
	public PerformResult executeCatalogSyncJob(final String catalogId)
	{
		final SyncItemJobModel catalogSyncJob = getCatalogSyncJob(catalogId);
		if (catalogSyncJob == null)
		{
			LOG.error("Couldn't find 'SyncItemJob' for catalog [" + catalogId + "]", null);
			return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN);
		}
		else
		{
			final SyncItemCronJobModel syncJob = getLastFailedSyncCronJob(catalogSyncJob);

			LOG.info("Created cronjob [" + syncJob.getCode() + "] to synchronize catalog [" + catalogId
					+ "] staged to online version.");
			LOG.info("Starting synchronization, this may take a while ...");

			getCronJobService().performCronJob(syncJob, true);

			LOG.info("Synchronization complete for catalog [" + catalogId + "]");

			final CronJobResult result = syncJob.getResult();
			final CronJobStatus status = syncJob.getStatus();

			return new PerformResult(result, status);
		}
	}

	/**
	 * Returns the last cronjob if exists and failed or the new one otherwise
	 *
	 * @param syncItemJob
	 * @return synchronization cronjob - new one or the last one if failed
	 */
	protected SyncItemCronJobModel getLastFailedSyncCronJob(final SyncItemJobModel syncItemJob)
	{
		SyncItemCronJobModel syncCronJob = null;
		if (CollectionUtils.isNotEmpty(syncItemJob.getCronJobs()))
		{
			final List<CronJobModel> cronjobs = new ArrayList(syncItemJob.getCronJobs());
			cronjobs.sort((final CronJobModel cronJob1, final CronJobModel cronJob2) -> {
				if (cronJob1 == null || cronJob1.getEndTime() == null || cronJob2 == null || cronJob2.getEndTime() == null)
				{
					return 0;
				}
				return cronJob1.getEndTime().compareTo(cronJob2.getEndTime());
			});

			final SyncItemCronJobModel latestCronJob = (SyncItemCronJobModel) cronjobs.get(cronjobs.size() - 1);
			final CronJobResult result = latestCronJob.getResult();
			final CronJobStatus status = latestCronJob.getStatus();
			if (CronJobStatus.FINISHED.equals(status) && !CronJobResult.SUCCESS.equals(result))
			{
				syncCronJob = latestCronJob;
			}
		}
		if (syncCronJob == null)
		{
			syncCronJob = getModelService().create(CatalogVersionSyncCronJobModel.class);
			syncCronJob.setJob(syncItemJob);
		}

		syncCronJob.setLogToDatabase(false);
		syncCronJob.setLogToFile(false);
		syncCronJob.setForceUpdate(false);
		getModelService().save(syncCronJob);

		return syncCronJob;
	}


	protected void processEditSyncAttributeDescriptors(final SyncItemJobModel job, final String catalogId,
			final Map<Class<?>, List<EditSyncAttributeDescriptorData>> editSyncDescriptors)
	{
		if (!editSyncDescriptors.isEmpty())
		{
			for (final Entry<Class<?>, List<EditSyncAttributeDescriptorData>> entry : editSyncDescriptors.entrySet())
			{
				for (final EditSyncAttributeDescriptorData descriptor : editSyncDescriptors.get(entry.getKey()))
				{
					processEditSyncAttributeDescriptor(job, entry.getKey(), descriptor);
				}
			}
		}
	}

	/**
	 * Configures sync attributes of a {@link ComposedTypeModel}. The following attributes can be configured:
	 * IncludeInSync, CopyByValue, Untranslatable
	 *
	 * @param syncJob
	 *           the synchronization job
	 * @param clazz
	 *           The class of {@link ComposedTypeModel}
	 * @param descriptor
	 *           Holds values for attributes to be modified for a given {@link SyncItemJobModel}
	 */
	protected void processEditSyncAttributeDescriptor(final SyncItemJobModel syncJob, final Class<?> clazz,
			final EditSyncAttributeDescriptorData descriptor)
	{

		final ComposedTypeModel composedType = tryGetComposedType(clazz);
		final AttributeDescriptorModel attributeDesc = tryGetAttributeDescriptor(composedType, descriptor.getQualifier());
		if (composedType != null && attributeDesc != null)
		{
			final SyncAttributeDescriptorConfigModel cfg = getSyncAttrDescConfig(syncJob, attributeDesc);
			if (cfg != null && Boolean.TRUE.equals(cfg.getIncludedInSync()))
			{
				LOG.info("Editing [" + composedType.getCode() + "] attribute [" + attributeDesc.getQualifier() + "] in sync job ["
						+ syncJob.getCode() + "]");

				if (descriptor.getIncludeInSync() != null)
				{
					cfg.setIncludedInSync(descriptor.getIncludeInSync());
					getModelService().save(cfg);
					LOG.info("Setting attribute includeInSync to [" + descriptor.getIncludeInSync() + "]");
				}
				if (descriptor.getCopyByValue() != null)
				{
					cfg.setCopyByValue(descriptor.getCopyByValue());
					getModelService().save(cfg);
					LOG.info("Setting attribute copyByValue to [" + descriptor.getCopyByValue() + "]");
				}
				if (descriptor.getUntranslatable() != null)
				{
					cfg.setUntranslatable(descriptor.getUntranslatable());
					getModelService().save(cfg);
					LOG.info("Setting attribute untranslatable to [" + descriptor.getUntranslatable() + "]");
				}
			}
		}
	}


	protected SyncAttributeDescriptorConfigModel getSyncAttrDescConfig(final SyncItemJobModel syncJob,
			final AttributeDescriptorModel attributeDesc)
	{
		try
		{
			final SyncAttributeDescriptorConfigModel example = new SyncAttributeDescriptorConfigModel();
			example.setSyncJob(syncJob);
			example.setAttributeDescriptor(attributeDesc);
			return getFlexibleSearchService().getModelByExample(example);
		}
		catch (AmbiguousIdentifierException | ModelNotFoundException e) //NOSONAR
		{
			final SyncAttributeDescriptorConfigModel syncAttrDescConfig = getModelService()
					.create(SyncAttributeDescriptorConfigModel.class);
			syncAttrDescConfig.setAttributeDescriptor(attributeDesc);
			syncAttrDescConfig.setSyncJob(syncJob);
			syncAttrDescConfig.setIncludedInSync(Boolean.TRUE);
			getModelService().save(syncAttrDescConfig);

			return syncAttrDescConfig;
		}
	}

	protected SyncItemJobModel getCatalogSyncJob(final String catalogId)
	{
		return getSyncJobForCatalogAndQualifier(catalogId, null);
	}

	protected SyncItemJobModel getSyncJobForCatalog(final String catalogId)
	{
		return getSyncJobForCatalogAndQualifier(catalogId, createJobIdentifier(catalogId));
	}

	protected SyncItemJobModel getSyncJobForCatalogAndQualifier(final String catalogId, final String qualifier)
	{
		try
		{
			final CatalogVersionModel source = getCatalogVersion(catalogId, OFFLINE_VERSION);
			final CatalogVersionModel target = getCatalogVersion(catalogId, ONLINE_VERSION);

			return getCatalogSynchronizationService().getSyncJob(source, target, qualifier);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			LOG.error(String.format("CatalogVersion[catalogId=%s] cannot be found out", catalogId), e);
		}

		return null;
	}

	protected void processRootTypes(final SyncItemJobModel job, final String catalogId, final List<String> rootTypes)
	{
		if (!rootTypes.isEmpty())
		{
			final List<ComposedTypeModel> newRootTypes = new LinkedList(
					(job.getRootTypes() != null ? job.getRootTypes() : Collections.emptyList()));
			for (final String rootType : rootTypes)
			{
				final ComposedTypeModel ct = tryGetComposedType(rootType);
				if (ct != null && !newRootTypes.contains(ct))
				{
					LOG.info("adding Root Type [" + ct.getCode() + "] to Sync Job for Catalog [" + catalogId + "]");
					newRootTypes.add(ct);
				}
			}
			job.setRootTypes(newRootTypes);
			getModelService().save(job);
		}
	}

	protected ComposedTypeModel tryGetComposedType(final Class<?> clazz)
	{
		try
		{
			return getTypeService().getComposedTypeForClass(clazz);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("unable to resolve typecode for class " + clazz, e);
		}

		return null;
	}

	protected ComposedTypeModel tryGetComposedType(final String typeCode)
	{
		try
		{
			return getTypeService().getComposedTypeForCode(typeCode);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("unable to resolve typecode " + typeCode, e);
		}

		return null;
	}

	protected AttributeDescriptorModel tryGetAttributeDescriptor(final ComposedTypeModel composedType, final String attributeName)
	{
		try
		{
			if (composedType != null)
			{
				return getTypeService().getAttributeDescriptor(composedType, attributeName);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attribute [" + attributeName + "] for type [" + composedType.getCode() + "] not found");
		}

		return null;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected List<String> getContentCatalogRootTypeCodes()
	{
		return contentCatalogRootTypeCodes;
	}

	public void setContentCatalogRootTypeCodes(final List<String> contentCatalogRootTypeCodes)
	{
		this.contentCatalogRootTypeCodes = contentCatalogRootTypeCodes;
	}

	protected List<String> getProductCatalogRootTypeCodes()
	{
		return productCatalogRootTypeCodes;
	}

	public void setProductCatalogRootTypeCodes(final List<String> productCatalogRootTypeCodes)
	{
		this.productCatalogRootTypeCodes = productCatalogRootTypeCodes;
	}

	protected Map<Class<?>, List<EditSyncAttributeDescriptorData>> getContentCatalogEditSyncDescriptors()
	{
		return contentCatalogEditSyncDescriptors;
	}

	public void setContentCatalogEditSyncDescriptors(
			final Map<Class<?>, List<EditSyncAttributeDescriptorData>> contentCatalogEditSyncDescriptors)
	{
		this.contentCatalogEditSyncDescriptors = contentCatalogEditSyncDescriptors;
	}

	protected Map<Class<?>, List<EditSyncAttributeDescriptorData>> getProductCatalogEditSyncDescriptors()
	{
		return productCatalogEditSyncDescriptors;
	}

	public void setProductCatalogEditSyncDescriptors(
			final Map<Class<?>, List<EditSyncAttributeDescriptorData>> productCatalogEditSyncDescriptors)
	{
		this.productCatalogEditSyncDescriptors = productCatalogEditSyncDescriptors;
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

	protected CatalogSynchronizationService getCatalogSynchronizationService()
	{
		return catalogSynchronizationService;
	}

	@Required
	public void setCatalogSynchronizationService(final CatalogSynchronizationService catalogSynchronizationService)
	{
		this.catalogSynchronizationService = catalogSynchronizationService;
	}

	protected CatalogService getCatalogService()
	{
		return catalogService;
	}

	@Required
	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}

	protected CronJobService getCronJobService()
	{
		return cronJobService;
	}

	@Required
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
