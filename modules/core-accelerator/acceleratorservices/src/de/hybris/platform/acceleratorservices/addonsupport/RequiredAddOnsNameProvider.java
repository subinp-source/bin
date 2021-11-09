/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.addonsupport;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.util.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class RequiredAddOnsNameProvider
{

	private final Map<String, List<String>> extensionAddOns;

	public RequiredAddOnsNameProvider()
	{
		extensionAddOns = new HashMap<String, List<String>>();
	}

	/**
	 * Get the names of all dependent addOns. The list returned is sorted alphabetically.
	 *
	 * @param extensionName:
	 *           name of the extension
	 * @return: list of addOns names
	 */
	public List<String> getAddOns(final String extensionName)
	{
		if (extensionAddOns.containsKey(extensionName))
		{
			return extensionAddOns.get(extensionName);
		}

		final List<String> dependentAddOns = getDependantAddOns(extensionName);
		extensionAddOns.put(extensionName, dependentAddOns);
		return dependentAddOns;
	}

	protected List<String> getDependantAddOns(final String extensionName)
	{
		if (StringUtils.isEmpty(extensionName))
		{
			return Collections.emptyList();
		}
		final ExtensionInfo extensionInfo = getExtensionInfo(extensionName);
		final Set<ExtensionInfo> allRequiredExtensionInfos = extensionInfo.getAllRequiredExtensionInfos();

		// Check if each required extension is an addon
		final Set<String> allAddOns = new HashSet<String>();
		for (final ExtensionInfo extension : allRequiredExtensionInfos)
		{
			if (isAddOnExtension(extension))
			{
				allAddOns.add(extension.getName());
			}
		}

		// Get the addon names in the correct order
		final List<String> extNames = new ArrayList<>(getExtensionNames());
		extNames.sort(String.CASE_INSENSITIVE_ORDER);

		final List<String> addOnsInOrder = new ArrayList<String>();
		for (final String extName : extNames)
		{
			if (allAddOns.contains(extName))
			{
				addOnsInOrder.add(extName);
			}
		}
		return addOnsInOrder;
	}

	protected boolean isAddOnExtension(final ExtensionInfo extensionInfo)
	{
		return new File(extensionInfo.getExtensionDirectory(), "acceleratoraddon").exists();
	}

	protected ExtensionInfo getExtensionInfo(final String extName)
	{
		return Utilities.getExtensionInfo(extName);
	}

	protected List<String> getExtensionNames()
	{
		return Utilities.getExtensionNames();
	}
}
