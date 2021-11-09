/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.commons.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *  Factory creates instance of ResponseEntity with stub values.
 */
public class RestResponseFactory
{
	public static <T> ResponseEntity<T> newStubInstance()
	{
	
		final HttpStatus response = HttpStatus.OK;
		
		return new ResponseEntity<T>(response)
		{
			@Override
			public T getBody()
			{
				return null;
			}
		};
	}
}
