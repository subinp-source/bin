/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.odata2services.filter.ExpressionVisitorParameters
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupStrategy
import de.hybris.platform.odata2services.odata.schema.entity.EntitySetNameGenerator
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.UriInfo
import org.junit.Test
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

@UnitTest
class DefaultExpressionVisitorFactoryUnitTest extends Specification
{
    def parameters = Stub(ExpressionVisitorParameters) {
        getUriInfo() >> Stub(UriInfo)
        getContext() >> Stub(ODataContext)
    }
    def integrationKeyConverter = Stub IntegrationKeyToODataEntryGenerator
    def itemLookupRequestFactory = Stub ItemLookupRequestFactory
    def itemSearchService = Stub ItemSearchService
    def operatorConverter = Stub Converter
	def entitySetNameGenerator = Stub EntitySetNameGenerator

	def expressionVisitorFactory = new DefaultExpressionVisitorFactory(
		integrationKeyConverter: integrationKeyConverter,
		itemLookupRequestFactory: itemLookupRequestFactory,
		itemSearchService: itemSearchService,
		operatorConverter: operatorConverter,
		entitySetNameGenerator: entitySetNameGenerator)
	
	@Test
	def "create creates BinaryExpressionVisitor with 4 strategies"()
	{
		when:
		def expressionVisitor = expressionVisitorFactory.create(parameters) as DefaultExpressionVisitor
		def binaryExpressionVisitor = expressionVisitor.getBinaryExpressionVisitor() as DefaultBinaryExpressionVisitor

		then:
		binaryExpressionVisitor.strategies.size() == 4
	}

	@Test
	def "createNavigationPropertyWithIntegrationKeyVisitingStrategy creates expected NavigationPropertyWithIntegrationKeyVisitingStrategy"()
	{
		given:
		def createdStrategy =  expressionVisitorFactory.createNavigationPropertyWithIntegrationKeyVisitingStrategy(parameters) as NavigationPropertyWithIntegrationKeyVisitingStrategy

		expect:
		createdStrategy.integrationKeyConverter == integrationKeyConverter
		createdStrategy.context == parameters.getContext()
		createdStrategy.itemLookupRequestFactory == itemLookupRequestFactory
		createdStrategy.itemSearchService == itemSearchService
		createdStrategy.getOperatorConverter() == operatorConverter
	}


	@Test
	def "createCombineWhereClauseConditionsVisitingStrategy creates expected CombineWhereClauseConditionsVisitingStrategy"()
	{
		given:
		def createdStrategy = expressionVisitorFactory.createCombineWhereClauseConditionsVisitingStrategy() as CombineWhereClauseConditionVisitingStrategy

		expect:
		createdStrategy.operatorConverter == operatorConverter
	}

	@Test
	def "createSimplePropertyVisitingStrategy creates expected SimplePropertyVisitingStrategy"()
	{
		given:
		def createdStrategy = expressionVisitorFactory.createSimplePropertyVisitingStrategy() as SimplePropertyVisitingStrategy

		expect:
		createdStrategy.operatorConverter == operatorConverter
	}

	@Test
	def "createNavigationPropertyVisitingStrategy creates expected createNavigationPropertyVisitingStrategy"()
	{
		given:
		def createdStrategy = expressionVisitorFactory.createNavigationPropertyVisitingStrategy(parameters) as NavigationPropertyVisitingStrategy
		
		expect:
		createdStrategy.context == parameters.context
		createdStrategy.itemLookupRequestFactory == itemLookupRequestFactory
		createdStrategy.itemSearchService == itemSearchService
		createdStrategy.operatorConverter == operatorConverter
	}

	@Test
	def "create creates MemberExpressionVisitor with entity set name generator and uri info" ()
	{
		given:
		def createdVisitor = expressionVisitorFactory.createMemberExpressionVisitor(parameters) as DefaultMemberExpressionVisitor

		expect:
		createdVisitor.entitySetNameGenerator == entitySetNameGenerator
		createdVisitor.uriInfo == parameters.uriInfo
	}

	@Test
	def "createPropertyExpressionVisitor creates DefaultPropertyExpressionVisitor" ()
	{
		expect:
		expressionVisitorFactory.createPropertyExpressionVisitor() instanceof DefaultPropertyExpressionVisitor
	}

	@Test
	def "createLiteralExpressionVisitor creates DefaultLiteralExpressionVisitor" ()
	{
		expect:
		expressionVisitorFactory.createLiteralExpressionVisitor() instanceof DefaultLiteralExpressionVisitor
	}

	@Test
	def "create creates a new instance for every call"()
	{
		given:
		def expressionVisitor1 = expressionVisitorFactory.create(parameters)
		def expressionVisitor2 = expressionVisitorFactory.create(parameters)

		expect:
		!expressionVisitor1.is(expressionVisitor2)
	}
}