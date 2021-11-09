package de.hybris.training.backoffice.renders;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.training.core.model.DealerBaseModel;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

import java.util.logging.Logger;

public class TrainingRenderDealerId extends AbstractWidgetComponentRenderer<Listcell, ListColumn, DealerBaseModel> {

    String s="hai";
    static final private Logger LOG = Logger.getLogger(String.valueOf(TrainingRenderDealerId.class));


    @Override
    public void render(Listcell listcell, ListColumn listColumn, DealerBaseModel dealerBaseModel, DataType dataType, WidgetInstanceManager widgetInstanceManager) {


        LOG.info("inside AbstractWidgetComponentRenderer.."+s);
        final Div div = new Div();
        final Label label = new Label();
        String Dealer_id= dealerBaseModel.getUniqueId();
        int i=0;
        while(i<Dealer_id.length() && Dealer_id.charAt(i)=='0'){
            i++;
        }
        StringBuilder sb =new StringBuilder(Dealer_id);
        sb.replace(0,i,"");
        Dealer_id=sb.toString();
        label.setValue(Dealer_id);
        div.appendChild(label);
        listcell.appendChild(div);
        fireComponentRendered(listcell,listColumn,dealerBaseModel);




    }
}
