/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.threadcontext.impl;

import de.hybris.platform.commerceservices.threadcontext.ThreadContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the ThreadContext
 */
public class DefaultThreadContext implements ThreadContext
{
	private final ThreadContext parent;
	private final Map<String, Object> attributes = new HashMap<String, Object>();

	public DefaultThreadContext()
	{
		this(null);
	}

	public DefaultThreadContext(final ThreadContext parent)
	{
		this.parent = parent;
	}

	@Override
	public void setAttribute(final String name, final Object value)
	{
		attributes.put(name, value);
	}

	@Override
	public <T> T getAttribute(final String name)
	{
		// Look for the value in the current map
		final T value = (T) attributes.get(name);
		if (value != null || attributes.containsKey(name))
		{
			return value;
		}

		// Delegate to parent
		if (parent != null)
		{
			parent.getAttribute(name);
		}

		// Not found
		return null;
	}

	@Override
	public void removeAttribute(final String name)
	{
		attributes.remove(name);
	}
}
