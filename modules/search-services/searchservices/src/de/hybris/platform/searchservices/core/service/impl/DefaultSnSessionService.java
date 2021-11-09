/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnSessionService}.
 */
public class DefaultSnSessionService implements SnSessionService
{
	protected static final String DISABLE_RESTRICTIONS_ATTRIBUTE = "disableRestrictions";
	protected static final String DISABLE_RESTRICTIONS_GROUP_INHERITANCE_ATTRIBUTE = "disableRestrictionGroupInheritance";

	private SessionService sessionService;
	private UserService userService;
	private I18NService i18nService;
	private CatalogVersionService catalogVersionService;
	private SnCommonConfigurationService snCommonConfigurationService;

	@Override
	public void initializeSession()
	{
		final Session session = sessionService.getCurrentSession();
		final JaloSession jaloSession = (JaloSession) sessionService.getRawSession(session);
		jaloSession.createLocalSessionContext();

		i18nService.setLocalizationFallbackEnabled(true);

		enableSearchRestrictions();
	}

	@Override
	public void initializeSessionForContext(final SnContext context)
	{
		initializeSession();

		final SnIndexType indexType = context.getIndexType();

		final UserModel user = snCommonConfigurationService.getUser(indexType.getId());
		userService.setCurrentUser(user);

		final List<CatalogVersionModel> catalogVersions = snCommonConfigurationService.getCatalogVersions(indexType.getId());
		catalogVersionService.setSessionCatalogVersions(catalogVersions);
	}

	@Override
	public void destroySession()
	{
		final Session session = sessionService.getCurrentSession();
		final JaloSession jaloSession = (JaloSession) sessionService.getRawSession(session);
		jaloSession.removeLocalSessionContext();
	}

	@Override
	public void enableSearchRestrictions()
	{
		sessionService.setAttribute(DISABLE_RESTRICTIONS_ATTRIBUTE, Boolean.FALSE);
		sessionService.setAttribute(DISABLE_RESTRICTIONS_GROUP_INHERITANCE_ATTRIBUTE, Boolean.FALSE);
	}

	@Override
	public void disableSearchRestrictions()
	{
		sessionService.setAttribute(DISABLE_RESTRICTIONS_ATTRIBUTE, Boolean.TRUE);
		sessionService.setAttribute(DISABLE_RESTRICTIONS_GROUP_INHERITANCE_ATTRIBUTE, Boolean.TRUE);
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public SnCommonConfigurationService getSnCommonConfigurationService()
	{
		return snCommonConfigurationService;
	}

	@Required
	public void setSnCommonConfigurationService(final SnCommonConfigurationService snCommonConfigurationService)
	{
		this.snCommonConfigurationService = snCommonConfigurationService;
	}
}
