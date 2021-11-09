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
package de.hybris.platform.odata2services.odata.persistence;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.POST_PERSIST_HOOK;
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.PRE_PERSIST_HOOK;
import static de.hybris.platform.odata2services.odata.persistence.StorageRequest.storageRequestBuilder;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.odata2services.constants.Odata2servicesConstants;
import de.hybris.platform.odata2services.converter.ODataEntryToIntegrationItemConverter;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpHeaders;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultStorageRequestFactory implements StorageRequestFactory
{
	private static final Logger LOG = Log.getLogger(DefaultStorageRequestFactory.class);

	private ODataContextLanguageExtractor localeExtractor;
	private ODataEntryToIntegrationItemConverter entryConverter;

	@Override
	public StorageRequest create(final ODataContext oDataContext, final String responseContentType, final EdmEntitySet entitySet, final ODataEntry entry)
	{
		final Locale contentLocale = getLocaleExtractor().extractFrom(oDataContext, HttpHeaders.CONTENT_LANGUAGE);
		final Locale acceptLocale = getLocaleExtractor().getAcceptLanguage(oDataContext)
				.orElse(contentLocale);

		try
		{
			final IntegrationItem item = convert(oDataContext, entitySet, entry);
			return storageRequestBuilder()
					.withEntitySet(entitySet)
					.withODataEntry(entry)
					.withAcceptLocale(acceptLocale)
					.withContentLocale(contentLocale)
					.withPrePersistHook(oDataContext.getRequestHeader(PRE_PERSIST_HOOK))
					.withPostPersistHook(oDataContext.getRequestHeader(POST_PERSIST_HOOK))
					.withIntegrationObject(item.getIntegrationObjectCode())
					.withServiceRoot(oDataContext.getPathInfo().getServiceRoot())
					.withContentType(responseContentType)
					.withRequestUri(oDataContext.getPathInfo().getRequestUri())
					.withReplaceAttributes(isReplaceAttributes(oDataContext))
					.withItemCanBeCreated(isItemCanBeCreated(oDataContext))
					.withIntegrationItem(item)
					.build();
		}
		catch (final ODataException e)
		{
			LOG.error("Exception while accessing metadata for entry {}", entry);
			throw new OdataRequestDataProcessingException(e);
		}
	}

	/**
	 * Determines if the request is to replace the attributes provided in the payload
	 * @param oDataContext Context used in making the decision
	 * @return {@code true}, replace attributes, otherwise {@code false}
	 */
	protected boolean isReplaceAttributes(final ODataContext oDataContext)
	{
		return isHttpMethod(oDataContext, ODataHttpMethod.PATCH);
	}

	/**
	 * Determines whether the request insists on updating an existing item only or allows new item creation.
	 * @param context context used in making the decision
	 * @return {@code true}, if the item can be created when it does not exist in the system; {@code false}, when new item cannot
	 * be created and only an existing item should be updated.
	 */
	protected boolean isItemCanBeCreated(final ODataContext context)
	{
		return isHttpMethod(context, ODataHttpMethod.POST);
	}

	private boolean isHttpMethod(final ODataContext oDataContext, final ODataHttpMethod method)
	{
		return method.equals(deriveContextHttpMethod(oDataContext));
	}

	private ODataHttpMethod deriveContextHttpMethod(final ODataContext context)
	{
		return ((ODataRequest) context.getParameter(Odata2servicesConstants.ODATA_REQUEST)).getMethod();
	}

	protected IntegrationItem convert(final ODataContext context, final EdmEntitySet entitySet, final ODataEntry entry) throws EdmException
	{
		return getEntryConverter().convert(context, entitySet, entry);
	}

	protected ODataContextLanguageExtractor getLocaleExtractor()
	{
		return localeExtractor;
	}

	@Required
	public void setLocaleExtractor(final ODataContextLanguageExtractor localeExtractor)
	{
		this.localeExtractor = localeExtractor;
	}

	protected ODataEntryToIntegrationItemConverter getEntryConverter()
	{
		return entryConverter;
	}

	@Required
	public void setEntryConverter(final ODataEntryToIntegrationItemConverter converter)
	{
		entryConverter = converter;
	}
}
