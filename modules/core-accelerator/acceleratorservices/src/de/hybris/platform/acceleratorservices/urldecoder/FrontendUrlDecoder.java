/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.urldecoder;

/**
 * Interface for 'decoding' a Frontend Url into a related business object.
 * 
 * 
 */
public interface FrontendUrlDecoder<T>
{
	T decode(final String url);
}
