/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel.URL;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator to provide the url value if the related item is of type ContentPageModel, ProductModel or CategoryModel.
 */
public class LinkComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>>
{
	protected static final String PRODUCT_PREFIX = "/p/";
	protected static final String CATEGORY_PREFIX = "/c/";

	private Predicate<CMSItemModel> linkComponentPredicate;

	@Override
	public void populate(final CMSItemModel cmsItemModel, final Map<String, Object> stringObjectMap) throws ConversionException
	{
		// When the url value is already set for external link, do not override it
		// External link could be a full path to an external page or a relative path to another category or product page
		if (getLinkComponentPredicate().test(cmsItemModel) && !stringObjectMap.containsKey(URL))
		{
			final CMSLinkComponentModel linkComponent = (CMSLinkComponentModel) cmsItemModel;
			String url = null;
			if (Objects.nonNull(linkComponent.getContentPage()))
			{
				url = linkComponent.getContentPage().getLabel();
			}
			else if (Objects.nonNull(linkComponent.getProduct()))
			{
				url = PRODUCT_PREFIX + linkComponent.getProduct().getCode();
			}
			else if (Objects.nonNull(linkComponent.getCategory()))
			{
				url = CATEGORY_PREFIX + linkComponent.getCategory().getCode();
			}

			if (Objects.nonNull(url))
			{
				stringObjectMap.put(URL, url);
			}
		}
	}

	protected Predicate<CMSItemModel> getLinkComponentPredicate()
	{
		return linkComponentPredicate;
	}

	@Required
	public void setLinkComponentPredicate(final Predicate<CMSItemModel> linkComponentPredicate)
	{
		this.linkComponentPredicate = linkComponentPredicate;
	}
}
