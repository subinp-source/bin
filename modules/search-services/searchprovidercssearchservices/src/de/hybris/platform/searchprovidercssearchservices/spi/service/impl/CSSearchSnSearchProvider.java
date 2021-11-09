/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchprovidercssearchservices.spi.service.impl;

import static de.hybris.platform.searchservices.util.ConverterUtils.convert;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.factory.ClientFactory;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistryClientService;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.searchprovidercssearchservices.admin.data.FieldDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.IndexConfigurationDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.IndexDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.IndexTypeDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.LanguageDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.QualifierDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.QualifierTypeDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.SynonymDictionaryDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.SynonymEntryDTO;
import de.hybris.platform.searchprovidercssearchservices.constants.SearchprovidercssearchservicesConstants;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentBatchOperationRequestDTO;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentBatchOperationResponseDTO;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentBatchRequestDTO;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentBatchResponseDTO;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentDTO;
import de.hybris.platform.searchprovidercssearchservices.indexer.data.FieldTypeDTO;
import de.hybris.platform.searchprovidercssearchservices.indexer.data.IndexerOperationDTO;
import de.hybris.platform.searchprovidercssearchservices.indexer.data.IndexerOperationStatusDTO;
import de.hybris.platform.searchprovidercssearchservices.indexer.data.IndexerOperationTypeDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.AndQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.BucketsFacetFilterDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.EqualQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.GreaterThanOrEqualQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.GreaterThanQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.LessThanOrEqualQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.LessThanQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.MatchQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.MatchTermQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.MatchTermsQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.NotEqualQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.NotQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.OrQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.PromotedHitsRankRuleDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.QueryFunctionRankRuleDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.RangeBucketsFacetRequestDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.RangeBucketsFacetResponseDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.RangeQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.SearchQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.SearchResultDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.TermBucketsFacetRequestDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.TermBucketsFacetResponseDTO;
import de.hybris.platform.searchprovidercssearchservices.spi.data.CSSearchSnSearchProviderConfiguration;
import de.hybris.platform.searchprovidercssearchservices.spi.service.CSSearchClient;
import de.hybris.platform.searchprovidercssearchservices.suggest.data.SuggestQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.suggest.data.SuggestResultDTO;
import de.hybris.platform.searchservices.admin.data.SnCurrency;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.data.SnLanguage;
import de.hybris.platform.searchservices.admin.data.SnSynonymDictionary;
import de.hybris.platform.searchservices.admin.data.SnSynonymEntry;
import de.hybris.platform.searchservices.admin.service.SnSynonymDictionaryService;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.SnRuntimeException;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.document.data.SnDocument;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchOperationRequest;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchOperationResponse;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchRequest;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchResponse;
import de.hybris.platform.searchservices.enums.SnDocumentOperationStatus;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.enums.SnIndexerOperationStatus;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.indexer.data.SnIndexerOperation;
import de.hybris.platform.searchservices.search.data.SnAndQuery;
import de.hybris.platform.searchservices.search.data.SnBucketsFacetFilter;
import de.hybris.platform.searchservices.search.data.SnEqualQuery;
import de.hybris.platform.searchservices.search.data.SnGreaterThanOrEqualQuery;
import de.hybris.platform.searchservices.search.data.SnGreaterThanQuery;
import de.hybris.platform.searchservices.search.data.SnLessThanOrEqualQuery;
import de.hybris.platform.searchservices.search.data.SnLessThanQuery;
import de.hybris.platform.searchservices.search.data.SnMatchQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnNotEqualQuery;
import de.hybris.platform.searchservices.search.data.SnNotQuery;
import de.hybris.platform.searchservices.search.data.SnOrQuery;
import de.hybris.platform.searchservices.search.data.SnPromotedHitsRankRule;
import de.hybris.platform.searchservices.search.data.SnQueryFunctionRankRule;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetRequest;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.SnRangeQuery;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetRequest;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.impl.AbstractSnSearchProvider;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;

import com.hybris.charon.RawResponse;
import com.hybris.charon.exp.ClientException;
import com.hybris.charon.exp.ServerException;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;


/**
 * Implementation of {@link SnSearchProvider} for the search core service.
 */
public class CSSearchSnSearchProvider extends AbstractSnSearchProvider<CSSearchSnSearchProviderConfiguration>
		implements InitializingBean
{
	private static final Logger LOG = LoggerFactory.getLogger(CSSearchSnSearchProvider.class);

	private DestinationService<ConsumedDestinationModel> destinationService;
	private ApiRegistryClientService apiRegistryClientService;
	private CommonI18NService commonI18NService;
	private ClientFactory clientFactory;
	private SnSynonymDictionaryService snSynonymDictionaryService;

	private MapperFacade mapperFacade;

	@Override
	public void afterPropertiesSet()
	{
		final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		registerMappings(mapperFactory);
		mapperFacade = mapperFactory.getMapperFacade();
	}

	protected void registerMappings(final MapperFactory mapperFactory)
	{
		mapperFactory.registerMapper(new ListCustomMapper());
		mapperFactory.registerMapper(new MapCustomMapper());

		// map query types
		mapperFactory.classMap(SnAndQuery.class, AndQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnOrQuery.class, OrQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnNotQuery.class, NotQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnMatchTermQuery.class, MatchTermQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnMatchTermsQuery.class, MatchTermsQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnMatchQuery.class, MatchQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnEqualQuery.class, EqualQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnNotEqualQuery.class, NotEqualQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnGreaterThanOrEqualQuery.class, GreaterThanOrEqualQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnGreaterThanQuery.class, GreaterThanQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnLessThanOrEqualQuery.class, LessThanOrEqualQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnLessThanQuery.class, LessThanQueryDTO.class).byDefault().register();
		mapperFactory.classMap(SnRangeQuery.class, RangeQueryDTO.class).byDefault().register();

		// map rank rules
		mapperFactory.classMap(SnQueryFunctionRankRule.class, QueryFunctionRankRuleDTO.class).byDefault().register();
		mapperFactory.classMap(SnPromotedHitsRankRule.class, PromotedHitsRankRuleDTO.class).byDefault().register();

		// map facets
		mapperFactory.classMap(SnTermBucketsFacetRequest.class, TermBucketsFacetRequestDTO.class).byDefault().register();
		mapperFactory.classMap(SnRangeBucketsFacetRequest.class, RangeBucketsFacetRequestDTO.class).byDefault().register();
		mapperFactory.classMap(SnTermBucketsFacetResponse.class, TermBucketsFacetResponseDTO.class).byDefault().register();
		mapperFactory.classMap(SnRangeBucketsFacetResponse.class, RangeBucketsFacetResponseDTO.class).byDefault().register();
		mapperFactory.classMap(SnBucketsFacetFilter.class, BucketsFacetFilterDTO.class).byDefault().register();
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Override
	public void exportConfiguration(final SnContext context) throws SnException
	{
		final CSSearchClient client = createClient(context);
		exportSynonymDictionaries(client, context);
		exportIndexConfiguration(client, context);
		exportIndexType(client, context);
	}

	protected void exportSynonymDictionaries(final CSSearchClient client, final SnContext context) throws SnException
	{
		final SnIndexConfiguration indexConfiguration = context.getIndexConfiguration();
		if (indexConfiguration == null || CollectionUtils.isEmpty(indexConfiguration.getSynonymDictionaryIds()))
		{
			return;
		}

		try
		{
			for (final String synonymDictionaryId : indexConfiguration.getSynonymDictionaryIds())
			{
				final Optional<SnSynonymDictionary> synonymDictionaryOptional = snSynonymDictionaryService
						.getSynonymDictionaryForId(synonymDictionaryId);
				if (synonymDictionaryOptional.isPresent())
				{
					final SynonymDictionaryDTO synonymDictionaryDTO = convertSynonymDictionary(synonymDictionaryOptional.get());
					client.createOrUpdateSynonymDictionary(synonymDictionaryDTO.getId(), synonymDictionaryDTO).toBlocking()
							.subscribe();
				}
			}
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Create/Update synonym dictionaries failed", e);
		}
	}

	protected SynonymDictionaryDTO convertSynonymDictionary(final SnSynonymDictionary source)
	{
		final SynonymDictionaryDTO target = new SynonymDictionaryDTO();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setLanguageIds(source.getLanguageIds());
		target.setEntries(ConverterUtils.convertAll(source.getEntries(), this::convertSynonymEntry));

		return target;
	}

	protected SynonymEntryDTO convertSynonymEntry(final SnSynonymEntry source)
	{
		final SynonymEntryDTO target = new SynonymEntryDTO();
		target.setId(source.getId());
		target.setInput(source.getInput());
		target.setSynonyms(source.getSynonyms());

		return target;
	}

	protected void exportIndexConfiguration(final CSSearchClient client, final SnContext context) throws SnException
	{
		final SnIndexConfiguration indexConfiguration = context.getIndexConfiguration();
		if (indexConfiguration == null)
		{
			return;
		}

		try
		{
			final IndexConfigurationDTO indexConfigurationDTO = convertIndexConfiguration(indexConfiguration);
			client.createOrUpdateIndexConfiguration(indexConfigurationDTO.getId(), indexConfigurationDTO).toBlocking().subscribe();
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Create/Update configuration failed", e);
		}
	}

	protected IndexConfigurationDTO convertIndexConfiguration(final SnIndexConfiguration source)
	{
		final IndexConfigurationDTO target = new IndexConfigurationDTO();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setLanguages(ConverterUtils.convertAll(source.getLanguages(), this::convertLanguage));
		target.setQualifierTypes(List.of(convertCurrencyQualifierType(source)));
		target.setSynonymDictionaryIds(source.getSynonymDictionaryIds());

		return target;
	}

	protected LanguageDTO convertLanguage(final SnLanguage source)
	{
		final LanguageDTO target = new LanguageDTO();
		target.setId(source.getId());
		target.setName(source.getName());
		return target;
	}

	protected QualifierTypeDTO convertCurrencyQualifierType(final SnIndexConfiguration configuration)
	{
		final QualifierTypeDTO qualifierType = new QualifierTypeDTO();
		qualifierType.setId(SearchprovidercssearchservicesConstants.CURRENCY_QUALIFIER_TYPE_ID);
		qualifierType.setQualifiers(ConverterUtils.convertAll(configuration.getCurrencies(), this::convertCurrencyQualifier));

		return qualifierType;
	}

	protected QualifierDTO convertCurrencyQualifier(final SnCurrency source)
	{
		final QualifierDTO target = new QualifierDTO();
		target.setId(source.getId());
		target.setName(source.getName());
		return target;
	}

	protected void exportIndexType(final CSSearchClient client, final SnContext context) throws SnException
	{
		final SnIndexType indexType = context.getIndexType();
		if (indexType == null)
		{
			return;
		}

		try
		{
			final IndexTypeDTO indexTypeDTO = convertIndexType(indexType);
			client.createOrUpdateIndexType(indexTypeDTO.getId(), indexTypeDTO).toBlocking().subscribe();
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Create/Update type failed", e);
		}
	}

	protected IndexTypeDTO convertIndexType(final SnIndexType source)
	{
		final IndexTypeDTO target = new IndexTypeDTO();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setIndexConfigurationId(source.getIndexConfigurationId());
		target.setFields(ConverterUtils.convertAll(source.getFields().values(), this::convertField));

		return target;
	}

	protected FieldDTO convertField(final SnField source)
	{
		final FieldDTO target = new FieldDTO();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setFieldType(convert(source.getFieldType(), this::convertFieldType));
		target.setRetrievable(source.getRetrievable());
		target.setSearchable(source.getSearchable());
		target.setLocalized(source.getLocalized());
		target.setQualifierTypeId(source.getQualifierTypeId());
		target.setMultiValued(source.getMultiValued());
		target.setUseForSuggesting(source.getUseForSuggesting());
		target.setUseForSpellchecking(source.getUseForSpellchecking());
		target.setWeight(source.getWeight());

		return target;
	}

	protected FieldTypeDTO convertFieldType(final SnFieldType source)
	{
		return FieldTypeDTO.valueOf(source.name());
	}

	@Override
	public void createIndex(final SnContext context, final String indexId) throws SnException
	{
		try
		{
			final CSSearchClient client = createClient(context);

			// as there is no way to check if the index exists we always try to create it and ignore the errors
			final IndexDTO indexDTO = new IndexDTO();
			indexDTO.setId(indexId);
			indexDTO.setIndexTypeId(context.getIndexType().getId());

			client.createIndex(indexDTO).toBlocking().subscribe();
		}
		catch (final ClientException e)
		{
			throw new SnException("Create/Update index failed", e);
		}
		catch (final ServerException e)
		{
			LOG.warn("Create index failed", e);
		}
	}

	@Override
	public void deleteIndex(final SnContext context, final String indexId) throws SnException
	{
		try
		{
			final CSSearchClient client = createClient(context);
			client.deleteIndex(indexId).toBlocking().subscribe();
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Delete index failed", e);
		}
	}

	@Override
	public SnIndexerOperation createIndexerOperation(final SnContext context, final SnIndexerOperationType indexerOperationType,
			final int totalItems) throws SnException
	{
		try
		{
			final SnIndexType type = context.getIndexType();

			final IndexerOperationDTO indexerOperationDTO = new IndexerOperationDTO();
			indexerOperationDTO.setIndexTypeId(type.getId());
			indexerOperationDTO.setOperationType(convert(indexerOperationType, this::convertIndexerOperationType));
			indexerOperationDTO.setTotalItems(Integer.valueOf(totalItems));

			final CSSearchClient client = createClient(context);
			final RawResponse<IndexerOperationDTO> response = client.createIndexerOperation(indexerOperationDTO).toBlocking()
					.single();

			return convertIndexerOperation(response.content().toBlocking().single());
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Create indexer operation failed", e);
		}
	}

	@Override
	public SnIndexerOperation updateIndexerOperationStatus(final SnContext context, final String indexerOperationId,
			final SnIndexerOperationStatus status, final String errorMessage) throws SnException
	{
		try
		{
			final Map<String, Object> mergePatch = new HashMap<>();
			mergePatch.put("status", status);
			mergePatch.put("errorMessage", errorMessage);

			final CSSearchClient client = createClient(context);
			final RawResponse<IndexerOperationDTO> response = client.patchIndexerOperation(indexerOperationId, mergePatch)
					.toBlocking().single();

			return convertIndexerOperation(response.content().toBlocking().single());
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Update indexer operation status failed", e);
		}
	}

	protected IndexerOperationTypeDTO convertIndexerOperationType(final SnIndexerOperationType source)
	{
		return IndexerOperationTypeDTO.valueOf(source.name());
	}

	protected SnIndexerOperationType convertIndexerOperationType(final IndexerOperationTypeDTO source)
	{
		return SnIndexerOperationType.valueOf(source.name());
	}

	protected IndexerOperationStatusDTO convertIndexerOperationStatus(final SnIndexerOperationStatus source)
	{
		return IndexerOperationStatusDTO.valueOf(source.name());
	}

	protected SnIndexerOperationStatus convertIndexerOperationStatus(final IndexerOperationStatusDTO source)
	{
		return SnIndexerOperationStatus.valueOf(source.name());
	}

	protected SnIndexerOperation convertIndexerOperation(final IndexerOperationDTO source)
	{
		final SnIndexerOperation target = new SnIndexerOperation();
		target.setId(source.getId());
		target.setIndexTypeId(source.getIndexTypeId());
		target.setIndexId(source.getIndexId());
		target.setOperationType(convert(source.getOperationType(), this::convertIndexerOperationType));
		target.setStatus(convert(source.getStatus(), this::convertIndexerOperationStatus));

		return target;
	}

	@Override
	public SnDocumentBatchResponse executeDocumentBatch(final SnContext context, final String indexId,
			final SnDocumentBatchRequest documentBatchRequest, final String indexerOperationId) throws SnException
	{
		try
		{
			final DocumentBatchRequestDTO request = convertDocumenBatchRequest(documentBatchRequest, indexerOperationId);

			final CSSearchClient client = createClient(context);
			final RawResponse<DocumentBatchResponseDTO> response = client.executeDocumentBatch(indexId, request).toBlocking()
					.single();

			return convertDocumenBatchResponse(response.content().toBlocking().single());
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Execute document batch failed", e);
		}
	}

	protected DocumentDTO convertDocument(final SnDocument source)
	{
		return mapperFacade.map(source, DocumentDTO.class);
	}

	protected DocumentBatchRequestDTO convertDocumenBatchRequest(final SnDocumentBatchRequest source,
			final String indexerOperationId)
	{
		final DocumentBatchRequestDTO target = new DocumentBatchRequestDTO();
		target.setId(source.getId());
		target.setIndexerOperationId(indexerOperationId);
		target.setRequests(ConverterUtils.convertAll(source.getRequests(), this::convertDocumenBatchOperationRequest));

		return target;
	}

	protected DocumentBatchOperationRequestDTO convertDocumenBatchOperationRequest(final SnDocumentBatchOperationRequest source)
	{
		final DocumentBatchOperationRequestDTO target = new DocumentBatchOperationRequestDTO();
		target.setMethod(convertDocumenOperationType(source.getOperationType()));
		target.setId(source.getId());
		target.setBody(ConverterUtils.convert(source.getDocument(), this::convertDocument));

		return target;
	}

	protected String convertDocumenOperationType(final SnDocumentOperationType source)
	{
		if (SnDocumentOperationType.CREATE == source)
		{
			return "POST";
		}
		else if (SnDocumentOperationType.CREATE_UPDATE == source)
		{
			return "PUT";
		}
		else if (SnDocumentOperationType.DELETE == source)
		{
			return "DELETE";
		}
		else
		{
			throw new SnRuntimeException(MessageFormat.format("Cannot convert document operation type ''{0}''", source));
		}
	}

	protected SnDocumentBatchResponse convertDocumenBatchResponse(final DocumentBatchResponseDTO source)
	{
		final SnDocumentBatchResponse target = new SnDocumentBatchResponse();
		target.setResponses(ConverterUtils.convertAll(source.getResponses(), this::convertDocumenBatchOperationResponse));

		return target;
	}

	protected SnDocumentBatchOperationResponse convertDocumenBatchOperationResponse(final DocumentBatchOperationResponseDTO source)
	{
		final SnDocumentBatchOperationResponse target = new SnDocumentBatchOperationResponse();
		target.setId(source.getId());
		target.setStatus(convertDocumenOperationStatus(source.getStatusCode()));

		return target;
	}

	protected SnDocumentOperationStatus convertDocumenOperationStatus(final Integer statusCode)
	{
		final SnDocumentOperationStatus result;
		if (statusCode.intValue() == HttpStatus.CREATED.value())
		{
			result = SnDocumentOperationStatus.CREATED;
		}
		else if (statusCode.intValue() == HttpStatus.OK.value())
		{
			result = SnDocumentOperationStatus.UPDATED;
		}
		else if (statusCode.intValue() == HttpStatus.NO_CONTENT.value())
		{
			result = SnDocumentOperationStatus.DELETED;
		}
		else if (statusCode.intValue() == HttpStatus.INTERNAL_SERVER_ERROR.value())
		{
			result = SnDocumentOperationStatus.FAILED;
		}
		else
		{
			throw new SnRuntimeException(
					MessageFormat.format("Cannot convert document operation result for status code ''{0}''", statusCode));
		}
		return result;
	}

	@Override
	public void commit(final SnContext context, final String indexId) throws SnException
	{
		try
		{
			final CSSearchClient client = createClient(context);
			client.commit(indexId).toBlocking().subscribe();
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Flush index failed", e);
		}
	}

	@Override
	public SnSearchResult search(final SnContext context, final String indexId, final SnSearchQuery searchQuery) throws SnException
	{
		try
		{
			final SearchQueryDTO searchQueryDTO = convertSearchQuery(searchQuery);

			final String languageHeader = createLanguageHeader(context);
			final String qualifierHeader = createQualifierHeader(context);
			final CSSearchClient client = createClient(context);
			final RawResponse<SearchResultDTO> response = client.search(languageHeader, qualifierHeader, indexId, searchQueryDTO)
					.toBlocking().single();

			return convertSearchResult(searchQuery, response.content().toBlocking().single());
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Search operation failed", e);
		}
	}

	protected SearchQueryDTO convertSearchQuery(final SnSearchQuery source)
	{
		return mapperFacade.map(source, SearchQueryDTO.class);
	}

	protected SnSearchResult convertSearchResult(final SnSearchQuery searchQuery, final SearchResultDTO source)
	{
		return mapperFacade.map(source, SnSearchResult.class);
	}

	@Override
	public SnSuggestResult suggest(final SnContext context, final String indexId, final SnSuggestQuery suggestQuery)
			throws SnException
	{
		try
		{
			final SuggestQueryDTO suggestQueryDTO = convertSuggestQuery(suggestQuery);

			final String languageHeader = createLanguageHeader(context);
			final String qualifierHeader = createQualifierHeader(context);
			final CSSearchClient client = createClient(context);
			final RawResponse<SuggestResultDTO> response = client.suggest(languageHeader, qualifierHeader, indexId, suggestQueryDTO)
					.toBlocking().single();

			return convertSuggestResult(response.content().toBlocking().single());
		}
		catch (final ClientException | ServerException e)
		{
			throw new SnException("Suggest query failed", e);
		}
	}

	protected SuggestQueryDTO convertSuggestQuery(final SnSuggestQuery source)
	{
		return mapperFacade.map(source, SuggestQueryDTO.class);
	}

	protected SnSuggestResult convertSuggestResult(final SuggestResultDTO source)
	{
		return mapperFacade.map(source, SnSuggestResult.class);
	}

	protected String createLanguageHeader(final SnContext context)
	{
		if (MapUtils.isEmpty(context.getQualifiers()))
		{
			return null;
		}

		final List<SnQualifier> languages = context.getQualifiers().get(SearchservicesConstants.LANGUAGE_QUALIFIER_TYPE);
		if (CollectionUtils.isEmpty(languages))
		{
			return null;
		}

		return languages.get(0).getId();
	}

	protected String createQualifierHeader(final SnContext context)
	{
		if (MapUtils.isEmpty(context.getQualifiers()))
		{
			return null;
		}

		return context.getQualifiers().entrySet().stream().filter(this::filterQualifierHeader).map(this::buildQualifierHeader)
				.collect(Collectors.joining(","));
	}

	protected boolean filterQualifierHeader(final Entry<String, List<SnQualifier>> entry)
	{
		return entry.getKey() != null && !StringUtils.equals(entry.getKey(), SearchservicesConstants.LANGUAGE_QUALIFIER_TYPE)
				&& CollectionUtils.isNotEmpty(entry.getValue());
	}

	protected String buildQualifierHeader(final Entry<String, List<SnQualifier>> entry)
	{
		return entry.getKey() + "=" + entry.getValue().get(0).getId();
	}

	protected CSSearchClient createClient(final SnContext context) throws SnException
	{
		try
		{
			final CSSearchSnSearchProviderConfiguration searchProviderConfiguration = getSearchProviderConfiguration(context);

			final ConsumedDestinationModel destination = destinationService.getDestinationByIdAndByDestinationTargetId(
					searchProviderConfiguration.getDestinationId(), searchProviderConfiguration.getDestinationTargetId());

			final Map<String, String> clientConfig = apiRegistryClientService.buildClientConfig(CSSearchClient.class, destination);

			return clientFactory.client(clientFactory.buildCacheKey(destination), CSSearchClient.class, clientConfig);
		}

		catch (final CredentialException e)
		{
			throw new SnException(e);
		}
	}

	public DestinationService<ConsumedDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService<ConsumedDestinationModel> destinationService)
	{
		this.destinationService = destinationService;
	}

	public ApiRegistryClientService getApiRegistryClientService()
	{
		return apiRegistryClientService;
	}

	@Required
	public void setApiRegistryClientService(final ApiRegistryClientService apiRegistryClientService)
	{
		this.apiRegistryClientService = apiRegistryClientService;
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

	public ClientFactory getClientFactory()
	{
		return clientFactory;
	}

	@Required
	public void setClientFactory(final ClientFactory clientFactory)
	{
		this.clientFactory = clientFactory;
	}

	public SnSynonymDictionaryService getSnSynonymDictionaryService()
	{
		return snSynonymDictionaryService;
	}

	@Required
	public void setSnSynonymDictionaryService(final SnSynonymDictionaryService snSynonymDictionaryService)
	{
		this.snSynonymDictionaryService = snSynonymDictionaryService;
	}

	protected static class ListCustomMapper extends CustomMapper<List<Object>, List<Object>>
	{
		@Override
		public void mapAtoB(final List<Object> source, final List<Object> destination, final MappingContext context)
		{
			map(source, destination, context);
		}

		@Override
		public void mapBtoA(final List<Object> source, final List<Object> destination, final MappingContext context)
		{
			map(source, destination, context);
		}

		protected void map(final List<Object> source, final List<Object> destination, final MappingContext context)
		{
			final Type<Object> sourceElementType = context.getResolvedSourceType().getNestedType(0);
			final Type<Object> destinationElementType = context.getResolvedDestinationType().getNestedType(0);

			destination.clear();

			for (final Object sourceElement : source)
			{
				if (sourceElement == null)
				{
					destination.add(null);
				}
				else
				{
					final Object destinationElement = mapperFacade.map(sourceElement, sourceElementType, destinationElementType,
							context);
					destination.add(destinationElement);
				}
			}
		}
	}

	protected static class MapCustomMapper extends CustomMapper<Map<String, Object>, Map<String, Object>>
	{
		@Override
		public void mapAtoB(final Map<String, Object> source, final Map<String, Object> destination, final MappingContext context)
		{
			map(source, destination);
		}

		@Override
		public void mapBtoA(final Map<String, Object> source, final Map<String, Object> destination, final MappingContext context)
		{
			map(source, destination);
		}

		protected void map(final Map<String, Object> source, final Map<String, Object> destination)
		{
			destination.clear();
			destination.putAll(source);
		}
	}
}
