/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.persistence.polyglot.config

import de.hybris.platform.persistence.polyglot.model.Identity
import de.hybris.platform.persistence.polyglot.ItemStateRepository
import de.hybris.platform.persistence.polyglot.PolyglotPersistence
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RepositoriesConfigForTypeIdentifierAndTypecodeSpec extends Specification {

    @Shared
    private ItemStateRepositoryFactory factory
    @Shared
    private ItemStateRepository repoA = Mock(ItemStateRepository)
    @Shared
    private ItemStateRepository repoB = Mock(ItemStateRepository)

    def setup() {
        final List<RepositoryConfig> configs = new PolyglotRepositoriesConfigProviderBuilder()//
                .newRepository(repoA)//
                .withTypecode(1).withCondition(11L, "")//
                .withTypecode(2).withCondition(20L, "")//
                .withTypecode(3).withCondition(30L, "")//
                .withTypecode(4).withCondition(40L, "a")//
                .withTypecode(5)//
                .newRepository(repoB)//
                .withTypecode(3).withCondition(31L, "")//
                .withTypecode(4).withCondition(40L, "b")//
                .build()
        final PolyglotRepositoriesConfigProvider provider = new MockedPolyglotRepositoryConfigProvider(configs)
        factory = new ItemStateRepositoryFactory(provider)
    }

    @Unroll
    @Test
    def testSupportTypeBasedOnType(int typecode, long identity, boolean full, boolean partial, boolean none, ItemStateRepository[] repositories) {
        given:
        final TypeInfo typeInfo = new TypeIdentifierTypeInfo(typecode, identity)

        when:
        final RepositoryResult repository = factory.getRepository(typeInfo)

        then:
        repository.isFullySupported() == full
        repository.isPartiallySupported() == partial
        repository.isNotSupported() == none
        repository.getRepositories().size() == repositories.size()
        repository.getRepositories().containsAll(repositories)

        where:
        typecode | identity | full  | partial | none  | repositories
        1        | 10       | false | false   | true  | []
        1        | 11       | false | true    | false | [repoA]
        1        | 12       | false | false   | true  | []
        2        | 20       | false | true    | false | [repoA]
        3        | 30       | false | true    | false | [repoA]
        3        | 31       | false | true    | false | [repoB]
        4        | 40       | false | true    | false | [repoA, repoB]
        1        | 20       | false | false   | true  | []
        2        | 99       | false | false   | true  | []
        5        | 0        | true  | false   | false | [repoA]
        5        | -1       | true  | false   | false | [repoA]
        5        | 99       | true  | false   | false | [repoA]
        1        | -1       | false | true    | false | [repoA]
    }

    private class TypeIdentifierTypeInfo implements TypeInfo {
        private final int typecode
        private final long identity

        TypeIdentifierTypeInfo(final int typeCode, final long identity) {
            this.typecode = typeCode
            this.identity = identity
        }

        @Override
        int getTypeCode() {
            return typecode
        }

        @Override
        Identity getIdentity() {
            return identity >= 0 ? PolyglotPersistence.identityFromLong(identity) : PolyglotPersistence.unknownIdentity()
        }

        @Override
        TypeInfo getMoreSpecificTypeInfo(final String qualifier) {
            return null
        }
    }

}
