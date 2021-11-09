/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao.impl;

import de.hybris.platform.apiregistryservices.dao.DestinationDao;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link DestinationDao}
 *
 * @param <T>
 *           the type parameter which extends the {@link AbstractDestinationModel} type
 */
public class DefaultDestinationDao<T extends AbstractDestinationModel> extends AbstractItemDao implements DestinationDao
{
	protected static final String SELECT_PK_FROM = "SELECT {pk} FROM {";
	protected static final String ID_PARAMETER = "id";
	protected static final String TARGET_PARAMETER = "destinationTargetId";
	protected static final String CHANNEL_PARAMETER = "channel";
	protected static final String ACTIVE_PARAMETER = "active";

	protected static final String AND = " AND {";
	protected static final String NON_TEMPLATE_DT_CLAUSE = " ({DTM:"
			+ DestinationTargetModel.TEMPLATE + "}=0 OR {DTM:" + DestinationTargetModel.TEMPLATE + "} is NULL)";

	protected static final String GET_ALL_D_QUERY = SELECT_PK_FROM + AbstractDestinationModel._TYPECODE + "} ";

	protected static final String GET_ALL_NONTEMPLATE_D_QUERY = SELECT_PK_FROM + AbstractDestinationModel._TYPECODE
			+ " AS ADM JOIN "
			+ DestinationTargetModel._TYPECODE + " AS DTM ON {ADM:" + AbstractDestinationModel.DESTINATIONTARGET + "}={DTM:"
			+ DestinationTargetModel.PK + "}} WHERE "+ NON_TEMPLATE_DT_CLAUSE;

	protected static final String GET_ALL_CONSUMED_D_QUERY = "select {pk} from {" + ConsumedDestinationModel._TYPECODE + "} ";

	protected static final String GET_ALL_D_BY_TARGET = SELECT_PK_FROM + AbstractDestinationModel._TYPECODE + " AS d JOIN "
			+ DestinationTargetModel._TYPECODE + " AS t ON {d:" + AbstractDestinationModel.DESTINATIONTARGET + "}={t:"
			+ DestinationTargetModel.PK + "}} WHERE {t:" + DestinationTargetModel.ID + "}=?destinationTargetId";

	protected static final String ID_CLAUSE = "WHERE {" + AbstractDestinationModel.ID + "}=?" + ID_PARAMETER;
	protected static final String AND_ACTIVE_CLAUSE = AND + "d:" + AbstractDestinationModel.ACTIVE + "}=?" + ACTIVE_PARAMETER;

	protected static final String GET_ALL_D_BY_CHANNEL = SELECT_PK_FROM + AbstractDestinationModel._TYPECODE + " AS d JOIN "
			+ DestinationTargetModel._TYPECODE + " AS t ON {d:" + AbstractDestinationModel.DESTINATIONTARGET + "}={t:"
			+ DestinationTargetModel.PK + "}} WHERE {t:" + DestinationTargetModel.DESTINATIONCHANNEL + "}=?" + CHANNEL_PARAMETER;

	protected static final String AND_TEMPLATE_CLAUSE = " AND " + NON_TEMPLATE_DT_CLAUSE;

	protected static final String GET_ALL_EXPOSED_BY_CLIENT_QUERY = "select {" + AbstractDestinationModel.PK + "} " + "from {"
			+ AbstractDestinationModel._TYPECODE + " as destination " + "JOIN " + ExposedOAuthCredentialModel._TYPECODE
			+ " as credential ON {destination:" + AbstractDestinationModel.CREDENTIAL + "}={credential:"
			+ ExposedOAuthCredentialModel.PK + "} " + "JOIN " + OAuthClientDetailsModel._TYPECODE + " as oauth ON {credential:"
			+ ExposedOAuthCredentialModel.OAUTHCLIENTDETAILS + "}={oauth:" + OAuthClientDetailsModel.PK + "} JOIN "
			+ DestinationTargetModel._TYPECODE + " AS DTM ON {destination:" + AbstractDestinationModel.DESTINATIONTARGET + "}={DTM:"
			+ DestinationTargetModel.PK + "}} where {oauth:" +OAuthClientDetailsModel.CLIENTID + "}=?" + OAuthClientDetailsModel.CLIENTID
			+ AND + " destination:" + AbstractDestinationModel.ACTIVE + "}=?" + ACTIVE_PARAMETER + AND_TEMPLATE_CLAUSE;


	protected static final String GET_ACTIVE_EXPOSED_DESTINATIONS_BY_TARGET = SELECT_PK_FROM + ExposedDestinationModel._TYPECODE + " AS EDM JOIN "
			+ DestinationTargetModel._TYPECODE + " AS DTM ON {EDM:" + AbstractDestinationModel.DESTINATIONTARGET + "}={DTM:"
			+ DestinationTargetModel.PK + "}} WHERE {DTM:" + DestinationTargetModel.ID + "}=?destinationTargetId"
			+ " AND {EDM:" + AbstractDestinationModel.ACTIVE  + "}=?" + ACTIVE_PARAMETER;

	protected static final String GET_DESTINATIONS_BY_ID_AND_TARGET= GET_ALL_D_BY_TARGET + " AND {d:"+ AbstractCredentialModel.ID
			+ "}=?"+ ID_PARAMETER;
	protected static final String GET_ALL_EXPOSED_DESTINATIONS_BY_CREDENTIAL_ID="select {"+ExposedDestinationModel.PK+"} from {"
			+ ExposedDestinationModel._TYPECODE	+ " as destination " + " JOIN " + AbstractCredentialModel._TYPECODE
			+ " as credential ON {destination:" + AbstractDestinationModel.CREDENTIAL + "}={credential:"
			+ AbstractCredentialModel.PK + "}} WHERE {credential:"+ AbstractCredentialModel.ID+ "}=?"+ ID_PARAMETER;
 	/**
	 * @deprecated since 1905. Use {@link DestinationDao#getDestinationsByDestinationTargetId(String)}
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public List<T> getDestinationsByChannel(final DestinationChannel channel)
	{
		final Map queryParams = new HashMap();
		queryParams.put(CHANNEL_PARAMETER, channel);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_D_BY_CHANNEL, queryParams);
		final SearchResult<T> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<T> getDestinationsByDestinationTargetId(final String destinationTargetId)
	{
		final Map queryParams = new HashMap();
		queryParams.put(TARGET_PARAMETER, destinationTargetId);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_D_BY_TARGET, queryParams);
		final SearchResult<T> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	/**
	 * @deprecated since 1905. Use {@link DestinationDao#findDestinationByIdAndByDestinationTargetId(String, String)}'
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Override
	public T getDestinationById(final String id)
	{
		final Map queryParams = new HashMap();
		queryParams.put(ID_PARAMETER, id);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_D_QUERY + ID_CLAUSE, queryParams);
		final SearchResult<T> result = getFlexibleSearchService().search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<T> findAllDestinations()
	{
		final Map queryParams = new HashMap();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_NONTEMPLATE_D_QUERY, queryParams);
		return getFlexibleSearchService().<T> search(query).getResult();
	}

	@Override
	public List<ExposedDestinationModel> findActiveExposedDestinationsByClientId(final String clientId)
	{
		final Map queryParams = new HashMap();
		queryParams.put(OAuthClientDetailsModel.CLIENTID, clientId);
		queryParams.put(ACTIVE_PARAMETER, true);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_EXPOSED_BY_CLIENT_QUERY, queryParams);
		final SearchResult<ExposedDestinationModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	/**
	 * @deprecated since 1905. Use {@link DestinationDao#findActiveExposedDestinationsByDestinationTargetId(String)}
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public List<ExposedDestinationModel> findActiveExposedDestinationsByChannel(final DestinationChannel channel)
	{
		final Map queryParams = new HashMap();
		queryParams.put(CHANNEL_PARAMETER, channel);
		queryParams.put(ACTIVE_PARAMETER, true);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_D_BY_CHANNEL + AND_TEMPLATE_CLAUSE + AND_ACTIVE_CLAUSE, queryParams);
		final SearchResult<ExposedDestinationModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<ExposedDestinationModel> findActiveExposedDestinationsByDestinationTargetId(final String destinationTargetId)
	{
		final Map queryParams = new HashMap();
		queryParams.put(TARGET_PARAMETER, destinationTargetId);
		queryParams.put(ACTIVE_PARAMETER, true);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ACTIVE_EXPOSED_DESTINATIONS_BY_TARGET, queryParams);
		final SearchResult<ExposedDestinationModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public T findDestinationByIdAndByDestinationTargetId(final String destinationId,
			final String destinationTargetId)
	{
		final Map queryParams = new HashMap();
		queryParams.put(ID_PARAMETER, destinationId);
		queryParams.put(TARGET_PARAMETER, destinationTargetId);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_DESTINATIONS_BY_ID_AND_TARGET, queryParams);
		final SearchResult<T> result = getFlexibleSearchService().search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<ConsumedDestinationModel> findAllConsumedDestinations()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_CONSUMED_D_QUERY);
		return getFlexibleSearchService().<ConsumedDestinationModel> search(query).getResult();
	}

	@Override
	public List<ExposedDestinationModel> findExposedDestinationsByCredentialId(final String credentialId)
	{
		final Map queryParams = new HashMap();
		queryParams.put(ID_PARAMETER, credentialId);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_EXPOSED_DESTINATIONS_BY_CREDENTIAL_ID,queryParams);
		return getFlexibleSearchService().<ExposedDestinationModel> search(query).getResult();
	}
}
