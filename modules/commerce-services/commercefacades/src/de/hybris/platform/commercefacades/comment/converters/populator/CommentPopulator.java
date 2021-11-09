/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.comment.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commercefacades.comment.data.CommentData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;


public class CommentPopulator implements Populator<CommentModel, CommentData>
{
	private Converter<UserModel, PrincipalData> principalConverter;

	@Override
	public void populate(final CommentModel source, final CommentData target) throws ConversionException
	{
		validateParameterNotNull(source, "Parameter source cannot be null.");
		validateParameterNotNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setText(source.getText());
		target.setCreationDate(source.getCreationtime());
		target.setAuthor(getPrincipalConverter().convert(source.getAuthor()));
		target.setFromCustomer(Boolean.valueOf(source.getAuthor() instanceof CustomerModel));
	}

	protected Converter<UserModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(final Converter<UserModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}
}
