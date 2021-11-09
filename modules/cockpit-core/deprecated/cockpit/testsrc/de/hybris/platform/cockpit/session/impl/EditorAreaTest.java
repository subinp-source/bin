/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.session.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test class that tests {@link EditorArea}
 */
@UnitTest
public class EditorAreaTest
{
	@Mock
	private EditorArea editorArea;

	@Mock
	private ItemModel itemModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSetModelLocalizedValue()
	{
		editorArea.setModelLocalizedValue(itemModel, "", "de", null);
		verify(editorArea).setModelLocalizedValue(eq(itemModel), anyString(), eq("de"), any());
	}

}
