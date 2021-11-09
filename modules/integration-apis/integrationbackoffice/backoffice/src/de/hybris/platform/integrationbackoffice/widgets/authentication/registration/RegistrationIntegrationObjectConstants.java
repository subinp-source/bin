/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.registration;

/**
 * Class to provide all the constants for integration object registration within its package
 */
class RegistrationIntegrationObjectConstants
{
	static final String EXPOSED_DESTINATION_URL = "{ccv2.services.backoffice.url.0}/odata2webservices/%s";
	static final String ENDPOINT_URL = "{ccv2.services.backoffice.url.0}/odata2webservices/%s/$metadata";
	static final String CALLBACK_URL = "{ccv2.services.api.url.0}/oauth2_implicit_callback";
	static final String OAUTH_URL = "{ccv2.services.api.url.0}/authorizationserver/oauth/token";
	static final String RESOURCES = "integrationbackoffice.client.credentials.resources";
	static final String AUTHORITIES = "integrationbackoffice.client.credentials.authorities";

	static final String ICC_CREDENTIAL_OAUTH = "icc-oauth-";
	static final String ICC_CREDENTIAL_BASIC = "icc-basic-";
	static final String ICC_BASIC_SOCKET_IN = "iccBasic";
	static final String ICC_BASIC_SOCKET_OUT = "iccBasicOutput";
	static final String ICC_OAUTH_SOCKET_IN = "iccOAuth";
	static final String ICC_OAUTH_SOCKET_OUT = "iccOAuthOutput";
	static final String REGISTER_BUTTON = "registerIntegrationObject";

	static final String EXPOSED_DESTINATION_PREFIX = "cc-";
	static final String ENDPOINT_SUFFIX = " Integration API";
	static final String ENDPOINT_VERSION = "v1";
	static final String METADATA = "-metadata";

	static final String OBJECTS_UPDATED_EVENT = "objectsUpdated";
	static final String OBJECTS_UPDATED_EVENT_TYPE = "updatedObjectIsNew";
	static final String NOTIFICATION_TYPE = "JustMessage";

	static final String VALIDATE_LABEL = "validate.input";
	static final String MANDATORY_INPUT = "mandatory.input";
	static final String SELECT_ONLY_ONE = "select.one.option";
	static final String REQUIRED_FILED = "required.fields";
	static final String REQUIRED_DESTINATION = "empty.destination.target";
	static final String SUCCESS_MESSAGE = "exposed.destination.created";

	static final String QUERY = "SELECT {%s} FROM {%s}";
	static final String DELIMITER = ",";
	static final String EMPTY_STRING = "";

	static final String VALID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+{}[]|:;<>?,./";
	static final int DEFAULT_PASSWORD_LENGTH = 12;
	static final int MESSAGE_LENGTH = 2;

	private RegistrationIntegrationObjectConstants()
	{
		//Add private constructor
	}
}