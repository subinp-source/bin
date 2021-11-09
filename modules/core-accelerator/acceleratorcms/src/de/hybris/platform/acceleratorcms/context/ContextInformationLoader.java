/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.context;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for context information loader
 */
public interface ContextInformationLoader
{
	CMSSiteModel initializeSiteFromRequest(final String absoluteURL);

	void initializePreviewRequest(final PreviewDataModel previewDataModel);

	void setCatalogVersions();

	void loadFakeContextInformation(final HttpServletRequest httpRequest, final PreviewDataModel previewData);

	void storePreviewData(final PreviewDataModel previewData);

}
