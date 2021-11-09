/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.importing.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultParserRegistryTest
{

	@Mock
	private ImportParameterParser parser;

	private final DefaultParserRegistry registry = new DefaultParserRegistry();

	@Before
	public void setUp()
	{
		registry.setParsers(Lists.newArrayList(parser));
	}

	@Test
	public void shouldParserBeReturnedWhenReferenceFormatMatchesAnyParser()
	{
		// given
		final String referenceFormat = "xx:xx";
		given(parser.matches(referenceFormat)).willReturn(true);

		// when
		final ImportParameterParser returnedParser = registry.getParser(referenceFormat);

		// then
		assertThat(returnedParser).isEqualTo(parser);
	}

	@Test
	public void shouldRuntimeExceptionBeThrownWhenNoParserCanBeFound()
	{
		// given
		given(parser.matches(any())).willReturn(false);

		// expect
		assertThatThrownBy(() -> registry.getParser("any")).isInstanceOf(RuntimeException.class);
	}

}
