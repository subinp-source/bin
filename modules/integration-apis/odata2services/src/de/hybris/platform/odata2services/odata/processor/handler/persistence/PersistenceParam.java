/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence;

import java.io.InputStream;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.UriInfo;

/**
 * Defines the parameters needed to perform the persistence operation
 */
public interface PersistenceParam
{
	/**
	 * Gets the {@link UriInfo}
	 * @return URI info
	 */
	UriInfo getUriInfo();

	/**
	 * Gets the {@link EdmEntitySet}
	 * @return entity set
	 */
	EdmEntitySet getEntitySet();

	/**
	 * Gets the {@link EdmEntityType}
	 * @return entity type
	 */
	EdmEntityType getEntityType();

	/**
	 * Gets the payload of the request
	 * @return content
	 */
	InputStream getContent();

	/**
	 * Gets the content type of the payload
	 * @return content type
	 */
	String getRequestContentType();

	/**
	 * Gets the response content type
	 * @return content type
	 */
	String getResponseContentType();

	/**
	 * Gets the context of the request as an {@link ODataContext}
	 * @return context
	 */
	ODataContext getContext();
}
