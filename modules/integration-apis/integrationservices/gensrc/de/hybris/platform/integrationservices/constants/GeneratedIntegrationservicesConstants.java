/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedIntegrationservicesConstants
{
	public static final String EXTENSIONNAME = "integrationservices";
	public static class TC
	{
		public static final String ABSTRACTINTEGRATIONOBJECTITEMATTRIBUTE = "AbstractIntegrationObjectItemAttribute".intern();
		public static final String HTTPMETHOD = "HttpMethod".intern();
		public static final String INTEGRATIONAPIMEDIA = "IntegrationApiMedia".intern();
		public static final String INTEGRATIONOBJECT = "IntegrationObject".intern();
		public static final String INTEGRATIONOBJECTITEM = "IntegrationObjectItem".intern();
		public static final String INTEGRATIONOBJECTITEMATTRIBUTE = "IntegrationObjectItemAttribute".intern();
		public static final String INTEGRATIONOBJECTITEMCLASSIFICATIONATTRIBUTE = "IntegrationObjectItemClassificationAttribute".intern();
		public static final String INTEGRATIONOBJECTITEMVIRTUALATTRIBUTE = "IntegrationObjectItemVirtualAttribute".intern();
		public static final String INTEGRATIONOBJECTVIRTUALATTRIBUTEDESCRIPTOR = "IntegrationObjectVirtualAttributeDescriptor".intern();
		public static final String INTEGRATIONREQUESTSTATUS = "IntegrationRequestStatus".intern();
		public static final String ITEMTYPEMATCHENUM = "ItemTypeMatchEnum".intern();
		public static final String MONITOREDREQUEST = "MonitoredRequest".intern();
		public static final String MONITOREDREQUESTERROR = "MonitoredRequestError".intern();
	}
	public static class Attributes
	{
		// no constants defined.
	}
	public static class Enumerations
	{
		public static class HttpMethod
		{
			public static final String POST = "POST".intern();
			public static final String DELETE = "DELETE".intern();
			public static final String PATCH = "PATCH".intern();
		}
		public static class IntegrationRequestStatus
		{
			public static final String SUCCESS = "SUCCESS".intern();
			public static final String ERROR = "ERROR".intern();
		}
		public static class ItemTypeMatchEnum
		{
			public static final String ALL_SUB_AND_SUPER_TYPES = "ALL_SUB_AND_SUPER_TYPES".intern();
			public static final String RESTRICT_TO_ITEM_TYPE = "RESTRICT_TO_ITEM_TYPE".intern();
			public static final String ALL_SUBTYPES = "ALL_SUBTYPES".intern();
		}
	}
	public static class Relations
	{
		public static final String INTEGOBJ2INTEGOBJITEM = "IntegObj2IntegObjItem".intern();
		public static final String INTEGOBJITEM2CLASSIFICATIONINTEGOBJITEMATTR = "IntegObjItem2ClassificationIntegObjItemAttr".intern();
		public static final String INTEGOBJITEM2INTEGOBJITEMATTR = "IntegObjItem2IntegObjItemAttr".intern();
		public static final String INTEGOBJITEM2VIRTUALINTEGOBJITEMATTR = "IntegObjItem2VirtualIntegObjItemAttr".intern();
	}
	
	protected GeneratedIntegrationservicesConstants()
	{
		// private constructor
	}
	
	
}
