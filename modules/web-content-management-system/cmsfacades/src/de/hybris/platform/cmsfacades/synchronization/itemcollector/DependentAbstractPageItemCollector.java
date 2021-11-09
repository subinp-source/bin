/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemcollector;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.common.itemcollector.ItemCollector;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;


/**
 * Collects the original page dependency of a given {@link AbstractPageModel}.
 */
public class DependentAbstractPageItemCollector implements ItemCollector<AbstractPageModel>
{

	@Override
	public List<? extends ItemModel> collect(final AbstractPageModel item)
	{
		return item.getOriginalPage() != null ? singletonList(item.getOriginalPage()) : emptyList();
	}

}
