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
        return new SpringThemeAwareFreemarkerConfiguration();
    }
}
