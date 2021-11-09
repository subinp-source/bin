/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.components;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.ComponentWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentAdapterUtil.ComponentAdaptedData;

import org.junit.Test;


/**
 * JUnit Tests for the ComponentWsDTOAdapter
 */
@UnitTest
public class ComponentWsDTOAdapterTest
{
	private static final String TEST_NAME = "TestName";

	ComponentWsDTOAdapter componentWsDTOAdapter = new ComponentWsDTOAdapter();

	@Test
	public void shouldNotMarshalNullComponent()
	{
		final ComponentAdaptedData componentAdaptedDataResult = componentWsDTOAdapter.marshal(null);

		assertThat(componentAdaptedDataResult, equalTo(null));
	}

	@Test
	public void shouldMarshalComponent()
	{
		final ComponentWsDTO componentWsDTO = new ComponentWsDTO();
		componentWsDTO.setName(TEST_NAME);

		final ComponentAdaptedData componentAdaptedDataResult = componentWsDTOAdapter.marshal(componentWsDTO);

		assertThat(componentAdaptedDataResult.name, equalTo(TEST_NAME));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowUnsupportedOperationException() throws Exception
	{
		componentWsDTOAdapter.unmarshal(null);
	}
}