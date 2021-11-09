/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

import java.util.function.Predicate;


/**
 * Predicate to verify that CMSItemModel contains two fields (external and urlLink) at the same time.
 */
public class ModelContainsLinkTogglePredicate implements Predicate<CMSItemModel>
{
	private TypeService typeService;
	private Predicate<ComposedTypeModel> cmsComposedTypeContainsLinkTogglePredicate;

	@Override
	public boolean test(CMSItemModel cmsItemModel)
	{
		ComposedTypeModel composedTypeModel = getTypeService().getComposedTypeForClass(cmsItemModel.getClass());
		return getCmsComposedTypeContainsLinkTogglePredicate().test(composedTypeModel);
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected Predicate<ComposedTypeModel> getCmsComposedTypeContainsLinkTogglePredicate()
	{
		return cmsComposedTypeContainsLinkTogglePredicate;
	}

	@Required
	public void setCmsComposedTypeContainsLinkTogglePredicate(
			Predicate<ComposedTypeModel> cmsComposedTypeContainsLinkTogglePredicate)
	{
		this.cmsComposedTypeContainsLinkTogglePredicate = cmsComposedTypeContainsLinkTogglePredicate;
	}
}
