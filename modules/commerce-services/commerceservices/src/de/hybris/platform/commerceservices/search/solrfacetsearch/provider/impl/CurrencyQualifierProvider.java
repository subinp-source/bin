/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;


/**
 * Qualifier provider for currencies.
 *
 * <p>
 * The available qualifiers will be created based on the configured currencies for the index configuration (see
 * {@link IndexConfig#getCurrencies()}).
 *
 * <p>
 * It supports the following types:
 * <ul>
 * <li>{@link CurrencyModel}
 * </p>
 *
 * @deprecated Since 5.6. Replaced by {@link de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider}
 */
@Deprecated(since = "5.6", forRemoval = true)
public class CurrencyQualifierProvider extends de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider
{
	// deprecated, uses implementation from de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider
}
