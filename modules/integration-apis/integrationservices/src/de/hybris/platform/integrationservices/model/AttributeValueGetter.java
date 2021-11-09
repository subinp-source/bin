/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import java.util.Locale;
import java.util.Map;

/**
 * An AttributeValueGetter provides get access to an attribute's value
 */
public interface AttributeValueGetter
{
    /**
     * Gets the value from the given {@link Object} using the default locale
     * @param model Reference to the model that contains the value to retrieve from
     * @return The value or null
     */
    Object getValue(Object model);

    /**
     * Gets the value from the given {@link Object} using the specified locale
     * @param model Reference to the model that contains the value to retrieve from
     * @param locale The locale of the value to retrieve
     * @return The value or null
     */
    Object getValue(Object model, Locale locale);

    /**
     * Gets the values from the given {@link Object} using the specified locales
     * @param model Reference to the model that contains the value to retrieve from
     * @param locales The locales of the value to retrieve
     * @return A map of localized values
     */
    Map<Locale, Object> getValues(Object model, Locale... locales);
}
