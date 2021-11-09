/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException
import de.hybris.platform.integrationservices.item.LocalizedValue
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

@UnitTest
class DefaultPrimitiveValueHandlerUnitTest extends Specification {

	private static final int HOUR_IN_MS = 3600000

	def localizationService = Stub IntegrationLocalizationService
	def handler = new DefaultPrimitiveValueHandler(localizationService: localizationService)

	@Test
	@Unroll
	def "convert payload value with non-localized #clazz.simpleName attribute"() {
		given:
		def descriptor = attributeDescriptor clazz

		expect:
		handler.convert(descriptor, payloadValue) == expectedValue

		where:
		clazz      | payloadValue           | expectedValue
		Calendar   | Calendar.getInstance() | payloadValue.getTime()
		String     | 'value'                | payloadValue
		Character  | 'C'                    | payloadValue as Character
		Integer    | 55                     | payloadValue
		Boolean    | true                   | payloadValue
		BigInteger | '9223372036854775809'  | payloadValue as BigInteger
	}

	@Test
	@Unroll
	def "convert payload value with localized #type.simpleName attribute having [en:#enValue, fr:#frValue] value"() {
		given:
		def descriptor = localizedAttributeDescriptor type

		expect:
		handler.convert(descriptor, LocalizedValue.of(Locale.ENGLISH, enValue).combine(Locale.FRENCH, frValue)) == [(Locale.ENGLISH): expectEnValue, (Locale.FRENCH): expectedFrValue]

		where:
		type       | enValue                              | frValue                             | expectEnValue         | expectedFrValue
		String     | 'Hello'                              | 'Bonjour'                           | enValue               | frValue
		Boolean    | true                                 | false                               | enValue               | frValue
		Calendar   | Calendar.getInstance(Locale.ENGLISH) | Calendar.getInstance(Locale.FRENCH) | enValue.time          | frValue.time
		Integer    | 1                                    | 2                                   | enValue               | frValue
		Character  | 'E'                                  | 'F'                                 | enValue as Character  | frValue as Character
		BigInteger | '9223372036854775809'                | '92233720368547758010'              | enValue as BigInteger | frValue as BigInteger
	}

	@Test
	def "convert value with invalid locale re-throws exception from localizationService"() {
		given:
		def descriptor = localizedAttributeDescriptor String
		and: 'locale is not supported'
        localizationService.validateLocale(_ as Locale) >> { throw new LocaleNotSupportedException(Locale.KOREAN) }

		when:
		handler.convert descriptor, LocalizedValue.of(Locale.KOREAN, null)

		then:
		thrown LocaleNotSupportedException
	}

	@Test
	def "convert value with invalid character value throws exception"() {
		given:
		def descriptor = Stub(TypeAttributeDescriptor) {
			isPrimitive() >> true
			isSettable(_) >> true
			getAttributeType() >> Stub(TypeDescriptor) {
				getTypeCode() >> Character.class.name
			}
		}

		when:
		handler.convert descriptor, 55

		then:
		thrown InvalidAttributeValueException
	}

	@Test
	@Unroll
	def "convert sets calendar with #condition"() {
		given: 'a Calendar with date time 9999-12-31T23:59:59 in a timezone one hour before this one'
		def msOneHourBeforeCurrentTimeZone = TimeZone.getDefault().getRawOffset() - HOUR_IN_MS
		def timeZoneIdOneHourBeforeCurrentTimeZone = TimeZone.getAvailableIDs(msOneHourBeforeCurrentTimeZone)[0]
		def timeZoneOneHourBeforeCurrentTimeZone = TimeZone.getTimeZone timeZoneIdOneHourBeforeCurrentTimeZone
		def year9999InTimeZoneOneHourBeforeCurrentTimeZone = new Calendar.Builder()
				.setTimeZone(timeZoneOneHourBeforeCurrentTimeZone)
				.setDate(year, Calendar.DECEMBER, 31)
				.setTimeOfDay(23, 59, 59)
				.build()

		and: 'a date attribute'
		def descriptor = attributeDescriptor Calendar

		when:
		Date year10kResetTo9999 = handler.convert(descriptor, year9999InTimeZoneOneHourBeforeCurrentTimeZone) as Date

		then:
		def instant = Instant.ofEpochMilli(year10kResetTo9999.getTime())
		def localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
		with(localDateTime) {
			getYear() == expectedYear
			getMonth() == expectedMonth
			getDayOfMonth() == expectedDay
			getHour() == expectedHour
			getMinute() == expectedMin
			getSecond() == expectedSec
		}

		where:
		condition                                                             | year | expectedYear | expectedMonth  | expectedDay | expectedHour | expectedMin | expectedSec
		'year greater than 9999 in current timezone is set to 9999'           | 9999 | 9999         | Month.DECEMBER | 31          | 23           | 59          | 59
		'year less than 9999 in current timezone rolls over to the next year' | 9998 | 9999         | Month.JANUARY  | 1           | 0            | 59          | 59
	}

	private TypeAttributeDescriptor localizedAttributeDescriptor(Class attrType = String.class) {
		def attribute = attributeDescriptor(attrType)
		attribute.isLocalized() >> true
		attribute
	}

	private TypeAttributeDescriptor attributeDescriptor(Class attrType = String.class) {
		Stub(TypeAttributeDescriptor) {
			isPrimitive() >> true
			getAttributeType() >> Stub(TypeDescriptor) {
				getTypeCode() >> attrType.name
			}
			isSettable(_) >> true
		}
	}
}
