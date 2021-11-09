/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.registration;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel;

import java.util.List;

/**
 * This interface provides the functionality to create an input channel configuration ICC from an integration object IO
 * and to add the IO to destination targets with Basic or OAuth credentials.
 */
public interface RegisterIntegrationObjectService
{
	/**
	 * For the given ICC create the exposed destinations
	 *
	 * @param destinationTargets   given destination targets
	 * @param inboundChannelConfig given ICC
	 * @param credential           given credentials to be used for the exposed destination
	 */
	void createExposedDestinations(List<DestinationTargetModel> destinationTargets,
	                               InboundChannelConfigurationModel inboundChannelConfig,
	                               AbstractCredentialModel credential);

	/**
	 * Create exposed OAuth credential for the exposed destination
	 *
	 * @param integrationCCD given integration CCD
	 */
	void createExposedOAuthCredential(IntegrationClientCredentialsDetailsModel integrationCCD);

	/**
	 * Create basic credential based on the an existing employee
	 *
	 * @param username given employee ID
	 * @param password given employee password
	 * @return returns associated basic credential with the employee
	 */
	AbstractCredentialModel createBasicCredential(String username, String password);

	/**
	 * Create OAuth credential based on the an existing employee
	 *
	 * @param employee given employee
	 * @return returns associated OAuth credential with the employee
	 */
	AbstractCredentialModel createOAuthCredential(EmployeeModel employee);

	/**
	 * Read destination targets
	 *
	 * @return destination targets
	 */
	List<DestinationTargetModel> readDestinationTargets();

	/**
	 * Read basic credentials
	 *
	 * @return basic credentials
	 */
	List<BasicCredentialModel> readBasicCredentials();

	/**
	 * Read exposed OAuth credentials
	 *
	 * @return exposed OAuth credentials
	 */
	List<ExposedOAuthCredentialModel> readExposedOAuthCredentials();

	/**
	 * Read employees
	 *
	 * @return employees
	 */
	List<EmployeeModel> readEmployees();
}
