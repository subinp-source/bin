/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.indexer.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.impl.PksSnIndexerItemSource;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class PksSnIndexerItemSourceTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private SnIndexerContext indexerContext;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createItemSource() throws Exception
	{
		// given
		final PK pk1 = PK.fromLong(1);
		final PK pk2 = PK.fromLong(2);
		final PK pk3 = PK.fromLong(3);

		// when
		final SnIndexerItemSource itemSource = new PksSnIndexerItemSource(List.of(pk1, pk2, pk3));
		final List<PK> pks = itemSource.getPks(indexerContext);

		// then
		assertThat(pks).isNotNull();
		assertThat(pks).hasSize(3);
		assertThat(pks).containsExactly(pk1, pk2, pk3);
	}

	@Test
	public void createEmptyItemSource() throws Exception
	{
		// when
		final SnIndexerItemSource itemSource = new PksSnIndexerItemSource(null);
		final List<PK> pks = itemSource.getPks(indexerContext);

		// then
		assertThat(pks).isNotNull();
		assertThat(pks).hasSize(0);
	}
}
