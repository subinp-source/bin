/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.event;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.QuoteModel;


/**
 * Event to indicate that a quote is expired
 */
public class QuoteExpiredEvent extends AbstractQuoteSubmitEvent<BaseSiteModel>
{
    /**
     * Default Constructor
     *
     * @param quote
     */
    public QuoteExpiredEvent(final QuoteModel quote)
    {
        super(quote, null, null);
    }
}