/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static org.assertj.core.api.Assertions.assertThat;

import com.hybris.backoffice.workflow.designer.renderer.CutNodeLabelMapper;
import org.junit.Before;
import org.junit.Test;


public class CutNodeLabelMapperTest
{

	private CutNodeLabelMapper mapper = new CutNodeLabelMapper();

	@Before
	public void setUp()
	{
		mapper.setMaxLength(5);
	}

	@Test
	public void shouldLabelBeCut()
	{
		// given
		final String tooLongLabel = "abcdef";
		final String expectedOutput = "abcde...";

		// when
		final String output = mapper.apply(tooLongLabel);

		// then
		assertThat(output).isEqualTo(expectedOutput);
	}

	@Test
	public void shouldLabelNotBeCutWhenItsLengthEqualsMaxLength()
	{
		// given
		final String labelEqualsMaxLength = "abcde";

		// when
		final String output = mapper.apply(labelEqualsMaxLength);

		// then
		assertThat(output).isEqualTo(labelEqualsMaxLength);
	}

	@Test
	public void shouldLabelNotBeCutWhenItsLengthIsShorterThanMaxLength()
	{
		// given
		final String labelShorterThansMaxLength = "abc";

		// when
		final String output = mapper.apply(labelShorterThansMaxLength);

		// then
		assertThat(output).isEqualTo(labelShorterThansMaxLength);
	}

}
