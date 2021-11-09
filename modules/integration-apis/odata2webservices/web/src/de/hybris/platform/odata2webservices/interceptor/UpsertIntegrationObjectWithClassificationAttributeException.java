/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.interceptor;

/**
 * Throws this exception when a user is trying to upsert an Integration Object with classification attributes.
 * This exception will be removed once POST and PATCH for classification attributes are implemented.
 */
public final class UpsertIntegrationObjectWithClassificationAttributeException extends RuntimeException
{
    private static final String ERROR_MSG = "Executing a %s request on an Integration Object configured with classification attributes is not currently supported.";

    private final String method;

    public UpsertIntegrationObjectWithClassificationAttributeException(final String method)
    {
        super(String.format(ERROR_MSG, method.toUpperCase()));
        this.method = method.toUpperCase();
    }

    public String getMethod()
    {
        return method;
    }
}
