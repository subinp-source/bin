/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.messagecentercsfacades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.messagecentercsfacades.data.ConversationMessageData;
import de.hybris.platform.messagecentercsfacades.data.ConversationMessageListData;

import java.util.List;

import org.springframework.util.Assert;


public class MessageListPopulator implements Populator<List<ConversationMessageData>, ConversationMessageListData>
{

	@Override
	public void populate(final List<ConversationMessageData> source, final ConversationMessageListData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setMessages(source);

	}

}
