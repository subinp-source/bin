/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.converters.populator;

import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;

import org.springframework.util.Assert;


/**
 * Convert a PrincipalModel to a PrincipalData
 */
public class PrincipalPopulator implements Populator<PrincipalModel, PrincipalData>
{

	@Override
	public void populate(final PrincipalModel source, final PrincipalData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setUid(source.getUid());
		target.setName(source.getDisplayName());
	}
}
