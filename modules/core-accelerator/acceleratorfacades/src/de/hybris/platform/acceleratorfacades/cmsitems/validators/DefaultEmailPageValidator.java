/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.validators;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Locale;
import java.util.stream.Stream;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.FIELD_NOT_EMAIL;
import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED_L10N;

/**
 * Default implementation of the validator for {@link EmailPageModel}
 */
public class DefaultEmailPageValidator implements Validator<EmailPageModel>
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailPageValidator.class.getName());

    private ValidationErrorsProvider validationErrorsProvider;

    private LanguageFacade languageFacade;

    @Override
    public void validate(EmailPageModel emailPageModel)
    {
        getLanguages().forEach((languageData) -> {
            Locale locale = new Locale(languageData.getIsocode());

            final String fromName = emailPageModel.getFromName(locale);
            if (languageData.isRequired() && Strings.isBlank(fromName))
            {
                addValidationError(EmailPageModel.FROMNAME, languageData, FIELD_REQUIRED_L10N);
            }

            final String fromEmail = emailPageModel.getFromEmail(locale);
            if (languageData.isRequired() && Strings.isBlank(fromEmail))
            {
                addValidationError(EmailPageModel.FROMEMAIL, languageData, FIELD_REQUIRED_L10N);
            }

            if (Strings.isBlank(fromEmail)) {
                return;
            }

            try
            {
                InternetAddress internetAddress = new InternetAddress(fromEmail);
                internetAddress.validate();
            }
            catch (AddressException e)
            {
                LOG.error("Address not valid", e);
                addValidationError(EmailPageModel.FROMEMAIL, languageData, FIELD_NOT_EMAIL);
            }
        });
    }

    protected Stream<LanguageData> getLanguages()
    {
        return getLanguageFacade().getLanguages().stream();
    }

    protected void addValidationError(final String field, final LanguageData languageData, final String errorCode)
    {
        getValidationErrorsProvider().getCurrentValidationErrors().add(newValidationErrorBuilder() //
                .field(field) //
                .language(languageData.getIsocode()) //
                .errorCode(errorCode) //
                .errorArgs(new Object[] { languageData.getIsocode() }) //
                .build());
    }

    protected ValidationErrorsProvider getValidationErrorsProvider()
    {
        return validationErrorsProvider;
    }

    @Required
    public void setValidationErrorsProvider(final ValidationErrorsProvider validationErrorsProvider)
    {
        this.validationErrorsProvider = validationErrorsProvider;
    }

    protected LanguageFacade getLanguageFacade()
    {
        return languageFacade;
    }

    @Required
    public void setLanguageFacade(final LanguageFacade languageFacade)
    {
        this.languageFacade = languageFacade;
    }

}
