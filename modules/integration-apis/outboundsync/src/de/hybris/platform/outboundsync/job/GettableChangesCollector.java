/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.job;

import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;

import java.util.List;

/**
 * A {@link ChangesCollector} that allows the caller to get the changes
 * @deprecated not used anymore for outbound sync. Replaced by {@link CountingChangesCollector}
 */
@Deprecated(since = "1905.2003-CEP", forRemoval = true)
public interface GettableChangesCollector extends ChangesCollector
{
	/**
	 * Gets a list of changes
	 * @return The list of changes or an empty list
	 */
	List<ItemChangeDTO> getChanges();
}
