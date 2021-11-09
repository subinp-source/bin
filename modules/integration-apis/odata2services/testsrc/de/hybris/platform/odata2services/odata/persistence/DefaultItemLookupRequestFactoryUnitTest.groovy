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
package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.PaginationParameters
import de.hybris.platform.integrationservices.search.SimplePropertyWhereClauseCondition
import de.hybris.platform.integrationservices.search.WhereClauseConditions
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.config.ODataServicesConfiguration
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException
import de.hybris.platform.odata2services.converter.ODataEntryToIntegrationItemConverter
import de.hybris.platform.odata2services.filter.ExpressionVisitorFactory
import de.hybris.platform.odata2services.filter.ExpressionVisitorParameters
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator
import de.hybris.platform.odata2services.odata.persistence.lookup.InvalidQueryParameterException
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor
import org.apache.commons.lang3.tuple.Pair
import org.apache.olingo.odata2.api.commons.HttpHeaders
import org.apache.olingo.odata2.api.commons.InlineCount
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.KeyPredicate
import org.apache.olingo.odata2.api.uri.NavigationPropertySegment
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.api.uri.UriInfo
import org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor
import org.apache.olingo.odata2.api.uri.expression.FilterExpression
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultItemLookupRequestFactoryUnitTest extends Specification {
    static final def INTEGRATION_OBJECT = "thisServiceName"
    static final def INTEGRATION_KEY_VALUE = "asdf|fdsa|asdf"
    static final def EXPECTED_PAGING_ERROR_CODE = "invalid_query_parameter"
    static final def ACCEPT_LOCALE = Locale.ENGLISH
    static final def DEFAULT_PAGE_SIZE = 2

    @Shared
    def oDataEntry = Stub ODataEntry
    @Shared
    def integrationItem = Stub(IntegrationItem) {
        getIntegrationKey() >> INTEGRATION_KEY_VALUE
        getIntegrationObjectCode() >> INTEGRATION_OBJECT
    }
    @Shared
    def filterExpression = Stub(FilterExpression) {
        accept(_ as ExpressionVisitor) >> new WhereClauseConditions()
    }

    def entryConverter = Stub(ODataEntryToIntegrationItemConverter) {
        convert(_, _, oDataEntry) >> integrationItem
    }
    def integrationKeyToODataEntryGenerator = Stub(IntegrationKeyToODataEntryGenerator)
    def localeExtractor = Stub(ODataContextLanguageExtractor) {
        extractFrom(_ as ODataContext, HttpHeaders.ACCEPT_LANGUAGE) >> ACCEPT_LOCALE
    }
    private ODataServicesConfiguration oDataServicesConfiguration = Stub(ODataServicesConfiguration) {
        getMaxPageSize() >> 3
        getDefaultPageSize() >> DEFAULT_PAGE_SIZE
    }
    def serviceNameExtractor = Stub(ServiceNameExtractor) {
        extract(_ as ODataContext, _ as String) >> INTEGRATION_OBJECT
        extract(_ as ODataContext) >> INTEGRATION_OBJECT
    }
    def descriptorService = Stub(ItemTypeDescriptorService) {
        getTypeDescriptor(INTEGRATION_OBJECT, _) >> Optional.of(Stub(TypeDescriptor))
    }
    def expressionVisitorFactory = Stub(ExpressionVisitorFactory) {
        create(_ as ExpressionVisitorParameters) >> Stub(ExpressionVisitor)
    }

    private DefaultItemLookupRequestFactory factory = new DefaultItemLookupRequestFactory(
            integrationKeyToODataEntryGenerator: integrationKeyToODataEntryGenerator,
            localeExtractor: localeExtractor,
            serviceNameExtractor: serviceNameExtractor,
            oDataServicesConfiguration: oDataServicesConfiguration,
            entryConverter: entryConverter,
            itemTypeDescriptorService: descriptorService,
            expressionVisitorFactory: expressionVisitorFactory)

    @Test
    @Unroll
    def "lookup request \$skip option when at most 1 skip #skipValue present in uriInfo"() {
        given:
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.getSkip() >> skipValue
        uriInfo.getSkipToken() >> skipTokenValue
        uriInfo.getTop() >> 1

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest.skip == expected

        where:
        skipValue | skipTokenValue | expected
        1         | null           | 1
        null      | null           | 0
        null      | "1"            | 1
    }

    @Test
    @Unroll
    def "error thrown when invalid \$skipToken=#skipToken provided"() {
        given:
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.getSkip() >> 1
        uriInfo.getTop() >> 1
        uriInfo.getSkipToken() >> skipToken

        when:
        factory.create(uriInfo, oDataContext(), "")

        then:
        def exception = thrown(InvalidQueryParameterException)
        exception.getCode() == EXPECTED_PAGING_ERROR_CODE

        where:
        skipToken << ["1.1", "notAnInt", "-1"]
    }

    @Test
    @Unroll
    def "lookup request #condition_desc count option when \$inlineCount with #inlineCntValue present"() {
        given:
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.getInlineCount() >> inlineCntValue

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest.includeTotalCount() == expected
        !lookupRequest.countOnly

        where:
        inlineCntValue       | condition_desc | expected
        InlineCount.ALLPAGES | 'has'          | true
        InlineCount.NONE     | 'has no'       | false
    }

    @Test
    @Unroll
    def "lookup request #res_description count option when \$count #res_condition"() {
        given:
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.isCount() >> countCondition


        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest.includeTotalCount() == expected
        lookupRequest.countOnly == expected

        where:
        res_condition | res_description | countCondition | expected
        'present'     | 'has'           | true           | true
        'absent'      | 'has no'        | false          | false
    }

    @Test
    @Unroll
    def "lookup request \$top option when topValue = #topValue present in uriInfo"() {
        given:
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.getSkip() >> 1
        uriInfo.getSkipToken() >> null
        uriInfo.getTop() >> topValue

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest.top == expected

        where:
        topValue | expected
        3        | 3
        4        | 3
        null     | 2
    }

    @Test
    @Unroll
    def "lookup request \$expand option when expandValue = #expandValue present in uriInfo"() {
        given:
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.getExpand() >> expandValue

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest.expand == expandValue

        where:
        expandValue << [[], [[null]], [[Stub(NavigationPropertySegment)]]]
    }

    @Test
    @Unroll
    def "create with no integration key present, and filter is #filter"() {
        given: "uriInfo with no key, and filter is #filter"
        def uriInfo = uriInfoWithoutIntegrationKey()
        uriInfo.getFilter() >> filter
        and: "the integration key resolves to an OData entry"
        integrationKeyToODataEntryGenerator.generate(uriInfo.startEntitySet, uriInfo.keyPredicates) >> oDataEntry

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest != null
        lookupRequest.filter == whereClause

        where:
        filter           | whereClause
        filterExpression | new WhereClauseConditions()
        null             | null
    }

    @Test
    def "create with integration key present"() {
        given: "uriInfo and context created"
        def uriInfo = uriInfoWithIntegrationKey()
        and: "the integration key resolves to an OData entry"
        integrationKeyToODataEntryGenerator.generate(uriInfo.startEntitySet, uriInfo.keyPredicates) >> oDataEntry

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest != null
        lookupRequest.integrationKey == INTEGRATION_KEY_VALUE
        lookupRequest.integrationObjectCode == INTEGRATION_OBJECT
        lookupRequest.ODataEntry == oDataEntry
        lookupRequest.integrationItem == integrationItem
        lookupRequest.typeDescriptor
    }

    @Test
    def "create with integration key not present"() {
        given: 'uriInfo does not contain integration key'
        def lookupRequest = factory.create(uriInfoWithoutIntegrationKey(), oDataContext(), "")

        expect:
        lookupRequest != null
        lookupRequest.integrationKey == null
    }

    @Test
    def "create with integration key when key converter throws exception"() {
        given:
        def uriInfo = uriInfoWithIntegrationKey()
        integrationKeyToODataEntryGenerator.generate(uriInfo.startEntitySet, uriInfo.keyPredicates) >> {
            throw new EdmException(EdmException.PROVIDERPROBLEM)
        }

        when:
        factory.create(uriInfo, oDataContext(), "")

        then:
        def exception = thrown(InternalProcessingException)
        exception.getCode() == "internal_error"
    }

    @Test
    def "create with integration key and filter present, filter is ignored"() {
        given: "uriInfo with key and filter supplied"
        def uriInfo = uriInfoWithIntegrationKey()
        uriInfo.getFilter() >> Stub(FilterExpression)
        and: "the integration key resolves to an OData entry"
        integrationKeyToODataEntryGenerator.generate(uriInfo.startEntitySet, uriInfo.keyPredicates) >> oDataEntry

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest != null
        lookupRequest.filter == null
    }

    @Test
    def 'create with context, entity set, and attributes'() {
        given:
        def entitySet = Stub EdmEntitySet
        def pair = Pair.of('a', 'b')

        when:
        def request = factory.create oDataContext(), entitySet, pair

        then:
        with(request) {
            top == DEFAULT_PAGE_SIZE
            entitySet == entitySet
            filter.conditions == [SimplePropertyWhereClauseCondition.eq('a', 'b')]
            integrationObjectCode == INTEGRATION_OBJECT
            acceptLocale == Locale.ENGLISH
            typeDescriptor
        }
    }

    @Test
    def 'creates request with filter conditions'() {
        given:
        def entitySet = Stub EdmEntitySet
        def filter = new WhereClauseConditions()

        when:
        def request = factory.createWithFilter oDataContext(), entitySet, filter

        then:
        with(request) {
            top == DEFAULT_PAGE_SIZE
            entitySet == entitySet
            filter == filter
            integrationObjectCode == INTEGRATION_OBJECT
            acceptLocale == Locale.ENGLISH
            typeDescriptor
        }
    }

    @Test
    def "create with DeleteUriInfo"() {
        given:
        def uriInfo = deleteUriInfo()
        and:
        integrationKeyToODataEntryGenerator.generate(uriInfo.startEntitySet, uriInfo.keyPredicates) >> oDataEntry

        when:
        def request = factory.create(uriInfo, oDataContext())

        then:
        request.entitySet == uriInfo.startEntitySet
        request.ODataEntry == oDataEntry
        request.integrationItem == integrationItem
        request.integrationKey == INTEGRATION_KEY_VALUE
        request.integrationObjectCode == INTEGRATION_OBJECT
        request.typeDescriptor
    }

    @Test
    def "create with DeleteUriInfo when key converter throws exception"() {
        given:
        def uriInfo = deleteUriInfo()
        and:
        integrationKeyToODataEntryGenerator.generate(uriInfo.startEntitySet, uriInfo.keyPredicates) >> {
            throw new EdmException(EdmException.PROVIDERPROBLEM)
        }

        when:
        factory.create(uriInfo, oDataContext())

        then:
        def exception = thrown(InternalProcessingException)
        exception.getCode() == "internal_error"
    }

    @Test
    def "createFrom creates new request with given parameters"() {
        given:
        def entitySet = entitySet()
        def request = Mock(ItemLookupRequest) {
            getEntitySet() >> Stub(EdmEntitySet) { getEntityType() >> Stub(EdmEntityType) }
            getODataEntry() >> Stub(ODataEntry)
            getAcceptLocale() >> Locale.ENGLISH
            getIntegrationItem() >> Stub(IntegrationItem)
            getTypeDescriptor() >> Stub(TypeDescriptor)
            getPaginationParameters() >> Optional.of(PaginationParameters.create(4, 5))
        }

        when:
        def newRequest = factory.createFrom(request, entitySet, oDataEntry)

        then:
        newRequest.entitySet == entitySet
        newRequest.entitySet.entityType == entitySet.entityType
        newRequest.acceptLocale == request.acceptLocale
        newRequest.ODataEntry == oDataEntry
        newRequest.integrationItem == request.integrationItem
        newRequest.typeDescriptor == request.typeDescriptor
        newRequest.paginationParameters == request.paginationParameters
    }

    @Test
    def 'type descriptor is populated when integration key is not present'() {
        given: 'URI does not contain integration key'
        def entityType = 'SomeItem'
        def uriInfo = uriInfoWithoutIntegrationKey(entityType)
        and: 'descriptor service find matching type descriptor'
        def typeDescriptor = Stub TypeDescriptor
        factory.itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
            getTypeDescriptor(INTEGRATION_OBJECT, entityType) >> Optional.of(typeDescriptor)
        }

        when:
        def lookupRequest = factory.create(uriInfo, oDataContext(), "")

        then:
        lookupRequest.typeDescriptor.is typeDescriptor
    }

    @Test
    def 'throws exception when type descriptor cannot be populated'() {
        given: 'URI does not contain integration key'
        def uriInfo = uriInfoWithoutIntegrationKey()
        and: 'descriptor service does not find a matching type descriptor'
        factory.itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
            getTypeDescriptor(INTEGRATION_OBJECT, _) >> Optional.empty()
        }

        when:
        factory.create(uriInfo, oDataContext(), "")

        then:
        def e = thrown IntegrationObjectItemNotFoundException
        e.message.contains INTEGRATION_OBJECT
    }

    private UriInfo uriInfoWithoutIntegrationKey(String entityType = '') {
        Mock(UriInfo) {
            getKeyPredicates() >> []
            getStartEntitySet() >> entitySet(entityType)
        }
    }

    private UriInfo uriInfoWithIntegrationKey() {
        Mock(UriInfo) {
            getKeyPredicates() >> [Stub(KeyPredicate)]
            getStartEntitySet() >> entitySet()
        }
    }

    private EdmEntitySet entitySet(String entityType = '') {
        Stub(EdmEntitySet) {
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> entityType
            }
        }
    }

    private DeleteUriInfo deleteUriInfo() {
        Stub(DeleteUriInfo) {
            getStartEntitySet() >> entitySet()
        }
    }

    private ODataContext oDataContext() {
        Stub(ODataContext) {
            getPathInfo() >> Mock(PathInfo) {
                getServiceRoot() >> new URI("https://localhost:9002/odata2webservices/$INTEGRATION_OBJECT")
            }
        }
    }
}
