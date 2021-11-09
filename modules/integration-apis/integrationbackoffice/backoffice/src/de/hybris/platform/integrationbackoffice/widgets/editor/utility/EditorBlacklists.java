/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.utility;

import java.util.Collections;
import java.util.Set;

/**
 * Blacklists that will be used to exclude certain types from displaying in the UI tool.
 */
public final class EditorBlacklists
{

	private static Set<String> attributeBlackList;
	private static Set<String> typesBlackList;

	private EditorBlacklists()
	{
		throw new IllegalStateException("Utility class");
	}

	public static Set<String> getAttributeBlackList()
	{
		if (attributeBlackList == null)
		{
			final Set<String> blackList = Set.of(
					"allDocuments",
					"assignedCockpitItemTemplates",
					"comments",
					"creationtime",
					"itemtype",
					"modifiedtime",
					"owner",
					"pk",
					"savedValues",
					"sealed",
					"synchronizationSources",
					"synchronizedCopies",
					"classificationIndexString");

			attributeBlackList = Collections.unmodifiableSet(blackList);
		}
		return attributeBlackList;
	}

	public static Set<String> getTypesBlackList()
	{
		if (typesBlackList == null)
		{
			final Set<String> blackList = Set.of(
					"Item",
					"LogFile",
					"Trigger",
					"ItemSyncTimestamp",
					"ProcessTaskLog",
					"CronJob",
					"JobSearchRestriction",
					"Step");
			typesBlackList = Collections.unmodifiableSet(blackList);
		}
		return typesBlackList;
	}

}
