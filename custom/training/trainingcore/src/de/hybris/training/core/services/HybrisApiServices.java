package de.hybris.training.core.services;

import de.hybris.training.core.Dao.HybrisApiDao;
import de.hybris.training.core.model.DriverBaseModel;

import javax.annotation.Resource;
import java.util.List;

public class HybrisApiServices {

    @Resource

    private HybrisApiDao hybrisApiDao;

    public List<DriverBaseModel> getDriverDetailsByName(final  String nameOfDriver){
        final List<DriverBaseModel> storeModels=hybrisApiDao.getDriverDetailsByName(nameOfDriver);
        return storeModels;
    }





}