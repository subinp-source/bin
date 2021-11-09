/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.admin.service.impl

import static org.assertj.core.api.Assertions.assertThat

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.admin.service.SnFieldProvider
import de.hybris.platform.searchservices.admin.service.impl.DefaultSnFieldFactory
import de.hybris.platform.searchservices.admin.service.impl.DefaultSnFieldProviderDefinition

import org.junit.Test
import org.springframework.context.ApplicationContext

import spock.lang.Specification


@UnitTest
public class DefaultSnFieldFactorySpec extends Specification {

	static final String FIELD_PROVIDER_1_BEAN_ID = "fieldProvider1"
	static final String FIELD_PROVIDER_2_BEAN_ID = "fieldProvider2"

	static final int HIGH_PRIORITY = 200
	static final int LOW_PRIORITY = 50

	ApplicationContext applicationContext = Mock()

	SnIndexType indexType = Mock()

	SnFieldProvider fieldProvider1 = Mock()
	SnFieldProvider fieldProvider2 = Mock()

	SnField field1 = Mock()
	SnField field2 = Mock()
	SnField field3 = Mock()
	SnField field4 = Mock()

	def setup() {
		applicationContext.getBean(FIELD_PROVIDER_1_BEAN_ID, SnFieldProvider.class) >> fieldProvider1
		applicationContext.getBean(FIELD_PROVIDER_2_BEAN_ID, SnFieldProvider.class) >> fieldProvider2
	}

	DefaultSnFieldFactory createFieldFactory() {
		final DefaultSnFieldFactory fieldFactory = new DefaultSnFieldFactory()
		fieldFactory.setApplicationContext(applicationContext)

		fieldFactory.afterPropertiesSet()

		return fieldFactory
	}

	@Test
	def "No field configured"() {
		given:
		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitions = new HashMap<>()

		applicationContext.getBeansOfType(DefaultSnFieldProviderDefinition.class) >> providerDefinitions

		final DefaultSnFieldFactory listenersFactory = createFieldFactory()

		when:
		final List<SnField> fields = listenersFactory.getDefaultFields(indexType)

		then:
		assertThat(fields).isEmpty()
	}

	@Test
	def "Single field configured from single provider"() {
		given:
		final DefaultSnFieldProviderDefinition providerDefinition =
			new DefaultSnFieldProviderDefinition(priority: HIGH_PRIORITY, fieldProvider: fieldProvider1)

		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitions = new HashMap<>()
		providerDefinitions.put(FIELD_PROVIDER_1_BEAN_ID, providerDefinition)

		applicationContext.getBeansOfType(DefaultSnFieldProviderDefinition.class) >> providerDefinitions
		fieldProvider1.getDefaultFields(indexType) >> List.of(field1)

		final DefaultSnFieldFactory listenersFactory = createFieldFactory()

		when:
		final List<SnField> fields = listenersFactory.getDefaultFields(indexType)

		then:
		assertThat(fields).containsExactly(field1)
	}

	@Test
	def "Mutiple fields configured from single provider 1"() {
		given:
		final DefaultSnFieldProviderDefinition providerDefinition =
			new DefaultSnFieldProviderDefinition(priority: HIGH_PRIORITY, fieldProvider: fieldProvider1)

		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitions = new HashMap<>()
		providerDefinitions.put(FIELD_PROVIDER_1_BEAN_ID, providerDefinition)

		applicationContext.getBeansOfType(DefaultSnFieldProviderDefinition.class) >> providerDefinitions
		fieldProvider1.getDefaultFields(indexType) >> List.of(field1, field2)

		final DefaultSnFieldFactory listenersFactory = createFieldFactory()

		when:
		final List<SnField> fields = listenersFactory.getDefaultFields(indexType)

		then:
		assertThat(fields).containsExactly(field1, field2)
	}

	@Test
	def "Mutiple fields configured from single provider 2"() {
		given:
		final DefaultSnFieldProviderDefinition providerDefinition =
			new DefaultSnFieldProviderDefinition(priority: HIGH_PRIORITY, fieldProvider: fieldProvider1)

		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitions = new HashMap<>()
		providerDefinitions.put(FIELD_PROVIDER_1_BEAN_ID, providerDefinition)

		applicationContext.getBeansOfType(DefaultSnFieldProviderDefinition.class) >> providerDefinitions
		fieldProvider1.getDefaultFields(indexType) >> List.of(field2, field1)

		final DefaultSnFieldFactory listenersFactory = createFieldFactory()

		when:
		final List<SnField> fields = listenersFactory.getDefaultFields(indexType)

		then:
		assertThat(fields).containsExactly(field2, field1)
	}

	@Test
	def "Multiple fields configured from multiple providers 1"() {
		given:
		final DefaultSnFieldProviderDefinition providerDefinition1 =
			new DefaultSnFieldProviderDefinition(priority: HIGH_PRIORITY, fieldProvider: fieldProvider1)

		final DefaultSnFieldProviderDefinition providerDefinition2 =
			new DefaultSnFieldProviderDefinition(priority: LOW_PRIORITY, fieldProvider: fieldProvider2)

		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitions = new HashMap<>()
		providerDefinitions.put(FIELD_PROVIDER_1_BEAN_ID, providerDefinition1)
		providerDefinitions.put(FIELD_PROVIDER_2_BEAN_ID, providerDefinition2)

		applicationContext.getBeansOfType(DefaultSnFieldProviderDefinition.class) >> providerDefinitions
		fieldProvider1.getDefaultFields(indexType) >> List.of(field1, field2)
		fieldProvider2.getDefaultFields(indexType) >> List.of(field3, field4)

		final DefaultSnFieldFactory listenersFactory = createFieldFactory()

		when:
		final List<SnField> fields = listenersFactory.getDefaultFields(indexType)

		then:
		assertThat(fields).containsExactly(field1, field2, field3, field4)
	}

	@Test
	def "Multiple fields configured from multiple providers 2"() {
		given:
		final DefaultSnFieldProviderDefinition providerDefinition1 =
			new DefaultSnFieldProviderDefinition(priority: LOW_PRIORITY, fieldProvider: fieldProvider1)

		final DefaultSnFieldProviderDefinition providerDefinition2 =
			new DefaultSnFieldProviderDefinition(priority: HIGH_PRIORITY, fieldProvider: fieldProvider2)

		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitions = new HashMap<>()
		providerDefinitions.put(FIELD_PROVIDER_1_BEAN_ID, providerDefinition1)
		providerDefinitions.put(FIELD_PROVIDER_2_BEAN_ID, providerDefinition2)

		applicationContext.getBeansOfType(DefaultSnFieldProviderDefinition.class) >> providerDefinitions
		fieldProvider1.getDefaultFields(indexType) >> List.of(field1, field2)
		fieldProvider2.getDefaultFields(indexType) >> List.of(field3, field4)

		final DefaultSnFieldFactory listenersFactory = createFieldFactory()

		when:
		final List<SnField> fields = listenersFactory.getDefaultFields(indexType)

		then:
		assertThat(fields).containsExactly(field3, field4, field1, field2)
	}
}
