/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.organization.services.impl;

import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitParameter;

import java.util.Optional;

import org.junit.Assert;


public class OrgUnitServiceTestUtil
{
	public static OrgUnitModel getUnit(final String uid, final DefaultOrgUnitService defaultOrgUnitService)
	{
		final Optional<OrgUnitModel> unitOptional = defaultOrgUnitService.getUnitForUid(uid);
		Assert.assertTrue(String.format("OrgUnitModel with uid '%s' not found.", uid), unitOptional.isPresent());
		return unitOptional.get();
	}

	public static OrgUnitModel getParentUnit(final OrgUnitModel unit, final DefaultOrgUnitService defaultOrgUnitService)
	{
		final Optional<OrgUnitModel> parentUnitOptional = defaultOrgUnitService.getParent(unit);
		Assert.assertTrue(String.format("Parent for OrgUnitModel with uid '%s' not found.", unit.getUid()),
				parentUnitOptional.isPresent());
		return parentUnitOptional.get();
	}

	public static OrgUnitParameter createOrgUnitParam(final OrgUnitModel orgUnit, final OrgUnitModel parentUnit, final String uid,
			final String name, final Boolean active, final String description)
	{
		final OrgUnitParameter parameter = new OrgUnitParameter();
		if (orgUnit == null)
		{
			// create unit
			parameter.setUid(uid);
			parameter.setName(name);
			parameter.setActive(active);
			parameter.setDescription(description);
		}
		else
		{
			// update unit
			orgUnit.setActive(active);
			orgUnit.setUid(uid);
			orgUnit.setName(name);
			orgUnit.setDescription(description);
			parameter.setOrgUnit(orgUnit);
		}
		parameter.setParentUnit(parentUnit);
		return parameter;
	}
}
