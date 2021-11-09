package de.hybris.training.backoffice.editors;
import com.hybris.cockpitng.editor.commonreferenceeditor.NestedObjectCreator;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import de.hybris.training.core.model.DriverBaseModel;
import org.apache.commons.lang3.StringUtils;
import java.util.concurrent.atomic.AtomicReference;





public  class MyDriverTextEditor<T> extends DefaultReferenceEditor<T> {
    @Override
    public String getStringRepresentationOfObject(T obj) {


        if ((obj instanceof NestedObjectCreator)) {
            NestedObjectCreator nestedObjectCreator = (NestedObjectCreator)obj;
            String typeName = this.getLabelService().getObjectLabel(StringUtils.isNotBlank(nestedObjectCreator.getUserChosenType()) ? nestedObjectCreator.getUserChosenType() : this.getTypeCode());
            return ((NestedObjectCreator)obj).getLabel(typeName);
        } else {
            AtomicReference<String> label = new AtomicReference();
            DriverBaseModel vehicledriverbase =(DriverBaseModel)obj;
            this.getLabelProvider().ifPresentOrElse((provider) -> {
                label.set(provider.getLabel(obj));
            }, () -> {
                label.set(this.getLabelService().getObjectLabel(vehicledriverbase.getNameOfDriver()));
            });
            return (String)label.get();
        }


    }
}
