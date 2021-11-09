/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.document.service;

import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.Collection;


/**
 * Service for looking up the CMS Document Page for a template name.
 */
public interface DocumentPageService
{
	/**
	 * Retrieves {@link DocumentPageModel} given its template name.
	 *
	 * @param templateName
	 * 		template name to retrieve the {@link DocumentPageModel}
	 * @param catalogVersions
	 * 		collection of {@link CatalogVersionModel}
	 * @return DocumentPage object if found, null otherwise
	 */
	DocumentPageModel findDocumentPageByTemplateName(final String templateName, final Collection<CatalogVersionModel> catalogVersions);
}
