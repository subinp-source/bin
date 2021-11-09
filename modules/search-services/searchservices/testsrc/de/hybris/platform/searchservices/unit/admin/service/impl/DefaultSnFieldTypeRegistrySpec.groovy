/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.admin.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo
import de.hybris.platform.searchservices.admin.service.impl.DefaultSnFieldTypeRegistry
import de.hybris.platform.searchservices.enums.SnFieldType

import java.util.stream.Collectors

import org.junit.Test
import org.springframework.context.ApplicationContext

import spock.lang.Specification


@UnitTest
public class DefaultSnFieldTypeRegistrySpec extends Specification {

	static final String FIELD_TYPE_INFO_1_BEAN_ID = "fieldTypeInfo1"
	static final String FIELD_TYPE_INFO_2_BEAN_ID = "fieldTypeInfo2"

	ApplicationContext applicationContext = Mock()

	SnFieldTypeInfo fieldTypeInfo1 = Mock()
	SnFieldTypeInfo fieldTypeInfo2 = Mock()

	DefaultSnFieldTypeRegistry createFieldTypeRegistry() {
		final DefaultSnFieldTypeRegistry fieldTypeRegistry = new DefaultSnFieldTypeRegistry()
		fieldTypeRegistry.setApplicationContext(applicationContext)

		fieldTypeRegistry.afterPropertiesSet()

		return fieldTypeRegistry
	}

	@Test
	def "No field type info configured"() {
		given:
		final Map<String, SnFieldTypeInfo> fieldtypeInfoBeans = new HashMap<>()

		applicationContext.getBeansOfType(SnFieldTypeInfo.class) >> fieldtypeInfoBeans

		final DefaultSnFieldTypeRegistry fieldTypeRegistry = createFieldTypeRegistry()

		when:
		List<SnFieldTypeInfo> fieldtypeInfos = SnFieldType.values().stream().map(fieldTypeRegistry.&getFieldTypeInfo)
			.collect(Collectors.toList())

		then:
		fieldtypeInfos.every {
			it == null
		}
	}

	@Test
	def "Single field type info configured"() {
		given:
		fieldTypeInfo1.getFieldType() >> SnFieldType.STRING

		final Map<String, SnFieldTypeInfo> fieldtypeInfoBeans = [
			(FIELD_TYPE_INFO_1_BEAN_ID): fieldTypeInfo1
		]

		applicationContext.getBeansOfType(SnFieldTypeInfo.class) >> fieldtypeInfoBeans

		final DefaultSnFieldTypeRegistry fieldTypeRegistry = createFieldTypeRegistry()

		when:
		SnFieldTypeInfo resultFieldTypeInfo = fieldTypeRegistry.getFieldTypeInfo(SnFieldType.STRING)

		then:
		resultFieldTypeInfo.is(fieldTypeInfo1)
	}

	@Test
	def "Multiple field type infos configured"() {
		given:
		fieldTypeInfo1.getFieldType() >> SnFieldType.STRING
		fieldTypeInfo2.getFieldType() >> SnFieldType.INTEGER

		final Map<String, SnFieldTypeInfo> fieldtypeInfoBeans = [
			(FIELD_TYPE_INFO_1_BEAN_ID): fieldTypeInfo1,
			(FIELD_TYPE_INFO_2_BEAN_ID): fieldTypeInfo2
		]

		applicationContext.getBeansOfType(SnFieldTypeInfo.class) >> fieldtypeInfoBeans

		final DefaultSnFieldTypeRegistry fieldTypeRegistry = createFieldTypeRegistry()

		when:
		SnFieldTypeInfo resultFieldTypeInfo1 = fieldTypeRegistry.getFieldTypeInfo(SnFieldType.STRING)
		SnFieldTypeInfo resultFieldTypeInfo2 = fieldTypeRegistry.getFieldTypeInfo(SnFieldType.INTEGER)

		then:
		resultFieldTypeInfo1.is(fieldTypeInfo1)
		resultFieldTypeInfo2.is(fieldTypeInfo2)
	}
}
