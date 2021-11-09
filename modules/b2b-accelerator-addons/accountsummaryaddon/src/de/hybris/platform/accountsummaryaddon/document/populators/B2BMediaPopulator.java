/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.populators;

import org.springframework.util.Assert;

import de.hybris.platform.accountsummaryaddon.document.data.MediaData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;


public class B2BMediaPopulator<SOURCE extends MediaModel, TARGET extends MediaData> implements Populator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setRealFileName(source.getRealFileName());
		target.setDownloadURL(source.getDownloadURL());
	}
}
