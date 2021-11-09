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

/**
 * A ChangesCollectorProvider provides the caller a {@link ChangesCollector} of type {@code T}
 *
 * @param <T> Specific type of the ChangesCollector
 */
public interface ChangesCollectorProvider<T extends ChangesCollector>
{
	/**
	 * Get an instance of the {@link ChangesCollector}
	 * @return ChangesCollector
	 */
	T getCollector();
}
