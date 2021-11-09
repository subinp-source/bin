/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;

/**
 * Validates that abstract types are not instanced.
 */
public class InstanceCreationOfAbstractTypeValidator implements PersistenceContextValidator
{
	public void validate(final PersistenceContext context){
		if (context.getIntegrationItem().getItemType().isAbstract())
		{
			throw new InstanceCreationOfAbstractTypeException(context.getIntegrationItem().getItemType().getItemCode());
		}

	}
}
