/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.validation.InstanceCreationOfAbstractTypeException;
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException;
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException;
import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException;
import de.hybris.platform.integrationservices.search.NonUniqueItemFoundException;
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidationException;
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.OData2ServicesException;
import de.hybris.platform.odata2services.odata.errors.AttributeNotInIntegrationObjectException;
import de.hybris.platform.odata2services.odata.persistence.InvalidEntryDataException;
import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException;
import de.hybris.platform.odata2services.odata.persistence.PersistenceService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.StorageRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidPropertyValueException;
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException;
import de.hybris.platform.odata2services.odata.processor.ODataPayloadProcessingException;
import de.hybris.platform.odata2services.odata.processor.PersistenceErrorRuntimeException;
import de.hybris.platform.odata2services.odata.processor.handler.EntityLogger;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;

import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * ODataProcessor handler that persists a request
 */
public class PersistenceHandler implements ODataProcessorHandler<PersistenceParam, ODataResponse>
{
	private static final Logger LOG = Log.getLogger(PersistenceHandler.class);
	private static final EntityProviderReadProperties READ_CFG =
			EntityProviderReadProperties.init().mergeSemantic(false).build();

	private StorageRequestFactory storageRequestFactory;
	private PersistenceService persistenceService;

	@Override
	public ODataResponse handle(final PersistenceParam param) throws ODataException
	{
		final EdmEntitySet entitySet = param.getEntitySet();
		final EdmEntityType entityType = param.getEntityType();
		final ODataEntry entry = convertToODataEntry(param);

		LOG.info("Entity requested to persist under Hybris Commerce system : {}", entitySet.getName());
		EntityLogger.logRequestEntity(entityType, entry);

		final StorageRequest storageRequest = createStorageRequest(param, entry);
		final ODataEntry persistedEntry = persist(storageRequest);
		return buildResponse(param, storageRequest, persistedEntry).build();
	}

	/**
	 * Converts the request payload to an {@link ODataEntry}
	 *
	 * @param param Parameters from the request
	 * @return {@link ODataEntry} representing the request payload
	 * @throws EdmException is thrown when there's a failure in the conversion
	 */
	protected ODataEntry convertToODataEntry(final PersistenceParam param) throws EdmException
	{
		try
		{
			return EntityProvider.readEntry(param.getRequestContentType(), param.getEntitySet(), param.getContent(), READ_CFG);
		}
		catch (final EntityProviderException e)
		{
			return handleEntityProviderException(param, e);
		}
		catch (final RuntimeException e)
		{
			return handleException(param, e);
		}
	}

	private ODataEntry handleException(final PersistenceParam param, final Exception e) throws EdmException
	{
		LOG.error("Exception occurred while parsing the request body. Failed to create entity of type {}",
				param.getEntityType().getName(), e);
		throw new ODataPayloadProcessingException(e);
	}

	private ODataEntry handleEntityProviderException(final PersistenceParam param, final EntityProviderException e)
			throws EdmException
	{
		final List<?> content = e.getMessageReference().getContent();
		if (e.getMessageReference()
		     .getKey()
		     .equals(EntityProviderException.ILLEGAL_ARGUMENT.getKey()))
		{
			throw new AttributeNotInIntegrationObjectException((List<Object>) content);
		}
		else
		{
			return handleException(param, e);
		}
	}

	private StorageRequest createStorageRequest(final PersistenceParam param, final ODataEntry entry)
	{
		return getStorageRequestFactory().create(param, entry);
	}

	/**
	 * Persist the request payload
	 *
	 * @param storageRequest Contains information on how to persist the request payload
	 * @return The persisted {@link ODataEntry}
	 * @throws EdmException is thrown if there's a failure in persistence
	 */
	protected ODataEntry persist(final StorageRequest storageRequest) throws EdmException
	{
		try
		{
			return getPersistenceService().createEntityData(storageRequest);
		}
		catch (final InvalidAttributeValueException e)
		{
			LOG.error("An InvalidAttributeException was thrown");
			throw new InvalidPropertyValueException(e.getMessage());
		}
		catch (final OData2ServicesException e)
		{
			LOG.error("", e);
			throw new InvalidEntryDataException(e, storageRequest);
		}
		catch (final LocaleNotSupportedException e)
		{
			throw new LocaleNotSupportedException(e.getLanguage(), storageRequest.getIntegrationKey());
		}
		catch (final IntegrationAttributeException | PersistenceRuntimeApplicationException | ItemNotFoundException | ItemSearchRequestValidationException | TypeAccessPermissionException | NonUniqueItemFoundException | InstanceCreationOfAbstractTypeException e)
		{
			throw e;
		}
		catch (final RuntimeException e)
		{
			LOG.error("RuntimeException while trying to persist an ODataEntry:", e);
			throw new PersistenceErrorRuntimeException(e, storageRequest.getIntegrationKey());
		}
	}

	/**
	 * Creates an {@link ODataResponse} for the request
	 *
	 * @param param          Parameters from the request
	 * @param ctx            Storage request contains the information on how to create the response
	 * @param persistedEntry The persisted {@link ODataEntry}
	 * @return The {@link ODataResponse}
	 * @throws ODataException is thrown if a failure occurs creating the response
	 */
	protected ODataResponse.ODataResponseBuilder buildResponse(final PersistenceParam param, final PersistenceContext ctx,
	                                                           final ODataEntry persistedEntry) throws ODataException
	{
		final EntityProviderWriteProperties writeProperties = writeProperties(param.getContext());
		return getODataResponseBuilder(writeEntry(param, persistedEntry, writeProperties))
				.header(HttpHeaders.CONTENT_LANGUAGE, ctx.getAcceptLocale().toString());
	}

	private ODataResponse writeEntry(final PersistenceParam param, final ODataEntry persistedEntry,
	                                 final EntityProviderWriteProperties writeProperties) throws EntityProviderException
	{
		return EntityProvider.writeEntry(param.getResponseContentType(), param.getEntitySet(), persistedEntry.getProperties(),
				writeProperties);
	}

	private EntityProviderWriteProperties writeProperties(final ODataContext context) throws ODataException
	{
		return EntityProviderWriteProperties
				.serviceRoot(context.getPathInfo().getServiceRoot())
				.build();
	}

	protected ODataResponse.ODataResponseBuilder getODataResponseBuilder(final ODataResponse readerResponse)
	{
		return ODataResponse.fromResponse(readerResponse);
	}

	protected PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	@Required
	public void setPersistenceService(final PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	protected StorageRequestFactory getStorageRequestFactory()
	{
		return storageRequestFactory;
	}

	@Required
	public void setStorageRequestFactory(final StorageRequestFactory storageRequestFactory)
	{
		this.storageRequestFactory = storageRequestFactory;
	}
}
