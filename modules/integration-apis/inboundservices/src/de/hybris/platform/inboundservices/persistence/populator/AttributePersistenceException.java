/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.exception.IntegrationAttributeProcessingException;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * A base class for all attribute value persistence related exceptions.
 */
public class AttributePersistenceException extends IntegrationAttributeProcessingException
{
	private final transient PersistenceContext persistenceContext;

	/**
	 * Instantiates this exception
	 * @param message a message for the new exception instance.
	 * @param descriptor descriptor of the attribute, whose value could not be persisted.
	 * @param context context of the item being persisted.
	 */
	public AttributePersistenceException(final String message, final TypeAttributeDescriptor descriptor, final PersistenceContext context)
	{
		super(message, descriptor);
		persistenceContext = context;
	}

	/**
	 * Instantiates this exception
	 * @param message a message for the new exception instance.
	 * @param descriptor descriptor of the attribute, whose value could not be persisted.
	 * @param context context of the item being persisted.
	 * @param cause a failure converted into this exception.
	 */
	public AttributePersistenceException(final String message, final TypeAttributeDescriptor descriptor, final PersistenceContext context, final Throwable cause)
	{
		super(message, descriptor, cause);
		persistenceContext = context;
	}

	/**
	 * Provides context associated with the attribute persistence failure.
	 * @return a persistence context being processed when the failure occurred.
	 */
	public PersistenceContext getPersistenceContext()
	{
		return persistenceContext;
	}

	/**
	 * Retrieves integration key of the root (original) item associated with the context.
	 * @return integration key of the payload root item.
	 */
	public String getIntegrationKey()
	{
		return persistenceContext.getRootContext().getIntegrationItem().getIntegrationKey();
	}
}
