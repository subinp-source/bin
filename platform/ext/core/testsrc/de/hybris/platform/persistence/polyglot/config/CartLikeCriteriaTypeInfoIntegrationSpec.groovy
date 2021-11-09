/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.persistence.polyglot.config

import com.google.common.base.Preconditions
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.PK
import de.hybris.platform.persistence.polyglot.TypeInfoFactory
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria.CriteriaBuilder
import de.hybris.platform.persistence.polyglot.model.Identity
import de.hybris.platform.persistence.polyglot.ItemStateRepository
import de.hybris.platform.persistence.polyglot.PolyglotPersistence
import de.hybris.platform.persistence.polyglot.model.TypeId
import de.hybris.platform.persistence.polyglot.search.dialect.CriteriaExtractor
import de.hybris.platform.persistence.polyglot.search.dialect.PolyglotDialect
import de.hybris.platform.servicelayer.ServicelayerBaseSpecification
import org.junit.Test
import spock.lang.Shared
import spock.lang.Unroll

import java.util.regex.Pattern

@IntegrationTest
class CartLikeCriteriaTypeInfoIntegrationSpec extends ServicelayerBaseSpecification {

    @Shared
    private ItemStateRepositoryFactory factory
    @Shared
    private ItemStateRepository repoA = Mock(ItemStateRepository)

    @Shared
    private CriteriaExtractor.TypeNameConverter typeNameConverter;

    def setup() {
        final List<RepositoryConfig> configs = new PolyglotRepositoriesConfigProviderBuilder()//
                .newRepository(repoA)//
                .withTypecode(1).withCondition(11, "pk").withCondition(14, "pk").withCondition(15, "c")
                .withTypecode(2).withCondition(-1, "c")
                .withTypecode(3)
                .withTypecode(4)
                .withTypecode(5).withCondition(51, "a")
                .withTypecode(6).withCondition(61, "b")
                .withTypecode(8).withCondition(81, "a").withCondition(81, "b")
                .withTypecode(9).withCondition(-1, "a")
                .build()
        final PolyglotRepositoriesConfigProvider provider = new MockedPolyglotRepositoryConfigProvider(configs)
        factory = new ItemStateRepositoryFactory(provider)


        typeNameConverter = new CriteriaExtractor.TypeNameConverter() {

            private static final Pattern pattern = Pattern.compile("Type_(?<typeCode>\\d+)_(?<identity>\\d+)")

            @Override
            TypeId getTypeId(final String typeName) {

                def matcher = pattern.matcher(typeName)

                Preconditions.checkArgument(matcher.matches())
                Identity identity = identity(matcher.group("identity"))
                int typecode = Integer.valueOf(matcher.group("typeCode"))
                return TypeId.fromTypeId(identity, typecode);
            }

            @Override
            List<TypeId> getSubTypes(final String typeName) {
                return Collections.emptyList()
            }
        }
    }

    @Unroll
    @Test
    def testSupportTypeBasedOnSearchCriteria(String query, Map<String, Integer> params, boolean full, boolean partial, boolean none, ItemStateRepository[] repositories) {
        given:
        final CriteriaBuilder cb = PolyglotDialect.prepareCriteriaBuilder(query, typeNameConverter);
        Map<String, Object> entries = params.collectEntries { [(it.key): itemPkWithTypeCode(it.value)] }
        cb.withParameters(entries)
        def criteria = cb.build()
        final TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(criteria);

        when:
        println "${query}; ${params}"
        final RepositoryResult repository = factory.getRepository(typeInfo)

        then:
        repository.isFullySupported() == full
        repository.isPartiallySupported() == partial
        repository.isNotSupported() == none
        repository.getRepositories().size() == repositories.size()
        repository.getRepositories().containsAll(repositories)

        where:
        query                                     | params           | full  | partial | none  | repositories
        //full
        'get {Type_5_51} where {a}=?p'            | ['p': 3]         | true  | false   | false | [repoA]        //get {51} where a=331
        'get {Type_8_81} where {a}=?p'            | ['p': 4]         | true  | false   | false | [repoA]        //get {81} where a=331
        'get {Type_8_81} where {b}=?p'            | ['p': 4]         | true  | false   | false | [repoA]        //get {81} where a=441
        'get {Type_8_81} where {a}=?p and {b}=?r' | ['p': 3, 'r': 4] | true  | false   | false | [repoA]        //get {81} where a=331 and b=441
        'get {Type_1_11} where {pk}=?p'           | ['p': 3]         | true  | false   | false | [repoA]        //get {11} where pk=331
        'get {Type_3_33}'                         | [:]              | true  | false   | false | [repoA]        //get {33}
        'get {Type_9_91} where {a}=?p'            | ['p': 3]         | true  | false   | false | [repoA]        //get {91}  where a=331
        'get {Type_1_15} where {c}=?p'            | ['p': 3]         | true  | false   | false | [repoA]        //get {15} where c=331
        'get {Type_2_21} where {c}=?p'            | ['p': 3]         | true  | false   | false | [repoA]        //get {21} where c=331
        //partial
        'get {Type_9_91}'                         | [:]              | false | true    | false | [repoA]        //get {91}
        'get {Type_1_11}'                         | [:]              | false | true    | false | [repoA]        //get {11}
        'get {Type_5_51} where {pk}=?p'           | ['p': 5]         | false | true    | false | [repoA]        //get {51} where pk=551
        'get {Type_1_15}'                         | [:]              | false | true    | false | [repoA]        //get {15}
        'get {Type_8_81} where {a}=?p or {b}=?r'  | ['p': 3, 'r': 4] | false | true    | false | [repoA]        //get {81} where a=331 or b=441
        'get {Type_8_81} where {a}=?p or {b}=?r'  | ['p': 3, 'r': 7] | false | true    | false | [repoA]        //get {81} where a=331 or b=711
        'get {Type_8_81} where {a}=?p and {b}=?r' | ['p': 3, 'r': 7] | false | true    | false | [repoA]        //get {81} where a=331 and b=711
        'get {Type_8_81} where {a}=?p or {b}=?r'  | ['p': 7, 'r': 7] | false | true    | false | [repoA]        //get {81} where a=711 or b=711
        //none
        'get {Type_1_12}'                         | [:]              | false | false   | true  | []        //get {12}
        'get {Type_6_62}'                         | [:]              | false | false   | true  | []        //get {62}
        'get {Type_1_11} where {pk}=?p'           | ['p': 7]         | false | false   | true  | []        //get {11} where pk=711
        'get {Type_5_51} where {a}=?p'            | ['p': 7]         | false | false   | true  | []        //get {51} where a=711
        'get {Type_8_81} where {a}=?p and {b}=?r' | ['p': 7, 'r': 7] | false | false   | true  | []        //get {81} where a=711 and b=711
    }

    Identity itemPkWithTypeCode(int typeCode) {
        return PolyglotPersistence.identityFromLong(PK.createUUIDPK(typeCode).getLong())
    }

    Identity identity(String l) {
        return PolyglotPersistence.identityFromLong(Long.valueOf(l))
    }
}
