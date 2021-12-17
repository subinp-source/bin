/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedXyformsservicesConstants
{
	public static final String EXTENSIONNAME = "xyformsservices";
	public static class TC
	{
		public static final String YFORMCMSCOMPONENT = "YFormCMSComponent".intern();
		public static final String YFORMDATA = "YFormData".intern();
		public static final String YFORMDATAACTIONENUM = "YFormDataActionEnum".intern();
		public static final String YFORMDATAHISTORY = "YFormDataHistory".intern();
		public static final String YFORMDATATYPEENUM = "YFormDataTypeEnum".intern();
		public static final String YFORMDEFINITION = "YFormDefinition".intern();
		public static final String YFORMDEFINITIONSTATUSENUM = "YFormDefinitionStatusEnum".intern();
	}
	public static class Attributes
	{
		public static class Category
		{
			public static final String ALLYFORMDEFINITIONS = "allYFormDefinitions".intern();
			public static final String YFORMDEFINITIONS = "yFormDefinitions".intern();
		}
		public static class Product
		{
			public static final String ALLYFORMDEFINITIONS = "allYFormDefinitions".intern();
		}
	}
	public static class Enumerations
	{
		public static class YFormDataActionEnum
		{
			public static final String VIEW = "VIEW".intern();
			public static final String EDIT = "EDIT".intern();
			public static final String NEW = "NEW".intern();
		}
		public static class YFormDataTypeEnum
		{
			public static final String DRAFT = "DRAFT".intern();
			public static final String DATA = "DATA".intern();
		}
		public static class YFormDefinitionStatusEnum
		{
			public static final String ENABLED = "ENABLED".intern();
			public static final String DISABLED = "DISABLED".intern();
		}
	}
	public static class Relations
	{
		public static final String CATEGORY2YFORMDEFINITIONRELATION = "Category2YFormDefinitionRelation".intern();
		public static final String YFORMDATA2YFORMDATAHISTORYRELATION = "YFormData2YFormDataHistoryRelation".intern();
		public static final String YFORMDEFINITION2YFORMDATARELATION = "YFormDefinition2YFormDataRelation".intern();
	}
	
	protected GeneratedXyformsservicesConstants()
	{
		// private constructor
	}
	
	
}
