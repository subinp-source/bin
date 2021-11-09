/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.dto;

/**
 *  Enum with the change types handled by the integration services outbound sync extension.
 */
public enum OutboundChangeType
{
	/**
	 * Enum value an item Creation for Outbound Change Type
	 */
	CREATED,
	/**
	 * Enum value an item Deletion for Outbound Change Type
	 */
	DELETED,
	/**
	 * Enum value an item Modification for Outbound Change Type
	 */
	MODIFIED


}
