/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookbackoffice.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class WebhookExceptionTranslationHandlerUnitTest
{
	private final String TARGET_MODEL = "WebhookConfigurationModel";
	
	@Spy
	private WebhookExceptionTranslationHandler exceptionHandler;

	@Before
	public void setUp()
	{
		doReturn("WebhookConfigurationModel").when(exceptionHandler).getConfigParameter();
	}

	@Test
	public void testCanHandle()
	{
		Throwable throwable = new Throwable();
		assertThat(exceptionHandler.canHandle(throwable)).isFalse();

		ObjectSavingException savingException = new ObjectSavingException("test", new Throwable());
		assertThat(exceptionHandler.canHandle(savingException)).isFalse();

		ModelSavingException modelSavingException = new ModelSavingException("This error message is not working");
		assertThat(exceptionHandler.canHandle(modelSavingException)).isFalse();

		ModelSavingException modelSavingException2 = new ModelSavingException(
				String.format("The error message must contain keyword: %s to be caught.", TARGET_MODEL));
		assertThat(exceptionHandler.canHandle(modelSavingException2)).isTrue();

		ObjectSavingException savingException2 = new ObjectSavingException(
				String.format("The error message must contain keyword: %s to be caught.", TARGET_MODEL), new Throwable());
		assertThat(exceptionHandler.canHandle(savingException2)).isFalse();

		ObjectSavingException savingException3 = new ObjectSavingException("test", modelSavingException2);
		assertThat(exceptionHandler.canHandle(savingException3)).isTrue();
	}

	@Test
	public void testToString()
	{
		// toString will be called only when canHandle returns true.

		Throwable throwable = new Throwable();
		assertThat(exceptionHandler.toString(throwable)).isEqualTo(null);

		ObjectSavingException savingException = new ObjectSavingException("test", new Throwable());
		assertThat(exceptionHandler.toString(savingException)).isEqualTo("Object test could not be saved");

		ModelSavingException modelSavingException = new ModelSavingException(
				"If given exception is not supported, its error message will still be output.");
		assertThat(exceptionHandler.toString(modelSavingException)).isEqualTo(
				"If given exception is not supported, its error message will still be output.");

		ModelSavingException modelSavingException2 = new ModelSavingException(
				String.format("The error message must contain keyword %s to be caught.", TARGET_MODEL));
		assertThat(exceptionHandler.toString(modelSavingException2)).isEqualTo(
				String.format("The error message must contain keyword %s to be caught.", TARGET_MODEL));

		// Exception and its cause bother not supported, output throwable's error message
		ObjectSavingException savingException2 = new ObjectSavingException("TestObjectId", new Throwable());
		assertThat(exceptionHandler.toString(savingException2)).isEqualTo("Object TestObjectId could not be saved");

		// Exception's cause is supported but with wrong error message, output throwable's error message
		ObjectSavingException savingException3 = new ObjectSavingException("test", modelSavingException);
		assertThat(exceptionHandler.toString(savingException3)).isEqualTo("Object test could not be saved");

		// Exception and its cause are both supported but with wrong error message, output throwable's error message
		ModelSavingException modelSavingException5 = new ModelSavingException("Wrong error message.", modelSavingException);
		assertThat(exceptionHandler.toString(modelSavingException5)).isEqualTo("Wrong error message.");

		// Exception and its cause are both supported. output error message that contains keyword
		ModelSavingException modelSavingException6 = new ModelSavingException("Wrong error message.", modelSavingException2);
		assertThat(exceptionHandler.toString(modelSavingException6)).isEqualTo(
				String.format("The error message must contain keyword %s to be caught.", TARGET_MODEL));

		// Exception is supported with right error message. output error message that contains keyword
		ModelSavingException modelSavingException7 = new ModelSavingException(
				String.format("The error message must contain keyword %s to be caught.", TARGET_MODEL), modelSavingException);
		assertThat(exceptionHandler.toString(modelSavingException7)).isEqualTo(
				String.format("The error message must contain keyword %s to be caught.", TARGET_MODEL));
	}
}
