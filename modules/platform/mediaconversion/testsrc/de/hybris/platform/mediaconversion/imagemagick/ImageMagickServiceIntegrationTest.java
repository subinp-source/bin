/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.io.File;
import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author pohl
 */
@ManualTest
public class ImageMagickServiceIntegrationTest extends ServicelayerBaseTest
{

	@Resource
	private ImageMagickService imageMagickService;

	@Test
	public void testSimpleConvert() throws Exception
	{
		final File tmpFile = File.createTempFile("test_", ".gif");
		this.imageMagickService.convert(Arrays.asList("logo:", tmpFile.getAbsolutePath()));
		Assert.assertTrue("File created.", tmpFile.isFile());
		Assert.assertTrue("File exists.", tmpFile.exists());
		Assert.assertTrue("File is readable.", tmpFile.canRead());
		Assert.assertTrue("File is deleted.", tmpFile.delete());
	}
}
