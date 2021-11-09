/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder.converters.populator;

import de.hybris.platform.commercefacades.store.data.StoreCountData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.store.pojo.StoreCountInfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * populates {@link StoreCountData} from {@link StoreCountInfo}
 */
public class StoreCountPopulator implements Populator<StoreCountInfo, StoreCountData> {
    @Override
    public void populate(final StoreCountInfo source, final StoreCountData target) {
        if (source != null && target != null) {
            target.setIsoCode(source.getIsoCode());
            target.setCount(source.getCount());
            target.setType(source.getType() == null ? null : source.getType().toString());
            target.setName(source.getName());
            //case of multiple regions per country
            if (!CollectionUtils.isEmpty(source.getStoreCountInfoList())) {
                final List<StoreCountData> regions = new ArrayList<>();
                source.getStoreCountInfoList().forEach(region -> {
                    final StoreCountData regionData = new StoreCountData();
                    this.populate(region, regionData);
                    regions.add(regionData);
                });
                target.setStoreCountDataList(regions);
            }
        }
    }
}