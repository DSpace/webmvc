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

import freemarker.ext.beans.BeansWrapper;
import org.dspace.core.ConfigurationManager;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class SpringThemeAwareFreeMarkerConfigurationFactoryBean extends FreeMarkerConfigurationFactoryBean {
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
        Configuration config = new SpringThemeAwareFreemarkerConfiguration();

        BeansWrapper wrapper = new BeansWrapper();
        wrapper.setExposeFields(true);
        wrapper.setSimpleMapWrapper(true);
        config.setObjectWrapper(wrapper);

        return config;
    }

    @Override
    public void setTemplateLoaderPaths(String[] templateLoaderPaths) {
        for (int i = 0 ; i < templateLoaderPaths.length; i++) {
            while (templateLoaderPaths[i].contains("${")) {
                int startPos = templateLoaderPaths[i].indexOf("${");
                int endPos = templateLoaderPaths[i].indexOf("}", startPos);

                if (endPos < startPos) {
                    break;
                }

                String propertyName = templateLoaderPaths[i].substring(startPos + 2, endPos);
                String propertyValue = ConfigurationManager.getProperty(propertyName);

                if (propertyValue == null) {
                    break;
                }

                templateLoaderPaths[i] = templateLoaderPaths[i].replaceAll("\\$\\{" + propertyName + "}", propertyValue);
            }
        }
        super.setTemplateLoaderPaths(templateLoaderPaths);
    }
}
