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
public class CxCmsActionReversePopulatorTest
{
	private static final String COMPONENT = "component1";
	private static final String CONTAINER = "container1";

	private final CxCmsActionReversePopulator cxCmsActionReversePopulator = new CxCmsActionReversePopulator();


	@Test
	public void shouldPopulate()
	{
		final CxCmsActionData source = new CxCmsActionData();
		source.setContainerId(CONTAINER);
		source.setComponentId(COMPONENT);
		final CxCmsActionModel target = new CxCmsActionModel();
		cxCmsActionReversePopulator.populate(source, target);

		Assert.assertEquals(CONTAINER, target.getContainerId());
		Assert.assertEquals(COMPONENT, target.getComponentId());
	}



}
