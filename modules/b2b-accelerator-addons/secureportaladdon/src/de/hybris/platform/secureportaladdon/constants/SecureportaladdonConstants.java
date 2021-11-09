/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.constants;


/**
 * Global class for all Secureportaladdon constants. You can add global constants for your extension into this class.
 */
public final class SecureportaladdonConstants extends GeneratedSecureportaladdonConstants // NOSONAR
{
	public static final String EXTENSIONNAME = "secureportaladdon";

	private SecureportaladdonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public final static class UserGroups
	{
		public final static String REGISTRATION_APPROVER_GROUP = "b2bregistrationapprovergroup";
	}

	public final static class Workflows
	{
		public final static String REGISTRATION_WORKFLOW = "B2BUserRegistration";

		public final static class Actions
		{
			public final static String REGISTRATION_APPROVAL = "B2BRegistrationApproval";
		}

		public final static class Decisions
		{
			public final static String REGISTRATION_APPROVED = "B2BRRegistrationApproved";
			public final static String REGISTRATION_REJECTED = "B2BRRegistrationRejected";
		}

	}

	public final static class Processes
	{
		public final static String REGISTRATION_PENDING_APPROVAL = "b2bRegistrationPendingApprovalProcess";
	}

	public final static class EmailTemplates
	{
		public final static String REGISTRATION_PENDING_APPROVAL = "RegistrationPendingApprovalEmailTemplate";
		public final static String REGISTRATION_APPROVED = "RegistrationApprovedEmailTemplate";
		public final static String REGISTRATION_REJECTED = "RegistrationRejectedEmailTemplate";
		public final static String REGISTRATION_RECEIVED = "RegistrationReceivedEmailTemplate";
	}
}
