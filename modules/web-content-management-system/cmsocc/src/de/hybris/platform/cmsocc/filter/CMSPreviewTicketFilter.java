/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.filter;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cmsocc.redirect.strategies.PageRedirectStrategy;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter that resolves the preview ticket id from the requested url and activates it.
 */
public class CMSPreviewTicketFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(CMSPreviewTicketFilter.class);

	private CMSPreviewService cmsPreviewService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private BaseSiteService baseSiteService;
	private CatalogVersionService catalogVersionService;
	private SessionService sessionService;
	private TimeService timeService;
	private PageRedirectStrategy pageRedirectStrategy;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		final String previewTicketId = request.getParameter(CMSFilter.PREVIEW_TICKET_ID_PARAM);

		if (StringUtils.isNotBlank(previewTicketId))
		{
			getSessionService().setAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM, previewTicketId);
			final PreviewDataModel previewData = getPreviewDataModel(previewTicketId);

			if (Objects.nonNull(previewData))
			{
				setSiteInSession(previewData.getActiveSite());
				setLanguageInSession(previewData.getLanguage());
				setTimeInSession(previewData.getTime());

				if (Objects.nonNull(previewData.getCatalogVersions()) && !previewData.getCatalogVersions().isEmpty())
				{
					getCatalogVersionService().setSessionCatalogVersions(previewData.getCatalogVersions());
				}

				if (redirectRequest(request, response, previewData))
				{
					// request is redirected, do not invoke the next filter
					return;
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Verifies whether a request needs to be redirected when the preview ticket provides a pageId that would override
	 * the pageId value defined in the request url. If the request needs to be redirected, it will build the redirect url
	 * and perform the redirection.
	 *
	 * @param request
	 *           - the http servlet request
	 * @param response
	 *           - the http servlet response
	 * @param previewData
	 *           - the preview data
	 * @return {@code true} when the request needs to be redirected, otherwise {@code false}
	 */
	protected boolean redirectRequest(final HttpServletRequest request, final HttpServletResponse response,
			final PreviewDataModel previewData)
	{
		if (getPageRedirectStrategy().shouldRedirect(request, previewData))
		{
			final String redirectUrl = getPageRedirectStrategy().getRedirectUrl(request, previewData);
			sendRedirect(response, redirectUrl);
			return true;
		}
		return false;
	}

	/**
	 * Retrieves preview data for a given preview ticket id
	 *
	 * @param previewTicketId
	 *           - preview ticket id
	 * @return preview data model
	 */
	protected PreviewDataModel getPreviewDataModel(final String previewTicketId)
	{
		final CMSPreviewTicketModel previewTicket = getCmsPreviewService().getPreviewTicket(previewTicketId);
		if (previewTicket != null)
		{
			return previewTicket.getPreviewData();
		}
		return null;
	}

	/**
	 * Sends the redirect request given the redirect url
	 *
	 * @param response
	 *           - the current response
	 * @param redirectUrl
	 *           - the redirect url
	 */
	protected void sendRedirect(final HttpServletResponse response, final String redirectUrl)
	{
		if (StringUtils.isNotBlank(redirectUrl))
		{
			try
			{
				response.sendRedirect(response.encodeRedirectURL(redirectUrl));
			}
			catch (final IOException ex)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Exception occurred during redirecting URL", ex);
				}
			}
		}
	}

	/**
	 * Sets the current language in the session
	 *
	 * @param language
	 *           - the language to be set as the current language
	 */
	protected void setLanguageInSession(final LanguageModel language)
	{
		if (Objects.nonNull(language) && !language.equals(getCommerceCommonI18NService().getCurrentLanguage()))
		{
			getCommerceCommonI18NService().setCurrentLanguage(language);

			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Setting %s as the current language in the session", language));
			}
		}
	}

	/**
	 * Sets the current time in the session
	 *
	 * @param time
	 *           - the time to be set as the current time
	 */
	protected void setTimeInSession(final Date time)
	{
		if (Objects.nonNull(time))
		{
			getTimeService().setCurrentTime(time);
			getSessionService().setAttribute(Cms2Constants.PREVIEW_TIME, time);
		}
	}

	/**
	 * Sets the current site in the session
	 *
	 * @param site
	 *           - the site to be set as the current site
	 */
	protected void setSiteInSession(final CMSSiteModel site)
	{
		if (Objects.nonNull(site) && !site.getUid().equals(getBaseSiteService().getCurrentBaseSite().getUid()))
		{
			getBaseSiteService().setCurrentBaseSite(site, false);
		}
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(final CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}

	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
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

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	protected PageRedirectStrategy getPageRedirectStrategy()
	{
		return pageRedirectStrategy;
	}

	@Required
	public void setPageRedirectStrategy(final PageRedirectStrategy pageRedirectStrategy)
	{
		this.pageRedirectStrategy = pageRedirectStrategy;
	}
}