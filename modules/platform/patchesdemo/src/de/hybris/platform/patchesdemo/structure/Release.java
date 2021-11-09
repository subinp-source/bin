/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.structure;

/**
 * Represent project releases. String used in constructor will be used to create paths to impexes that should be
 * imported in logs etc. (should be unique).
 */
public enum Release implements de.hybris.platform.patches.Release
{

	R1("01"), R2("02"), E1("ERROR");

	private String releaseId;

	Release(final String releaseId)
	{
		this.releaseId = releaseId;
	}

	@Override
	public String getReleaseId()
	{
		return this.releaseId;
	}

}
