/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.service;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.model.media.MediaFormatModel;

import java.util.Collection;


/**
 * Provide methods for retrieving media format information for a given CMS component model.
 */
public interface CMSMediaFormatService
{
	/**
	 * Retrieve a list of all media formats which are supported by the specified CMS component model
	 *
	 * @param classType
	 *           the CMS component model class
	 * @return the collection of media formats; never <tt>null</tt>
	 */
	Collection<MediaFormatModel> getMediaFormatsByComponentType(final Class<? extends AbstractCMSComponentModel> classType);
}
