/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.strategies.merge;

import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;

import java.util.List;

/**
 * An interface representing a strategy for merging two product configuration lists.
 */
public interface ProductConfigurationMergeStrategy {

    /**
     * Merges two product configuration lists
     *
     * @param firstConfiguration
     * 			the first list configuration to merge
     * @param secondConfiguration
     * 			the second list configuration to merge
     * @return the result of merging two product configuration item lists
     */
    List<ConfigurationInfoData> merge(final List<ConfigurationInfoData> firstConfiguration,
        final List<ConfigurationInfoData> secondConfiguration);
}
