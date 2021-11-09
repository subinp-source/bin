/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesestoreservices.map.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocoderResult
{
	private GeoLocation location;
	private String precise;
	private String confidence;

	/**
	 * @return the location
	 */
	public GeoLocation getLocation()
	{
		return location;
	}

	/**
	 * @param location
	 *           the location to set
	 */
	public void setLocation(final GeoLocation location)
	{
		this.location = location;
	}

	/**
	 * @return the precise
	 */
	public String getPrecise()
	{
		return precise;
	}

	/**
	 * @param precise
	 *           the precise to set
	 */
	public void setPrecise(final String precise)
	{
		this.precise = precise;
	}

	/**
	 * @return the confidence
	 */
	public String getConfidence()
	{
		return confidence;
	}

	/**
	 * @param confidence
	 *           the confidence to set
	 */
	public void setConfidence(final String confidence)
	{
		this.confidence = confidence;
	}





}
