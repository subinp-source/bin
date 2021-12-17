package de.hybris.training.core.Dao.impl;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.training.core.Dao.HybrisApiDao;
import de.hybris.training.core.model.DriverBaseModel;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HybrisApiDaoImpl extends AbstractItemDao implements HybrisApiDao {

    private static final Logger LOGGER=Logger.getLogger(HybrisApiDaoImpl.class);
    private static final String STORE_QUERY="SELECT {" + DriverBaseModel.PK + "} FROM{" + DriverBaseModel._TYPECODE + "} WHERE {" + DriverBaseModel.NAMEOFDRIVER +"}= ?nameOfDriver ";
    private static final String STORE_QUERY2="SELECT {pk} FROM {DriverBase}";


    @Override
    public List<DriverBaseModel> getDriverDetailsByName(String nameOfDriver) {
        ServicesUtil.validateParameterNotNull(nameOfDriver,"driver name must not be null");

        final Map<String,Object> params =new HashMap<>();
        params.put("nameOfDriver",nameOfDriver);
        LOGGER.info(getFlexibleSearchService().search(STORE_QUERY,params
        ).getClass());
        final SearchResult<DriverBaseModel> stores =getFlexibleSearchService().search(STORE_QUERY,params);
        return stores.getResult()==null? Collections.emptyList():stores.getResult();
    }

    @Override
    public List<DriverBaseModel> getDriverDetails() {

        final SearchResult<DriverBaseModel> stores =getFlexibleSearchService().search(STORE_QUERY2);
        return stores.getResult()==null? Collections.emptyList():stores.getResult();
    }
}

