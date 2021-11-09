/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor;

import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.odata2services.odata.processor.handler.ODataProcessorHandler;
import de.hybris.platform.odata2services.odata.processor.handler.delete.DefaultDeleteParam;
import de.hybris.platform.odata2services.odata.processor.handler.delete.DeleteParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.DefaultPersistenceParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.BatchParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.ChangeSetParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.DefaultBatchParam;
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.DefaultChangeSetParam;
import de.hybris.platform.odata2services.odata.processor.handler.read.DefaultReadParam;
import de.hybris.platform.odata2services.odata.processor.handler.read.ReadParam;

import java.io.InputStream;
import java.util.List;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.UriInfo;
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetCountUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.api.uri.info.PutMergePatchUriInfo;
import org.springframework.beans.factory.annotation.Required;

/**
 * The OData2Processor implements some of the operations defined in the {@link ODataSingleProcessor}.
 * It uses {@link ODataProcessorHandler} to handle a particular operation.
 */
public class OData2Processor extends ODataSingleProcessor
{
	private ODataProcessorHandler<PersistenceParam, ODataResponse> persistenceHandler;
	private ODataProcessorHandler<BatchParam, ODataResponse> batchPersistenceHandler;
	private ODataProcessorHandler<ChangeSetParam, BatchResponsePart> changeSetPersistenceHandler;
	private ODataProcessorHandler<PersistenceParam, ODataResponse> patchPersistenceHandler;
	private ODataProcessorHandler<DeleteParam, ODataResponse> deleteHandler;
	private ODataProcessorHandler<ReadParam, ODataResponse> readHandler;
	private AccessRightsService accessRightsService;
	private ServiceNameExtractor serviceNameExtractor;
	private ItemTypeDescriptorService itemTypeDescriptorService;

	@Override
	public ODataResponse createEntity(final PostUriInfo uriInfo, final InputStream content,
	                                  final String requestContentType, final String responseContentType) throws ODataException
	{
		final PersistenceParam param = getPersistenceParam((UriInfo) uriInfo, content, requestContentType, responseContentType);
		accessRightsService.checkCreatePermission(extractTypeCode(param));
		return getPersistenceHandler().handle(param);
	}

	@Override
	public ODataResponse updateEntity(final PutMergePatchUriInfo uriInfo, final InputStream content,
	                                  final String requestContentType,
	                                  final boolean merge, final String contentType) throws ODataException
	{
		final PersistenceParam param = getPersistenceParam((UriInfo) uriInfo, content, requestContentType, contentType);
		accessRightsService.checkUpdatePermission(extractTypeCode(param));
		return getPatchPersistenceHandler().handle(param);
	}

	@Override
	public ODataResponse executeBatch(final BatchHandler handler, final String contentType, final InputStream content)
			throws ODataException
	{
		final BatchParam param = getBatchParam(handler, contentType, content);
		return getBatchPersistenceHandler().handle(param);
	}

	@Override
	public BatchResponsePart executeChangeSet(final BatchHandler batchHandler, final List<ODataRequest> requests)
			throws ODataException
	{
		final ChangeSetParam param = getChangeSetParam(batchHandler, requests);
		return getChangeSetPersistenceHandler().handle(param);
	}

	@Override
	public ODataResponse deleteEntity(final DeleteUriInfo uriInfo, final String contentType) throws ODataException
	{
		final DeleteParam param = getDeleteParam(uriInfo);
		accessRightsService.checkDeletePermission(extractTypeCode(param));
		return getDeleteHandler().handle(param);
	}

	@Override
	public ODataResponse countEntitySet(final GetEntitySetCountUriInfo uriInfo, final String contentType) throws ODataException
	{
		return read((UriInfo) uriInfo, contentType);
	}

	@Override
	public ODataResponse readEntity(final GetEntityUriInfo uriInfo, final String contentType) throws ODataException
	{
		return read((UriInfo) uriInfo, contentType);
	}

	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) throws ODataException
	{
		return read((UriInfo) uriInfo, contentType);
	}

	/**
	 * Reads the data specified by the given parameters
	 *
	 * @param uriInfo     The URI Info containing the specification of the read (e.g. filter, expand, URL)
	 * @param contentType Response content type
	 * @return {@link ODataResponse} containing data that was read
	 * @throws ODataException is thrown when there's a failure in the read
	 */
	protected ODataResponse read(final UriInfo uriInfo, final String contentType) throws ODataException
	{
		final ReadParam param = getReadParam(uriInfo, contentType);
		accessRightsService.checkReadPermission(extractTypeCode(param));
		return getReadHandler().handle(param);
	}

	/**
	 * Gets a {@link PersistenceParam} from the given parameters
	 *
	 * @param uriInfo             URI info containing the information that's needed for the persistence
	 * @param content             The request payload
	 * @param requestContentType  Request payload content type
	 * @param responseContentType Response payload content type
	 * @return An instance of the parameter object
	 */
	protected PersistenceParam getPersistenceParam(final UriInfo uriInfo, final InputStream content,
	                                               final String requestContentType, final String responseContentType)
	{
		return DefaultPersistenceParam.persistenceParam()
		                              .withContent(content)
		                              .withContext(getContext())
		                              .withRequestContentType(requestContentType)
		                              .withResponseContentType(responseContentType)
		                              .withUriInfo(uriInfo)
		                              .build();
	}

	/**
	 * Gets a {@link BatchParam} from the given parameters
	 *
	 * @param batchHandler        The {@link BatchHandler} that handles the batch data
	 * @param content             The request payload
	 * @param responseContentType Response payload content type
	 * @return An instance of the parameter object
	 */
	protected BatchParam getBatchParam(final BatchHandler batchHandler, final String responseContentType,
	                                   final InputStream content)
	{
		return DefaultBatchParam.batchParam()
		                        .withBatchHandler(batchHandler)
		                        .withResponseContentType(responseContentType)
		                        .withContent(content)
		                        .withContext(getContext())
		                        .build();
	}

	/**
	 * Gets a {@link ChangeSetParam} from the given parameters
	 *
	 * @param batchHandler The {@link BatchHandler} that handles the batch changeset data
	 * @param requests     The list of changesets as {@link ODataRequest}s
	 * @return An instance of the parameter object
	 */
	protected ChangeSetParam getChangeSetParam(final BatchHandler batchHandler, final List<ODataRequest> requests)
	{
		return DefaultChangeSetParam.changeSetParam()
		                            .withBatchHandler(batchHandler)
		                            .withRequests(requests)
		                            .build();
	}

	/**
	 * Gets a {@link DeleteParam} from the given parameters
	 *
	 * @param uriInfo URI info containing the information that delete uses, such as the item's integration key
	 * @return An instance of the parameter object
	 */
	protected DeleteParam getDeleteParam(final DeleteUriInfo uriInfo)
	{
		return DefaultDeleteParam.deleteParam()
		                         .withUriInfo(uriInfo)
		                         .withContext(getContext())
		                         .build();
	}

	/**
	 * Gets a {@link ReadParam} from the given parameters
	 *
	 * @param uriInfo             URI info containing the information that read uses
	 * @param responseContentType Response content type
	 * @return An instance of the parameter object
	 */
	protected ReadParam getReadParam(final UriInfo uriInfo, final String responseContentType)
	{
		return DefaultReadParam.readParam()
		                       .withUriInfo(uriInfo)
		                       .withResponseContentType(responseContentType)
		                       .withContext(getContext())
		                       .build();
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

	private String extractTypeCode(final PersistenceParam param)
	{
		try
		{
			return getTypeDescriptor(param.getContext(), param.getUriInfo().getStartEntitySet()).getTypeCode();
		}
		catch (final ODataException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	private String extractTypeCode(final ReadParam param)
	{
		try
		{
			return getTypeDescriptor(param.getContext(), param.getUriInfo().getStartEntitySet()).getTypeCode();
		}
		catch (final ODataException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	private String extractTypeCode(final DeleteParam param)
	{
		try
		{
			return getTypeDescriptor(param.getContext(), param.getUriInfo().getStartEntitySet()).getTypeCode();
		}
		catch (final ODataException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	private TypeDescriptor getTypeDescriptor(final ODataContext context, final EdmEntitySet edmEntitySet) throws EdmException
	{
		final String ioCode = serviceNameExtractor.extract(context);
		final String type = edmEntitySet.getEntityType().getName();
		return itemTypeDescriptorService.getTypeDescriptor(ioCode, type)
		                                .orElseThrow(() -> new IntegrationObjectItemNotFoundException(ioCode, type));
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
	public void setAccessRightsService(final AccessRightsService accessRightsService)
	{
		this.accessRightsService = accessRightsService;
	}

	@Required
	public void setServiceNameExtractor(final ServiceNameExtractor serviceNameExtractor)
	{
		this.serviceNameExtractor = serviceNameExtractor;
	}

	@Required
	public void setItemTypeDescriptorService(
			final ItemTypeDescriptorService itemTypeDescriptorService)
	{
		this.itemTypeDescriptorService = itemTypeDescriptorService;
	}
}
