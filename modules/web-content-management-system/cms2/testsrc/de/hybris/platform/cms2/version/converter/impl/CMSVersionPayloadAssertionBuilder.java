/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.converter.impl;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class CMSVersionPayloadAssertionBuilder
{
	private CMSVersionPayloadAnalyzer comparator;
	private String attribute;
	private List<Consumer<CMSVersionPayloadAnalyzer.PayloadAttribute>> assertions;

	private CMSVersionPayloadAssertionBuilder() { }

	public static CMSVersionPayloadAssertionBuilder aModel()
	{
		return new CMSVersionPayloadAssertionBuilder();
	}

	public CMSVersionPayloadAssertionBuilder withComparator(final CMSVersionPayloadAnalyzer comparator)
	{
		this.comparator = comparator;
		return this;
	}

	public CMSVersionPayloadAssertionBuilder withAttribute(final String attribute)
	{
		this.attribute = attribute;
		return this;
	}

	public CMSVersionPayloadAssertionBuilder withAssertions(final Consumer<CMSVersionPayloadAnalyzer.PayloadAttribute>... assertions)
	{
		this.assertions = Arrays.stream(assertions).collect(toList());
		return this;
	}

	public void check()
	{
		final CMSVersionPayloadAnalyzer.PayloadAttribute payloadAttribute = comparator.getAttributeByName(attribute);
		assertions.forEach(assertion -> assertion.accept(payloadAttribute));
	}
}
