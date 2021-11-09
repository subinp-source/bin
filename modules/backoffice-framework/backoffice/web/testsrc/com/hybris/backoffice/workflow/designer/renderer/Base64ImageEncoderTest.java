/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static org.assertj.core.api.Assertions.assertThat;

import com.hybris.backoffice.workflow.designer.renderer.Base64ImageEncoder;
import org.junit.Test;


public class Base64ImageEncoderTest
{
	@Test
	public void shouldEncodeToImageBase64()
	{
		// given
		final String imageAsStringToEncode = "imageAsStringToEncode";
		final String imageAsStringBase64Encoded = "aW1hZ2VBc1N0cmluZ1RvRW5jb2Rl";
		final Base64ImageEncoder base64ImageEncoder = new Base64ImageEncoder();

		// when
		final String result = base64ImageEncoder.encode(imageAsStringToEncode);

		// then
		assertThat(result).isEqualTo("data:image/svg+xml;base64," + imageAsStringBase64Encoded);
	}
}
