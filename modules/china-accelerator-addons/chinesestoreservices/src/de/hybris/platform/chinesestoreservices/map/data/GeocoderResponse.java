/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesestoreservices.map.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocoderResponse
{
	private String status;

	private GeocoderResult result;


	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the results
	 */
	public GeocoderResult getResult()
	{
		return result;
	}

	/**
	 * @param results
	 *           the results to set
	 */
	public void setResult(final GeocoderResult result)
	{
		this.result = result;
	}


}
