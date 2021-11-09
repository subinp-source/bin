/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.os.process;

/**
 * @author pohl
 */
class Handle<T>
{
	private T value;

	public T get()
	{
		return this.value;
	}

	public void set(final T value)
	{
		this.value = value;
	}
}
