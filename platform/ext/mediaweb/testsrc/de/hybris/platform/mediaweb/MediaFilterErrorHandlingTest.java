/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaweb;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.web.WebAppMediaFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class MediaFilterErrorHandlingTest extends ServicelayerBaseTest
{
    @Mock
    private HttpServletRequest httpRequest;
    @Mock
    private HttpServletResponse httpResponse;
    @Mock
    private FilterChain chain;

    private final MediaFilter mediaFilter = new TestMediaFilter();
    private final WebAppMediaFilter webAppMediaFilter = new WebAppMediaFilter();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotShowHttp500ErrorWhenAccessingNonExistingFileForMediaFilter() throws Exception
    {
        // given
        given(httpRequest.getServletPath()).willReturn("medias?");
        given(httpRequest.getParameter("context")).willReturn(
                "bWFzdGVyfHJvb3R8MTIwNDB8aW1hZ2UvanBlZ3xoODYvaGQ3LzI5MTI3MTcwMjYyNDB8YjY0NTU3MWVjMGVhY2U2ZmIxMTQxMGY3MGI3ZTYxYjBmOTY1ZmZjZDJmNDc0YTExNDY2ZjJlNWM0MmFmZDU2ZQ");

        // when
        mediaFilter.doFilter(httpRequest, httpResponse, null);

        // then fine
        verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void shouldNotShowHttp500ErrorWhenAccessingNonExistingFileForWebAppMediaFilter() throws Exception
    {
        // given
        given(httpRequest.getServletPath()).willReturn("medias?");

        given(httpRequest.getParameter("context")).willReturn(
                "bWFzdGVyfHJvb3R8MTIwNDB8aW1hZ2UvanBlZ3xoODYvaGQ3LzI5MTI3MTcwMjYyNDB8YjY0NTU3MWVjMGVhY2U2ZmIxMTQxMGY3MGI3ZTYxYjBmOTY1ZmZjZDJmNDc0YTExNDY2ZjJlNWM0MmFmZDU2ZQ");

        // when
        webAppMediaFilter.doFilter(httpRequest, httpResponse, chain);

        // then fine
        verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }


    public static class TestMediaFilter extends MediaFilter {
	    @Override
	    protected void setCurretTenantByID(final Iterable<String> mediaContext)
	    {
		    //do not change tenant
	    }

	    @Override
	    protected void unsetCurrentTenant()
	    {
		    //do not change tenant
	    }
    }
}
