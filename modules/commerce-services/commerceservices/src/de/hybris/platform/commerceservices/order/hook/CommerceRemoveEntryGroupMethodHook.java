/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.RemoveEntryGroupParameter;

import javax.annotation.Nonnull;

public interface CommerceRemoveEntryGroupMethodHook
{
	/**
	 *  Executed after commerce remove entry group
	 *
	 * @param parameter
	 * @param result
	 */
	void afterRemoveEntryGroup(@Nonnull final RemoveEntryGroupParameter parameter, CommerceCartModification result);

	/**
	 *
	 * Executed before commerce remove entry gtoup
	 *
	 * @param parameter
	 */
	void beforeRemoveEntryGroup(@Nonnull final RemoveEntryGroupParameter parameter) throws CommerceCartModificationException;

}
