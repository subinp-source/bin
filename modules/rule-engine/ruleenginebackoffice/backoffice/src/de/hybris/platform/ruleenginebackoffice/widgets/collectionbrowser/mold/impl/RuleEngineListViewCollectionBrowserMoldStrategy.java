/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleenginebackoffice.widgets.collectionbrowser.mold.impl;

import static com.google.common.collect.Lists.newArrayList;

import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.ListViewCollectionBrowserMoldStrategy;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listbox;


/**
 * Rule engine specific List view mold strategy for Collection Browser widget
 * <p/>
 * Renders data in simple list form with paging<br>
 * Uses zk {@link Listbox} component
 */
public class RuleEngineListViewCollectionBrowserMoldStrategy extends ListViewCollectionBrowserMoldStrategy
{
	@Override
	protected void onClickItemEvent(final Event event, final Object item)
	{
		getContext().notifyItemsSelected(newArrayList(item));
		getContext().notifyItemClicked(item);
	}

}
