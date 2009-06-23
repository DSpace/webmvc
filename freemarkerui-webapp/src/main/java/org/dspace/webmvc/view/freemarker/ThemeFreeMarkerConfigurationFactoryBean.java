package org.dspace.webmvc.view.freemarker;

import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class ThemeFreeMarkerConfigurationFactoryBean extends FreeMarkerConfigurationFactoryBean {
    /**
     * Get the configuration object for Freemarker
     *
     * In this implementation, we use a custom Configuration object, that integrates
     * with Spring's theme support to allow templates to be overridden in themes.
     *
     * @param templateLoaders
     * @return
     */
    @Override
    protected Configuration newConfiguration() throws IOException, TemplateException {
        return new SpringThemeAwareConfiguration();
    }
}
