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

import org.dspace.webmvc.theme.SpringThemeContextUtils;

/**
 * Extension of Freemarker configuration class, that searches a path defined in a Spring theme's
 * properties file for templates (to override the standard template).
 *
 * In some ways, it would be easier and better to do this in a TemplateLoader, but then the
 * TemplateCache would be broken.
 */
public class SpringThemeAwareConfiguration extends Configuration {
    @Override
    public Template getTemplate(String name, Locale locale, String encoding, boolean parse) throws IOException {

        // TODO This method resolves the active template path for the given theme, and attempts to get a template from there
        // Ideally, it should get theme template paths in turn going back through the theme parent hierarchy
        // Then, a theme could override a specific template, but the parent can resolve the others
        // That would allow proper theme hierarchical definitions

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

        // No template in the theme path, so get a template from the standard location
        return super.getTemplate(name, locale, encoding, parse);
    }
}
