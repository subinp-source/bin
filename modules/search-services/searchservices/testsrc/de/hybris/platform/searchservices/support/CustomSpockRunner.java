/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.searchservices.support;

import de.hybris.platform.testframework.UnifiedHybrisTestRunner;

import java.lang.reflect.Method;

import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.spockframework.runtime.Sputnik;
import org.spockframework.runtime.model.SpecInfo;
import org.springframework.util.ReflectionUtils;


public class CustomSpockRunner extends Sputnik implements UnifiedHybrisTestRunner
{
	private final CustomTestClassRunnerLogic testClassRunnerLogic;

	public CustomSpockRunner(final Class<?> clazz) throws InitializationError
	{
		super(clazz);
		testClassRunnerLogic = new CustomTestClassRunnerLogic(this, this);
	}

	@Override
	public void filter(final Filter filter) throws NoTestsRemainException
	{
		testClassRunnerLogic.filter(filter);
	}

	@Override
	public void run(final RunNotifier notifier)
	{
		testClassRunnerLogic.run(notifier);
	}

	@Override
	public Class getCurrentTestClass()
	{
		final Method method = ReflectionUtils.findMethod(getClass().getSuperclass(), "getSpec");
		ReflectionUtils.makeAccessible(method);
		final SpecInfo specInfo = (SpecInfo) ReflectionUtils.invokeMethod(method, this);

		return specInfo.getReflection();
	}

	@Override
	public void superFilter(final Filter filter) throws NoTestsRemainException
	{
		super.filter(filter);
	}

	@Override
	public void superRun(final RunNotifier notifier)
	{
		super.run(notifier);
	}
}
