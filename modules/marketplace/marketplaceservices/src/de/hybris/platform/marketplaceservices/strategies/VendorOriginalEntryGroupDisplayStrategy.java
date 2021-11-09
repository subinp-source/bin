/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

/**
 * Strategy for read Vendor EntryGroup display setting
 */
public interface VendorOriginalEntryGroupDisplayStrategy
{

	/**
	 * decide whether to display the other EntryGroups besides VendorGroups in marketplace
	 */
	boolean shouldDisplayOriginalEntryGroup();
}
