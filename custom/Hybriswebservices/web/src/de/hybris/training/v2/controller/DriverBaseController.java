
package de.hybris.training.v2.controller;


import de.hybris.platform.commercewebservicescommons.dto.dealerbasestores.DealerBaseStoresAllDataListWSDTO;
import de.hybris.platform.commercewebservicescommons.dto.driverbasestores.DriverBaseStoresAllDataListWSDTO;
import de.hybris.platform.commercewebservicescommons.dto.driverbasestores.DriverBaseStoresDataListWSDTO;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.training.facades.DealerBaseFacade.DealerBaseStoresFacade;
import de.hybris.training.facades.DriverBaseFacade.DriverBaseStoresFacade;
import de.hybris.training.queues.data.DealerBaseStoresDataList;
import de.hybris.training.queues.data.DriverBaseStoresDataList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller

@RequestMapping(value = "/{baseSiteId}")

@Api(tags = "Hybris driver and dealer stores")
public class DriverBaseController extends BaseCommerceController{


    private static final Logger LOGGER=Logger.getLogger(DriverBaseController.class);

    @Resource(name ="driverBaseStoresFacade")
    private DriverBaseStoresFacade driverBaseStoresFacade;

    //@Secured("ROLE_TRUSTED_CLIENT")
    @RequestMapping(value="/{nameOfDriver}",method= RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "getDriverDetailsByName" ,value = "Get a specific driver base store detail",notes ="Returns a specific Store based on driver name",authorizations = {@Authorization(value ="oauth2_client_credentials")})
    @ApiBaseSiteIdParam
    public DriverBaseStoresDataListWSDTO getDriverDetailsByName(@ApiParam(value = "nameOfDriver",required = true)

        @PathVariable

          final String nameOfDriver,
                                                                @ApiParam(value = "Response Configuration. This is the list of the fields that should be returned in the response body",allowableValues = "BASIC,DEFAULT,FULL")
           @RequestParam(defaultValue = DEFAULT_FIELD_SET)
                                                                final String fields)
    {
        LOGGER.info("driver name is :"+nameOfDriver);
        final DriverBaseStoresDataList driverBaseStoresDataList=new DriverBaseStoresDataList();
        driverBaseStoresDataList.setDriverbasestores(driverBaseStoresFacade.getDriverBaseDetails(nameOfDriver));
        return getDataMapper().map(driverBaseStoresDataList,DriverBaseStoresDataListWSDTO.class,fields);

    }

/*    @Resource(name ="driverBaseStoresFacade")
    private DriverBaseStoresFacade driverBaseStoresFacade;*/

    //@Secured("ROLE_TRUSTED_CLIENT")
    @RequestMapping(value="/allDriver",method= RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "getDriverDetails" ,value = "Get a specific driver base store detail",notes ="Returns a specific Store based on driver name",authorizations = {@Authorization(value ="oauth2_client_credentials")})
    @ApiBaseSiteIdParam
    public DriverBaseStoresAllDataListWSDTO getDriverDetailsByName(@ApiParam(value = "Response Configuration. This is the list of the fields that should be returned in the response body",allowableValues = "BASIC,DEFAULT,FULL")
                                                                   @RequestParam(defaultValue = DEFAULT_FIELD_SET)
                                                                   final String fields)
    {

        final DriverBaseStoresDataList driverBaseStoresDataList=new DriverBaseStoresDataList();
        driverBaseStoresDataList.setDriverbasestores(driverBaseStoresFacade.getAllDriverDetails());
        return getDataMapper().map(driverBaseStoresDataList,DriverBaseStoresAllDataListWSDTO.class,fields);

    }



    @Resource(name ="dealerBaseStoresFacade")
    private DealerBaseStoresFacade dealerBaseStoresFacade;

    //@Secured("ROLE_TRUSTED_CLIENT")
    @RequestMapping(value="/allDealer",method= RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "getDealerDetails" ,value = "Get a specific dealer base store detail",notes ="Returns a specific Store based on dealer name",authorizations = {@Authorization(value ="oauth2_client_credentials")})
    @ApiBaseSiteIdParam
    public DealerBaseStoresAllDataListWSDTO getDealerDetailsByName(@ApiParam(value = "Response Configuration. This is the list of the fields that should be returned in the response body",allowableValues = "BASIC,DEFAULT,FULL")
                                                                   @RequestParam(defaultValue = DEFAULT_FIELD_SET)
                                                                   final String fields)
    {

        final DealerBaseStoresDataList dealerBaseStoresDataList=new DealerBaseStoresDataList();
        dealerBaseStoresDataList.setDealerbasestores(dealerBaseStoresFacade.getAllDealerDetails());
        return getDataMapper().map(dealerBaseStoresDataList,DealerBaseStoresAllDataListWSDTO.class,fields);

    }



}

