/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Helper class for API export to kyma
 */
public class KymaApiExportHelper
{
	public static final String API_REG_SERVICE_ID = "kymaintegrationservices.kyma_api_reg_service_id";
	public static final String DEFAULT_API_REG_SERVICE_ID = "kyma-services";
	public static final String GETINFO_DESTINATION_ID_KEY = "kymaintegrationservices.kyma_getinfo_consumed_destination_id";
	public static final String DEFAULT_GETINFO_DESTINATION_ID = "kyma-getinfo";
	public static final String RENEWAL_SERVICE_ID = "kymaintegrationservices.kyma_renewal_consumed_destination_id";
	public static final String DEFAULT_RENEWAL_SERVICE_ID = "kyma-renewal";


	private KymaApiExportHelper()
	{
	}

	/**
	 * Method to get formatted destinationId for kyma
	 *
	 * @param exposedDestination ExposedDestination
	 * @return formatted destinationId
	 */
	public static String getDestinationId(final ExposedDestinationModel exposedDestination)
	{
		return exposedDestination.getId() + "-" + exposedDestination.getEndpoint().getVersion();
	}

	/**
	 * Method to check if two urls strings correspond to the same destination
	 *
	 * @param urlString1 Url String
	 * @param urlString2 Url String
	 * @return true if urls are equal
	 */
	public static boolean isUrlsEqualIgnoringQuery(final String urlString1, final String urlString2) throws URISyntaxException
	{
		return getUrlWithoutParameters(urlString1).equals(getUrlWithoutParameters(urlString2));
	}

	private static URI getUrlWithoutParameters(final String url) throws URISyntaxException
	{
		final URI uri = new URI(url).normalize();
		return new URI(uri.getScheme(), uri.getAuthority(), removeLastSlashFromPath(uri.getPath()), null, null);
	}

	private static String removeLastSlashFromPath(final String path)
	{
		if (path == null)
		{
			return null;
		}
		if (path.endsWith("/"))
		{
			return path.substring(0, path.length() - 1);
		}
		return path;
	}
}
