/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.persistence.polyglot.config

import de.hybris.platform.persistence.polyglot.TypeInfoFactory
import de.hybris.platform.persistence.polyglot.ItemStateRepository
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class RepositoriesConfigForTypecodeTypeInfoSpec extends Specification {

    @Shared
    private ItemStateRepositoryFactory factory
    @Shared
    private ItemStateRepository repoA = Mock(ItemStateRepository)
    @Shared
    private ItemStateRepository repoB = Mock(ItemStateRepository)

    def setup() {
        final List<RepositoryConfig> configs = new PolyglotRepositoriesConfigProviderBuilder()//
                .newRepository(repoA)//
                .withTypecode(1).withCondition(-1, "pk").withCondition(-1, "param")
                .withTypecode(2)
                .withTypecode(3).withCondition(-1, "paramA")
                .newRepository(repoB)//
                .withTypecode(3).withCondition(-1, "paramB")
                .build()
        final PolyglotRepositoriesConfigProvider provider = new MockedPolyglotRepositoryConfigProvider(configs)
        factory = new ItemStateRepositoryFactory(provider)
    }

    @Unroll
    @Test
    def testSupportTypeBasedOnTypecode(int typecode, boolean full, boolean partial, boolean none, ItemStateRepository[] repositories) {
        given:
        final TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(typecode)

        when:
        final RepositoryResult repository = factory.getRepository(typeInfo)

        then:
        repository.isFullySupported() == full
        repository.isPartiallySupported() == partial
        repository.isNotSupported() == none
        repository.getRepositories().size() == repositories.size()
        repository.getRepositories().containsAll(repositories)

        where:
        typecode | full  | partial | none  | repositories
        1        | false | true    | false | [repoA]
        2        | true  | false   | false | [repoA]
        3        | false | true    | false | [repoA, repoB]
        9        | false | false   | true  | []
    }
}