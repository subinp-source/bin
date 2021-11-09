/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.filter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.search.ConjunctiveOperator
import de.hybris.platform.integrationservices.search.NoResultWhereClauseCondition
import de.hybris.platform.integrationservices.search.WhereClauseCondition
import de.hybris.platform.integrationservices.search.WhereClauseConditions
import de.hybris.platform.odata2services.filter.impl.CombineWhereClauseConditionVisitingStrategy
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator
import org.junit.Test
import org.springframework.core.convert.converter.Converter
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CombineWhereClauseConditionVisitingStrategyUnitTest extends Specification
{
	def combineWhereClauseConditionVisitingStrategy = new CombineWhereClauseConditionVisitingStrategy()
	def binaryOperatorToSqlOperatorConverter = Mock(Converter)
	@Shared
	def noResultCondition = new NoResultWhereClauseCondition().toWhereClauseConditions()
	@Shared
	def whereClauseConditionWithResult = new WhereClauseCondition("%s %s '%s'", 'code', '=', 'abc').toWhereClauseConditions()
	def binaryExpression = Mock(BinaryExpression)

	def setup()
	{
		combineWhereClauseConditionVisitingStrategy.setOperatorConverter(binaryOperatorToSqlOperatorConverter)
	}

	@Test
	def "is applicable when leftResult && rightResult are instances of WhereClauseConditions"()
	{
		given:
		def operator = BinaryOperator.OR
		def leftResult = Mock(WhereClauseConditions)
		def rightResult = Mock(WhereClauseConditions)

		expect:
		combineWhereClauseConditionVisitingStrategy.isApplicable(binaryExpression, operator, leftResult, rightResult)
	}

	@Test
	def "is not applicable if any result parameter is not an instance of WhereClauseCondition"()
	{
		given:
		def operator = BinaryOperator.EQ
		def leftResult = "code"
		def rightResult = Mock(WhereClauseConditions)

		expect:
		!combineWhereClauseConditionVisitingStrategy.isApplicable(binaryExpression, operator, leftResult, rightResult)
	}

	@Test
	@Unroll
	def "both conditions have results"()
	{
		given:
		def leftAttributeName = 'code'
		def leftAttributeVal = 'abc1'
		def eq = "="
		def rightAttributeName = "name"
		def rightAttributeVal = 'abc2'
		binaryOperatorToSqlOperatorConverter.convert(operator) >> opString
		def leftResult = new WhereClauseCondition("{%s} %s '%s'", leftAttributeName, eq, leftAttributeVal).toWhereClauseConditions()
		def rightResult = new WhereClauseCondition("{%s} %s '%s'", rightAttributeName, eq, rightAttributeVal).toWhereClauseConditions()

		when:
		def actualResult = combineWhereClauseConditionVisitingStrategy.visit(binaryExpression, operator, leftResult, rightResult)

		then:
		actualResult == new WhereClauseConditions([new WhereClauseCondition("{%s} %s '%s'", leftAttributeName, eq, leftAttributeVal, conjuctiveOp), new WhereClauseCondition("{%s} %s '%s'", rightAttributeName, eq, rightAttributeVal)])

		where:
		opString | operator           | conjuctiveOp
		"AND"    | BinaryOperator.AND | ConjunctiveOperator.AND
		"OR"     | BinaryOperator.OR  | ConjunctiveOperator.OR
	}


	@Test
	@Unroll
	def "visiting with leftResult #leftResult and rightResult #rightResult results in #expected"() {
		given:
		def operator = BinaryOperator.OR
		binaryOperatorToSqlOperatorConverter.convert(operator) >> "OR"

		when:
		def actualResult = combineWhereClauseConditionVisitingStrategy.visit(binaryExpression, operator, leftResult, rightResult)

		then:
		actualResult == expected

		where:
		leftResult 						| rightResult						| expected
		whereClauseConditionWithResult 	| noResultCondition					| whereClauseConditionWithResult
		noResultCondition 				| whereClauseConditionWithResult	| whereClauseConditionWithResult
		noResultCondition 				| noResultCondition					| noResultCondition
	}
}