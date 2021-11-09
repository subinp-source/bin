/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Provides current spring {@link ApplicationContext} for non-bean classes.
 */
public class SpringContextProvider implements ApplicationContextAware
{
    private static ApplicationContext context;

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		setContextThreadSafe(applicationContext);
	}

	public static ApplicationContext getContext()
	{
		return context;
	}

	protected static synchronized void setContextThreadSafe(final ApplicationContext applicationContext)
          throws BeansException
    {
        context = applicationContext;
    }
}
