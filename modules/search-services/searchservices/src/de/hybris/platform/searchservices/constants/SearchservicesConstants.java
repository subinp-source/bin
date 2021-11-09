/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.constants;

import java.util.Set;
import java.util.regex.Pattern;


public class SearchservicesConstants extends GeneratedSearchservicesConstants
{
	public static final String EXTENSIONNAME = "searchservices";

	public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9_\\-]*[A-Za-z0-9]$");

	public static final String ID_FIELD = "_id_";
	public static final String SCORE_FIELD = "_score_";

	public static final String PK_FIELD = "pk";
	public static final String CATALOG_VERSION_FIELD = "catalogVersion";

	public static final String LANGUAGE_QUALIFIER_TYPE = "language";
	public static final String CURRENCY_QUALIFIER_TYPE = "currency";

	public static final String INDEXER_ITEM_SOURCE_PARAM_START_TIME = "startTime";

	public static final String TERM_BUCKETS_FACET_SORT_COUNT_DESC = "_count_desc_";
	public static final String TERM_BUCKETS_FACET_SORT_ID_ASC = "_id_asc_";
	public static final String TERM_BUCKETS_FACET_SORT_ID_DESC = "_id_desc_";
	public static final String TERM_BUCKETS_FACET_SORT_NAME_ASC = "_name_asc_";
	public static final String TERM_BUCKETS_FACET_SORT_NAME_DESC = "_name_desc_";

	public static final Set<String> TERM_BUCKETS_FACET_SORTS = Set.of(TERM_BUCKETS_FACET_SORT_COUNT_DESC,
			TERM_BUCKETS_FACET_SORT_ID_ASC, TERM_BUCKETS_FACET_SORT_ID_DESC, TERM_BUCKETS_FACET_SORT_NAME_ASC,
			TERM_BUCKETS_FACET_SORT_NAME_DESC);

	private SearchservicesConstants()
	{
		//empty
	}
}
