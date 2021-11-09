/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.task;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dao.DestinationTargetDao;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.kymaintegrationservices.dto.CertificateRenewalData;
import de.hybris.platform.kymaintegrationservices.services.CertificateService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;




@IntegrationTest
public class KymaCertificateRenewalTaskRunnerTest extends ServicelayerTransactionalTest
{
	private static final String RETRY_CERTIFICATE_RATIO = "kymaintegrationservices.certificate_renewal_retry_ratio";

	private KymaCertificateRenewalTaskRunner kymaCertificateRenewalTaskRunner;

	@Mock
	private CertificateService certificateService;

	@Mock
	private DestinationTargetDao destinationTargetDao;

	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private TaskService taskService;

	private TaskModel task;

	private ConsumedCertificateCredentialModel consumedCertificateCredential;

	private Date sampleExecutionDate;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		kymaCertificateRenewalTaskRunner = new KymaCertificateRenewalTaskRunner();
		kymaCertificateRenewalTaskRunner.setCertificateService(certificateService);
		kymaCertificateRenewalTaskRunner.setModelService(modelService);
		kymaCertificateRenewalTaskRunner.setTaskService(taskService);
		kymaCertificateRenewalTaskRunner.setDestinationTargetDao(destinationTargetDao);

		consumedCertificateCredential = modelService.create(ConsumedCertificateCredentialModel.class);
		consumedCertificateCredential.setId("test-cert");

		task = kymaCertificateRenewalTaskRunner.getModelService().create(TaskModel.class);
		task.setContextItem(consumedCertificateCredential);
		task.setRunnerBean("kymaCertificateRenewalTaskRunner");

		sampleExecutionDate =  new DateTime().plusDays(2).toDate();
	}

	@Test
	public void certificateRenewedSuccessfully() throws CredentialException
	{
		when(certificateService.renewCertificate(any(ConsumedCertificateCredentialModel.class))).thenReturn(consumedCertificateCredential);

		final CertificateRenewalData certificateRenewalData = new CertificateRenewalData();
		certificateRenewalData.setAheadTime(TimeUnit.DAYS.toMillis(60));
		certificateRenewalData.setExpiryDate(new DateTime(sampleExecutionDate).plusDays(364).toDate());

		task.setExecutionDate(sampleExecutionDate);
		task.setContext(certificateRenewalData);

		kymaCertificateRenewalTaskRunner.run(taskService, task);

		final List<TaskModel> correctTaskModels = findTasks(consumedCertificateCredential);

		assertEquals(0, correctTaskModels.size());
	}

	@Test
	public void certificateRenewalFailed() throws CredentialException
	{
		when(certificateService.renewCertificate(any(ConsumedCertificateCredentialModel.class))).thenThrow(new CredentialException("not accessible"));

		final CertificateRenewalData certificateRenewalData = new CertificateRenewalData();
		certificateRenewalData.setAheadTime(TimeUnit.DAYS.toMillis(60));
		certificateRenewalData.setExpiryDate(new DateTime(sampleExecutionDate).plusDays(364).toDate());

		task.setExecutionDate(sampleExecutionDate);
		task.setContext(certificateRenewalData);

		kymaCertificateRenewalTaskRunner.run(taskService, task);

		final List<TaskModel> correctTaskModels = findTasks(consumedCertificateCredential);
		assertEquals(1, correctTaskModels.size());

		final long aheadTimeStamp = TimeUnit.DAYS.toMillis(60) / Config.getInt(RETRY_CERTIFICATE_RATIO, 6);
		final long futureExecutionDateTimeStamp = sampleExecutionDate.getTime() + aheadTimeStamp;
		final Date futureExecutionDate = new Date(futureExecutionDateTimeStamp);

		final TaskModel taskModel = correctTaskModels.get(0);

		assertEquals(futureExecutionDate, taskModel.getExecutionDate());
	}

	@Test
	public void retryCertificateRenewalAfterExpirationDate() throws CredentialException
	{
		when(certificateService.renewCertificate(any(ConsumedCertificateCredentialModel.class))).thenThrow(new CredentialException("not accessible"));
		final DestinationTargetModel destinationTargetModel = new DestinationTargetModel();
		destinationTargetModel.setId("testingPurpose");
		when(destinationTargetDao.findDestinationTargetByCredentialId(anyString())).thenReturn(destinationTargetModel);

		final CertificateRenewalData certificateRenewalData = new CertificateRenewalData();
		certificateRenewalData.setAheadTime(TimeUnit.DAYS.toMillis(60));
		certificateRenewalData.setExpiryDate(sampleExecutionDate);

		task.setExecutionDate(new DateTime(sampleExecutionDate).plusDays(364).toDate());
		task.setContext(certificateRenewalData);

		kymaCertificateRenewalTaskRunner.run(taskService, task);

		final List<TaskModel> correctTaskModels = findTasks(consumedCertificateCredential);

		assertEquals(0, correctTaskModels.size());
	}

	protected List<TaskModel>findTasks(ConsumedCertificateCredentialModel consumedCertificateCredential)
	{
		final String GET_ALL_TASKS = "select {pk} from {" + TaskModel._TYPECODE + "} ";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_TASKS);
		final SearchResult<TaskModel> result = flexibleSearchService.search(query);

		final List<TaskModel> correctTaskModels = new ArrayList<>();
		final List<TaskModel> numberOfTasks  = result.getResult();

		for (final TaskModel taskModel : numberOfTasks)
		{
			final ItemModel itemModel = taskModel.getContextItem();
			if(itemModel instanceof ConsumedCertificateCredentialModel)
			{
				final ConsumedCertificateCredentialModel consumedCertificateCredentialNew = (ConsumedCertificateCredentialModel) itemModel;
				if(consumedCertificateCredentialNew.equals(consumedCertificateCredential))
				{
					correctTaskModels.add(taskModel);
				}
			}
		}
		return correctTaskModels;
	}

}
