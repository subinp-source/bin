/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;


public interface ModifiableChecker<T>
{
	boolean canModify(T given);
}
