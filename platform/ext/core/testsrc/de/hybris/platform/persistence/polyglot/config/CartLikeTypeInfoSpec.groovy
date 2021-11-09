/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.persistence.polyglot.config


import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.persistence.polyglot.model.Identity
import de.hybris.platform.persistence.polyglot.ItemStateRepository
import de.hybris.platform.persistence.polyglot.PolyglotPersistence
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CartLikeTypeInfoSpec extends Specification {

    @Shared
    private ItemStateRepositoryFactory factory
    @Shared
    private ItemStateRepository repoA = Mock(ItemStateRepository)

    def setup() {
        final List<RepositoryConfig> configs = new PolyglotRepositoriesConfigProviderBuilder()//
                .newRepository(repoA)//
                .withTypecode(1).withCondition(11, "pk").withCondition(14, "pk").withCondition(15, "c")
                .withTypecode(2).withCondition(-1, "c")
                .withTypecode(3)
                .withTypecode(4)
                .withTypecode(5).withCondition(51, "a")
                .withTypecode(6).withCondition(61, "b")
                .withTypecode(9).withCondition(-1, "a")
                .build()
        final PolyglotRepositoriesConfigProvider provider = new MockedPolyglotRepositoryConfigProvider(configs)
        factory = new ItemStateRepositoryFactory(provider)
    }

    @Unroll
    @Test
    def testSupportTypeBasedOnSearchCriteria(String[] typeInfoInput, boolean full, boolean partial, boolean none, ItemStateRepository[] repositories) {
        given:
        final TypeInfo typeInfo = new CustomTypeInfo(typeInfoInput)

        when:
        final RepositoryResult repository = factory.getRepository(typeInfo)

        then:
        repository.isFullySupported() == full
        repository.isPartiallySupported() == partial
        repository.isNotSupported() == none
        repository.getRepositories().size() == repositories.size()
        repository.getRepositories().containsAll(repositories)

        where:
        typeInfoInput         | full  | partial | none  | repositories
        //full
        ["5_51_a", "3_-1_-"]  | true  | false   | false | [repoA]        //get {51} where a=331
        ["1_11_pk", "3_-1_-"] | true  | false   | false | [repoA]        //get {11} where pk=331
        ["3_33_-"]            | true  | false   | false | [repoA]        //get {33}
        ["9_91_a", "3_-1_-"]  | true  | false   | false | [repoA]        //get {91}  where a=331
        ["1_15_c", "3_-1_-"]  | true  | false   | false | [repoA]        //get {15} where c=331
        ["2_21_c", "3_-1_-"]  | true  | false   | false | [repoA]        //get {21} where c=331
        //partial
        ["9_91_-"]            | false | true    | false | [repoA]        //get {91}
        ["1_11_-"]            | false | true    | false | [repoA]        //get {11}
        ["5_51_pk", "5_-1_-"] | false | true    | false | [repoA]        //get {51} where pk=551
        ["1_15_-"]            | false | true    | false | [repoA]        //get {15}
        //none
        ["1_12_-"]            | false | false   | true  | []        //get {12}
        ["6_62_-"]            | false | false   | true  | []        //get {62}
        ["1_11_pk", "8_-1_-"] | false | false   | true  | []        //get {11} where pk=881
        ["5_51_a", "7_-1_-"]  | false | false   | true  | []        //get {51} where a=711
    }

    @Unroll
    @Test
    def testSupportTypeBasedOnGetByPk(String[] typeInfoInput, boolean full, boolean partial, boolean none, ItemStateRepository[] repositories) {
        given:
        final TypeInfo typeInfo = new CustomTypeInfo(typeInfoInput)

        when:
        final RepositoryResult repository = factory.getRepository(typeInfo)

        then:
        repository.isFullySupported() == full
        repository.isPartiallySupported() == partial
        repository.isNotSupported() == none
        repository.getRepositories().size() == repositories.size()
        repository.getRepositories().containsAll(repositories)

        where:
        typeInfoInput | full  | partial | none  | repositories
        //full
        ["3_-1_-"]    | true  | false   | false | [repoA]        //get (331)
        //partial
        ["1_-1_-"]    | false | true    | false | [repoA]        //get (141)
        ["1_-1_-"]    | false | true    | false | [repoA]        //get (151)
        ["5_-1_-"]    | false | true    | false | [repoA]        //get (511)
        ["6_-1_-"]    | false | true    | false | [repoA]        //get (621)
        //none
        ["8_-1_-"]    | false | false   | true  | []             //get (811)
    }

    private class CustomTypeInfo implements TypeInfo {
        private final int typecode
        private final Identity identity
        private final String qualifier
        private final String[] subElements

        CustomTypeInfo(final String[] elements) {
            if (elements.size() > 1) {
                subElements = elements[1..elements.size() - 1]
            } else {
                subElements = []
            }
            String[] pattern = elements[0].split("_")

            this.typecode = Integer.valueOf(pattern[0])

            long identityLong = Integer.valueOf(pattern[1])
            if (identityLong > 0) {
                this.identity = PolyglotPersistence.identityFromLong(identityLong)
            } else {
                this.identity = PolyglotPersistence.unknownIdentity()
            }

            this.qualifier = pattern[2]
        }

        @Override
        int getTypeCode() {
            return typecode
        }

        @Override
        Identity getIdentity() {
            return this.identity
        }

        @Override
        TypeInfo getMoreSpecificTypeInfo(final String qualifier) {
            if (this.qualifier.equals(qualifier) && !this.qualifier.equals("-") && subElements.size() > 0) {
                return new CustomTypeInfo(subElements)
            }
            return null
        }
    }
}
