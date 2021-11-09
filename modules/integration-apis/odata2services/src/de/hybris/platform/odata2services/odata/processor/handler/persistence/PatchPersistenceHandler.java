/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@code PersistenceHandler} that processes OData PATCH requests.
 */
public class PatchPersistenceHandler extends PersistenceHandler
{
	private IntegrationKeyToODataEntryGenerator integrationKeyToODataEntryGenerator;
	private RequestValidator requestValidator;

	@Override
	protected ODataEntry convertToODataEntry(final PersistenceParam param) throws EdmException
	{
		final ODataEntry pathEntry = getODataEntryFromKey(param);
		final ODataEntry payloadEntry = super.convertToODataEntry(param);
		validateRequest(param, pathEntry, payloadEntry);
		// if payload does not contain key attributes, populate them
		payloadEntry.getProperties().putAll(pathEntry.getProperties());
		return payloadEntry;
	}

	private void validateRequest(final PersistenceParam param, final ODataEntry path, final ODataEntry payload)
	{
		final var validator = getRequestValidator();
		if (validator != null)
		{
			validator.validate(param, path, payload);
		}
	}

	private ODataEntry getODataEntryFromKey(final PersistenceParam param) throws EdmException
	{
		return getIntegrationKeyToODataEntryGenerator().generate(param.getEntitySet(), param.getUriInfo().getKeyPredicates());
	}

	@Override
	protected ODataResponse.ODataResponseBuilder buildResponse(final PersistenceParam param, final PersistenceContext ctx,
	                                                           final ODataEntry persistedEntry) throws ODataException
	{
		final ODataResponse.ODataResponseBuilder builder = super.buildResponse(param, ctx, persistedEntry);
		return builder.status(HttpStatusCodes.OK);
	}

	protected IntegrationKeyToODataEntryGenerator getIntegrationKeyToODataEntryGenerator()
	{
		return integrationKeyToODataEntryGenerator;
	}

	@Required
	public void setIntegrationKeyToODataEntryGenerator(
			final IntegrationKeyToODataEntryGenerator integrationKeyToODataEntryGenerator)
	{
		this.integrationKeyToODataEntryGenerator = integrationKeyToODataEntryGenerator;
	}

	protected RequestValidator getRequestValidator()
	{
		return requestValidator;
	}

	/**
	 * Injects implementation of the entry validator to use.
	 *
	 * @param validator a validator to use for validating the payload and/or the integration key. If {@code null}, no validation will
	 *                  be performed.
	 */
	public void setRequestValidator(final RequestValidator validator)
	{
		requestValidator = validator;
	}
}
