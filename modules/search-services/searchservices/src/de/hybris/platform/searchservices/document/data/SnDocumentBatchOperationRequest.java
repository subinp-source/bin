/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.document.data;

import de.hybris.platform.searchservices.enums.SnDocumentOperationType;


public class SnDocumentBatchOperationRequest
{
	private String id;
	private SnDocumentOperationType operationType;
	private SnDocument document;

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public SnDocumentOperationType getOperationType()
	{
		return operationType;
	}

	public void setOperationType(final SnDocumentOperationType operationType)
	{
		this.operationType = operationType;
	}

	public SnDocument getDocument()
	{
		return document;
	}

	public void setDocument(final SnDocument document)
	{
		this.document = document;
	}
}
