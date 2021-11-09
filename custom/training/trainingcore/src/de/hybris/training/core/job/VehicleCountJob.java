package de.hybris.training.core.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.tx.Transaction;
import de.hybris.training.core.Dao.VehicleCountDao;
import de.hybris.training.core.model.VehicleBaseModel;
import de.hybris.training.core.model.VehicleCountCronJobModel;

import java.util.List;
import java.util.logging.Logger;

public class VehicleCountJob extends AbstractJobPerformable<VehicleCountCronJobModel> {

    private VehicleCountDao vehicleCountDao;
    private ModelService modelService;
    private IndexerService indexerService;
    private FacetSearchConfigService facetSearchConfigService;
    private BaseSiteService baseSiteService;


    private final static Logger LOG = Logger.getLogger(VehicleCountJob.class.getName());


    @Override
    public PerformResult perform(final VehicleCountCronJobModel vehicleCountCronJobModel) {


        List<VehicleBaseModel> vehicle = vehicleCountDao.findAllVehicleCount();
        LOG.info("vehicle count is "+vehicle.size());

        List<VehicleBaseModel> newVehicle = vehicleCountDao.findAllVehicleCount();

       for (VehicleBaseModel vehicleBaseModel:vehicle
             ) {
            String registrationNumber= vehicleBaseModel.getVehicleRegistrationNumber();
            int number=0;
            for(int i=0;i<registrationNumber.length();i++){
                if(registrationNumber.charAt(i)=='-'){

                    number ++;
                    if(number==3){
                        StringBuilder sb=new StringBuilder(registrationNumber);
                           while(sb.substring(i+1).length()!=4){

                               sb.insert(i+1,"0");
                           }
                        vehicleBaseModel.setVehicleRegistrationNumber(sb.toString());
                           modelService.saveAll(vehicleBaseModel);
                    }
                }

            }


        }

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    public VehicleCountDao getVehicleCountDao() {
        return vehicleCountDao;
    }

    public void setVehicleCountDao(VehicleCountDao vehicleCountDao) {
        this.vehicleCountDao = vehicleCountDao;
    }

    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public IndexerService getIndexerService() {
        return indexerService;
    }

    public void setIndexerService(IndexerService indexerService) {
        this.indexerService = indexerService;
    }

    public FacetSearchConfigService getFacetSearchConfigService() {
        return facetSearchConfigService;
    }

    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService) {
        this.facetSearchConfigService = facetSearchConfigService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }
}
