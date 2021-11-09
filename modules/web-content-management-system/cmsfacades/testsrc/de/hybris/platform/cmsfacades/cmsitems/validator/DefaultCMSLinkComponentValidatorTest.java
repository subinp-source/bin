/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.LINK_MISSING_ITEMS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.common.validator.impl.DefaultValidationErrors;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSLinkComponentValidatorTest
{
    @Spy
    @InjectMocks
    private DefaultCMSLinkComponentValidator validator;

    @Mock
    private LanguageFacade languageFacade;
    @Mock
    private ValidationErrorsProvider validationErrorsProvider;

    private final ValidationErrors validationErrors = new DefaultValidationErrors();

    @Before
    public void setup()
    {
        when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
        when(languageFacade.getLanguages()).thenReturn(Collections.emptyList());
    }

    @Test
    public void relativeUrlValidationTests()
    {
        validateUrlNoErrorsTest("/");
        validateUrlNoErrorsTest("/example");
        validateUrlNoErrorsTest("/example/a/b/#c");
        validateUrlNoErrorsTest("/!@#$&*()/");
        validateUrlNoErrorsTest("/../../file");
        validateUrlNoErrorsTest("example");
        validateUrlNoErrorsTest("example/a/b/#c");

        validateUrlHasErrorsTest("/./../../file");
    }

    @Test
    public void absoluteUrlValidationTests()
    {
        validateUrlNoErrorsTest("http://www.example.com");
        validateUrlNoErrorsTest("https://yahoo.co.in/");
        validateUrlNoErrorsTest("https://yahoo.co.in/a/b/c#d");
        validateUrlNoErrorsTest("https://co.in/a/b/c?d=e&f=g");
        validateUrlNoErrorsTest("http://example");
        validateUrlNoErrorsTest("http://localhost");
        validateUrlNoErrorsTest("http://www.t.com/!@$&*()-_=+~:?/");
        validateUrlNoErrorsTest("http://example.com/query?q=random%20word%20%A3500%20bank%20%24");
        validateUrlNoErrorsTest("http://www.example.com/%20word%20%A3500%20bank%20%24");
        validateUrlNoErrorsTest("https://yahoo.co.in.xx");
        validateUrlNoErrorsTest("http://.com");
        validateUrlNoErrorsTest("http://com.");
        validateUrlNoErrorsTest("http://::::@example.com");
        validateUrlNoErrorsTest("http:/example.com");
        validateUrlNoErrorsTest("http://example.com");
        validateUrlNoErrorsTest("http://example.123");
        validateUrlNoErrorsTest("https://localhost:9002/fff/v1/a/b/c/d?permissionNames=create,change,read,remove&types=SomeData");
        validateUrlNoErrorsTest("https://localhost:9002/rest/v2/electronics/cms/pages?pageLabelOrId=&cmsTicketId=");
        validateUrlNoErrorsTest("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest");
        validateUrlNoErrorsTest("http://[1fff:0:a88:85a3::ac1f]:8001/index.html");

        validateUrlHasErrorsTest("https://www.t.com/fff///////dd/gg");
        validateUrlHasErrorsTest("http://www.t.com/%/");
        validateUrlHasErrorsTest("http:// ");
        validateUrlHasErrorsTest("http://www.t.com/%s/");
        validateUrlHasErrorsTest("http://example.com/^{}|<>\\");
        validateUrlHasErrorsTest("http://///example.com");
    }

    @Test
    public void advancedSchemeUrlValidationTests()
    {
        validateUrlNoErrorsTest("something://example.com");
        validateUrlNoErrorsTest("telnet://192.0.2.16:80/");
        validateUrlNoErrorsTest("mailto:John.Doe@example.com");
        validateUrlNoErrorsTest("news:comp.infosystems.www.servers.unix");
        validateUrlNoErrorsTest("tel:+1-816-555-1212");
        validateUrlNoErrorsTest("urn:oasis:names:specification:docbook:dtd:xml:4.1.2");
        validateUrlNoErrorsTest("ldap://[2001:db8::7]/c=GB?objectClass?one");
        validateUrlNoErrorsTest("blob:https://example.org/40a5fb5a-d56d-4a33-b4e2-0acf6a8e5f641");
    }

    @Test
    public void noSchemeUrlValidationTests()
    {
        validateUrlNoErrorsTest("example.com");
        validateUrlNoErrorsTest("www.example.com");

        validateUrlHasErrorsTest(" ");
    }

    @Test
    public void urlWithIntegratedJavaScriptValidationTests()
    {
        validateUrlHasErrorsTest("javascript:alert(\"hello world!\");");

        validateUrlNoErrorsTest("eval(document.location.hash.substring(1))");
    }

    protected void validateUrlHasErrorsTest(final String url)
    {
        // GIVEN
        when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(new DefaultValidationErrors());
        final CMSLinkComponentModel model = new CMSLinkComponentModel();
        model.setUrl(url);

        // WHEN
        validator.validate(model);

        // THEN
        final List<ValidationError> errors = validationErrorsProvider.getCurrentValidationErrors().getValidationErrors();
        assertFalse("The URL = \"" + url + "\" validation failed", errors.isEmpty());
    }

    protected void validateUrlNoErrorsTest(final String url)
    {
        // GIVEN
        when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(new DefaultValidationErrors());
        final CMSLinkComponentModel model = new CMSLinkComponentModel();
        model.setUrl(url);

        // WHEN
        validator.validate(model);

        // THEN
        final List<ValidationError> errors = validationErrorsProvider.getCurrentValidationErrors().getValidationErrors();
        assertTrue("The URL = \"" + url + "\" validation failed", errors.isEmpty());
    }

    @Test
    public void testValidateWithoutRequiredAttributeAddErrors()
    {
        final CMSLinkComponentModel itemModel = new CMSLinkComponentModel();
        validator.validate(itemModel);

        final List<ValidationError> errors = validationErrorsProvider.getCurrentValidationErrors().getValidationErrors();

        assertEquals(5, errors.size());
        assertThat(errors.get(0).getField(), is("linkTo"));
        assertThat(errors.get(0).getErrorCode(), is(LINK_MISSING_ITEMS));
    }

    @Test
    public void testValidateWithProductModelAddNoError()
    {
        final CMSLinkComponentModel itemModel = new CMSLinkComponentModel();
        itemModel.setProduct(new ProductModel());
        validator.validate(itemModel);

        final List<ValidationError> errors = validationErrorsProvider.getCurrentValidationErrors().getValidationErrors();

        assertTrue(errors.isEmpty());
    }
}