/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.config.cms.dataattributeproviders;

import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.Map;



/**
 * 
 */
public interface TagDataAttributesProvider
{
	Map<String, String> getAttributes(AbstractCMSComponentModel comContainer, AbstractCMSComponentModel currentComponent,
			CmsPageRequestContextData currentCmsPageRequestContextData);
}
