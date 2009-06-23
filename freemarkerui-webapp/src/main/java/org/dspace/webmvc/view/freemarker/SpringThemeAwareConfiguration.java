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
        String themePath = SpringThemeContextUtils.getMessage("theme.template.path");

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
