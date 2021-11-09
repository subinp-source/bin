/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cmsfacades.util.builder.CatalogVersionSyncJobModelBuilder;
import de.hybris.platform.cmsfacades.util.dao.SyncJobDao;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_SYNC_USER_ID;


public class CatalogVersionSyncJobModelMother extends AbstractModelMother<CatalogVersionSyncJobModel>
{
	public static final String PHONE_SYNC_JOB = "phoneSyncJob";
	public static final String APPLE_SYNC_JOB = "appleSyncJob";

	private SyncJobDao syncJobDao;
	private UserService userService;
	private TypeService typeService;
	private CatalogVersionModelMother catalogVersionModelMother;

	public CatalogVersionSyncJobModel createSyncJob(
			final String code,
			final CatalogVersionModel target,
			final CatalogVersionModel source,
			final boolean active,
			final boolean syncPrincipalsOnly,
			final List<PrincipalModel> syncPrincipals,
			final UserModel sessionUser)
	{
		return getFromCollectionOrSaveAndReturn(() -> {
			return getSyncJobDao().getSyncJobsByCode(code);
		}, () -> {
			return CatalogVersionSyncJobModelBuilder.aModel() //
					.withActive(active) //
					.withSourceCatalogVersion(source) //
					.withTargetCatalogVersion(target) //
					.withSyncPrincipals(syncPrincipals) //
					.withSyncPrincipalsOnly(syncPrincipalsOnly) //
					.withSessionUser(sessionUser) //
					.withRootTypes(Arrays.asList(
							getType(CMSItemModel.class),
							getType(CMSRelationModel.class)
					))
					.build();
		});
	}

	public CatalogVersionSyncJobModel createPhoneSyncJobFromOnlineToStaged()
	{
		return createSyncJob(PHONE_SYNC_JOB, getOnlinePhoneCatalog(), getStagedPhoneCatalog(), true, false,
				Collections.emptyList(), null);
	}

	public CatalogVersionSyncJobModel createAppleSyncJobFromStagedToOnline()
	{
		final UserModel cmsSyncUser = getUserService().getUserForUID(CMS_SYNC_USER_ID);

		return createSyncJob(APPLE_SYNC_JOB, getOnlineAppleCatalog(), getStagedAppleCatalog(), true, false,
				Collections.emptyList(), cmsSyncUser);
	}

	protected CatalogVersionModel getOnlinePhoneCatalog()
	{
		return catalogVersionModelMother.createPhoneOnlineCatalogVersionModel();
	}

	protected CatalogVersionModel getStagedPhoneCatalog()
	{
		return catalogVersionModelMother.createPhoneStaged1CatalogVersionModel();
	}

	protected CatalogVersionModel getOnlineAppleCatalog()
	{
		return catalogVersionModelMother.createAppleOnlineCatalogVersionModel();
	}

	protected CatalogVersionModel getStagedAppleCatalog()
	{
		return catalogVersionModelMother.createAppleStagedCatalogVersionModel();
	}

	protected ComposedTypeModel getType(final Class clazz)
	{
		return typeService.getComposedTypeForClass(clazz);
	}

	protected SyncJobDao getSyncJobDao()
	{
		return syncJobDao;
	}

	@Required
	public void setSyncJobDao(final SyncJobDao syncJobDao)
	{
		this.syncJobDao = syncJobDao;
	}

	protected CatalogVersionModelMother getCatalogVersionModelMother()
	{
		return catalogVersionModelMother;
	}

	@Required
	public void setCatalogVersionModelMother(CatalogVersionModelMother catalogVersionModelMother)
	{
		this.catalogVersionModelMother = catalogVersionModelMother;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
