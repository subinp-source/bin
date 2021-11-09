/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util.featuretoggle;

/**
 * Represents features being worked on in Integration API
 */
public enum IntegrationApiFeature implements Feature
{
	FEATURE_DEFAULT("integrationservices.feature.default.enum.do.not.remove");

	private final String property;

	IntegrationApiFeature(final String property)
	{
		this.property = property;
	}

	public String getProperty()
	{
		return property;
	}
}
