/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

@UnitTest
class WhereClauseConditionUnitTest extends Specification {

    private static final def INCOMPLETE_TIME_PATTERN = Pattern.compile(":[0-9]'")
    private static final def DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("''yyyy-MM-dd HH:mm:ss.SSS''")

    @Test
	@Unroll
	def "condition1 #condition1 == condition2 #condition2 is #equal"() {
		expect:
		(condition1 == condition2) == equal
		(condition1.hashCode() == condition2.hashCode()) == equal

		where:
		condition1                                               | condition2                                                   | equal
		new WhereClauseCondition("abc")                          | new WhereClauseCondition("abc")                              | true
		new WhereClauseCondition("abc", ConjunctiveOperator.AND) | new WhereClauseCondition("abc", ConjunctiveOperator.AND)     | true
		new WhereClauseCondition("abc")                          | new WhereClauseCondition("abc", ConjunctiveOperator.UNKNOWN) | true
		new WhereClauseCondition("abc")                          | new WhereClauseCondition("abc", null)                        | true
		new WhereClauseCondition("abc")                          | new WhereClauseCondition("abc", ConjunctiveOperator.AND)     | false
		new WhereClauseCondition("abc")                          | new WhereClauseCondition("def")                              | false

	}

	@Test
	@Unroll
	def "extract attribute name from condition: '#condition'"() {
		given:
		def filter = new WhereClauseCondition(condition)

		when:
		def name = filter.getAttributeName()

		then:
		expectedName == name

		where:
		condition      | expectedName
		"{code} = 123" | "code"
		""             | ""
		"code = 123"   | ""
	}

	@Test
	@Unroll
	def "extract attribute value from condition: '#condition'"() {
		given:
		def filter = new WhereClauseCondition(condition)

		when:
		def value = filter.getAttributeValue()

		then:
		expectedValue == value

		where:
		condition      | expectedValue
		"{code} = 123" | "123"
		""             | ""
		"code * 123"   | ""
	}

	@Test
	def "getting the condition from the where clause condition"() {
		given:
		def whereClauseCondition = new WhereClauseCondition("some condition")

		expect:
		"some condition" == whereClauseCondition.getCondition()
	}

	@Test
	def "getting the conjunctive operator from the where clause condition"() {
		given:
		def whereClauseCondition = new WhereClauseCondition("some condition", ConjunctiveOperator.AND)

		expect:
		ConjunctiveOperator.AND == whereClauseCondition.getConjunctiveOperator()
	}

	@Test
	@Unroll
	def "extract compare operator from condition: '#condition'"() {
		given:
		def filter = new WhereClauseCondition(condition)

		when:
		def operator = filter.getCompareOperator()

		then:
		expectedOperator == operator

		where:
		condition      | expectedOperator
		"{code} = 123" | "= "
		""             | "IN "
		"code IN 123"  | "IN "
	}

	@Test
	def 'attribute name can be changed in a condition'() {
		given:
		def original = new WhereClauseCondition('template', 'attributeName', '=', 'code', ConjunctiveOperator.OR)

		when:
		def updated = original.changeAttributeName('qualifier')

		then: 'all fields are copied except the attribute name'
		updated?.condition == original.condition
		updated?.attributeName == 'qualifier'
		updated?.compareOperator == original.compareOperator
		updated?.attributeValue == original.attributeValue
		updated?.conjunctiveOperator == original.conjunctiveOperator
		and: 'the original did not mutate'
		!original.is(updated)
		original.attributeName == 'attributeName'
	}

    @Test
    @Unroll
    def "format and convert UTC time [#utcTimestamp] to local system time [#systemTimestamp] for flexible search"() {
        when:
        def condition = new WhereClauseCondition('template', 'attributeName', '=', utcTimestamp, ConjunctiveOperator.AND)
        def formattedCondition = condition.changeAttributeNameAndFormatDateValue("aliasName")

        then:
        systemTimestamp == formattedCondition.attributeValue

        where:
        utcTimestamp                 | systemTimestamp
        "'2020-02-18T16:33:12.0000'" | formatDateValue(utcTimestamp)
        "'2020-02-18T16:33:12.000'"  | formatDateValue(utcTimestamp)
        "'2020-02-18T16:33:12.0'"    | formatDateValue(utcTimestamp)
        "'2020-02-18T16:33:12'"      | formatDateValue(utcTimestamp)
        "'2020-02-18T16:33:1'"       | formatDateValue(utcTimestamp)
        "'2020-02-18T16:33'"         | formatDateValue(utcTimestamp)
        "'2020-02-18T16:3'"          | formatDateValue(utcTimestamp)
    }

    def formatDateValue(final String localDateTime) {
        def dateTimeValue = LocalDateTime.parse(prepareDateTimeValue(localDateTime))
        def utcDateTime = dateTimeValue.atZone(ZoneOffset.UTC)
        def systemDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())
        return DATE_TIME_FORMATTER.format(systemDateTime.toLocalDateTime())
    }

    def prepareDateTimeValue(final String dateTimeValue) {
        final def preparedDateTimeValue = new StringBuilder(dateTimeValue.replace("'", ""));
        return INCOMPLETE_TIME_PATTERN.matcher(dateTimeValue).find() ?
                preparedDateTimeValue.append("0").toString() : preparedDateTimeValue.toString()
    }

}
