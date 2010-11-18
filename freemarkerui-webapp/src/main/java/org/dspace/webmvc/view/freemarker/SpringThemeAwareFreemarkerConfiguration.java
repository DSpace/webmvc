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

package org.dspace.webmvc.view.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.util.Locale;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.dspace.webmvc.theme.SpringThemeContextUtils;
import org.dspace.webmvc.theme.SpringThemeUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Extension of Freemarker configuration class, that searches a path defined in a Spring theme's
 * properties file for templates (to override the standard template).
 *
 * In some ways, it would be easier and better to do this in a TemplateLoader, but then the
 * TemplateCache would be broken.
 */
public class SpringThemeAwareFreemarkerConfiguration extends Configuration {
    @Override
    public Template getTemplate(String name, Locale locale, String encoding, boolean parse) throws IOException {

        String themePath = SpringThemeContextUtils.getProperty("theme.template.path");

        // If we have a theme path, attempt locating the template within the theme first
        if (themePath != null) {
            try {
                return super.getTemplate(themePath + name, locale, encoding, parse);
            } catch (IOException ioe) {
                // Will throw an exception if there is no template in the theme path,
                // we will ignore it, and try the standard path locations
            }
        }

        String themeParentName = SpringThemeContextUtils.getProperty("theme.parent", null);
        if (!StringUtils.isEmpty(themeParentName)) {
            RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
            if (ra instanceof ServletRequestAttributes) {
                ServletRequestAttributes sra = (ServletRequestAttributes)ra;
                ThemeSource ts = RequestContextUtils.getThemeSource(sra.getRequest());

                if (ts != null) {
                    Theme parentTheme = ts.getTheme(themeParentName);

                    while (parentTheme != null) {
                        themePath = SpringThemeUtils.getProperty(parentTheme, "theme.template.path", locale, null);
                        if (themePath != null) {
                            try {
                                return super.getTemplate(themePath + name, locale, encoding, parse);
                            } catch (IOException ioe) {
                                // Will throw an exception if there is no template in the theme path,
                                // we will ignore it, and try the standard path locations
                            }
                        }

                        themeParentName = SpringThemeUtils.getProperty(parentTheme, "theme.parent", locale, null);
                        parentTheme = ts.getTheme(themeParentName);
                    }

                }
            }
        }

        // No template in the theme path, so get a template from the standard location
        return super.getTemplate(name, locale, encoding, parse);
    }
}
