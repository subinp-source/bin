/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;

/**
 * Validates the persistence context.
 */
public interface PersistenceContextValidator
{
	/**
	 * Validates the context is valid before persistence. If the context is not valid, an exception will be raised.
	 *
	 * @param context the context to validate
	 */
	void validate(PersistenceContext context);
}
