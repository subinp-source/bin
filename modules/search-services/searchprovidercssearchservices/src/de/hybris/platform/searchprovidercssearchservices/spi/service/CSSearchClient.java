/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchprovidercssearchservices.spi.service;


import static de.hybris.platform.searchprovidercssearchservices.constants.SearchprovidercssearchservicesConstants.ACCEPT_LANGUAGE_HEADER_KEY;
import static de.hybris.platform.searchprovidercssearchservices.constants.SearchprovidercssearchservicesConstants.ACCEPT_QUALIFIER_HEADER_KEY;

import de.hybris.platform.searchprovidercssearchservices.admin.data.FieldDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.IndexConfigurationDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.IndexDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.IndexTypeDTO;
import de.hybris.platform.searchprovidercssearchservices.admin.data.SynonymDictionaryDTO;
import de.hybris.platform.searchprovidercssearchservices.constants.SearchprovidercssearchservicesConstants;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentBatchRequestDTO;
import de.hybris.platform.searchprovidercssearchservices.document.data.DocumentBatchResponseDTO;
import de.hybris.platform.searchprovidercssearchservices.indexer.data.IndexerOperationDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.SearchQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.search.data.SearchResultDTO;
import de.hybris.platform.searchprovidercssearchservices.suggest.data.SuggestQueryDTO;
import de.hybris.platform.searchprovidercssearchservices.suggest.data.SuggestResultDTO;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.hybris.charon.RawResponse;
import com.hybris.charon.annotations.Control;
import com.hybris.charon.annotations.OAuth;
import com.hybris.charon.annotations.PATCH;

import rx.Observable;


@OAuth
@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:200}", timeout = "${timeout:15000}")
public interface CSSearchClient
{
	@GET
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_CONFIGURATION)
	Observable<RawResponse<IndexConfigurationDTO>> getIndexConfiguration(
			@PathParam("indexConfigurationId") final String indexConfigurationId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_CONFIGURATIONS)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexConfigurationDTO>> createIndexConfiguration(IndexConfigurationDTO indexConfiguration);

	@PUT
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_CONFIGURATION)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexConfigurationDTO>> createOrUpdateIndexConfiguration(
			@PathParam("indexConfigurationId") final String indexConfigurationId, IndexConfigurationDTO indexConfiguration);

	@DELETE
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_CONFIGURATION)
	Observable<RawResponse<Void>> deleteIndexConfiguration(@PathParam("indexConfigurationId") final String indexConfigurationId);

	@GET
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE)
	Observable<RawResponse<IndexTypeDTO>> getIndexType(@PathParam("indexTypeId") final String indexTypeId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPES)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexTypeDTO>> createIndexType(IndexTypeDTO indexType);

	@PUT
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexTypeDTO>> createOrUpdateIndexType(@PathParam("indexTypeId") final String indexTypeId,
			IndexTypeDTO indexType);

	@DELETE
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE)
	Observable<RawResponse<Void>> deleteIndexType(@PathParam("indexTypeId") final String indexTypeId);

	@GET
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE_FIELD)
	Observable<RawResponse<FieldDTO>> getIndexTypeField(@PathParam("indexTypeId") final String indexTypeId,
			@PathParam("fieldId") final String fieldId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE_FIELDS)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<FieldDTO>> createIndexTypeField(@PathParam("indexTypeId") final String indexTypeId, FieldDTO field);

	@PUT
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE_FIELD)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<FieldDTO>> createOrUpdateIndexTypeField(@PathParam("indexTypeId") final String indexTypeId,
			@PathParam("fieldId") final String fieldId, FieldDTO field);

	@DELETE
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_INDEX_TYPE_FIELD)
	Observable<RawResponse<Void>> deleteIndexTypeField(@PathParam("indexTypeId") final String indexTypeId,
			@PathParam("fieldId") final String fieldId);

	@GET
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_SYNONYM_DICTIONARY)
	Observable<RawResponse<SynonymDictionaryDTO>> getSynonymDictionary(
			@PathParam("synonymDictionaryId") final String synonymDictionaryId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_SYNONYM_DICTIONARIES)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<SynonymDictionaryDTO>> createSynonymDictionary(SynonymDictionaryDTO synonymDictionaryId);

	@PUT
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_SYNONYM_DICTIONARY)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<SynonymDictionaryDTO>> createOrUpdateSynonymDictionary(
			@PathParam("synonymDictionaryId") final String synonymDictionaryId, SynonymDictionaryDTO synonymDictionary);

	@DELETE
	@Path(SearchprovidercssearchservicesConstants.PATH_ADMIN_SYNONYM_DICTIONARY)
	Observable<RawResponse<Void>> deleteSynonymDictionary(@PathParam("synonymDictionaryId") final String synonymDictionaryId);

	@GET
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEX)
	Observable<RawResponse<IndexDTO>> getIndex(@PathParam("indexId") final String indexId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEXES)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexDTO>> createIndex(IndexDTO index);

	@DELETE
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEX)
	Observable<RawResponse<Void>> deleteIndex(@PathParam("indexId") final String indexId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEXER_OPERATIONS)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexerOperationDTO>> createIndexerOperation(IndexerOperationDTO indexerOperation);

	@PATCH
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEXER_OPERATION)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<IndexerOperationDTO>> patchIndexerOperation(
			@PathParam("indexerOperationId") final String indexerOperationId, Map<String, Object> mergePatch);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEX_DOCUMENTS_BATCH)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<DocumentBatchResponseDTO>> executeDocumentBatch(@PathParam("indexId") final String indexId,
			DocumentBatchRequestDTO documentBatchRequest);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEX_COMMIT)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<Void>> commit(@PathParam("indexId") final String indexId);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEX_SEARCH)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<SearchResultDTO>> search(@HeaderParam(ACCEPT_LANGUAGE_HEADER_KEY) String languageHeader,
			@HeaderParam(ACCEPT_QUALIFIER_HEADER_KEY) String qualifierHeader, @PathParam("indexId") final String indexId,
			SearchQueryDTO searchQuery);

	@POST
	@Path(SearchprovidercssearchservicesConstants.PATH_INDEX_SUGGEST)
	@Produces(MediaType.APPLICATION_JSON)
	Observable<RawResponse<SuggestResultDTO>> suggest(@HeaderParam(ACCEPT_LANGUAGE_HEADER_KEY) String languageHeader,
			@HeaderParam(ACCEPT_QUALIFIER_HEADER_KEY) String qualifierHeader, @PathParam("indexId") final String indexId,
			SuggestQueryDTO suggestQuery);
}
