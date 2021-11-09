/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hybris.yprofile.consent.cookie.EnhancedCookieGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class);
    private static final Map<String, String> SITE_ID_MAP = new HashMap<String, String>();

    static {
        SITE_ID_MAP.put("1", "electronics");
        SITE_ID_MAP.put("2", "apparel-de");
        SITE_ID_MAP.put("3", "apparel-uk");
    }

    private Utils() {
        //Default private constructor
    }

    public static String formatDouble(Double d){
        if(d == null){
            return "";
        }
        final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
        return decimalFormat.format(d);
    }

    public static String formatDate(Date d){
        final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return df2.format(d);
    }

    public static String remapSiteId(String siteId) {
       final  String result = SITE_ID_MAP.get(siteId);
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        return siteId;
    }

    public static Optional<String> getHeader(final HttpServletRequest request, final String headerName) {
        if (request == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(request.getHeader(headerName));
    }


    public static Optional<Cookie> getCookie(final HttpServletRequest request, final String cookieName) {
        if (request == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(WebUtils.getCookie(request, cookieName));
    }


    public static void setCookie(final EnhancedCookieGenerator enhancedCookieGenerator,
                                 final HttpServletResponse response,
                                 final String cookieName,
                                 final String cookieValue,
                                 final boolean isSessionCookie) {

        enhancedCookieGenerator.setCookieName(cookieName);
        enhancedCookieGenerator.addCookie(response, cookieValue, isSessionCookie);
    }

    public static void removeCookie(final EnhancedCookieGenerator enhancedCookieGenerator,
                                 final HttpServletResponse response,
                                 final String cookieName) {

        enhancedCookieGenerator.setCookieName(cookieName);
        enhancedCookieGenerator.removeCookie(response);
    }

    public static String parseObjectToJson(Object obj) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String event = obj.toString();
        try {
            event = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.debug("Encountered problem with json processing", e);
        }
        return event;
    }

}
