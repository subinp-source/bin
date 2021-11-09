/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.comments.builder;

import de.hybris.platform.commerceservices.service.data.CommerceCommentParameter;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * Builder for CommerceCommentParameter.
 */
public class CommerceCommentParameterBuilder
{
	private final CommerceCommentParameter parameter;

	public CommerceCommentParameterBuilder()
	{
		parameter = new CommerceCommentParameter();
	}

	public CommerceCommentParameter build()
	{
		return parameter;
	}

	public CommerceCommentParameterBuilder item(final ItemModel item)
	{
		parameter.setItem(item);
		return this;
	}

	public CommerceCommentParameterBuilder author(final UserModel author)
	{
		parameter.setAuthor(author);
		return this;
	}

	public CommerceCommentParameterBuilder text(final String text)
	{
		parameter.setText(text);
		return this;
	}

	public CommerceCommentParameterBuilder domainCode(final String domainCode)
	{
		parameter.setDomainCode(domainCode);
		return this;
	}

	public CommerceCommentParameterBuilder componentCode(final String componentCode)
	{
		parameter.setComponentCode(componentCode);
		return this;
	}

	public CommerceCommentParameterBuilder commentTypeCode(final String commentTypeCode)
	{
		parameter.setCommentTypeCode(commentTypeCode);
		return this;
	}

}
