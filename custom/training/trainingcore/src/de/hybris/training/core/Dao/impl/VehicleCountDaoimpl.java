package de.hybris.training.core.Dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.training.core.Dao.VehicleCountDao;
import de.hybris.training.core.model.VehicleBaseModel;

import java.util.Collections;
import java.util.List;

public class VehicleCountDaoimpl implements VehicleCountDao {

    private FlexibleSearchService flexibleSearchService;

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    @Override
    public List<VehicleBaseModel> findAllVehicleCount() {


        final String query ="select {pk} from {VehicleBase}";
        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
        searchQuery.setResultClassList(Collections.singletonList(VehicleBaseModel.class));
        final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
        return searchResult.getResult();



    }
}
