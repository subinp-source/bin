/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies;

/**
 *  A strategy for looking up a client reference identifier
 *
 */
public interface ClientReferenceLookupStrategy
{
	String lookupClientReferenceId();
}
