/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.store.BaseStoreModel;

import java.util.Map;


/**
 * The Interface B2BApprovalProcessLookUpStrategy.
 */
public interface B2BApprovalProcessLookUpStrategy
{

	/**
	 * Gets the processes.
	 *
	 * @param store
	 *           the store
	 * @return the processes
	 */
	Map<String, String> getProcesses(BaseStoreModel store);
}
