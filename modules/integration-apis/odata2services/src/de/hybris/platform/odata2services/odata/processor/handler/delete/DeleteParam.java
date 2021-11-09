/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.delete;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;

/**
 *  Defines the parameters needed to perform the delete operation
 */
public interface DeleteParam
{
	/**
	 * Gets the {@link ODataContext}
	 * @return OData context
	 */
	ODataContext getContext();

	/**
	 * Gets the {@link DeleteUriInfo}
	 * @return Uri info
	 */
	DeleteUriInfo getUriInfo();
}
