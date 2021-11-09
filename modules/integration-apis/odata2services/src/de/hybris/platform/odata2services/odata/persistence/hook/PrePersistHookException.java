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
package de.hybris.platform.odata2services.odata.persistence.hook;

/**
 * An exception that is thrown inside a {@link PrePersistHook} is translated to a {@code PrePersistHookException}
 */
public class PrePersistHookException extends PersistHookException
{
	private static final String HOOK_TYPE = "Pre-Persist-Hook";
	private static final String PRE_PERSIST_ERROR = "pre_persist_error";

	public PrePersistHookException(final String hookName, final Throwable cause, final String integrationKey)
	{
		super(hookName, HOOK_TYPE, PRE_PERSIST_ERROR, cause, integrationKey);
	}
}
