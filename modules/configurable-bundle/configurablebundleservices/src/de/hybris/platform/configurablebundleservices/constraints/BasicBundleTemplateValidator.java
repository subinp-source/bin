/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;

/**
 * Base class for bundle type validators.
 * <br/>
 *
 * Despite validators are not spring beans, this class and it's derivables support Spring DI.
 *
 * @param <C> validator's annotation class
 */
public abstract class BasicBundleTemplateValidator<C extends Annotation>
        extends TypeValidatorHelper
        implements ConstraintValidator<C, BundleTemplateModel>
{
    @Override
    public void initialize(final C annotation)
    {
        SpringContextProvider.getContext().getAutowireCapableBeanFactory().autowireBean(this);
    }
}
