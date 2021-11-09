/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.document.service.impl;

import de.hybris.platform.acceleratorservices.document.dao.DocumentPageDao;
import de.hybris.platform.acceleratorservices.document.service.DocumentPageService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;


/**
 * Default implementation for {@link DocumentPageService}
 */
public class DefaultDocumentPageService implements DocumentPageService
{
	private DocumentPageDao documentPageDao;

	@Override
	public DocumentPageModel findDocumentPageByTemplateName(final String templateName, final Collection<CatalogVersionModel> catalogVersions)
	{

		return getDocumentPageDao().findDocumentPageByTemplateName(templateName, catalogVersions);
	}

	protected DocumentPageDao getDocumentPageDao()
	{
		return documentPageDao;
	}

	@Required
	public void setDocumentPageDao(final DocumentPageDao documentPageDao)
	{
		this.documentPageDao = documentPageDao;
	}
}
