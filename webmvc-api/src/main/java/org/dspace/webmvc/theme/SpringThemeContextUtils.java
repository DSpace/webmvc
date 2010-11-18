/**
 * $Id: $
 * $URL: $
 * *************************************************************************
 * Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 * Licensed under the DuraSpace License.
 *
 * A copy of the DuraSpace License has been included in this
 * distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
 */

package org.dspace.webmvc.theme;

import org.springframework.context.NoSuchMessageException;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Utilities for managing the theme(s) to be used
 */
public class SpringThemeContextUtils {
    /**
     * Gets the name of the current theme
     * @return
     */
    public static String getName() {
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();
        return themeHolder == null ? null : themeHolder.getName();
    }

    public static Theme getCurrentTheme() {
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();
        return themeHolder == null ? null : themeHolder.currentTheme;
    }

    /**
     * Gets a property of the current theme
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();

        return themeHolder == null ? null : themeHolder.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        try {
            SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();

            return themeHolder == null ? defaultValue : themeHolder.getProperty(key);
        } catch (NoSuchMessageException nsme) {
            return defaultValue;
        }
    }

    /**
     * Set the current theme
     *
     * @param themeName
     * @param request
     * @param response
     */
    public static void setThemeName(String themeName, HttpServletRequest request, HttpServletResponse response) {
        ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(request);
        themeResolver.setThemeName(request, response, themeName);
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();
        if (!themeName.equals(themeHolder.getName())) {
            SpringThemeHolder.resetCurrentTheme();
        }
    }

    /**
     * Internal class for managing a theme definition
     */
    static private class SpringThemeHolder {
        Locale currentLocale;
        Theme  currentTheme;

        private static final String THEME_BEAN = SpringThemeHolder.class.getCanonicalName() + ".THEME_BEAN";

        /**
         * Get a property from the theme
         *
         * @param key
         * @return
         */
        String getProperty(String key) {
            return currentTheme == null ? null : currentTheme.getMessageSource().getMessage(key, null, currentLocale);
        }

        /**
         * Get the name of the theme
         * @return
         */
        String getName() {
            return currentTheme == null ? null : currentTheme.getName();
        }

        /**
         * Get a defintion for the current the theme
         *
         * @return
         */
        static SpringThemeHolder getCurrentTheme() {
            // Get the current request attributes from the thread
            RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
            if (ra instanceof ServletRequestAttributes) {
                // Get the theme path from the attributes
                SpringThemeHolder themeHolder = (SpringThemeHolder)ra.getAttribute(SpringThemeHolder.THEME_BEAN, RequestAttributes.SCOPE_REQUEST);

                // Don't have a theme path, so determine one from the request
                if (themeHolder == null) {
                    themeHolder = new SpringThemeHolder();

                    ServletRequestAttributes sra = (ServletRequestAttributes)ra;

                    // Get the theme resolver and source
                    ThemeResolver tr = RequestContextUtils.getThemeResolver(sra.getRequest());
                    ThemeSource ts = RequestContextUtils.getThemeSource(sra.getRequest());

                    if (tr != null && ts != null) {
                        // Get the current request, and the current theme
                        HttpServletRequest request = sra.getRequest();

                        themeHolder.currentLocale = request.getLocale();
                        themeHolder.currentTheme = ts.getTheme(tr.resolveThemeName(request));

                        // Cache the result in the request attributes
                        ra.setAttribute(SpringThemeHolder.THEME_BEAN, themeHolder, RequestAttributes.SCOPE_REQUEST);
                    }
                }

                return themeHolder;
            }

            return null;
        }

        public static void resetCurrentTheme() {
            // Get the current request attributes from the thread
            RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
            ra.removeAttribute(SpringThemeHolder.THEME_BEAN, RequestAttributes.SCOPE_REQUEST);
        }
    }
}
