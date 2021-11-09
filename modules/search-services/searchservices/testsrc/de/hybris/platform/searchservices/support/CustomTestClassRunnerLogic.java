/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.support;

import de.hybris.platform.core.Log4JUtils;
import de.hybris.platform.core.threadregistry.HybrisJUnitUtility;
import de.hybris.platform.testframework.ChainingRunNotifierWrapper;
import de.hybris.platform.testframework.DefaultAnnotationFilter;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.UnifiedHybrisTestRunner;
import de.hybris.platform.testframework.runlistener.ItemCreationListener;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;


/**
 * This class holds common logic used in hybris test runners
 */
public class CustomTestClassRunnerLogic
{
	private final UnifiedHybrisTestRunner unifiedHybrisTestRunner;
	private final Runner runner;

	static
	{
		Log4JUtils.startup();
	}

	/**
	 * Used logger instance.
	 */
	private static final Logger LOG = Logger.getLogger(CustomTestClassRunnerLogic.class);

	/**
	 * List of registered listeners determined from annotations of test class.
	 */
	private final Map<Class<? extends RunListener>, ? extends RunListener> listeners;
	/**
	 * Object holding tests results
	 */
	private final Result result = new Result();
	private Filter nonAnnotationFilter = null;

	public CustomTestClassRunnerLogic(final UnifiedHybrisTestRunner unifiedHybrisTestRunner,
			final Runner runner)
			throws InitializationError
	{
		this.unifiedHybrisTestRunner = unifiedHybrisTestRunner;
		this.runner = runner;
		this.listeners = determineListeners();
	}

	public void filter(final Filter filter) throws NoTestsRemainException
	{
		// need to keep a reference to the standard filter since ...
		if (!(filter instanceof DefaultAnnotationFilter))
		{
			this.nonAnnotationFilter = filter;
		}
		// ... super.filter internally replaces the filter by DefaultAnnotationFilter
		// but we still depend on the information in the default one!
		unifiedHybrisTestRunner.superFilter(filter);
	}

	public void run(final RunNotifier notifier)
	{
		// Filtering tests -> must pass current filter to preserve method level filtering
		final Filter filter = new DefaultAnnotationFilter(this.nonAnnotationFilter);
		try
		{
			this.filter(filter);
		}
		catch (final NoTestsRemainException e)
		{
			//notthing to test - all tests are skipped because of DefaultAnnotationFilter
			return;

		}

		// wrap the notifier to call all listeners afters in chain mode
		final ChainingRunNotifierWrapper wrappedNotifier = new ChainingRunNotifierWrapper(notifier);

		// register listeners
		for (final RunListener listener : listeners.values())
		{
			wrappedNotifier.addListener(listener);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Registered " + listener.getClass().getName() + " as run listener");
			}
		}

		try
		{
			HybrisJUnitUtility.registerJUnitMainThread();

			// as testRunStarted was already called on delegates we must call it for wrapped ones
			wrappedNotifier.fireTestRunStarted(runner.getDescription());


			unifiedHybrisTestRunner.superRun(wrappedNotifier);
		}
		finally
		{
			// as testRunFinished will be called on delegates we must call it for wrapped ones only
			wrappedNotifier.fireTestRunFinished(result);

			// remove listeners to not get called for finished test run (in wrong order)
			wrappedNotifier.removeAllListeners();
		}

	}

	/**
	 * Determines all listeners configured for test method.
	 *
	 * @throws InitializationError
	 *            error while instantiation of the listeners
	 */
	private Map<Class<? extends RunListener>, RunListener> determineListeners() throws InitializationError
	{
		final Map<Class<? extends RunListener>, RunListener> resultingListeners = new LinkedHashMap<Class<? extends RunListener>, RunListener>();

		final RunListener resultListener = this.result.createListener();
		resultingListeners.put(RunListener.class, resultListener);

		Class curClass = unifiedHybrisTestRunner.getCurrentTestClass();
		while (curClass != null)
		{
			final RunListeners listenerAnno = (RunListeners) curClass.getAnnotation(RunListeners.class);
			if (listenerAnno != null)
			{
				for (final Class<? extends RunListener> listener : listenerAnno.value())
				{
					try
					{
						resultingListeners.put(listener, listener.getDeclaredConstructor().newInstance());
					}
					catch (final SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException e)
					{
						throw new InitializationError(Collections.singletonList((Throwable) e));
					}
				}
			}
			curClass = curClass.getSuperclass();
		}

		// workaround
		resultingListeners.put(ItemCreationListener.class, new CustomItemCreationListener());

		return resultingListeners;
	}
}
