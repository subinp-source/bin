/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.validator.predicate;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given media code maps to an existing media.
 * <p>
 * Returns <tt>TRUE</tt> if the media exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class MediaCodeExistsPredicate implements Predicate<String>
{
	private MediaService mediaService;

	@Override
	public boolean test(final String target)
	{
		boolean result = Boolean.TRUE;
		try
		{
			getMediaService().getMedia(target);
		}
		catch (final UnknownIdentifierException e)
		{
			result = Boolean.FALSE;
		}
		catch (final AmbiguousIdentifierException e)
		{
			result = Boolean.TRUE;
		}
		return result;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

}
