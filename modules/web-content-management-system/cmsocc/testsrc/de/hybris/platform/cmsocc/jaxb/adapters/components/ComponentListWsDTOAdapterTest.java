/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.components;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.ComponentListWsDTO;
import de.hybris.platform.cmsocc.data.ComponentWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentListWsDTOAdapter.ListAdaptedComponents;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


/**
 * JUnit Tests for the ComponentListWsDTOAdapter
 */
@UnitTest
public class ComponentListWsDTOAdapterTest
{
	private static final String TEST_NAME = "TestName";

	private final ComponentListWsDTOAdapter componentListWsDTOAdapter = new ComponentListWsDTOAdapter();
	private final ComponentListWsDTO componentList = new ComponentListWsDTO();

	@Test
	public void shouldNotMarshalEmptyComponentList()
	{
		final ListAdaptedComponents listResult = componentListWsDTOAdapter.marshal(componentList);

		assertThat(listResult, equalTo(null));
	}

	@Test
	public void shouldNotMarshalNullComponentList()
	{
		final ListAdaptedComponents listResult = componentListWsDTOAdapter.marshal(null);

		assertThat(listResult, equalTo(null));
	}

	@Test
	public void shouldMarshalComponentList()
	{
		final ComponentWsDTO componentWsDTO = new ComponentWsDTO();
		final List<ComponentWsDTO> listComponentWsDTO = new ArrayList<ComponentWsDTO>();
		componentWsDTO.setName(TEST_NAME);
		listComponentWsDTO.add(componentWsDTO);
		componentList.setComponent(listComponentWsDTO);

		final ListAdaptedComponents listResult = componentListWsDTOAdapter.marshal(componentList);

		assertThat(listResult.component.get(0).name, equalTo(TEST_NAME));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowUnsupportedOperationException() throws Exception
	{
		componentListWsDTOAdapter.unmarshal(null);
	}
}