/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.core.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.admin.service.SnFieldProvider
import de.hybris.platform.searchservices.constants.SearchservicesConstants
import de.hybris.platform.searchservices.core.service.SnContext
import de.hybris.platform.searchservices.core.service.SnQualifierProvider
import de.hybris.platform.searchservices.core.service.SnQualifierType
import de.hybris.platform.searchservices.core.service.impl.DefaultSnQualifierType
import de.hybris.platform.searchservices.core.service.impl.DefaultSnQualifierTypeFactory

import org.junit.Test
import org.springframework.context.ApplicationContext

import spock.lang.Specification


@UnitTest
public class DefaultSnQualifierTypeFactorySpec extends Specification {

	static final String LANGUAGE_QUALIFIER_TYPE_BEAN_ID = "languageQualifierType"
	static final String CURRENCY_QUALIFIER_TYPE_BEAN_ID = "currencyQualifierType"

	ApplicationContext applicationContext = Mock()

	SnQualifierType languageQualifierType
	SnQualifierType currencyQualifierType

	SnQualifierProvider languageQualifierProvider = Mock()
	SnQualifierProvider currencyQualifierProvider = Mock()

	SnContext context = Mock()
	SnField field = Mock()

	def setup() {
		languageQualifierType = new DefaultSnQualifierType(id: SearchservicesConstants.LANGUAGE_QUALIFIER_TYPE, qualifierProvider: languageQualifierProvider)
		currencyQualifierType = new DefaultSnQualifierType(id: SearchservicesConstants.CURRENCY_QUALIFIER_TYPE, qualifierProvider: currencyQualifierProvider)

		applicationContext.getBean(LANGUAGE_QUALIFIER_TYPE_BEAN_ID, SnFieldProvider.class) >> languageQualifierType
		applicationContext.getBean(CURRENCY_QUALIFIER_TYPE_BEAN_ID, SnFieldProvider.class) >> currencyQualifierType
	}

	DefaultSnQualifierTypeFactory createQualifierTypeFactory() {
		DefaultSnQualifierTypeFactory qualifierTypeFactory = new DefaultSnQualifierTypeFactory()
		qualifierTypeFactory.setApplicationContext(applicationContext)

		qualifierTypeFactory.afterPropertiesSet()

		return qualifierTypeFactory
	}

	@Test
	def "No qualifier type configured"() {
		given:
		Map<String, SnQualifierType> qualifierTypeBeans = new HashMap<>()

		applicationContext.getBeansOfType(SnQualifierType.class) >> qualifierTypeBeans

		DefaultSnQualifierTypeFactory qualifierTypeFactory = createQualifierTypeFactory()

		when:
		List<String> qualifierTypeIds = qualifierTypeFactory.getAllQualifierTypeIds()
		List<SnQualifierType> qualifierTypes = qualifierTypeFactory.getAllQualifierTypes()

		then:
		qualifierTypeIds != null
		qualifierTypeIds.size() == 0

		qualifierTypes != null
		qualifierTypes.size() == 0
	}

	@Test
	def "Single qualifier type configured"() {
		given:
		Map<String, SnQualifierType> qualifierTypeBeans = new HashMap<>()
		qualifierTypeBeans.put(LANGUAGE_QUALIFIER_TYPE_BEAN_ID, languageQualifierType)

		applicationContext.getBeansOfType(SnQualifierType.class) >> qualifierTypeBeans

		DefaultSnQualifierTypeFactory qualifierTypeFactory = createQualifierTypeFactory()

		when:
		List<String> qualifierTypeIds = qualifierTypeFactory.getAllQualifierTypeIds()
		List<SnQualifierType> qualifierTypes = qualifierTypeFactory.getAllQualifierTypes()

		then:
		qualifierTypeIds != null
		qualifierTypeIds.size() == 1
		qualifierTypeIds[0] == SearchservicesConstants.LANGUAGE_QUALIFIER_TYPE

		qualifierTypes != null
		qualifierTypes.size() == 1
		qualifierTypes[0] == languageQualifierType
	}

	@Test
	def "Multiple qualifier types configured"() {
		given:
		Map<String, SnQualifierType> qualifierTypeBeans = new HashMap<>()
		qualifierTypeBeans.put(LANGUAGE_QUALIFIER_TYPE_BEAN_ID, languageQualifierType)
		qualifierTypeBeans.put(CURRENCY_QUALIFIER_TYPE_BEAN_ID, currencyQualifierType)

		applicationContext.getBeansOfType(SnQualifierType.class) >> qualifierTypeBeans

		DefaultSnQualifierTypeFactory qualifierTypeFactory = createQualifierTypeFactory()

		when:
		List<String> qualifierTypeIds = qualifierTypeFactory.getAllQualifierTypeIds()
		List<SnQualifierType> qualifierTypes = qualifierTypeFactory.getAllQualifierTypes()

		then:
		qualifierTypeIds != null
		qualifierTypeIds.size() == 2

		qualifierTypeIds.any {
			it == SearchservicesConstants.LANGUAGE_QUALIFIER_TYPE
		}

		qualifierTypeIds.any {
			it == SearchservicesConstants.CURRENCY_QUALIFIER_TYPE
		}

		qualifierTypes != null
		qualifierTypes.size() == 2

		qualifierTypes.any {
			it == languageQualifierType
		}

		qualifierTypes.any {
			it == currencyQualifierType
		}
	}

	@Test
	def "No qualifier type is returned for field"() {
		given:
		Map<String, SnQualifierType> qualifierTypeBeans = new HashMap<>()
		qualifierTypeBeans.put(LANGUAGE_QUALIFIER_TYPE_BEAN_ID, languageQualifierType)
		qualifierTypeBeans.put(CURRENCY_QUALIFIER_TYPE_BEAN_ID, currencyQualifierType)

		applicationContext.getBeansOfType(SnQualifierType.class) >> qualifierTypeBeans

		DefaultSnQualifierTypeFactory qualifierTypeFactory = createQualifierTypeFactory()

		when:
		Optional<SnQualifierType> qualifierType = qualifierTypeFactory.getQualifierType(context, field)

		then:
		qualifierType.isPresent() == false
	}

	@Test
	def "Qualifier type is returned for localized field"() {
		given:
		Map<String, SnQualifierType> qualifierTypeBeans = new HashMap<>()
		qualifierTypeBeans.put(LANGUAGE_QUALIFIER_TYPE_BEAN_ID, languageQualifierType)
		qualifierTypeBeans.put(CURRENCY_QUALIFIER_TYPE_BEAN_ID, currencyQualifierType)

		applicationContext.getBeansOfType(SnQualifierType.class) >> qualifierTypeBeans

		DefaultSnQualifierTypeFactory qualifierTypeFactory = createQualifierTypeFactory()

		field.getLocalized() >> Boolean.TRUE

		when:
		Optional<SnQualifierType> qualifierType = qualifierTypeFactory.getQualifierType(context, field)

		then:
		qualifierType.isPresent() == true
		qualifierType.get().is(languageQualifierType)
	}

	@Test
	def "Qualifier type is returned for qualified field"() {
		given:
		Map<String, SnQualifierType> qualifierTypeBeans = new HashMap<>()
		qualifierTypeBeans.put(LANGUAGE_QUALIFIER_TYPE_BEAN_ID, languageQualifierType)
		qualifierTypeBeans.put(CURRENCY_QUALIFIER_TYPE_BEAN_ID, currencyQualifierType)

		applicationContext.getBeansOfType(SnQualifierType.class) >> qualifierTypeBeans

		DefaultSnQualifierTypeFactory qualifierTypeFactory = createQualifierTypeFactory()

		field.getQualifierTypeId() >> SearchservicesConstants.CURRENCY_QUALIFIER_TYPE

		when:
		Optional<SnQualifierType> qualifierType = qualifierTypeFactory.getQualifierType(context, field)

		then:
		qualifierType.isPresent() == true
		qualifierType.get().is(currencyQualifierType)
	}
}
