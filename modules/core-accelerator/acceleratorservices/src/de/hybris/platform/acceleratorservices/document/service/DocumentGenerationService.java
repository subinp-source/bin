/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.document.service;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * Service for generating a document.
 */
public interface DocumentGenerationService
{
	/**
	 * Generates {@link MediaModel} from given business process and document page
	 *
	 * @param frontendTemplateName
	 * 		the code of the template to use for script generation
	 * @param businessProcessModel
	 * 		Business process object
	 * @return the {@link MediaModel}
	 */
	MediaModel generate(final String frontendTemplateName, final BusinessProcessModel businessProcessModel);
}
