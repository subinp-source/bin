/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.SnRuntimeException;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchRunnable;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchStrategy;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchStrategyFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnIndexerBatchRunnable}.
 */
public class DefaultSnIndexerBatchRunnable implements SnIndexerBatchRunnable
{
	private TenantService tenantService;
	private SessionService sessionService;
	private UserService userService;
	private CommonI18NService commonI18NService;
	private SnIndexTypeService snIndexTypeService;
	private SnIndexerBatchStrategyFactory snIndexerBatchStrategyFactory;

	private String tenantId;
	private String sessionUser;
	private String sessionLanguage;
	private String sessionCurrency;
	private String indexTypeId;
	private String indexId;
	private SnIndexerOperationType indexerOperationType;
	private List<SnIndexerItemSourceOperation> indexerItemSourceOperations;
	private String indexerOperationId;
	private String indexerBatchId;

	private SnIndexerBatchResponse indexerBatchResponse;

	@Override
	public void initialize(final SnIndexerContext indexerContext,
			final List<SnIndexerItemSourceOperation> indexerItemSourceOperations, final String indexerBatchId)
	{
		tenantId = tenantService.getCurrentTenantId();

		final UserModel user = userService.getCurrentUser();
		if (user != null)
		{
			sessionUser = user.getUid();
		}

		final LanguageModel language = commonI18NService.getCurrentLanguage();
		if (language != null)
		{
			sessionLanguage = language.getIsocode();
		}

		final CurrencyModel currency = commonI18NService.getCurrentCurrency();
		if (currency != null)
		{
			sessionCurrency = currency.getIsocode();
		}

		this.indexTypeId = indexerContext.getIndexType().getId();
		this.indexId = indexerContext.getIndexId();
		this.indexerOperationType = indexerContext.getIndexerOperationType();
		this.indexerItemSourceOperations = indexerItemSourceOperations;
		this.indexerOperationId = indexerContext.getIndexerOperationId();
		this.indexerBatchId = indexerBatchId;
	}

	@Override
	public void run()
	{
		try
		{
			initializeSession();

			final DefaultSnIndexerBatchRequest indexerBatchRequest = new DefaultSnIndexerBatchRequest(indexTypeId, indexId,
					indexerOperationType, indexerItemSourceOperations, indexerOperationId, indexerBatchId);
			final SnIndexerBatchStrategy indexerBatchStrategy = snIndexerBatchStrategyFactory
					.getIndexerBatchStrategy(indexerBatchRequest);
			indexerBatchResponse = indexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnException e)
		{
			throw new SnRuntimeException(e);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		finally
		{
			destroySession();
		}
	}

	protected void initializeSession()
	{
		final Tenant tenant = Registry.getTenantByID(tenantId);
		Registry.setCurrentTenant(tenant);

		sessionService.createNewSession();

		final UserModel user = userService.getUserForUID(sessionUser);
		userService.setCurrentUser(user);

		final LanguageModel language = commonI18NService.getLanguage(sessionLanguage);
		commonI18NService.setCurrentLanguage(language);

		final CurrencyModel currency = commonI18NService.getCurrency(sessionCurrency);
		commonI18NService.setCurrentCurrency(currency);
	}

	protected void destroySession()
	{
		sessionService.closeCurrentSession();
		Registry.unsetCurrentTenant();
	}

	protected String getTaskName()
	{
		return "Indexer Batch: indexerBatchId=" + indexerBatchId;
	}

	protected String getTenantId()
	{
		return tenantId;
	}

	protected void setTenantId(final String tenantId)
	{
		this.tenantId = tenantId;
	}

	protected String getSessionUser()
	{
		return sessionUser;
	}

	protected void setSessionUser(final String sessionUser)
	{
		this.sessionUser = sessionUser;
	}

	protected String getSessionLanguage()
	{
		return sessionLanguage;
	}

	protected void setSessionLanguage(final String sessionLanguage)
	{
		this.sessionLanguage = sessionLanguage;
	}

	protected String getSessionCurrency()
	{
		return sessionCurrency;
	}

	protected void setSessionCurrency(final String sessionCurrency)
	{
		this.sessionCurrency = sessionCurrency;
	}

	protected String getIndexTypeId()
	{
		return indexTypeId;
	}

	protected void setIndexTypeId(final String indexTypeId)
	{
		this.indexTypeId = indexTypeId;
	}

	protected SnIndexerOperationType getIndexerOperationType()
	{
		return indexerOperationType;
	}

	protected void setIndexerOperationType(final SnIndexerOperationType indexerOperationType)
	{
		this.indexerOperationType = indexerOperationType;
	}

	protected List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations()
	{
		return indexerItemSourceOperations;
	}

	protected void setIndexerItemSourceOperations(final List<SnIndexerItemSourceOperation> indexerItemSourceOperations)
	{
		this.indexerItemSourceOperations = indexerItemSourceOperations;
	}

	protected String getIndexId()
	{
		return indexId;
	}

	protected void setIndexId(final String indexId)
	{
		this.indexId = indexId;
	}

	protected String getIndexerOperationId()
	{
		return indexerOperationId;
	}

	protected void setIndexerOperationId(final String indexerOperationId)
	{
		this.indexerOperationId = indexerOperationId;
	}

	protected String getIndexerBatchId()
	{
		return indexerBatchId;
	}

	protected void setIndexerBatchId(final String indexerBatchId)
	{
		this.indexerBatchId = indexerBatchId;
	}

	@Override
	public SnIndexerBatchResponse getIndexerBatchResponse()
	{
		return indexerBatchResponse;
	}

	protected void setIndexerBatchResponse(final SnIndexerBatchResponse indexerBatchResponse)
	{
		this.indexerBatchResponse = indexerBatchResponse;
	}

	public TenantService getTenantService()
	{
		return tenantService;
	}

	@Required
	public void setTenantService(final TenantService tenantService)
	{
		this.tenantService = tenantService;
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

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public SnIndexTypeService getSnIndexTypeService()
	{
		return snIndexTypeService;
	}

	@Required
	public void setSnIndexTypeService(final SnIndexTypeService snIndexTypeService)
	{
		this.snIndexTypeService = snIndexTypeService;
	}

	public SnIndexerBatchStrategyFactory getSnIndexerBatchStrategyFactory()
	{
		return snIndexerBatchStrategyFactory;
	}

	@Required
	public void setSnIndexerBatchStrategyFactory(final SnIndexerBatchStrategyFactory snIndexerBatchStrategyFactory)
	{
		this.snIndexerBatchStrategyFactory = snIndexerBatchStrategyFactory;
	}
}
