/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedSapproductconfigservicesConstants
{
	public static final String EXTENSIONNAME = "sapproductconfigservices";
	public static class TC
	{
		public static final String CMSCARTCONFIGURATIONRESTRICTION = "CMSCartConfigurationRestriction".intern();
		public static final String CPQCONFIGURATORSETTINGS = "CPQConfiguratorSettings".intern();
		public static final String CPQORDERENTRYPRODUCTINFO = "CPQOrderEntryProductInfo".intern();
		public static final String MOCKVARIANTPRODUCT = "MockVariantProduct".intern();
		public static final String PRODUCTCONFIGURATION = "ProductConfiguration".intern();
		public static final String PRODUCTCONFIGURATIONPERSISTENCECLEANUPCRONJOB = "ProductConfigurationPersistenceCleanUpCronJob".intern();
		public static final String PRODUCTCONFIGURATIONPERSISTENCECLEANUPMODE = "ProductConfigurationPersistenceCleanUpMode".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrderEntry
		{
			public static final String CPQSTATUSSUMMARYMAP = "cpqStatusSummaryMap".intern();
			public static final String PRODUCTCONFIGURATION = "productConfiguration".intern();
			public static final String PRODUCTCONFIGURATIONDRAFT = "productConfigurationDraft".intern();
		}
		public static class ClassificationAttribute
		{
			public static final String CPQMEDIA = "cpqMedia".intern();
		}
		public static class ClassificationAttributeValue
		{
			public static final String CPQMEDIA = "cpqMedia".intern();
			public static final String DESCRIPTION = "description".intern();
		}
		public static class Product
		{
			public static final String PRODUCTCONFIGURATION = "productConfiguration".intern();
		}
		public static class User
		{
			public static final String PRODUCTCONFIGURATION = "productConfiguration".intern();
		}
	}
	public static class Enumerations
	{
		public static class ConfiguratorType
		{
			public static final String CPQCONFIGURATOR = "CPQCONFIGURATOR".intern();
		}
		public static class ProductConfigurationPersistenceCleanUpMode
		{
			public static final String ALL = "All".intern();
			public static final String ONLYPRODUCTRELATED = "OnlyProductRelated".intern();
			public static final String ONLYORPHANED = "OnlyOrphaned".intern();
		}
	}
	public static class Relations
	{
		public static final String PRODUCT2PRODUCTCONFIGS = "Product2ProductConfigs".intern();
		public static final String USER2PRODUCTCONFIGS = "User2ProductConfigs".intern();
	}
	
	protected GeneratedSapproductconfigservicesConstants()
	{
		// private constructor
	}
	
	
}
