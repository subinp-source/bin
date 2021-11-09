/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests

class DocumentationSuiteGuard {
	private static boolean isSuiteRunning = false;

	public static boolean isSuiteRunning() {
		return isSuiteRunning;
	}

	public static void setSuiteRunning(boolean isSuiteRunning) {
		this.isSuiteRunning = isSuiteRunning;
	}
}
