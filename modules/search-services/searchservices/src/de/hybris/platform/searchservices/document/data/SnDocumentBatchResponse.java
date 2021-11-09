/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.document.data;

import java.util.List;


public class SnDocumentBatchResponse
{
	private List<SnDocumentBatchOperationResponse> responses;

	public List<SnDocumentBatchOperationResponse> getResponses()
	{
		return responses;
	}

	public void setResponses(final List<SnDocumentBatchOperationResponse> responses)
	{
		this.responses = responses;
	}
}
