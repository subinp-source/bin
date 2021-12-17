/*

package de.hybris.training.v2.controller;

import de.hybris.platform.commercewebservicescommons.dto.dealerbasestores.DealerBaseStoresAllDataListWSDTO;
import de.hybris.platform.commercewebservicescommons.dto.driverbasestores.DriverBaseStoresAllDataListWSDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/electronics")
@Api(tags = "All dealers")
public class AllDealerController extends BaseCommerceController{


    private static final Logger LOGGER=Logger.getLogger(AllDealerController.class);

    @Resource(name ="dealerBaseStoresFacade")
    private DealerBaseStoresFacade dealerBaseStoresFacade;

    //@Secured("ROLE_TRUSTED_CLIENT")
    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "getDealerDetails" ,value = "Get a specific driver base store detail",notes ="Returns a specific Store based on driver name",authorizations = {@Authorization(value ="oauth2_client_credentials")})
    @ApiBaseSiteIdParam
    public DealerBaseStoresAllDataListWSDTO getDriverDetailsByName(@ApiParam(value = "Response Configuration. This is the list of the fields that should be returned in the response body",allowableValues = "BASIC,DEFAULT,FULL")
                                                                   @RequestParam(defaultValue = DEFAULT_FIELD_SET)
                                                                   final String fields)
    {

        final DealerBaseStoresDataList dealerBaseStoresDataList=new DealerBaseStoresDataList();
        dealerBaseStoresDataList.setDealerbasestores(dealerBaseStoresFacade.getAllDealerDetails());
        return getDataMapper().map(dealerBaseStoresDataList,DealerBaseStoresAllDataListWSDTO.class,fields);

    }


}

*/
