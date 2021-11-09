/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a where clause condition (e.g. "{code} = abc"). It doesn't include the keyword 'WHERE' in the condition.
 * The WhereClauseCondition can also store the conjunctive operator (AND, OR, etc.) to the next WhereClauseCondition.
 */
public class WhereClauseCondition
{
	private final String template;
	private final String attributeName;
	private final String attributeValue;
	private final String compareOperator;
	private final String condition;
	private final ConjunctiveOperator conjunctiveOperator;
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("''yyyy-MM-dd HH:mm:ss.SSS''");
	private static final Pattern INCOMPLETE_TIME_PATTERN = Pattern.compile(":[0-9]'");

	/**
	 * @param condition Where clause condition (e.g. "{code} = abc")
	 * @deprecated since 1905.09-CEP Please use a constructor from one of the subclasses that extend {@link WhereClauseCondition}
	 * <p>
	 * Stores the where clause condition without the conjunctive operator (AND, OR, etc) to the next WhereClauseCondition
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public WhereClauseCondition(final String condition)
	{
		this.condition = condition;
		this.template = "{%s} %s%s";
		this.conjunctiveOperator = ConjunctiveOperator.UNKNOWN;
		this.attributeName = extractAttributeNameFromCondition();
		this.compareOperator = calculateOperator();
		this.attributeValue = extractAttributeValueFromCondition();
	}

	/**
	 * @param condition           Where clause condition
	 * @param conjunctiveOperator Where clause conjunctive operator
	 * @deprecated since 1905.09-CEP Please use a constructor from one of the subclasses that extend {@link WhereClauseCondition}
	 * <p>
	 * Stores the where clause condition  with a conjunctive operator (e.g. AND, OR, etc) to the next WhereClauseCondition
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public WhereClauseCondition(final String condition, final ConjunctiveOperator conjunctiveOperator)
	{
		this.condition = condition;
		this.template = "{%s} %s%s";
		this.conjunctiveOperator = conjunctiveOperator != null ? conjunctiveOperator : ConjunctiveOperator.UNKNOWN;
		this.attributeName = extractAttributeNameFromCondition();
		this.compareOperator = calculateOperator();
		this.attributeValue = extractAttributeValueFromCondition();
	}

	/**
	 * Creates an instance of a where clause condition
	 *
	 * @param conditionTemplate - condition template
	 * @param attributeName     - attribute name
	 * @param compareOperator   - compare operator (e.g. "=" or "IN")
	 * @param attributeValue    - attribute value
	 */
	protected WhereClauseCondition(final String conditionTemplate, final String attributeName, final String compareOperator,
	                               final String attributeValue)
	{
		this(conditionTemplate, attributeName, compareOperator, attributeValue, ConjunctiveOperator.UNKNOWN);
	}

	/**
	 * Creates an instance of a where clause condition with a conjunctive operator
	 *
	 * @param conditionTemplate   - condition template
	 * @param attributeName       - attribute name
	 * @param compareOperator     - compare operator (e.g. "=" or "IN")
	 * @param attributeValue      - attribute value
	 * @param conjunctiveOperator - conjunctive operator
	 */
	protected WhereClauseCondition(final String conditionTemplate, final String attributeName, final String compareOperator,
	                               final String attributeValue, final ConjunctiveOperator conjunctiveOperator)
	{
		this.condition = String.format(conditionTemplate, attributeName, compareOperator, attributeValue);
		this.conjunctiveOperator = conjunctiveOperator != null ? conjunctiveOperator : ConjunctiveOperator.UNKNOWN;
		this.attributeName = attributeName;
		this.compareOperator = compareOperator;
		this.attributeValue = attributeValue;
		this.template = conditionTemplate;
	}

	/**
	 * Creates a duplicate of the @param whereCC with the @param operator
	 *
	 * @param whereCC  - where clause condition
	 * @param operator - conjunctive operator
	 */
	protected WhereClauseCondition(final WhereClauseCondition whereCC, final ConjunctiveOperator operator)
	{
		this(whereCC.template, whereCC.attributeName, whereCC.compareOperator, whereCC.attributeValue, operator);
	}

	public String getCondition()
	{
		return condition;
	}

	/**
	 * Extracts the attributeName from the condition.
	 * Example filter condition: "{supercategories} = 8796093055118" -> returns "supercategories"
	 *
	 * @return attributeName or "" if no attributeName is not found
	 */
	public String getAttributeName()
	{
		return attributeName;
	}

	/**
	 * Extracts the attribute value from the condition.
	 * Example filter condition "{supercategories} = 8796093055118" -> returns "8796093055118"
	 *
	 * @return attributeValue or "" if no value is found
	 */
	String getAttributeValue()
	{
		return attributeValue;
	}

	private String calculateOperator()
	{
		return condition.contains(" = ") ? "= " : "IN ";
	}

	private String extractAttributeNameFromCondition()
	{
		final int openCurlyIndex = condition.indexOf('{');
		final int closeCurlyIndex = condition.indexOf('}');
		if (openCurlyIndex > -1 && closeCurlyIndex > -1)
		{
			return condition.substring(openCurlyIndex + 1, closeCurlyIndex);
		}
		return "";
	}

	private String extractAttributeValueFromCondition()
	{
		final int operatorIndex = condition.lastIndexOf(compareOperator);
		if (operatorIndex > -1)
		{
			return condition.substring(operatorIndex + compareOperator.length());
		}
		return "";
	}

	public ConjunctiveOperator getConjunctiveOperator()
	{
		return conjunctiveOperator;
	}

	String getConjunctiveOperatorString()
	{
		return conjunctiveOperator.toString();
	}

	public WhereClauseConditions toWhereClauseConditions()
	{
		return new WhereClauseConditions(this);
	}

	protected String getCompareOperator()
	{
		return compareOperator;
	}

	/**
	 * Creates a copy of this condition, in which attribute name is replaced with the specified one.
	 *
	 * @param newName attribute name to be used in the new copy of this condition
	 * @return a new condition that has all fields except the attribute name set to values in this condition; name
	 * is set to the {@code newName}.
	 */
	WhereClauseCondition changeAttributeName(final String newName)
	{
		return new WhereClauseCondition(template, newName, compareOperator, attributeValue, conjunctiveOperator);
	}

	/**
	 * Creates a copy of this condition, in which attribute name is replaced with the specified one.
	 * The time zone is converted from UTC to the system time zone to format the date value properly to be used in flexible search.
	 *
	 * @param newName attribute name to be used in the new copy of this condition
	 * @return a new condition that has all fields except the attribute name set to values in this condition; name
	 * is set to the {@code newName}.
	 */
	WhereClauseCondition changeAttributeNameAndFormatDateValue(final String newName)
	{
		final LocalDateTime dateTime = LocalDateTime.parse(prepareDateTimeValue(attributeValue));
		final LocalDateTime systemDateTime = convertUTCTimeZoneToSystemTimeZone(dateTime);
		final String formattedSystemDateTime = DATE_TIME_FORMATTER.format(systemDateTime);
		return new WhereClauseCondition(template, newName, compareOperator, formattedSystemDateTime, conjunctiveOperator);
	}

	private LocalDateTime convertUTCTimeZoneToSystemTimeZone(final LocalDateTime dateTimeValue)
	{
		final ZonedDateTime utcDateTime = dateTimeValue.atZone(ZoneOffset.UTC);
		final ZonedDateTime systemDateTime = utcDateTime.withZoneSameInstant(getSystemTimeZone());
		return systemDateTime.toLocalDateTime();
	}

	private ZoneId getSystemTimeZone()
	{
		return ZoneId.systemDefault();
	}

	private String prepareDateTimeValue(final String dateTimeValue)
	{
		final StringBuilder preparedDateTimeValue = new StringBuilder(dateTimeValue.replace("'", ""));
		return INCOMPLETE_TIME_PATTERN.matcher(dateTimeValue).find() ?
				preparedDateTimeValue.append("0").toString() : preparedDateTimeValue.toString();
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final WhereClauseCondition that = (WhereClauseCondition) o;

		return new EqualsBuilder()
				.append(condition, that.condition)
				.append(conjunctiveOperator, that.conjunctiveOperator)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(condition)
				.append(conjunctiveOperator)
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "WhereClauseCondition{" +
				"condition='" + condition + '\'' +
				", conjunctiveOperator='" + conjunctiveOperator + '\'' +
				'}';
	}

}
