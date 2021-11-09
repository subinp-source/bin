/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.filter.ExpressionVisitorParameters.parametersBuilder;
import static de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest.itemLookupRequestBuilder;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.search.OrderExpression;
import de.hybris.platform.integrationservices.search.SimplePropertyWhereClauseCondition;
import de.hybris.platform.integrationservices.search.WhereClauseCondition;
import de.hybris.platform.integrationservices.search.WhereClauseConditions;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.config.ODataServicesConfiguration;
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException;
import de.hybris.platform.odata2services.converter.ODataEntryToIntegrationItemConverter;
import de.hybris.platform.odata2services.filter.ExpressionVisitorFactory;
import de.hybris.platform.odata2services.filter.ExpressionVisitorParameters;
import de.hybris.platform.odata2services.filter.NoFilterResultException;
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator;
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidIntegrationKeyException;
import de.hybris.platform.odata2services.odata.persistence.lookup.InvalidQueryParameterException;
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.olingo.odata2.api.commons.HttpHeaders;
import org.apache.olingo.odata2.api.commons.InlineCount;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.UriInfo;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;
import org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor;
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@link ItemLookupRequestFactory}
 */
public class DefaultItemLookupRequestFactory implements ItemLookupRequestFactory
{
	private static final Logger LOG = Log.getLogger(DefaultItemLookupRequestFactory.class);

	private IntegrationKeyToODataEntryGenerator integrationKeyToODataEntryGenerator;
	private ODataContextLanguageExtractor localeExtractor;
	private ServiceNameExtractor serviceNameExtractor;
	private ODataServicesConfiguration oDataServicesConfiguration;
	private ExpressionVisitorFactory expressionVisitorFactory;
	private ODataEntryToIntegrationItemConverter entryConverter;
	private ItemTypeDescriptorService descriptorService;

	@Override
	public ItemLookupRequest create(final UriInfo uriInfo, final ODataContext context, final String contentType)
	{
		try
		{
			final TypeDescriptor typeDescriptor = toTypeDescriptor(context, uriInfo);
			final ItemLookupRequest.ItemLookupRequestBuilder builder = hasKey(uriInfo) ?
					createWithIntegrationKey(typeDescriptor, uriInfo, context, contentType) :
					createBasic(typeDescriptor, uriInfo, context, contentType);
			return builder.build();
		}
		catch (final ODataException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	@Override
	public ItemLookupRequest create(final ODataContext context, final EdmEntitySet edmEntitySet, final ODataEntry oDataEntry,
	                                final String integrationKey) throws EdmException
	{
		final Locale locale = getLocaleExtractor().extractFrom(context, HttpHeaders.ACCEPT_LANGUAGE);
		final TypeDescriptor typeDescriptor = toTypeDescriptor(context, edmEntitySet);
		final IntegrationItem item = convert(typeDescriptor, context, oDataEntry);
		return itemLookupRequestBuilder()
				.withAcceptLocale(locale)
				.withIntegrationObject(item.getIntegrationObjectCode())
				.withEntitySet(edmEntitySet)
				.withODataEntry(oDataEntry)
				.withIntegrationItem(item)
				.build();
	}

	@Override
	public ItemLookupRequest create(final ODataContext context, final EdmEntitySet edmEntitySet,
	                                final Pair<String, String> attribute) throws EdmException
	{
		final WhereClauseCondition condition = SimplePropertyWhereClauseCondition.eq(attribute.getKey(), attribute.getValue());
		return createWithFilter(context, edmEntitySet, new WhereClauseConditions(condition));
	}

	@Override
	public @NotNull ItemLookupRequest createWithFilter(final ODataContext context, final EdmEntitySet entitySet,
	                                                   final WhereClauseConditions filter)
			throws EdmException
	{
		final Locale locale = getLocaleExtractor().extractFrom(context, HttpHeaders.ACCEPT_LANGUAGE);
		final TypeDescriptor typeDescriptor = toTypeDescriptor(context, entitySet);
		return itemLookupRequestBuilder()
				.withAcceptLocale(locale)
				.withIntegrationObject(getServiceNameFromContext(context))
				.withEntitySet(entitySet)
				.withTypeDescriptor(typeDescriptor)
				.withTop(getODataServicesConfiguration().getDefaultPageSize())
				.withFilter(filter)
				.build();
	}

	@Override
	public ItemLookupRequest create(final DeleteUriInfo uriInfo, final ODataContext context)
	{
		final Locale locale = getLocaleExtractor().extractFrom(context, HttpHeaders.ACCEPT_LANGUAGE);
		final EdmEntitySet entitySet = uriInfo.getStartEntitySet();
		final List<KeyPredicate> keyPredicates = uriInfo.getKeyPredicates();

		try
		{
			final TypeDescriptor typeDescriptor = toTypeDescriptor(context, uriInfo);
			final ODataEntry oDataEntry = getIntegrationKeyToODataEntryGenerator().generate(entitySet, keyPredicates);
			final IntegrationItem item = convert(typeDescriptor, context, oDataEntry);
			return itemLookupRequestBuilder()
					.withAcceptLocale(locale)
					.withIntegrationObject(getServiceNameFromContext(context))
					.withODataEntry(oDataEntry)
					.withEntitySet(entitySet)
					.withIntegrationItem(item)
					.build();
		}
		catch (final EdmException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	@Override
	public ItemLookupRequest createFrom(
			final ItemLookupRequest request,
			final EdmEntitySet entitySet,
			final ODataEntry oDataEntry) throws EdmException
	{
		return itemLookupRequestBuilder().from(request)
		                                 .withEntitySet(entitySet)
		                                 .withODataEntry(oDataEntry)
		                                 .build();
	}

	private ItemLookupRequest.ItemLookupRequestBuilder createBasic(
			final TypeDescriptor typeDescriptor,
			final UriInfo uriInfo,
			final ODataContext context,
			final String contentType) throws ODataException
	{
		final EdmEntitySet entitySet = uriInfo.getStartEntitySet();
		final Locale locale = getLocaleExtractor().extractFrom(context, HttpHeaders.ACCEPT_LANGUAGE);
		final ItemLookupRequest.ItemLookupRequestBuilder builder = itemLookupRequestBuilder()
				.withEntitySet(entitySet)
				.withAcceptLocale(locale)
				.withSkip(deriveSkip(uriInfo))
				.withTop(deriveTop(uriInfo.getTop()))
				.withCount(uriInfo.getInlineCount() == InlineCount.ALLPAGES || uriInfo.isCount())
				.withCountOnly(uriInfo.isCount())
				.withExpand(uriInfo.getExpand())
				.withNavigationSegments(uriInfo.getNavigationSegments())
				.withIntegrationObject(typeDescriptor.getIntegrationObjectCode())
				.withServiceRoot(context.getPathInfo().getServiceRoot())
				.withContentType(contentType)
				.withRequestUri(context.getPathInfo().getRequestUri())
				.withTypeDescriptor(typeDescriptor);
		return buildWithFilter(builder, uriInfo, context);
	}

	private TypeDescriptor toTypeDescriptor(final ODataContext context, final DeleteUriInfo uriInfo) throws EdmException
	{
		return toTypeDescriptor(context, uriInfo.getStartEntitySet());
	}

	private TypeDescriptor toTypeDescriptor(final ODataContext context, final EdmEntitySet entitySet) throws EdmException
	{
		final String ioCode = getServiceNameFromContext(context);
		final String type = entitySet.getEntityType().getName();
		return descriptorService.getTypeDescriptor(ioCode, type)
		                        .orElseThrow(() -> new IntegrationObjectItemNotFoundException(ioCode, type));
	}

	protected ItemLookupRequest.ItemLookupRequestBuilder buildWithFilter(final ItemLookupRequest.ItemLookupRequestBuilder builder,
	                                                                     final UriInfo uriInfo, final ODataContext context)
			throws ExceptionVisitExpression, ODataApplicationException
	{
		if (isOnlyUseFilterWhenKeyIsNotSupplied(uriInfo))
		{
			handleFilter(builder, uriInfo, context);
		}
		if (uriInfo.getOrderBy() != null)
		{
			handleOrderBy(builder, uriInfo, context);
		}
		return builder;
	}

	private boolean isOnlyUseFilterWhenKeyIsNotSupplied(final UriInfo uriInfo)
	{
		return uriInfo.getKeyPredicates().isEmpty() && uriInfo.getFilter() != null;
	}

	protected void handleFilter(final ItemLookupRequest.ItemLookupRequestBuilder builder, final UriInfo uriInfo,
	                            final ODataContext context) throws ExceptionVisitExpression, ODataApplicationException
	{
		final ExpressionVisitor visitor = createExpressionVisitor(uriInfo, context);
		try
		{
			builder.withFilter((WhereClauseConditions) uriInfo.getFilter().accept(visitor));
		}
		catch (final NoFilterResultException e)
		{
			LOG.trace("No filter result found", e);
			builder.withHasNoFilterResult(true);
		}
	}

	protected void handleOrderBy(final ItemLookupRequest.ItemLookupRequestBuilder builder, final UriInfo uriInfo,
	                             final ODataContext context) throws ExceptionVisitExpression, ODataApplicationException
	{
		final ExpressionVisitor visitor = createExpressionVisitor(uriInfo, context);
		final List<OrderExpression> orderBy = ((List<?>) uriInfo.getOrderBy().accept(visitor)).stream()
		                                                                                      .map(entry -> (OrderExpression) entry)
		                                                                                      .collect(Collectors.toCollection(
				                                                                                      ArrayList::new));
		builder.withOrderBy(orderBy);
	}

	protected ExpressionVisitor createExpressionVisitor(final UriInfo uriInfo, final ODataContext context)
	{
		final ExpressionVisitorParameters parameters = parametersBuilder()
				.withODataContext(context)
				.withUriInfo(uriInfo)
				.build();

		return expressionVisitorFactory.create(parameters);
	}

	private ItemLookupRequest.ItemLookupRequestBuilder createWithIntegrationKey(
			final TypeDescriptor typeDescriptor, final UriInfo uriInfo,
			final ODataContext context,
			final String contentType) throws ODataException
	{
		final ItemLookupRequest.ItemLookupRequestBuilder builder = createBasic(typeDescriptor, uriInfo, context, contentType);
		final EdmEntitySet entitySet = uriInfo.getStartEntitySet();
		final ODataEntry calculatedODataEntry = getIntegrationKeyToODataEntryGenerator().generate(entitySet,
				uriInfo.getKeyPredicates());
		final IntegrationItem item = convert(typeDescriptor, context, calculatedODataEntry);
		return builder
				.withODataEntry(calculatedODataEntry)
				.withIntegrationObject(item.getIntegrationObjectCode())
				.withIntegrationItem(item);
	}

	protected static boolean hasKey(final UriInfo uriInfo)
	{
		return !uriInfo.getKeyPredicates().isEmpty();
	}

	private IntegrationItem convert(final TypeDescriptor typeDescriptor,
	                                final ODataContext context,
	                                final ODataEntry oDataEntry)
	{
		return getEntryConverter().convert(context, typeDescriptor, oDataEntry);
	}

	protected Integer deriveSkip(final UriInfo uriInfo)
	{
		final String skipToken = uriInfo.getSkipToken();
		final Integer skip = uriInfo.getSkip();

		if (skip != null)
		{
			if (skipToken == null)
			{
				return skip;
			}
			throw new InvalidQueryParameterException(
					"$skip and $skiptoken query parameters cannot be combined in the same get request.");
		}
		else if (skipToken != null)
		{
			if (skipToken.matches("\\d+"))
			{
				return Integer.parseInt(skipToken);
			}
			throw new InvalidQueryParameterException("$skiptoken value must be an integer");
		}
		return 0;
	}

	protected Integer deriveTop(final Integer uriInfoTop)
	{
		if (uriInfoTop != null)
		{
			final int maxPageSize = getODataServicesConfiguration().getMaxPageSize();
			if (uriInfoTop > maxPageSize)
			{
				LOG.warn("Requested parameter value for $top exceeds the maximum value of {}.", maxPageSize);
				return maxPageSize;
			}
			return uriInfoTop;
		}
		return getODataServicesConfiguration().getDefaultPageSize();
	}

	protected String getServiceNameFromContext(final ODataContext context)
	{
		return getServiceNameExtractor().extract(context);
	}

	/**
	 * @see IntegrationKeyToODataEntryGenerator#generate(EdmEntitySet, List)
	 * @deprecated This method will be removed because {@link IntegrationKeyToODataEntryGenerator} now has a method that takes
	 * in a list of {@link KeyPredicate}s
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected String getIntegrationKey(final EdmEntitySet edmEntitySet,
	                                   final List<KeyPredicate> keyPredicates) throws EdmException
	{
		final EdmEntityType entityType = edmEntitySet.getEntityType();
		if (keyPredicates.size() == 1
				&& INTEGRATION_KEY_PROPERTY_NAME.equals(keyPredicates.get(0).getProperty().getName()))
		{
			return keyPredicates.get(0).getLiteral();
		}

		throw new InvalidIntegrationKeyException(entityType.getName());
	}

	protected ODataServicesConfiguration getODataServicesConfiguration()
	{
		return oDataServicesConfiguration;
	}

	protected IntegrationKeyToODataEntryGenerator getIntegrationKeyToODataEntryGenerator()
	{
		return integrationKeyToODataEntryGenerator;
	}

	@Required
	public void setIntegrationKeyToODataEntryGenerator(
			final IntegrationKeyToODataEntryGenerator integrationKeyToODataEntryGenerator)
	{
		this.integrationKeyToODataEntryGenerator = integrationKeyToODataEntryGenerator;
	}

	protected ODataContextLanguageExtractor getLocaleExtractor()
	{
		return localeExtractor;
	}

	@Required
	public void setLocaleExtractor(final ODataContextLanguageExtractor localeExtractor)
	{
		this.localeExtractor = localeExtractor;
	}

	protected ServiceNameExtractor getServiceNameExtractor()
	{
		return serviceNameExtractor;
	}

	@Required
	public void setServiceNameExtractor(final ServiceNameExtractor serviceNameExtractor)
	{
		this.serviceNameExtractor = serviceNameExtractor;
	}

	@Required
	public void setODataServicesConfiguration(final ODataServicesConfiguration oDataServicesConfiguration)
	{
		this.oDataServicesConfiguration = oDataServicesConfiguration;
	}

	protected ExpressionVisitorFactory getExpressionVisitorFactory()
	{
		return expressionVisitorFactory;
	}

	@Required
	public void setExpressionVisitorFactory(final ExpressionVisitorFactory expressionVisitorFactory)
	{
		this.expressionVisitorFactory = expressionVisitorFactory;
	}

	protected ODataEntryToIntegrationItemConverter getEntryConverter()
	{
		return entryConverter;
	}

	@Required
	public void setEntryConverter(final ODataEntryToIntegrationItemConverter converter)
	{
		entryConverter = converter;
	}

	@Required
	public void setItemTypeDescriptorService(final ItemTypeDescriptorService service)
	{
		descriptorService = service;
	}
}
