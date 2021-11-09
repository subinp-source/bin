/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.core.model.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.searchservices.constants.SearchservicesConstants
import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor
import de.hybris.platform.servicelayer.i18n.L10NService
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import de.hybris.platform.servicelayer.model.ModelService

import org.junit.Test

import spock.lang.Specification
import spock.lang.Unroll


@UnitTest
public class IdentifierValidatorSpec extends Specification {

	@Unroll
	@Test
	def "Is not valid #testId: '#input'"(testId, input) {
		given:
		ModelService modelService = Mock(ModelService)
		L10NService l10nService = Mock(L10NService)

		ItemModel model = Mock(ItemModel)
		InterceptorContext context = Mock(InterceptorContext)

		TestSnInterceptor interceptor = new TestSnInterceptor()
		interceptor.setModelService(modelService)
		interceptor.setL10nService(l10nService)

		modelService.getAttributeValue(model, SearchservicesConstants.ID_FIELD) >> input

		when:
		interceptor.onValidate(model, context)

		then:
		thrown(InterceptorException)

		where:
		testId | input
		1      | ""
		2      | " "
		3      | " "
		4      | "-a-"
		5      | "_a_"
		6      | "-a"
		7      | "_a"
		8      | "a-"
		9      | "a_"
		10     | "-1-"
		11     | "_1_"
		12     | "-1"
		13     | "_1"
		14     | "1-"
		15     | "1_"
		16     | "a#a"
		17     | "a!a"
		18     | "a.a"
		19     | "a,a"
		20     | "a;a"
		21     | "a<a"
		22     | "a>a"
		23     | "a&a"
		24     | "a%a"
		25     | "a?a"
	}

	@Unroll
	@Test
	def "Is valid #testId: '#input'"(testId, input) {
		given:
		ModelService modelService = Mock(ModelService)
		L10NService l10nService = Mock(L10NService)

		ItemModel model = Mock(ItemModel)
		InterceptorContext context = Mock(InterceptorContext)

		TestSnInterceptor interceptor = new TestSnInterceptor()
		interceptor.setModelService(modelService)
		interceptor.setL10nService(l10nService)

		modelService.getAttributeValue(model, SearchservicesConstants.ID_FIELD) >> input

		when:
		interceptor.onValidate(model, context)

		then:
		noExceptionThrown()

		where:
		testId | input
		1      | null
		2      | "a"
		3      | "abc"
		4      | "1"
		5      | "123"
		6      | "a1"
		7      | "1a"
		8      | "a-c"
		9      | "a_c"
	}

	static class TestSnInterceptor extends AbstractSnInterceptor<ItemModel> {
		void onValidate(final ItemModel model, final InterceptorContext context) {
			validateIdentifier(model, SearchservicesConstants.ID_FIELD)
		}
	}
}
