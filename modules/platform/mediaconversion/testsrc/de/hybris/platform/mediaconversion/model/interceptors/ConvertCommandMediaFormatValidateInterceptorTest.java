/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.model.interceptors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;

@IntegrationTest
public class ConvertCommandMediaFormatValidateInterceptorTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Test
	public void shouldConsiderModelValidIfConversionCommandIsNull()
	{
		// given
		final ConversionMediaFormatModel mediaFormat = modelService.create(ConversionMediaFormatModel.class);
		mediaFormat.setQualifier("test");
		mediaFormat.setConversion(null);

		// when
		modelService.save(mediaFormat);

		// then
		assertThat(modelService.isNew(mediaFormat)).isFalse();
	}

	@Test
	public void shouldConsiderModelValidIfConversionCommandIsEmpty()
	{
		// given
		final ConversionMediaFormatModel mediaFormat = modelService.create(ConversionMediaFormatModel.class);
		mediaFormat.setQualifier("test");
		mediaFormat.setConversion("  ");

		// when
		modelService.save(mediaFormat);

		// then
		assertThat(modelService.isNew(mediaFormat)).isFalse();
	}

	@Test
	public void shouldConsiderModelValidIfConversionCommandDoesNotContainBlacklistedWords()
	{
		// given
		final ConversionMediaFormatModel mediaFormat = modelService.create(ConversionMediaFormatModel.class);
		mediaFormat.setQualifier("test");
		mediaFormat.setConversion("-thumbnail \"20x10\"");

		// when
		modelService.save(mediaFormat);

		// then
		assertThat(modelService.isNew(mediaFormat)).isFalse();
	}

	@Test
	public void shouldConsiderModelInvalidIfConversionCommandContainsBlacklistedWords()
	{
		// given
		final ConversionMediaFormatModel mediaFormat = modelService.create(ConversionMediaFormatModel.class);
		mediaFormat.setQualifier("test");
		mediaFormat.setConversion("-thumbnail \"20x10\" -clip-mask");

		try
		{
			// when
			modelService.save(mediaFormat);
			fail("should throw ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// then fine
		}
	}


}
