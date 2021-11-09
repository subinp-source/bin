/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;


import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.admin.data.SnCatalogVersion;
import de.hybris.platform.searchservices.admin.data.SnCurrency;
import de.hybris.platform.searchservices.admin.data.SnExpressionInfo;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.data.SnLanguage;
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnFieldTypeRegistry;
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnCommonConfigurationService}.
 */
public class DefaultSnCommonConfigurationService implements SnCommonConfigurationService
{
	protected static final String INDEX_TYPE_ID_PARAM = "indexTypeId";
	protected static final String EXPRESSION_PARAM = "expression";

	private UserService userService;
	private CommonI18NService commonI18NService;
	private CatalogService catalogService;
	private CatalogVersionService catalogVersionService;
	private SnIndexConfigurationService snIndexConfigurationService;
	private SnIndexTypeService snIndexTypeService;
	private SnFieldTypeRegistry snFieldTypeRegistry;

	@Override
	public UserModel getUser(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		if (StringUtils.isBlank(indexType.getIndexConfigurationId()))
		{
			return userService.getAnonymousUser();
		}

		final SnIndexConfiguration indexConfiguration = snIndexConfigurationService
				.getIndexConfigurationForId(indexType.getIndexConfigurationId()).orElseThrow();
		if (indexConfiguration.getUser() == null)
		{
			return userService.getAnonymousUser();
		}

		return userService.getUserForUID(indexConfiguration.getUser());
	}

	@Override
	public List<LanguageModel> getLanguages(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		if (StringUtils.isBlank(indexType.getIndexConfigurationId()))
		{
			return Collections.emptyList();
		}

		final SnIndexConfiguration indexConfiguration = snIndexConfigurationService
				.getIndexConfigurationForId(indexType.getIndexConfigurationId()).orElseThrow();
		if (CollectionUtils.isEmpty(indexConfiguration.getLanguages()))
		{
			return Collections.emptyList();
		}

		return indexConfiguration.getLanguages().stream().map(SnLanguage::getId).map(commonI18NService::getLanguage)
				.collect(Collectors.toList());
	}

	@Override
	public List<CurrencyModel> getCurrencies(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		if (StringUtils.isBlank(indexType.getIndexConfigurationId()))
		{
			return Collections.emptyList();
		}

		final SnIndexConfiguration indexConfiguration = snIndexConfigurationService
				.getIndexConfigurationForId(indexType.getIndexConfigurationId()).orElseThrow();
		if (CollectionUtils.isEmpty(indexConfiguration.getCurrencies()))
		{
			return Collections.emptyList();
		}

		return indexConfiguration.getCurrencies().stream().map(SnCurrency::getId).map(commonI18NService::getCurrency)
				.collect(Collectors.toList());
	}

	@Override
	public List<CatalogVersionModel> getCatalogVersions(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		if (StringUtils.isBlank(indexType.getIndexConfigurationId()))
		{
			return Collections.emptyList();
		}

		if (CollectionUtils.isEmpty(indexType.getCatalogsIds()) && CollectionUtils.isEmpty(indexType.getCatalogVersions()))
		{
			return Collections.emptyList();
		}

		final List<CatalogVersionModel> catalogVersions = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(indexType.getCatalogsIds()))
		{
			for (final String sourceCatalogId : indexType.getCatalogsIds())
			{
				final CatalogModel catalog = catalogService.getCatalogForId(sourceCatalogId);
				catalogVersions.addAll(catalog.getCatalogVersions());
			}
		}

		if (CollectionUtils.isNotEmpty(indexType.getCatalogVersions()))
		{
			for (final SnCatalogVersion sourceCatalogVersion : indexType.getCatalogVersions())
			{
				final CatalogVersionModel catalogVersion = catalogVersionService
						.getCatalogVersion(sourceCatalogVersion.getCatalogId(), sourceCatalogVersion.getVersion());
				catalogVersions.add(catalogVersion);
			}
		}

		return catalogVersions;
	}

	@Override
	public List<SnExpressionInfo> getFacetExpressions(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		return indexType.getFields().values().stream().filter(this::isValidFacetField).map(this::createExpression)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValidFacetExpression(final String indexTypeId, final String expression)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		final SnField field = indexType.getFields().get(expression);
		return isValidFacetField(field);
	}

	protected boolean isValidFacetField(final SnField field)
	{
		if (field == null)
		{
			return false;
		}

		final SnFieldTypeInfo fieldTypeInfo = snFieldTypeRegistry.getFieldTypeInfo(field.getFieldType());
		return fieldTypeInfo.isFacetable();
	}

	@Override
	public List<SnExpressionInfo> getSortExpressions(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		final List<SnExpressionInfo> expressions = indexType.getFields().values().stream().filter(this::isValidSortField)
				.map(this::createExpression).collect(Collectors.toList());

		// adds score and id expressions to the top
		expressions.addAll(0, List.of(createScoreExpression(), createIdExpression()));

		return expressions;
	}

	@Override
	public boolean isValidSortExpression(final String indexTypeId, final String expression)
	{
		if (SearchservicesConstants.ID_FIELD.equals(expression) || SearchservicesConstants.SCORE_FIELD.equals(expression))
		{
			return true;
		}

		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		final SnField field = indexType.getFields().get(expression);
		return isValidSortField(field);
	}

	protected boolean isValidSortField(final SnField field)
	{
		if (field == null || BooleanUtils.isTrue(field.getMultiValued()))
		{
			return false;
		}

		final SnFieldTypeInfo fieldTypeInfo = snFieldTypeRegistry.getFieldTypeInfo(field.getFieldType());
		return fieldTypeInfo.isSortable();
	}

	@Override
	public List<SnExpressionInfo> getGroupExpressions(final String indexTypeId)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		return indexType.getFields().values().stream().filter(this::isValidGroupField).map(this::createExpression)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValidGroupExpression(final String indexTypeId, final String expression)
	{
		final SnIndexType indexType = snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
		final SnField field = indexType.getFields().get(expression);
		return isValidGroupField(field);
	}

	protected boolean isValidGroupField(final SnField field)
	{
		if (field == null || BooleanUtils.isTrue(field.getMultiValued()))
		{
			return false;
		}

		final SnFieldTypeInfo fieldTypeInfo = snFieldTypeRegistry.getFieldTypeInfo(field.getFieldType());
		return fieldTypeInfo.isGroupable();
	}

	protected SnExpressionInfo createExpression(final SnField field)
	{
		final SnExpressionInfo target = new SnExpressionInfo();
		target.setExpression(field.getId());
		target.setName(field.getName());

		return target;
	}

	protected SnExpressionInfo createScoreExpression()
	{
		final SnExpressionInfo target = new SnExpressionInfo();
		target.setExpression(SearchservicesConstants.SCORE_FIELD);

		return target;
	}

	protected SnExpressionInfo createIdExpression()
	{
		final SnExpressionInfo target = new SnExpressionInfo();
		target.setExpression(SearchservicesConstants.ID_FIELD);

		return target;
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

	public CatalogService getCatalogService()
	{
		return catalogService;
	}

	@Required
	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
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

	public SnIndexConfigurationService getSnIndexConfigurationService()
	{
		return snIndexConfigurationService;
	}

	@Required
	public void setSnIndexConfigurationService(final SnIndexConfigurationService snIndexConfigurationService)
	{
		this.snIndexConfigurationService = snIndexConfigurationService;
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

	public SnFieldTypeRegistry getSnFieldTypeRegistry()
	{
		return snFieldTypeRegistry;
	}

	@Required
	public void setSnFieldTypeRegistry(final SnFieldTypeRegistry snFieldTypeRegistry)
	{
		this.snFieldTypeRegistry = snFieldTypeRegistry;
	}
}
