/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.utility.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

/**
 * A generator to generate a non-negative integer to be used with the IDs of both EndPoint and ExposedDestination when automatically creating them in InboundChannelConfiguration.
 * The generated number is based on what is currently the highest number associated with either the IDs of EndPoint or ExposedDestination that follows a specific pattern.
 * The IDs that are checked for creating the new number is of the format cc-iocode-{number}-metadata for EndPoint and cc-iocode-{number} for ExposedDestination.
 * The iocode in the ID is the lower case Integration Object Code and number in the ID is a optional positive number.
 *
 * Example:
 * If a user creates an InboundChannelConfiguration with IO "ProductIOTest" for the first time and links a DestinationTarget with it,
 * EndPoint: cc-productiotest-metadata, and
 * ExposedDestination: cc-productiotest
 * will be automatically created in the backend.
 *
 * If the user links another DestinationTarget with the ICC,
 * EndPoint: cc-productiotest-1-metadata, and
 * ExposedDestination: cc-productiotest-1
 * will be automatically created with plus-1 integer in the backend.
 */
public class NameSequenceNumberGenerator
{
	private final FlexibleSearchService flexibleSearchService;
	private static final String END_POINT_TABLE = "EndPoint";
	private static final String EXPOSED_DESTINATION_TABLE = "ExposedDestination";

	public NameSequenceNumberGenerator(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * Provides a non-negative integer value that could be added to the IDs of both newly created EndPoint and ExposedDestination.The number returned is: plus 1 the highest number which is currently associated with
	 * either the IDs of EndPoint model or ExposedDestination model. Zero is returned when there is no number associated with the IDs of neither EndPoint model nor ExposedDestination model.
	 * The IDs that are checked before creating a new number is of the format cc-iocode-{number}-metadata for EndPoint and cc-iocode-{number} for ExposedDestination,
	 * where the iocode in the ID is the lower case Integration Object Code and number in the ID is a optional positive number.
	 *
	 * @param ioCode the code of the IntegrationObject for which InboundChannelConfiguration is created
	 * @return A non negative integer which can be different for subsequent calls with the same ioCode.
	 * The number generated is to be used in the same format as mentioned with the same iocode to avoid getting conflicts with other EndPoint or ExposedDestination IDs
	 */
	@Nonnegative
	public int getGeneratedNumber(@NotNull String ioCode)
	{
		ioCode = ioCode.toLowerCase();
		final List<String> endPointIDs = getIDs(END_POINT_TABLE, ioCode);
		final List<String> exposedDestinationIDs = getIDs(EXPOSED_DESTINATION_TABLE, ioCode);
		final List<Integer> endPointIDNumbers = extractNumberFromIDs(endPointIDs, ioCode);
		final List<Integer> exposedDestinationIDNumbers = extractNumberFromIDs(exposedDestinationIDs, ioCode);
		final int availableHighestNumber1 = incrementHighestIDNumberByOne(endPointIDNumbers);
		final int availableHighestNumber2 = incrementHighestIDNumberByOne(exposedDestinationIDNumbers);
		return Math.max(availableHighestNumber1, availableHighestNumber2);
	}

	private int incrementHighestIDNumberByOne(final List<Integer> listIDNumbers)
	{
		listIDNumbers.sort(Comparator.reverseOrder());
		return listIDNumbers.isEmpty() ? 0 : (listIDNumbers.get(0) + 1);
	}

	private List<String> getIDs(final String tableName, final String ioCode)
	{
		final FlexibleSearchQuery query = getSearchQuery(tableName, ioCode);
		final SearchResult<String> result = flexibleSearchService.search(query);
		return filterIDs(result.getResult(), getFilterPattern(tableName, ioCode));
	}

	private List<String> filterIDs(final List<String> listOfIDs, final String patternString)
	{
		final Pattern pattern = Pattern.compile(patternString);
		return listOfIDs.stream()
		                .filter(id -> pattern.matcher(id).matches())
		                .collect(Collectors.toList());
	}

	private List<Integer> extractNumberFromIDs(final List<String> listOfIDs, final String ioName)
	{
		return listOfIDs.stream().map(id -> this.stripOutNonDigits(id, ioName)).map(this::convertToInt)
		                .collect(Collectors.toList());
	}

	private int convertToInt(final String id)
	{
		return StringUtils.isEmpty(id) ? 0 : Integer.parseInt(id);
	}

	private String getFilterPattern(final String tableName, final String ioCode)
	{
		return END_POINT_TABLE.equals(tableName) ? getPatternForEndPoint(ioCode) : getPatternForExposedDestination(ioCode);
	}

	private String getPatternForEndPoint(final String ioCode)
	{
		return "cc-" + ioCode + "-(?:\\d+-)?metadata";
	}

	private String getPatternForExposedDestination(final String ioCode)
	{
		return "cc-" + ioCode + "(?:-\\d+)?";
	}

	private FlexibleSearchQuery getSearchQuery(final String tableName, final String ioCode)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("ioCode", ioCode);
		final String queryString = getQuery(tableName);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(List.of(String.class));
		query.addQueryParameters(params);
		return query;
	}

	private String getQuery(final String tableName)
	{
		return "SELECT {id} FROM {" + tableName + "} WHERE {id} LIKE CONCAT('cc-',?ioCode,'%')";
	}

	/**
	 * As is described in JavaDocs of NameSequenceNumberGenerator and getGeneratedNumber(@NotNull String ioCode), this method is the key to extract
	 * auto-generated integer from the Endpoint or ExposedDestination names.
	 *
	 * @param id The name of an Endpoint or ExposedDestination.
	 * @param ioName The IO's name based on which the ICC is created.
	 * @return A non negative integer which can be different for subsequent calls with the same ioCode.
	 * The number generated is to be used in the same format as mentioned with the same iocode to avoid getting conflicts with other EndPoint or ExposedDestination IDs
	 */
	private String stripOutNonDigits(final String id, final String ioName)
	{
		Pattern idPattern = Pattern.compile("cc-" + ioName + "-?(?<nameNum>\\d+)?(?<metadata>-metadata)?");
		Matcher idMatcher = idPattern.matcher(id);
		if(idMatcher.find())
		{
			String autoCreatedNumber = idMatcher.group("nameNum");
			return autoCreatedNumber != null? autoCreatedNumber: "";
		}
		return "";
	}
}
