/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.update;

import static de.hybris.platform.core.initialization.SystemSetup.Process.UPDATE;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = IntegrationservicesConstants.EXTENSIONNAME)
public class RootItemSystemUpdate
{
	private static final Logger LOG = Log.getLogger(RootItemSystemUpdate.class);
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;

	@SystemSetup(process = UPDATE)
	public void updateNullRootItems()
	{
		LOG.info("Setting IntegrationObjectItem.root=false for all existing IntegrationObjectItems without a set root item.");

		final List<IntegrationObjectItemModel> nullRootItems = findIOIsWithNullRoot();
		nullRootItems.forEach(i -> i.setRoot(false));
		modelService.saveAll(nullRootItems);
	}

	private List<IntegrationObjectItemModel> findIOIsWithNullRoot()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {ioi." + ItemModel.PK +"} " +
				"FROM {"+ IntegrationObjectItemModel._TYPECODE + " AS ioi} " +
				"WHERE {ioi." + IntegrationObjectItemModel.ROOT + "} IS NULL");
		return flexibleSearchService.<IntegrationObjectItemModel>search(query).getResult();
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
