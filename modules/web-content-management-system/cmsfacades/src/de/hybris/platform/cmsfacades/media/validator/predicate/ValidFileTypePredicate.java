/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.validator.predicate;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given media file type maps to an existing extension type.
 * <p>
 * Returns <tt>TRUE</tt> if the type exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class ValidFileTypePredicate implements Predicate<String>
{
	private String supportedTypes;

	@Override
	public boolean test(final String target)
	{
		return Pattern.compile(",").splitAsStream(getSupportedTypes()) //
				.filter(extension -> target.toLowerCase().contains(extension.toLowerCase())).findAny().isPresent();
	}

	protected String getSupportedTypes()
	{
		return supportedTypes;
	}

	@Required
	public void setSupportedTypes(final String supportedTypes)
	{
		this.supportedTypes = supportedTypes;
	}

}
