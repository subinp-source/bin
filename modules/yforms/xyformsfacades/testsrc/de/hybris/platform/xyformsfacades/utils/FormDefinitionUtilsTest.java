/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.utils;

import static org.spockframework.util.Assert.notNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;


@UnitTest
public class FormDefinitionUtilsTest
{
	@Test
	public void getFormDefinitionContentShouldReturnCorrectResultTest() throws YFormServiceException, IOException
	{
		String correctContent = getContentAsString("/test/data/correct-input.xhtml");

		YFormDefinitionData yFormDefinitionData = new YFormDefinitionData();
		yFormDefinitionData.setApplicationId("ApplicationId");
		yFormDefinitionData.setFormId("FormId");

		String formDefinition = FormDefinitionUtils.getFormDefinitionContent(correctContent, yFormDefinitionData);
		notNull(formDefinition);
	}

	@Test(expected = YFormServiceException.class)
	public void getFormDefinitionContentShouldThrowExceptionTest() throws YFormServiceException, IOException
	{
		YFormDefinitionData yFormDefinitionData = new YFormDefinitionData();
		yFormDefinitionData.setApplicationId("ApplicationId");
		yFormDefinitionData.setFormId("FormId");

		String incorrectContent = getContentAsString("/test/data/vulnerable-input.xhtml");
		FormDefinitionUtils.getFormDefinitionContent(incorrectContent, yFormDefinitionData);
	}

	private String getContentAsString(String filename) throws IOException
	{
		String resource = this.getClass().getResource(filename).getPath();
		return Files.readString(Paths.get(resource));
	}
}
