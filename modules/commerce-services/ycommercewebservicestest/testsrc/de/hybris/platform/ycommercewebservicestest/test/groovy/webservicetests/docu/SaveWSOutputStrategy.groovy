/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.docu;


/**
 * Interface for strategies which save summary of test to file
 */
public interface SaveWSOutputStrategy {
	public static final String WS_OUTPUT_DIR = "resources/WS_OUTPUT";

	void saveFailedTest(SummaryOfRunningTest summary, Throwable t);

	void saveSucceededTest(SummaryOfRunningTest summary);
}
