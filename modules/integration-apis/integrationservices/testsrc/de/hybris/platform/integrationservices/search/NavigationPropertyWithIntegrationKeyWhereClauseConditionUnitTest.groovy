/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NavigationPropertyWithIntegrationKeyWhereClauseConditionUnitTest extends Specification {

	private static final String CONDITION_TEMPLATE = "{%s} %s %s"
	private static final String COMPARE_OPERATOR = "=";

	@Test
	@Unroll
	def "extract attribute name and value from condition"()
	{
		given:
		def actualCondition = String.format(CONDITION_TEMPLATE, actualAttrName, COMPARE_OPERATOR, actualAttrValue)

		when:
		NavigationPropertyWithIntegrationKeyWhereClauseCondition whereCC = new NavigationPropertyWithIntegrationKeyWhereClauseCondition(actualAttrName, actualAttrValue)

		then:
		with(whereCC) {
			attributeName == actualAttrName
			condition == actualCondition
			conjunctiveOperator == ConjunctiveOperator.UNKNOWN
		}
		whereCC.getAttributeValue() == actualAttrValue

		where:
		actualAttrName | actualAttrValue
		"attrName"     | "attrValue"
		""             | ""
	}
}
