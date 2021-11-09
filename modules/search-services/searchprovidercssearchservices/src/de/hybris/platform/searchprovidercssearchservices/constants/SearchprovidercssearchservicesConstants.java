/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchprovidercssearchservices.constants;

/**
 * Global class for constants.
 */
public final class SearchprovidercssearchservicesConstants
{
	public static final String EXTENSIONNAME = "searchprovidercssearchservices";

	public static final String MEDIA_TYPE_APPLICATION_MERGE_PATCH_JSON = "application/merge-patch+json";

	public static final String PATH_ADMIN = "/admin";

	public static final String PATH_ADMIN_INDEX_CONFIGURATIONS = PATH_ADMIN + "/indexConfigurations";
	public static final String PATH_ADMIN_INDEX_CONFIGURATION = PATH_ADMIN_INDEX_CONFIGURATIONS + "/{indexConfigurationId}";

	public static final String PATH_ADMIN_INDEX_TYPES = PATH_ADMIN + "/indexTypes";
	public static final String PATH_ADMIN_INDEX_TYPE = PATH_ADMIN_INDEX_TYPES + "/{indexTypeId}";

	public static final String PATH_ADMIN_INDEX_TYPE_FIELDS = PATH_ADMIN_INDEX_TYPE + "/fields";
	public static final String PATH_ADMIN_INDEX_TYPE_FIELD = PATH_ADMIN_INDEX_TYPE_FIELDS + "/{fieldId}";

	public static final String PATH_ADMIN_SYNONYM_DICTIONARIES = PATH_ADMIN + "/synonymDictionaries";
	public static final String PATH_ADMIN_SYNONYM_DICTIONARY = PATH_ADMIN_SYNONYM_DICTIONARIES + "/{synonymDictionaryId}";

	public static final String PATH_INDEXES = "/indexes";
	public static final String PATH_INDEX = PATH_INDEXES + "/{indexId}";

	public static final String PATH_INDEXER_OPERATIONS = "/indexer/indexerOperations";
	public static final String PATH_INDEXER_OPERATION = PATH_INDEXER_OPERATIONS + "/{indexerOperationId}";

	public static final String PATH_INDEX_DOCUMENTS = PATH_INDEX + "/documents";
	public static final String PATH_INDEX_DOCUMENTS_BATCH = PATH_INDEX_DOCUMENTS + "/$batch";
	public static final String PATH_INDEX_DOCUMENT = PATH_INDEX_DOCUMENTS + "/{documentId}";

	public static final String PATH_INDEX_COMMIT = PATH_INDEX + "/commit";

	public static final String PATH_INDEX_SEARCH = PATH_INDEX + "/search";
	public static final String PATH_INDEX_SUGGEST = PATH_INDEX + "/suggest";

	public static final String CURRENCY_QUALIFIER_TYPE_ID = "currency";

	public static final String ACCEPT_LANGUAGE_HEADER_KEY = "Accept-Language";
	public static final String ACCEPT_QUALIFIER_HEADER_KEY = "sap-commerce-cloud-accept-qualifier";

	private SearchprovidercssearchservicesConstants()
	{
		//empty
	}
}
