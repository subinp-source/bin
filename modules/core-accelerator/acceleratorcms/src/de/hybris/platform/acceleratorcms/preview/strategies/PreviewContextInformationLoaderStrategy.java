/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.preview.strategies;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;



public interface PreviewContextInformationLoaderStrategy
{
	void initContextFromPreview(PreviewDataModel preview);
}
