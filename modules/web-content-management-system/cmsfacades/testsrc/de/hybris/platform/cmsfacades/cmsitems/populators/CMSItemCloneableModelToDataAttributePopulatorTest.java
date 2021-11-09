/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.cloning.service.predicate.CMSItemCloneablePredicate;
import de.hybris.platform.core.model.ItemModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_CLONEABLE_NAME;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSItemCloneableModelToDataAttributePopulatorTest
{
	@InjectMocks
	private CMSItemCloneableModelToDataAttributePopulator populator;

	@Mock
	private CMSItemCloneablePredicate cmsItemCloneablePredicate;

	@Mock
	private ItemModel itemModel;

	private Map<String, Object> itemMap = new HashMap<>();

	@Test
	public void testItemModelIsCloneable()
	{
		// GIVEN
		when(cmsItemCloneablePredicate.test(itemModel)).thenReturn(true);

		// WHEN
		populator.populate(itemModel, itemMap);

		// THEN
		assertThat(itemMap.get(FIELD_CLONEABLE_NAME), is(true));
	}

	@Test
	public void testItemModelIsNotCloneable()
	{
		// GIVEN
		when(cmsItemCloneablePredicate.test(itemModel)).thenReturn(false);

		// WHEN
		populator.populate(itemModel, itemMap);

		// THEN
		assertThat(itemMap.get(FIELD_CLONEABLE_NAME), is(false));
	}
}
