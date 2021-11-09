/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.rules.backoffice.editors;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;

import org.junit.Test;
import org.zkoss.zul.Comboitem;
import org.junit.Before;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


// works only as integration test, class cast exception as unit test
@IntegrationTest
public class ProductConfigParameterRendererTest
{
	ProductConfigParameterRenderer classUnderTest = new ProductConfigParameterRenderer();

	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void testRender()
	{
		final Comboitem item = new Comboitem();
		final Object data = "label";
		classUnderTest.render(item, data, 1);

		assertEquals(data, item.getValue());
		assertEquals(data.toString(), item.getLabel());
	}
}
