package de.hybris.training.core.Dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.training.core.Dao.DealerApiDao;
import de.hybris.training.core.Dao.HybrisApiDao;
import de.hybris.training.core.model.DealerBaseModel;
import de.hybris.training.core.model.DriverBaseModel;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealerApiDaoImpl extends AbstractItemDao implements DealerApiDao {

    private static final Logger LOGGER=Logger.getLogger(HybrisApiDaoImpl.class);
    private static final String STORE_QUERY="SELECT {pk} FROM {DealerBase}";




    @Override
    public List<DealerBaseModel> getDealerDetails() {

        final SearchResult<DealerBaseModel> stores =getFlexibleSearchService().search(STORE_QUERY);
        return stores.getResult()==null? Collections.emptyList():stores.getResult();
    }


}
