/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zhtml.Li;
import org.zkoss.zul.Label;

import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@RunWith(MockitoJUnitRunner.class)
public class DefaultBulkEditValidationDetailsComponentFactoryTest
{

	private static final String VALIDATED_PROPERTY = "name";
	private static final String VALIDATED_PROPERTY_NAME = "Identifier";

	@Mock
	private LabelService labelService;

	@Spy
	@InjectMocks
	private DefaultBulkEditValidationDetailsComponentFactory factory;

	@Before
	public void setUp() throws Exception
	{
		CockpitTestUtil.mockZkEnvironment();
		doReturn(VALIDATED_PROPERTY_NAME).when(labelService).getObjectLabel(VALIDATED_PROPERTY);
	}

	@Test
	public void shouldLiBeCreatedWithoutHeaderLabel()
	{
		// given
		final String myErrorMessage = "My error message";
		final DefaultValidationInfo validationMessage = new DefaultValidationInfo();
		validationMessage.setValidationMessage(myErrorMessage);
		validationMessage.setInvalidPropertyPath(VALIDATED_PROPERTY);

		// when
		final Li li = factory.createValidationDetails(validationMessage);

		// then
		assertThat(((Label) li.getChildren().get(0)).getValue()).isEqualTo(String.format("[%s]: ", VALIDATED_PROPERTY_NAME));
		assertThat(((Label) li.getChildren().get(1)).getValue()).isEqualTo(myErrorMessage);
	}

}
