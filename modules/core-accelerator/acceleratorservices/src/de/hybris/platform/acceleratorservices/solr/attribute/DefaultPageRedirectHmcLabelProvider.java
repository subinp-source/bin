/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.solr.attribute;

import de.hybris.platform.acceleratorservices.model.redirect.SolrPageRedirectModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;


/**
 * Default label provider for attribute 'hmcLabel'
 * 
 */
public class DefaultPageRedirectHmcLabelProvider implements DynamicAttributeHandler<String, SolrPageRedirectModel>
{
	@Override
	public String get(final SolrPageRedirectModel model)
	{
		return model.getRedirectItem().getUid();
	}

	@Override
	public void set(final SolrPageRedirectModel model, final String value)
	{
		throw new UnsupportedOperationException("The attribute is readonly");
	}

}
