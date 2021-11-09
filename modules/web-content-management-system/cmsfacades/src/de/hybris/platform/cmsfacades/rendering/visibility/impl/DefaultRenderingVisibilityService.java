/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.visibility.impl;

import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityRule;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityService;
import de.hybris.platform.core.model.ItemModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Objects;


/**
 * Default implementation of {@link RenderingVisibilityService}.
 */
public class DefaultRenderingVisibilityService implements RenderingVisibilityService
{
	private List<RenderingVisibilityRule<ItemModel>> renderingVisibilityRules;

	@Override
	public boolean isVisible(ItemModel itemModel)
	{
		if (Objects.isNull(itemModel))
		{
			return false;
		}
		else
		{
			return getRenderingVisibilityRules()
					.stream()
					.filter(visibility -> visibility.restrictedBy().test(itemModel))
					.findFirst()
					.map(visibility -> visibility.isVisible(itemModel))
					.orElse(true);
		}
	}

	protected List<RenderingVisibilityRule<ItemModel>> getRenderingVisibilityRules()
	{
		return renderingVisibilityRules;
	}

	@Required
	public void setRenderingVisibilityRules(
			List<RenderingVisibilityRule<ItemModel>> renderingVisibilityRules)
	{
		this.renderingVisibilityRules = renderingVisibilityRules;
	}
}
