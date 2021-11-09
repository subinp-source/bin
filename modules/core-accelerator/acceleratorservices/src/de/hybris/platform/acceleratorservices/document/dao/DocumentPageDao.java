/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.document.dao;

import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;


/**
 * Data Access for looking up the Document page for a template name.
 */
public interface DocumentPageDao extends Dao
{
	/**
	 * Retrieves {@link DocumentPageModel} given its frontend template name.
	 *
	 * @param frontendTemplateName
	 * 		the frontend template name
	 * @return {@link DocumentPageModel} object if found, null otherwise
	 */
	DocumentPageModel findDocumentPageByTemplateName(final String frontendTemplateName, final Collection<CatalogVersionModel> catalogVersions);
}
