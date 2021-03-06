/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsocc.validation;

import de.hybris.platform.messagecentercsocc.dto.conversation.ConversationMessageWsDTO;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ConversationMessageListValidator implements Validator
{
	private String fieldPath;
	private boolean canBeEmpty = true;
	private static final String SENTTIME = "Sent Time";
	private static final String CONTENT = "Content";

	@Override
	public boolean supports(final Class clazz)
	{
		return ConversationMessageWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final List<ConversationMessageWsDTO> messages = (List<ConversationMessageWsDTO>) (fieldPath == null ? target
				: errors.getFieldValue(fieldPath));
		if (CollectionUtils.isEmpty(messages))
		{
			if (!canBeEmpty)
			{
				errors.reject("field.required");
			}
		}
		else
		{
			int idx = 1;
			for (final ConversationMessageWsDTO message : messages)
			{
				if (message.getSentTime() == null)
				{
					addFieldRequiredError(errors, SENTTIME, idx);
				}
				if (StringUtils.isEmpty(message.getContent()))
				{
					addFieldRequiredError(errors, CONTENT, idx);
				}
				idx = idx + 1;
			}
		}
	}

	protected void addFieldRequiredError(final Errors errors, final String field, final int line)
	{
		errors.reject("field.required.conversationMessage", new String[]
		{ field, String.valueOf(line) }, "{0} is required in message {1}.");
	}

	public String getFieldPath()
	{
		return fieldPath;
	}

	public void setFieldPath(final String fieldPath)
	{
		this.fieldPath = fieldPath;
	}

	public boolean getCanBeEmpty()
	{
		return canBeEmpty;
	}

	public void setCanBeEmpty(final boolean canBeEmpty)
	{
		this.canBeEmpty = canBeEmpty;
	}

}
