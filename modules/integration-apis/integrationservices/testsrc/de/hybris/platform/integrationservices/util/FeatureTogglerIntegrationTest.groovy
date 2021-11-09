/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.util.featuretoggle.Feature
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

@IntegrationTest
class FeatureTogglerIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    @Resource
    ConfigurationService configurationService

    @Test
    @Unroll
    def "isFeatureEnabled is #value"() {
        given:
        configurationService.getConfiguration().setProperty(TestFeature.FEATURE_TEST_EXISTING.property, value as String)

        expect:
        de.hybris.platform.integrationservices.util.featuretoggle.FeatureToggler.isFeatureEnabled(TestFeature.FEATURE_TEST_EXISTING) == value

        where:
        value << [true, false]
    }

    @Test
    @Unroll
    def "isFeatureEnabled returns false when feature is #feature"() {
        expect:
        !de.hybris.platform.integrationservices.util.featuretoggle.FeatureToggler.isFeatureEnabled(feature)

        where:
        feature << [null, TestFeature.FEATURE_EMPTY_PROPERTY, TestFeature.FEATURE_NON_EXISTING]
    }

    private static enum TestFeature implements Feature {

        FEATURE_EMPTY_PROPERTY(''),
        FEATURE_NON_EXISTING('non.existing.property'),
        FEATURE_TEST_EXISTING('existing.property')

        private String property

        TestFeature(String property) {
            this.property = property
        }

        String getProperty() {
            return property
        }
    }
}
