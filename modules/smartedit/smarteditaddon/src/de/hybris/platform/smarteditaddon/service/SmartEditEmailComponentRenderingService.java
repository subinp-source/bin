/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.service;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;


/**
 * Service designed to render email supported cms components
 */
public interface SmartEditEmailComponentRenderingService
{

	/**
	 * Renders given CmsComponent for email template purposes
	 *
	 * @param component
	 * 		to render
	 * @return html string
	 */
	String renderComponent(AbstractCMSComponentModel component);
}
