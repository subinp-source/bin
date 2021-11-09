/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors;

import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.Map;

/**
 * Abstract class for visiting {@link AbstractCMSComponentContainerModel} models for the cms synchronization service to work properly.
 * @param <CMSCONTAINERTYPE> the media container type that extends {@link AbstractCMSComponentContainerModel}
 */
public abstract class AbstractCMSComponentContainerModelVisitor<CMSCONTAINERTYPE extends AbstractCMSComponentContainerModel> extends AbstractCMSComponentModelVisitor<CMSCONTAINERTYPE>
{

	@SuppressWarnings("deprecation")
	@Override
	public List<ItemModel> visit(CMSCONTAINERTYPE source, List<ItemModel> path, Map<String, Object> ctx)
	{
		return super.visit(source, path, ctx);
	}

}
