package org.dspace.webmvc.theme;

import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class SpringThemeContextUtils {
    public static String getName() {
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();
        return themeHolder == null ? null : themeHolder.getName();
    }

    public static String getMessage(String key) {
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();

        return themeHolder == null ? null : themeHolder.getMessage(key);
    }

    public static void setThemeName(String themeName, HttpServletRequest request, HttpServletResponse response) {
        ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(request);
        themeResolver.setThemeName(request, response, themeName);
        SpringThemeHolder themeHolder = SpringThemeHolder.getCurrentTheme();
        if (!themeName.equals(themeHolder.getName())) {
            SpringThemeHolder.resetCurrentTheme();
        }
    }

    static private class SpringThemeHolder {
        Locale currentLocale;
        Theme  currentTheme;

        private static final String THEME_BEAN = SpringThemeHolder.class.getCanonicalName() + ".THEME_BEAN";

        String getMessage(String key) {
            return currentTheme == null ? null : currentTheme.getMessageSource().getMessage(key, null, currentLocale);
        }

        String getName() {
            return currentTheme == null ? null : currentTheme.getName();
        }

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
