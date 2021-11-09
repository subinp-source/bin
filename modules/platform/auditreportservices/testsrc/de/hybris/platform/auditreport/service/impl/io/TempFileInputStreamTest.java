/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.auditreport.service.impl.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


public class TempFileInputStreamTest
{

	public static final String TEMP_FILE_CONTENT = "I'll be back!";

	@Test
	public void closeShouldDeleteTheTempFile() throws IOException
	{
		//given
		final File tempFile = File.createTempFile(TempFileInputStreamTest.class.getSimpleName(), ".test");
		IOUtils.write(TEMP_FILE_CONTENT, new FileOutputStream(tempFile));
        final StringWriter writer = new StringWriter();

        assertThat(tempFile.exists()).isTrue();

        //when
        try (final TempFileInputStream in = new TempFileInputStream(tempFile))
		{
			IOUtils.copy(in, writer);
		}

        //then
        assertThat(writer.toString()).isEqualTo(TEMP_FILE_CONTENT);
        assertThat(tempFile.exists()).isFalse();
	}
}
