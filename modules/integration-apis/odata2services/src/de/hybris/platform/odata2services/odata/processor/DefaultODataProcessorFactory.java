/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.processor;

import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.config.ODataServicesConfiguration;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.PersistenceService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequestFactory;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;
import de.hybris.platform.odata2services.odata.processor.handler.delete.DeleteParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.BatchParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.ChangeSetParam;
import de.hybris.platform.odata2services.odata.processor.handler.read.ReadParam;
import de.hybris.platform.odata2services.odata.processor.reader.EntityReaderRegistry;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.springframework.beans.factory.annotation.Required;

/**
 * A default implementation of the factory, which creates new instance of the processor for every invocation.
 */
public class DefaultODataProcessorFactory implements ODataProcessorFactory
{
	private PersistenceService persistenceService;
	private ModelService modelService;
	private ODataServicesConfiguration oDataServicesConfiguration;
	private EntityReaderRegistry entityReaderRegistry;
	private ItemLookupRequestFactory itemLookupRequestFactory;
	private StorageRequestFactory storageRequestFactory;
	private AccessRightsService accessRightsService;
	private ServiceNameExtractor serviceNameExtractor;
	private ItemTypeDescriptorService itemTypeDescriptorService;

	private ODataProcessorHandler<PersistenceParam, ODataResponse> persistenceHandler;
	private ODataProcessorHandler<BatchParam, ODataResponse> batchPersistenceHandler;
	private ODataProcessorHandler<ChangeSetParam, BatchResponsePart> changeSetPersistenceHandler;
	private ODataProcessorHandler<PersistenceParam, ODataResponse> patchPersistenceHandler;
	private ODataProcessorHandler<DeleteParam, ODataResponse> deleteHandler;
	private ODataProcessorHandler<ReadParam, ODataResponse> readHandler;

	@Override
	public ODataSingleProcessor createProcessor(final ODataContext context)
	{
		final OData2Processor processor = new OData2Processor();
		processor.setPersistenceHandler(getPersistenceHandler());
		processor.setBatchPersistenceHandler(getBatchPersistenceHandler());
		processor.setChangeSetPersistenceHandler(getChangeSetPersistenceHandler());
		processor.setPatchPersistenceHandler(getPatchPersistenceHandler());
		processor.setDeleteHandler(getDeleteHandler());
		processor.setReadHandler(getReadHandler());
		processor.setContext(context);
		processor.setAccessRightsService(accessRightsService);
		processor.setServiceNameExtractor(getServiceNameExtractor());
		processor.setItemTypeDescriptorService(getItemTypeDescriptorService());
		return processor;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	@Required
	public void setPersistenceService(final PersistenceService service)
	{
		persistenceService = service;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected ODataServicesConfiguration getODataServicesConfiguration()
	{
		return oDataServicesConfiguration;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	@Required
	public void setODataServicesConfiguration(final ODataServicesConfiguration configuration)
	{
		oDataServicesConfiguration = configuration;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	@Required
	public void setModelService(final ModelService service)
	{
		modelService = service;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected EntityReaderRegistry getEntityReaderRegistry()
	{
		return entityReaderRegistry;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	@Required
	public void setEntityReaderRegistry(final EntityReaderRegistry registry)
	{
		entityReaderRegistry = registry;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected ItemLookupRequestFactory getItemLookupRequestFactory()
	{
		return itemLookupRequestFactory;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	@Required
	public void setItemLookupRequestFactory(final ItemLookupRequestFactory factory)
	{
		itemLookupRequestFactory = factory;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	public StorageRequestFactory getStorageRequestFactory()
	{
		return storageRequestFactory;
	}

	/**
	 * @deprecated This is no longer needed with using {@link OData2Processor}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	@Required
	public void setStorageRequestFactory(final StorageRequestFactory storageRequestFactory)
	{
		this.storageRequestFactory = storageRequestFactory;
	}

	protected ODataProcessorHandler<PersistenceParam, ODataResponse> getPersistenceHandler()
	{
		return persistenceHandler;
	}

	@Required
	public void setPersistenceHandler(final ODataProcessorHandler<PersistenceParam, ODataResponse> persistenceHandler)
	{
		this.persistenceHandler = persistenceHandler;
	}

	protected ODataProcessorHandler<BatchParam, ODataResponse> getBatchPersistenceHandler()
	{
		return batchPersistenceHandler;
	}

	@Required
	public void setBatchPersistenceHandler(final ODataProcessorHandler<BatchParam, ODataResponse> batchPersistenceHandler)
	{
		this.batchPersistenceHandler = batchPersistenceHandler;
	}

	protected ODataProcessorHandler<ChangeSetParam, BatchResponsePart> getChangeSetPersistenceHandler()
	{
		return changeSetPersistenceHandler;
	}

	@Required
	public void setChangeSetPersistenceHandler(
			final ODataProcessorHandler<ChangeSetParam, BatchResponsePart> changeSetPersistenceHandler)
	{
		this.changeSetPersistenceHandler = changeSetPersistenceHandler;
	}

	protected ODataProcessorHandler<PersistenceParam, ODataResponse> getPatchPersistenceHandler()
	{
		return patchPersistenceHandler;
	}

	@Required
	public void setPatchPersistenceHandler(final ODataProcessorHandler<PersistenceParam, ODataResponse> patchPersistenceHandler)
	{
		this.patchPersistenceHandler = patchPersistenceHandler;
	}

	protected ODataProcessorHandler<DeleteParam, ODataResponse> getDeleteHandler()
	{
		return deleteHandler;
	}

	@Required
	public void setDeleteHandler(final ODataProcessorHandler<DeleteParam, ODataResponse> deleteHandler)
	{
		this.deleteHandler = deleteHandler;
	}

	protected ODataProcessorHandler<ReadParam, ODataResponse> getReadHandler()
	{
		return readHandler;
	}

	@Required
	public void setReadHandler(final ODataProcessorHandler<ReadParam, ODataResponse> readHandler)
	{
		this.readHandler = readHandler;
	}

	@Required
	public void setAccessRightsService(
			final AccessRightsService accessRightsService)
	{
		this.accessRightsService = accessRightsService;
	}

	protected ServiceNameExtractor getServiceNameExtractor()
	{
		return serviceNameExtractor;
	}

	@Required
	public void setServiceNameExtractor(final ServiceNameExtractor serviceNameExtractor)
	{
		this.serviceNameExtractor = serviceNameExtractor;
	}

	protected ItemTypeDescriptorService getItemTypeDescriptorService()
	{
		return itemTypeDescriptorService;
	}

	@Required
	public void setItemTypeDescriptorService(
			final ItemTypeDescriptorService itemTypeDescriptorService)
	{
		this.itemTypeDescriptorService = itemTypeDescriptorService;
	}
}
