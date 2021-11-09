/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.document.data;

import de.hybris.platform.searchservices.enums.SnDocumentOperationStatus;


public class SnDocumentBatchOperationResponse
{
	private String id;
	private SnDocumentOperationStatus status;

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public SnDocumentOperationStatus getStatus()
	{
		return status;
	}

	public void setStatus(final SnDocumentOperationStatus status)
	{
		this.status = status;
	}
}
