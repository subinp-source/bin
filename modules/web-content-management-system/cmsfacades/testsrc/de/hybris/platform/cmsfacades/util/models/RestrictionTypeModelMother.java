/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cmsfacades.util.dao.RestrictionTypeDao;

import org.springframework.beans.factory.annotation.Required;


public class RestrictionTypeModelMother extends AbstractModelMother<RestrictionTypeModel>
{
	protected static final Class<?> SUPER_TYPE = CMSItemModel.class;
	public static final String CODE_CMS_TIME_RESTRICTION = CMSTimeRestrictionModel._TYPECODE;

	private RestrictionTypeDao restrictionTypeDao;

	public RestrictionTypeModel CMSTimeRestriction()
	{
		return restrictionTypeDao.getRestrictionTypeByCode(CODE_CMS_TIME_RESTRICTION);
	}

	public RestrictionTypeDao getRestrictionTypeDao()
	{
		return restrictionTypeDao;
	}

	@Required
	public void setRestrictionTypeDao(final RestrictionTypeDao restrictionTypeDao)
	{
		this.restrictionTypeDao = restrictionTypeDao;
	}

}
