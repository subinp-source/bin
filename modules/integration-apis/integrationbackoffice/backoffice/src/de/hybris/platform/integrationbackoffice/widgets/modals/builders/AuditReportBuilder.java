/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.builders;

import de.hybris.platform.core.model.ItemModel;

import java.io.InputStream;
import java.util.Map;

public interface AuditReportBuilder
{
	Map<String, InputStream> generateAuditReport(ItemModel itemModel);

	void downloadAuditReport(Map<String, InputStream> files);
}

