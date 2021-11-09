/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 *
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.docu

/**
 * Aggregate information about web service request: resurce, httpMethod, accept(XML|JSON) and response
 */
class WSRequestSummary {
	def String resource;
	def String accept;
	def String httpMethod;
	def Object response;
}
