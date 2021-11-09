/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataexport.generic.config;

import de.hybris.platform.acceleratorservices.model.export.ExportDataCronJobModel;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;


/**
 * Customized FTP Session factory that configures the FTP session directly (not via spring xml unlike Default) before
 * creating the session.
 */
public class ConfigurableFtpSessionFactory extends DefaultFtpSessionFactory implements ConfigurableSessionFactory<FTPFile>
{
	@Override
	public Session<FTPFile> getSession(final Message<?> message)
	{
		final MessageHeaders headers = message.getHeaders();

		if (headers.containsKey("cronjob"))
		{
			final Object object = headers.get("cronjob");
			if (object instanceof ExportDataCronJobModel)
			{
				final ExportDataCronJobModel cronJobModel = (ExportDataCronJobModel) object;

				//set the session factory settings here
				this.setHost(cronJobModel.getThirdPartyHost());
				this.setUsername(cronJobModel.getThirdPartyUsername());
				this.setPassword(cronJobModel.getThirdPartyPassword());
			}
		}

		return getSession();
	}
}
