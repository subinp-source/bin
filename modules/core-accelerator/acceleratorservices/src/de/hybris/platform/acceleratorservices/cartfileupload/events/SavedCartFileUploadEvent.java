/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.cartfileupload.events;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.media.MediaModel;


public class SavedCartFileUploadEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private MediaModel fileMedia;

	public MediaModel getFileMedia()
	{
		return fileMedia;
	}

	public void setFileMedia(MediaModel fileMedia)
	{
		this.fileMedia = fileMedia;
	}

}
