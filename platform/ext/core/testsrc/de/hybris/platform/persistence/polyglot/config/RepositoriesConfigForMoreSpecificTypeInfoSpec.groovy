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


class RepositoriesConfigForMoreSpecificTypeInfoSpec extends Specification {

    @Shared
    private ItemStateRepositoryFactory factory
    @Shared
    private ItemStateRepository repoA = Mock(ItemStateRepository)
    @Shared
    private ItemStateRepository repoB = Mock(ItemStateRepository)

    def setup() {
        final List<RepositoryConfig> configs = new PolyglotRepositoriesConfigProviderBuilder()//
                .newRepository(repoA)//
                .withTypecode(1).withCondition(-1, "2")
                .withTypecode(2).withCondition(-1, "3")
                .withTypecode(3).withCondition(-1, "4")
                .withTypecode(4)
                .withTypecode(5).withCondition(-1, "4")
                .withTypecode(6).withCondition(-1, "5")
                .withTypecode(7).withCondition(-1, "6")
                .newRepository(repoB)//
                .withTypecode(1).withCondition(-1, "3")
                .withTypecode(3).withCondition(-1, "11")
                .withTypecode(11)
                .withTypecode(12).withCondition(-1, "11")
                .withTypecode(6).withCondition(-1, "12")
                .withTypecode(7).withCondition(-1, "6")
                .build()
        final PolyglotRepositoriesConfigProvider provider = new MockedPolyglotRepositoryConfigProvider(configs)
        factory = new ItemStateRepositoryFactory(provider)
    }

    @Unroll
    @Test
    def testSupportTypeBasedOnMoreSpecificTypeInfo(int typecode, int depth, boolean full, boolean partial, boolean none, ItemStateRepository[] repositories) {
        given:
        final TypeInfo typeInfo = new MoreSpecificTypeInfo(typecode, depth)

        when:
        final RepositoryResult repository = factory.getRepository(typeInfo)

        then:
        repository.isFullySupported() == full
        repository.isPartiallySupported() == partial
        repository.isNotSupported() == none
        repository.getRepositories().size() == repositories.size()
        repository.getRepositories().containsAll(repositories)

        where:
        typecode | depth | full  | partial | none  | repositories
        1        | -1    | true  | false   | false | [repoA, repoB]
        1        | 1     | false | true    | false | [repoA, repoB]
        2        | -1    | true  | false   | false | [repoA]
        4        | -1    | true  | false   | false | [repoA]
        11       | -1    | true  | false   | false | [repoB]
        12       | 0     | false | true    | false | [repoB]
        7        | 0     | false | true    | false | [repoA, repoB]
        7        | 1     | false | true    | false | [repoA, repoB]
        7        | 2     | false | true    | false | [repoA, repoB]
        7        | 3     | true  | false   | false | [repoA, repoB]
    }

    private class MoreSpecificTypeInfo implements TypeInfo {
        private final int typecode
        private final int depth

        MoreSpecificTypeInfo(final int typeCode, final int depth) {
            this.typecode = typeCode
            this.depth = depth
        }

        @Override
        int getTypeCode() {
            return typecode
        }

        @Override
        Identity getIdentity() {
            return PolyglotPersistence.unknownIdentity()
        }

        @Override
        TypeInfo getMoreSpecificTypeInfo(final String qualifier) {
            if (qualifier == "" || depth == 0) {
                return null
            }
            return new MoreSpecificTypeInfo(Integer.valueOf(qualifier), depth - 1)
        }
    }
}
