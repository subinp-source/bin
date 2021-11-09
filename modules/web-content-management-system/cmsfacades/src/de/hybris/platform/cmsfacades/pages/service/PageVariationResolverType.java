/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;


/**
 * Represents meta-information about a <code>AbstractPageModel</code> class and the page variation resolver required to
 * retrieve default or variation pages information.
 */
public interface PageVariationResolverType
{
	/**
	 * Get the typecode identifying the <code>AbstractPageModel</code>.
	 *
	 * @return the typecode
	 */
	String getTypecode();

	/**
	 * Get the resolver to be used when fetching default and variation pages for page type
	 * <code>AbstractPageModel</code>.
	 *
	 * @return the resolver
	 */
	PageVariationResolver<AbstractPageModel> getResolver();

}
