/*

package de.hybris.training.v2.controller;

import de.hybris.platform.commercewebservicescommons.dto.driverbasestores.DriverBaseStoresAllDataListWSDTO;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.training.facades.DriverBaseFacade.DriverBaseStoresFacade;
import de.hybris.training.queues.data.DriverBaseStoresDataList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Controller
@RequestMapping(value = "/apparel-uk")
@Api(tags = "All drivers")
public class AllDriverController extends BaseCommerceController{


    private static final Logger LOGGER=Logger.getLogger(AllDriverController.class);

    @Resource(name ="driverBaseStoresFacade")
    private DriverBaseStoresFacade driverBaseStoresFacade;

    //@Secured("ROLE_TRUSTED_CLIENT")
    @RequestMapping(method= RequestMethod.GET)
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


}



*/
