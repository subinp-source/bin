package de.hybris.training.core.services;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.training.core.Dao.HybrisApiDao;
import de.hybris.training.core.model.DriverBaseModel;

import javax.annotation.Resource;
import java.util.List;

public class HybrisApiServices {

    @Resource

    private HybrisApiDao hybrisApiDao;

    @Resource
    private ModelService modelService;

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public List<DriverBaseModel> getDriverDetailsByName(final  String nameOfDriver){
        final List<DriverBaseModel> storeModels=hybrisApiDao.getDriverDetailsByName(nameOfDriver);
        return storeModels;
    }

    public List<DriverBaseModel> getAllDriverDetails(){
        final List<DriverBaseModel> storeModels=hybrisApiDao.getDriverDetails();
        return storeModels;
    }


    public void removeDriverByName(String nameOfDriver) {

        for (DriverBaseModel driverBaseModel:hybrisApiDao.removeDriverInDao(nameOfDriver) )
        getModelService().remove(driverBaseModel);
    }


}