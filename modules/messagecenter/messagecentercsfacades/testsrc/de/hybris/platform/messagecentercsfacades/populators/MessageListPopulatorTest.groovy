/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsfacades.populators

import de.hybris.platform.messagecentercsfacades.data.ConversationMessageData
import de.hybris.platform.messagecentercsfacades.data.ConversationMessageListData
import de.hybris.platform.messagecentercsfacades.populators.MessageListPopulator

import org.junit.Test

import spock.lang.Specification

class MessageListPopulatorTest extends Specification
{
	def private List<ConversationMessageData> source 
	def private MessageListPopulator populator
	def private ConversationMessageListData target

	def setup()
	{
		source = Mock()
		populator = new MessageListPopulator();
		target = new ConversationMessageListData();
	}
	@Test
	def "testPopulator"()
	{
		when:
		populator.populate(source, target);
		then:
		source.equals(target.getMessages())

		when:
		populator.populate(source, null)
		then:
		thrown(IllegalArgumentException)

		when:
		populator.populate(null, target)
		then:
		thrown(IllegalArgumentException)
	}
}
