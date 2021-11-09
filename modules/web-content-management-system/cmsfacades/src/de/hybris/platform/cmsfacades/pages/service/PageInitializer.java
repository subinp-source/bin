/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;


/**
 * performs the necessary initialization that a newly created {@code AbstractPageModel} may require before saving.
 */
public interface PageInitializer
{
	/**
	 * Perform any necessary initialization onto a newly created page such as associating non shared slots
	 *
	 * @param page - the instance of {@code AbstractPageModel}
	 * @return the modified page
	 */
	AbstractPageModel initialize(AbstractPageModel page);
}
