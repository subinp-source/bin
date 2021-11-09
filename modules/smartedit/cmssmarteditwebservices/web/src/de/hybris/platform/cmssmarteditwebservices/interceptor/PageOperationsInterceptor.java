/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Default interceptor to run before controllers' execution to extract the catalogId and sourceCatalogVersion and
 * targetCatalogVersion from the request URI to determine if the current user has required sync permissions.
 */
public class PageOperationsInterceptor extends HandlerInterceptorAdapter
{

	private CatalogVersionService catalogVersionService;
	private UserService userService;
	private CatalogSynchronizationService catalogSynchronizationService;

	/**
	 * Determines if the current user has permissions to sync from source catalog version to target catalog version. In
	 * the case that the user does not have permission, we return an error in the HTTP response using the status code 403
	 * - Forbidden.
	 *
	 * @throws Exception
	 *            <ul>
	 *            <li>{@link UnknownIdentifierException} if no CatalogVersion with the specified catalog id and version
	 *            exists
	 *            <li>{@link AmbiguousIdentifierException} if more than one CatalogVersion is found with the specified
	 *            catalog id and version
	 *            </ul>
	 */
	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{
		final Map<String, String> pathVariables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (Objects.nonNull(pathVariables))
		{
			final String catalog = pathVariables.get(CmssmarteditwebservicesConstants.URI_CATALOG_ID);

			final HashMap<?, ?> bodyData = new ObjectMapper().readValue(request.getInputStream(), HashMap.class);
			final String sourceCatalogVersion = (String) bodyData.get(CmssmarteditwebservicesConstants.URI_SOURCE_CATALOG_VERSION);
			final String targetCatalogVersion = (String) bodyData.get(CmssmarteditwebservicesConstants.URI_TARGET_CATALOG_VERSION);
			if (StringUtils.isNotEmpty(catalog) && StringUtils.isNotEmpty(sourceCatalogVersion)
					&& StringUtils.isNotEmpty(targetCatalogVersion))
			{
				final Optional<SyncItemJobModel> syncItemJobModelOptional = getSyncItemJob(catalog, sourceCatalogVersion,
						targetCatalogVersion);
				final boolean activeJobExists = syncItemJobModelOptional.isPresent();

				if (!activeJobExists)
				{
					response.sendError(HttpStatus.FORBIDDEN.value(), "There are no active sync jobs for the provided details.");
					return false;
				}
				else
				{
					final UserModel principal = getUserService().getCurrentUser();
					final boolean canSync = getCatalogSynchronizationService().canSynchronize(syncItemJobModelOptional.get(),
							principal);
					if (!canSync)
					{
						response.sendError(HttpStatus.FORBIDDEN.value(),
								"The current user does not have sync permissions for the  provided source and target catalog versions to perform this operation.");
						return false;
					}

				}
			}

		}

		return true;
	}

	/**
	 * Returns first active synchronization job from source catalog.
	 *
	 * @param catalogId
	 *           the catalog name
	 * @param sourceCatalgVersion
	 *           the source catalog version name
	 * @param targetCatalgVersion
	 *           the target catalog version name
	 * @return {@link Optional} synchronization job
	 */
	protected Optional<SyncItemJobModel> getSyncItemJob(final String catalogId, final String sourceCatalgVersion,
			final String targetCatalogVersion)
	{
		final CatalogVersionModel sourceCatalog = getCatalogVersionModel(catalogId, sourceCatalgVersion);

		return sourceCatalog.getSynchronizations().stream()
				.filter(syncItemJob -> syncItemJob.getTargetVersion().getVersion().equals(targetCatalogVersion))
				.filter(SyncItemJobModel::getActive).findFirst();
	}

	/**
	 * Gets the catalogVersionModel
	 *
	 * @param catalog
	 *           the catalog name
	 * @param catalogVersion
	 *           the catalog version name
	 * @return the catalogVersionModel
	 */
	protected CatalogVersionModel getCatalogVersionModel(final String catalog, final String catalogVersion)
	{
		return getCatalogVersionService().getCatalogVersion(catalog, catalogVersion);
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
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

	protected CatalogSynchronizationService getCatalogSynchronizationService()
	{
		return catalogSynchronizationService;
	}

	@Required
	public void setCatalogSynchronizationService(final CatalogSynchronizationService catalogSynchronizationService)
	{
		this.catalogSynchronizationService = catalogSynchronizationService;
	}
}
