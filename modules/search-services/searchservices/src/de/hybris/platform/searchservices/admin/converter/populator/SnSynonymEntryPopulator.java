/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.converter.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.admin.data.SnSynonymEntry;
import de.hybris.platform.searchservices.model.SnSynonymEntryModel;


/**
 * Populates {@link SnSynonymEntry} from {@link SnSynonymEntryModel}.
 */
public class SnSynonymEntryPopulator implements Populator<SnSynonymEntryModel, SnSynonymEntry>
{
	@Override
	public void populate(final SnSynonymEntryModel source, final SnSynonymEntry target)
	{
		target.setId(source.getId());
		target.setInput(source.getInput());
		target.setSynonyms(source.getSynonyms());
	}
}
