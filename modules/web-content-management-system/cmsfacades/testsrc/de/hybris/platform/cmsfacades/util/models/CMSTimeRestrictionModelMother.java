/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSRestrictionDao;
import de.hybris.platform.cmsfacades.util.builder.CMSTimeRestrictionModelBuilder;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;


public class CMSTimeRestrictionModelMother extends AbstractModelMother<CMSTimeRestrictionModel>
{
	public static final String UID_TOMORROW = "uid-tomorrow";
	public static final String NAME_TOMORROW = "name-tomorrow";

	public static final String UID_TODAY = "uid-today";
	public static final String NAME_TODAY = "name-today";

	public static final String UID_NEXT_WEEK = "uid-next-week";
	public static final String NAME_NEXT_WEEK = "name-next-week";

	private CMSRestrictionDao restrictionDao;

	public CMSTimeRestrictionModel today(final CatalogVersionModel catalogVersion)
	{
		return createTodayRestrictionAndAssignToPages(catalogVersion);
	}

	@SuppressWarnings(
			{ "unchecked", "rawtypes" })
	public CMSTimeRestrictionModel createTodayRestrictionAndAssignToPages(final CatalogVersionModel catalogVersion, final AbstractPageModel... pages)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> (Collection) getRestrictionDao().findRestrictionsById(UID_TODAY, catalogVersion), //
				() -> CMSTimeRestrictionModelBuilder.aModel() //
				.withUid(UID_TODAY) //
				.withCatalogVersion(catalogVersion) //
				.withName(NAME_TODAY) //
				.withActiveFrom(DateUtils.truncate(new Date(), Calendar.DATE)) //
				.withActiveUntil(DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1)) //
				.withPages(pages) //
				.build());
	}

	@SuppressWarnings(
			{ "unchecked", "rawtypes" })
	public CMSTimeRestrictionModel createTodayRestrictionAndAssignToComponents(final CatalogVersionModel catalogVersion, final AbstractCMSComponentModel... components)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> (Collection) getRestrictionDao().findRestrictionsById(UID_TODAY, catalogVersion), //
				() -> CMSTimeRestrictionModelBuilder.aModel() //
						.withUid(UID_TODAY) //
						.withCatalogVersion(catalogVersion) //
						.withName(NAME_TODAY) //
						.withActiveFrom(DateUtils.truncate(new Date(), Calendar.DATE)) //
						.withActiveUntil(DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1)) //
						.withComponents(components) //
						.build());
	}

	@SuppressWarnings(
			{ "unchecked", "rawtypes" })
	public CMSTimeRestrictionModel tomorrow(final CatalogVersionModel catalogVersion, final AbstractPageModel... pages)
	{
		final Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return getFromCollectionOrSaveAndReturn( //
				() -> (Collection) getRestrictionDao().findRestrictionsById(UID_TOMORROW, catalogVersion), //
				() -> CMSTimeRestrictionModelBuilder.aModel() //
				.withUid(UID_TOMORROW) //
				.withCatalogVersion(catalogVersion) //
				.withName(NAME_TOMORROW) //
				.withActiveFrom(DateUtils.truncate(cal.getTime(), Calendar.DATE)) //
				.withActiveUntil(DateUtils.addMilliseconds(DateUtils.ceiling(cal.getTime(), Calendar.DATE), -1)) //
				.withPages(pages) //
				.build());
	}

	@SuppressWarnings(
			{ "unchecked", "rawtypes" })
	public CMSTimeRestrictionModel nextWeek(final CatalogVersionModel catalogVersion, final AbstractPageModel... pages)
	{
		final Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, 7);
		return getFromCollectionOrSaveAndReturn( //
				() -> (Collection) getRestrictionDao().findRestrictionsById(UID_NEXT_WEEK, catalogVersion), //
				() -> CMSTimeRestrictionModelBuilder.aModel() //
				.withUid(UID_NEXT_WEEK) //
				.withCatalogVersion(catalogVersion) //
				.withName(NAME_NEXT_WEEK) //
				.withActiveFrom(DateUtils.truncate(cal.getTime(), Calendar.DATE)) //
				.withActiveUntil(DateUtils.addMilliseconds(DateUtils.ceiling(cal.getTime(), Calendar.DATE), -1)) //
				.withPages(pages) //
				.build());
	}

	public CMSRestrictionDao getRestrictionDao()
	{
		return restrictionDao;
	}

	public void setRestrictionDao(final CMSRestrictionDao restrictionDao)
	{
		this.restrictionDao = restrictionDao;
	}
}
