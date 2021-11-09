/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.config;

import static org.junit.Assert.assertNotNull;

import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.junit.Test;


public class FacetSearchConfigSerializableTest extends AbstractIntegrationTest
{

	@Test
	public void testConfigSerializable() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfigIn = getFacetSearchConfig();

		// when
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(facetSearchConfigIn);
		out.close();

		final ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		final FacetSearchConfig configOut = (FacetSearchConfig) in.readObject();
		in.close();

		// then
		assertNotNull(configOut);
	}
}
