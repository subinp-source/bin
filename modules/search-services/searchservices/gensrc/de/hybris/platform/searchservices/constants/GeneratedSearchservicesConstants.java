/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedSearchservicesConstants
{
	public static final String EXTENSIONNAME = "searchservices";
	public static class TC
	{
		public static final String ABSTRACTSNINDEXERCRONJOB = "AbstractSnIndexerCronJob".intern();
		public static final String ABSTRACTSNINDEXERITEMSOURCE = "AbstractSnIndexerItemSource".intern();
		public static final String ABSTRACTSNSEARCHPROVIDERCONFIGURATION = "AbstractSnSearchProviderConfiguration".intern();
		public static final String FLEXIBLESEARCHSNINDEXERITEMSOURCE = "FlexibleSearchSnIndexerItemSource".intern();
		public static final String FULLSNINDEXERCRONJOB = "FullSnIndexerCronJob".intern();
		public static final String INCREMENTALSNINDEXERCRONJOB = "IncrementalSnIndexerCronJob".intern();
		public static final String SNCOLLECTIONPATTERNCONSTRAINT = "SnCollectionPatternConstraint".intern();
		public static final String SNDOCUMENTOPERATIONSTATUS = "SnDocumentOperationStatus".intern();
		public static final String SNDOCUMENTOPERATIONTYPE = "SnDocumentOperationType".intern();
		public static final String SNFIELD = "SnField".intern();
		public static final String SNFIELDTYPE = "SnFieldType".intern();
		public static final String SNINDEX = "SnIndex".intern();
		public static final String SNINDEXCONFIGURATION = "SnIndexConfiguration".intern();
		public static final String SNINDEXERITEMSOURCEOPERATION = "SnIndexerItemSourceOperation".intern();
		public static final String SNINDEXEROPERATION = "SnIndexerOperation".intern();
		public static final String SNINDEXEROPERATIONSTATUS = "SnIndexerOperationStatus".intern();
		public static final String SNINDEXEROPERATIONTYPE = "SnIndexerOperationType".intern();
		public static final String SNINDEXTYPE = "SnIndexType".intern();
		public static final String SNSYNONYMDICTIONARY = "SnSynonymDictionary".intern();
		public static final String SNSYNONYMENTRY = "SnSynonymEntry".intern();
		public static final String SNWEIGHTCONSTRAINT = "SnWeightConstraint".intern();
	}
	public static class Attributes
	{
		// no constants defined.
	}
	public static class Enumerations
	{
		public static class SnDocumentOperationStatus
		{
			public static final String CREATED = "CREATED".intern();
			public static final String UPDATED = "UPDATED".intern();
			public static final String DELETED = "DELETED".intern();
			public static final String FAILED = "FAILED".intern();
		}
		public static class SnDocumentOperationType
		{
			public static final String CREATE = "CREATE".intern();
			public static final String CREATE_UPDATE = "CREATE_UPDATE".intern();
			public static final String DELETE = "DELETE".intern();
		}
		public static class SnFieldType
		{
			public static final String STRING = "STRING".intern();
			public static final String TEXT = "TEXT".intern();
			public static final String BOOLEAN = "BOOLEAN".intern();
			public static final String INTEGER = "INTEGER".intern();
			public static final String LONG = "LONG".intern();
			public static final String FLOAT = "FLOAT".intern();
			public static final String DOUBLE = "DOUBLE".intern();
			public static final String DATE_TIME = "DATE_TIME".intern();
		}
		public static class SnIndexerOperationStatus
		{
			public static final String RUNNING = "RUNNING".intern();
			public static final String COMPLETED = "COMPLETED".intern();
			public static final String ABORTED = "ABORTED".intern();
			public static final String FAILED = "FAILED".intern();
		}
		public static class SnIndexerOperationType
		{
			public static final String FULL = "FULL".intern();
			public static final String INCREMENTAL = "INCREMENTAL".intern();
		}
	}
	public static class Relations
	{
		public static final String SNINCREMENTALINDEXERCRONJOB2INDEXERITEMSOURCEOPERATION = "SnIncrementalIndexerCronJob2IndexerItemSourceOperation".intern();
		public static final String SNINDEX2INDEXEROPERATION = "SnIndex2IndexerOperation".intern();
		public static final String SNINDEXCONFIGURATION2CURRENCY = "SnIndexConfiguration2Currency".intern();
		public static final String SNINDEXCONFIGURATION2INDEXTYPE = "SnIndexConfiguration2IndexType".intern();
		public static final String SNINDEXCONFIGURATION2LANGUAGE = "SnIndexConfiguration2Language".intern();
		public static final String SNINDEXCONFIGURATION2SEARCHPROVIDERCONFIGURATION = "SnIndexConfiguration2SearchProviderConfiguration".intern();
		public static final String SNINDEXCONFIGURATION2SYNONYMDICTIONARY = "SnIndexConfiguration2SynonymDictionary".intern();
		public static final String SNINDEXTYPE2CATALOG = "SnIndexType2Catalog".intern();
		public static final String SNINDEXTYPE2CATALOGVERSION = "SnIndexType2CatalogVersion".intern();
		public static final String SNINDEXTYPE2FIELD = "SnIndexType2Field".intern();
		public static final String SNINDEXTYPE2INDEXERCRONJOB = "SnIndexType2IndexerCronJob".intern();
		public static final String SNSYNONYMDICTIONARY2SYNONYMENTRY = "SnSynonymDictionary2SynonymEntry".intern();
	}
	
	protected GeneratedSearchservicesConstants()
	{
		// private constructor
	}
	
	
}
