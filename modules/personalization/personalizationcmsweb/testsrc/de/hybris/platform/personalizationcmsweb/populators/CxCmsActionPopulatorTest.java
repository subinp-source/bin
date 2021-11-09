/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcmsweb.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationcmsweb.data.CxCmsActionData;

import org.junit.Assert;
import org.junit.Test;


@UnitTest
public class CxCmsActionPopulatorTest
{
	private final CxCmsActionPopulator populator = new CxCmsActionPopulator();

	@Test
	public void shouldPopulateValue()
	{
		//given
		final CxCmsActionData target = new CxCmsActionData();
		final CxCmsActionModel source = new CxCmsActionModel();
		source.setComponentId("testId");

		//when
		populator.populate(source, target);

		//then
		Assert.assertEquals("testId", target.getComponentId());
	}

	@Test
	public void shouldPopulateNull()
	{
		//given
		final CxCmsActionData target = new CxCmsActionData();
		final CxCmsActionModel source = new CxCmsActionModel();

		//when
		populator.populate(source, target);

		//then
		Assert.assertNull(target.getComponentId());
	}
}
