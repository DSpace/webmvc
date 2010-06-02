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

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.util.PathMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ThemeChangeInterceptor extends HandlerInterceptorAdapter {

    private static final String VIEW_PREFIX       = "view:";
    private static final String URL_PREFIX        = "url:";
    private static final String CONTROLLER_PREFIX = "controller:";

    private final List<ThemeMapEntry> themeMappings = new ArrayList<ThemeMapEntry>();

    private PathMatcher pathMatcher = new AntPathMatcher();

    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    
	/**
	 * Default name of the theme specification parameter: "theme".
	 */
	public static final String DEFAULT_PARAM_NAME = "theme";

	private String paramName = DEFAULT_PARAM_NAME;

	/**
	 * Set the name of the parameter that contains a theme specification
	 * in a theme change request. Default is "theme".
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * Return the name of the parameter that contains a theme specification
	 * in a theme change request.
	 */
	public String getParamName() {
		return this.paramName;
	}

    public void setMappings(Properties mappings) {
        for (Map.Entry entry : mappings.entrySet()) {
            try {
                String key   = (String)entry.getKey();
                String value = (String)entry.getValue();

                if (key != null && value != null) {
                    ThemeMapEntry themeMapEntry = new ThemeMapEntry();

                    if (key.startsWith(VIEW_PREFIX)) {
                        themeMapEntry.mapType = MapType.VIEW;
                        key = key.substring(VIEW_PREFIX.length());
                    } else if (key.startsWith(URL_PREFIX)) {
                        themeMapEntry.mapType = MapType.URL;
                        key = key.substring(URL_PREFIX.length());
                    } else if (key.startsWith(CONTROLLER_PREFIX)) {
                        themeMapEntry.mapType = MapType.CONTROLLER;
                        key = key.substring(CONTROLLER_PREFIX.length());
                    }

                    themeMapEntry.path      = key;
                    themeMapEntry.themeName = value;

                    themeMappings.add(themeMapEntry);
                }
            } catch (ClassCastException cce) {
                // TODO Debug log class cast
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(request);
        if (themeResolver == null) {
            throw new IllegalStateException("No ThemeResolver found: not in a DispatcherServlet request?");
        }

        String newTheme = request.getParameter(this.paramName);
        if (newTheme != null) {
            themeResolver.setThemeName(request, response, newTheme);
        } else {
            ThemeMapEntry bestMatch = null;
            
            for (ThemeMapEntry entry : themeMappings) {

                if (entry.mapType == MapType.VIEW || entry.mapType == MapType.ANY) {
                    if (modelAndView != null && pathMatcher.match(entry.path, modelAndView.getViewName())) {
                        if (entry.isBestMatch(bestMatch)) {
                            bestMatch = entry;
                        }
                    }
                }

                if (entry.mapType == MapType.URL || entry.mapType == MapType.ANY) {
                    String path = urlPathHelper.getLookupPathForRequest(request);
                    if (pathMatcher.match(entry.path, path)) {
                        if (entry.isBestMatch(bestMatch)) {
                            bestMatch = entry;
                        }
                    }
                }

                if (entry.mapType == MapType.CONTROLLER || entry.mapType == MapType.ANY) {

                }
            }

            if (bestMatch != null) {
                themeResolver.setThemeName(request, response, bestMatch.themeName);
            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    private enum MapType { VIEW, URL, CONTROLLER, ANY };

    static private class ThemeMapEntry {
        MapType mapType  = MapType.ANY;
        String path;
        String  themeName;

        boolean isBestMatch(ThemeMapEntry tme) {
            if (tme == null) {
                return true;
            }

            if (mapType == MapType.VIEW && (tme.mapType == MapType.URL || tme.mapType == MapType.CONTROLLER)) {
                return true;
            }

            if (mapType == MapType.URL && tme.mapType == MapType.CONTROLLER) {
                return true;
            }

            return path.length() > tme.path.length();
        }
    }
}

