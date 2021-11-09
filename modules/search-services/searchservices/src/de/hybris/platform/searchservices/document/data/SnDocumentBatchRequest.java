/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.document.data;

import java.util.List;


public class SnDocumentBatchRequest
{
	private String id;
	private List<SnDocumentBatchOperationRequest> requests;

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public List<SnDocumentBatchOperationRequest> getRequests()
	{
		return requests;
	}

	public void setRequests(final List<SnDocumentBatchOperationRequest> requests)
	{
		this.requests = requests;
	}
}
