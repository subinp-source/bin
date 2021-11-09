/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.task;

import de.hybris.platform.apiregistryservices.dao.DestinationTargetDao;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.kymaintegrationservices.dto.CertificateRenewalData;
import de.hybris.platform.kymaintegrationservices.services.CertificateService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Task Runner for certificate renewal
 */
public class KymaCertificateRenewalTaskRunner implements TaskRunner<TaskModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(KymaCertificateRenewalTaskRunner.class);

	private static final String CERTIFICATE_RENEWAL_RETRY_RATIO = "kymaintegrationservices.certificate_renewal_retry_ratio";

	private CertificateService certificateService;
	private ModelService modelService;
	private TaskService taskService;
	private DestinationTargetDao destinationTargetDao;

	@Override
	public void run(final TaskService taskService, final TaskModel task)
	{
		final Date currentExecutionDate = task.getExecutionDate();
		final CertificateRenewalData certificateRenewalData = (CertificateRenewalData) task.getContext();
		final Date expirationDate = certificateRenewalData.getExpiryDate();
		final ConsumedCertificateCredentialModel consumedCertificateCredential = (ConsumedCertificateCredentialModel) task
				.getContextItem();
		final Long aheadTime = certificateRenewalData.getAheadTime();

		try
		{
			getCertificateService().renewCertificate(consumedCertificateCredential);
		}
		catch (final CredentialException e)
		{
			final Date nextExecutionDate = getNextCertificateRetrievalDate(currentExecutionDate, aheadTime);
			if (!nextExecutionDate.after(expirationDate))
			{
				LOG.warn(
						"Certificate renewal failed, a reattempt will happen at '{}'; the current certificate's expiry data is {}. Reason behind the certificate renewal failure was {}",
						nextExecutionDate, expirationDate, e);
				retryScheduleCertificateRetrievalTask(consumedCertificateCredential, nextExecutionDate, expirationDate, aheadTime);
			}
			else
			{
				final DestinationTargetModel destinationTarget = getDestinationTargetDao()
						.findDestinationTargetByCredentialId(consumedCertificateCredential.getId());
				destinationTarget.setRegistrationStatus(RegistrationStatus.ERROR);
				final String errorMessage = String.format(
						"Certificate renewal failed, a re-registration of the destination target with the name {%s} must be performed",
						destinationTarget.getId());
				destinationTarget.setRegistrationStatusInfo(errorMessage);
				getModelService().save(destinationTarget);
				LOG.error(errorMessage);
			}
		}
	}

	protected Date getNextCertificateRetrievalDate(final Date executionDate, final long aheadTime)
	{
		final long aheadTimeTimeStamp = aheadTime / Config.getInt(CERTIFICATE_RENEWAL_RETRY_RATIO, 10);
		final long futureExecutionDateTimeStamp = executionDate.getTime() + aheadTimeTimeStamp;
		return new Date(futureExecutionDateTimeStamp);
	}

	protected void retryScheduleCertificateRetrievalTask(final ConsumedCertificateCredentialModel consumedCertificateCredential, final Date executionDate, final Date expiryDate, final long aheadTime)
	{
		final CertificateRenewalData certificateRenewalData = new CertificateRenewalData();
		certificateRenewalData.setAheadTime(aheadTime);
		certificateRenewalData.setExpiryDate(expiryDate);

		final TaskModel task = getModelService().create(TaskModel.class);
		task.setRunnerBean("kymaCertificateRenewalTaskRunner");
		task.setExecutionDate(executionDate);
		task.setContextItem(consumedCertificateCredential);
		task.setContext(certificateRenewalData);
		getTaskService().scheduleTask(task);
	}

	@Override
	public void handleError(final TaskService taskService, final TaskModel task, final Throwable error)
	{
		LOG.error("Failed to retrieve the certificate with the cause {}", error.getMessage());
		if (LOG.isDebugEnabled())
		{
			LOG.debug(error.getMessage(), error);
		}
	}

	protected CertificateService getCertificateService()
	{
		return certificateService;
	}

	@Required
	public void setCertificateService(final CertificateService certificateService)
	{
		this.certificateService = certificateService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TaskService getTaskService()
	{
		return taskService;
	}

	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}

	protected DestinationTargetDao getDestinationTargetDao()
	{
		return destinationTargetDao;
	}

	@Required
	public void setDestinationTargetDao(final DestinationTargetDao destinationTargetDao)
	{
		this.destinationTargetDao = destinationTargetDao;
	}

}
