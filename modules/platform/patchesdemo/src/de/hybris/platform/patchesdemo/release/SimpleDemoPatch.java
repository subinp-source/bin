/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.release;

import de.hybris.platform.patches.organisation.ImportLanguage;
import de.hybris.platform.patchesdemo.structure.CountryOrganisation;
import de.hybris.platform.patchesdemo.structure.ShopOrganisation;

import java.util.Set;


/**
 * Patches demo specific interface, which provides default empty implementation for methods declared in
 * {@link DemoPatch} interface. Use in patch classes which do not need to implement all methods required by
 * {@link AbstractDemoPatch}. Example:
 *
 * <pre>
 * public class Patch2x1 extends AbstractDemoPatch implements SimpleDemoPatch
 * {
 * 	// no implementation for DemoPatch methods needed
 * 	// can override any if needed
 * }
 * </pre>
 */
public interface SimpleDemoPatch extends DemoPatch
{
	@Override
	default void createGlobalData(final Set<ImportLanguage> languages, boolean updateLanguagesOnly)
	{
		// empty
	}

	@Override
	default void createShopData(final ShopOrganisation unit, final Set<ImportLanguage> languages,
	                            final boolean updateLanguagesOnly)
	{
		// empty
	}

	@Override
	default void createCountryData(final CountryOrganisation country)
	{
		// empty
	}
}
