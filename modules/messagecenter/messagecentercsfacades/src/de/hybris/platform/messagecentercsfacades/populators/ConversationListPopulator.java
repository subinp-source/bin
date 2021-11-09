/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.messagecentercsfacades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.messagecentercsfacades.data.ConversationData;
import de.hybris.platform.messagecentercsfacades.data.ConversationDataList;

import java.util.List;

import org.springframework.util.Assert;


public class ConversationListPopulator implements Populator<List<ConversationData>, ConversationDataList>
{

	@Override
	public void populate(final List<ConversationData> source, final ConversationDataList target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setConversations(source);

	}

}
