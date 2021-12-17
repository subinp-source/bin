package de.hybris.training.core.services;

import de.hybris.training.core.Dao.DealerApiDao;
import de.hybris.training.core.Dao.HybrisApiDao;
import de.hybris.training.core.model.DealerBaseModel;
import de.hybris.training.core.model.DriverBaseModel;

import javax.annotation.Resource;
import java.util.List;

public class DealerApiServices {

    @Resource
    private DealerApiDao dealerApiDao;



    public List<DealerBaseModel> getAllDealerDetails(){
        final List<DealerBaseModel> storeModels=dealerApiDao.getDealerDetails();
        return storeModels;
    }
}
