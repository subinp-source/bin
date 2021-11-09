/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.release;

import de.hybris.platform.patches.organisation.ImportLanguage;
import de.hybris.platform.patchesdemo.structure.CountryOrganisation;
import de.hybris.platform.patchesdemo.structure.Release;
import de.hybris.platform.patchesdemo.structure.ShopOrganisation;
import de.hybris.platform.patchesdemo.structure.StructureState;

import java.util.Set;


/**
 * Example patch which runs previous patches.
 */
public class Patch2x0 extends AbstractDemoPatch
{

	public Patch2x0()
	{
		super("2_0", "02_00", Release.R2, StructureState.V2);
	}

	@Override
	public void createGlobalData(final Set<ImportLanguage> languages, final boolean updateLanguagesOnly)
	{
		importGlobalData("r02_00_010-globalDataExample.impex", languages, updateLanguagesOnly);
	}

	@Override
	public void createShopData(final ShopOrganisation unit, final Set<ImportLanguage> languages, final boolean updateLanguages)
	{
		importShopSpecificData("r02_00_010-shopDataExample.impex", languages, unit, updateLanguages);
	}

	@Override
	public void createCountryData(final CountryOrganisation country)
	{
		importCountryData("r02_00_010-countryDataExample.impex", country);
	}
}
