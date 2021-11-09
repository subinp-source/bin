/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup;

import de.hybris.platform.addonsupport.impex.AddonConfigDataImportType;
import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;


/**
 * Defines services for addon configuration data import
 */
public interface AddOnConfigDataImportService
{
	/**
	 * Imports data
	 *
	 * @param extensionName
	 *           the extension name
	 * @param importType
	 *           the data type to import
	 * @param macroParameters
	 *           the impex macro parameters
	 * @return whether the import was successful or not
	 */
	boolean executeImport(final String extensionName, AddonConfigDataImportType importType,
			ImpexMacroParameterData macroParameters);
}
